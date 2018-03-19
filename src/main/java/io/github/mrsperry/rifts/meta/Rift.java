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
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private ArrayList<LivingEntity> mobs;

    public Rift(Location location, RiftConfig config) {
        this.deactivated = false;
        this.config = config;

        CustomRiftSize size = config.getRiftSize();
        this.center = location;
        this.timer = size.length() * 60;
        this.maxMonsters = size.maxMonsters();
        int radius = size.radius();

        this.mobs = new ArrayList<>();
        ArrayList<Location> validLocations = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location current = new Location(this.center.getWorld(),
                            this.center.getBlockX() + x,
                            this.center.getBlockY() + y,
                            this.center.getBlockZ() + z);
                    if (SpawnUtils.isValidLocation(current)) {
                        validLocations.add(current);
                    }
                }
            }
        }

        RiftEffect riftEffect = new RiftEffect(this.config)
                .setCenter(this.center)
                .setValidLocations(validLocations);
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
        } else if (this.mobs.size() < this.maxMonsters) {
            SpawnUtils.spawn(this.center.clone().add(0, 5, 0), 1, (location, count) -> {
                LivingEntity mob = (LivingEntity) location.getWorld().spawnEntity(location, MobUtils.getRandomMob(this.config.getMonsters()));
                Random random = Main.getRandom();
                mob.setVelocity(new Vector((random.nextDouble() * 2) - 1, (random.nextDouble() * 2) - 1, (random.nextDouble() * 2) - 1));

                List<PotionEffectType> effects = MobUtils.getRandomEffects(this.config.getPotionEffects(), this.config.getMaxPotionsApplied());
                for(PotionEffectType effect : effects) {
                    PotionEffect potion = new PotionEffect(effect, Integer.MAX_VALUE, 1, false);
                    mob.addPotionEffect(potion);
                }
                this.mobs.add(mob);
            });
        }
    }

    private void stop() {
        this.deactivated = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(mobs.size() == 0 && !ending) {
                    ending = true;
                    Messenger.sendStopMessage(center.getWorld());
                    Bukkit.getScheduler().cancelTask(taskId);
                    Bukkit.getScheduler().cancelTask(effectId);
                    HandlerList.unregisterAll(RiftManager.getRiftById(riftId));
                    RiftManager.unregisterRift(riftId);
                    this.cancel();
                }

                if (mobs.size() > 0) {
                    LivingEntity entity = mobs.get(0);
                    if (!entity.isDead()) {
                        entity.damage(Integer.MAX_VALUE);
                    } else {
                        mobs.remove(0);
                        this.run();
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 2);
    }

    public void end() {
        this.timer = 0;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (MobUtils.listContainsEntity(this.mobs, entity)) {
            this.mobs.remove(entity);
            if(deactivated) {
                entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 2, 0);
                event.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (MobUtils.listContainsEntity(this.mobs, event.getEntity())) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (MobUtils.listContainsEntity(this.mobs, entity)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
            if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                event.setCancelled(true);
            }
            if (!event.isCancelled()) {
                entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation().add(0, 1, 0), 5);
            }
        }
    }
}
