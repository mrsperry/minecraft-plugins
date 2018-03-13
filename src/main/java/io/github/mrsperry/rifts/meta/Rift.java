package io.github.mrsperry.rifts.meta;

import io.github.mrsperry.rifts.Messenger;
import io.github.mrsperry.rifts.RiftManager;
import io.github.mrsperry.rifts.Main;
import io.github.mrsperry.rifts.configs.RiftConfig;
import io.github.mrsperry.rifts.utils.MobUtils;
import io.github.mrsperry.rifts.utils.SpawnUtils;
import io.github.mrsperry.rifts.meta.RiftSize.CustomRiftSize;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Rift implements Runnable, Listener {
    private int riftId;
    private int taskId;
    private int effectId;
    private boolean deactivated;
    private boolean ending = false;

    private RiftConfig config;

    private Location center;
    private double timer;
    private int maxMonsters;

    private ArrayList<Monster> monsters;
    private ArrayList<Location> validLocations;

    public Rift(Location location, RiftConfig config) {
        this.deactivated = false;
        this.config = config;

        CustomRiftSize size = config.getRiftSize();
        this.center = location;
        this.timer = size.length() * 60;
        this.maxMonsters = size.maxMonsters();
        int radius = size.radius();

        this.monsters = new ArrayList<>();
        this.validLocations = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location current = new Location(this.center.getWorld(),
                            this.center.getBlockX() + x,
                            this.center.getBlockY() + y,
                            this.center.getBlockZ() + z);
                    if (SpawnUtils.isValidLocation(current)) {
                        this.validLocations.add(current);
                    }
                }
            }
        }

        RiftEffect riftEffect = new RiftEffect(this.config)
                .setCenter(this.center)
                .setValidLocations(this.validLocations);
        this.effectId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), riftEffect, 0L, 5L);
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), this, 0, 20);
        this.riftId = RiftManager.registerRift(this);
        Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
        Messenger.sendStartMessage(this.center.getWorld());
    }

    public void run() {
        this.timer--;
        if (this.timer <= 0) {
            this.stop();
        } else if (this.validLocations.size() > 0 && this.monsters.size() < this.maxMonsters) {
            SpawnUtils.spawn(this.validLocations, 1, (location, count) -> {
                LivingEntity monster = (LivingEntity) location.getWorld().spawnEntity(location, MobUtils.getRandomMob(this.config.getMonsters()));
                List<PotionEffectType> effects = MobUtils.getRandomEffects(this.config.getPotionEffects(), this.config.getMaxPotionsApplied());

                for(PotionEffectType effect : effects) {
                    PotionEffect potion = new PotionEffect(effect, Integer.MAX_VALUE, 1, false);
                    monster.addPotionEffect(potion);
                }
                this.monsters.add((Monster) monster);
            });
        }
    }

    private void stop() {
        this.deactivated = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(monsters.size() == 0 && !ending) {
                    ending = true;
                    Messenger.sendStopMessage(center.getWorld());
                    Bukkit.getScheduler().cancelTask(taskId);
                    Bukkit.getScheduler().cancelTask(effectId);
                    HandlerList.unregisterAll(RiftManager.getRiftById(riftId));
                    RiftManager.unregisterRift(riftId);
                    this.cancel();
                }

                if (monsters.size() > 0) {
                    LivingEntity entity = monsters.get(0);
                    if (!entity.isDead()) {
                        entity.damage(entity.getHealth());
                        entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation().add(0, 1, 0), 5);
                    } else {
                        monsters.remove(0);
                        this.run();
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 2);
    }

    public void end() {
        this.timer = 0;
    }

    public int getID() {
        return this.riftId;
    }

    public Location getCenter() {
        return this.center;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (MobUtils.listContainsMonster(this.monsters, entity)) {
            this.monsters.remove(entity);
            if(deactivated) {
                entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 2, 0);
                event.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (MobUtils.listContainsMonster(this.monsters, event.getEntity())) {
            event.blockList().clear();
        }
    }
}
