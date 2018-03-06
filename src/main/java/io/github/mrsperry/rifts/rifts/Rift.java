package io.github.mrsperry.rifts.rifts;

import io.github.mrsperry.rifts.RiftManager;
import io.github.mrsperry.rifts.Rifts;
import io.github.mrsperry.rifts.configs.RiftConfig;
import io.github.mrsperry.rifts.utils.MobUtils;
import io.github.mrsperry.rifts.utils.SpawnUtils;
import io.github.mrsperry.rifts.rifts.RiftSize.CustomRiftSize;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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

public class Rift implements IRift, Runnable, Listener {
    private int riftId;
    private int taskId;

    private RiftConfig config;

    private Location center;
    private int radius;
    private double timer;
    private int maxMonsters;

    private ArrayList<Monster> monsters;
    private ArrayList<Location> validLocations;

    public Rift(Location location, RiftConfig config) {
        this.center = location;
        CustomRiftSize size = config.getRiftSize();

        this.radius = size.radius();
        this.timer = size.length() * 60;
        this.maxMonsters = size.maxMonsters();

        this.monsters = new ArrayList<>();

        this.validLocations = new ArrayList<>();
        int diameter = (this.radius * 2) + 1;
        for (int x = this.center.getBlockX() - this.radius; x < diameter; x++) {
            for (int y = this.center.getBlockY() - this.radius; y < diameter; y++) {
                for (int z = this.center.getBlockZ() - this.radius; z < diameter; z++) {
                    Location current = new Location(this.center.getWorld(), x, y, z);
                    if (SpawnUtils.isValidLocation(current)) {
                        this.validLocations.add(current);
                    }
                }
            }
        }

        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Rifts.getInstance(), this, 0, 20); // 20 ticks == 1 second
        this.riftId = RiftManager.registerRift(this);
        Bukkit.getServer().getPluginManager().registerEvents(this, Rifts.getInstance());
    }

    public void run() {
        this.timer--;
        if (this.timer == 0) {
            this.death();
        }

        this.center.getWorld().spawnParticle(this.config.getCoreParticle(), this.center, 1); // TODO: remove this; mark the center of the rift

        if (this.validLocations.size() > 0 && this.monsters.size() < this.maxMonsters) {
            SpawnUtils.spawn(this.validLocations, 10, (location, count) ->
                location.getWorld().spawnParticle(this.config.getAmbientParticle(), location, 1));

            SpawnUtils.spawn(this.validLocations, 3, (location, count) -> {
                LivingEntity monster = (LivingEntity) location.getWorld().spawnEntity(location, MobUtils.getRandomMob(this.config.getMonsters()));
                List<PotionEffectType> effects = MobUtils.getRandomEffects(config.getPotionEffects(), config.getMaxPotionsApplied());

                for(PotionEffectType effect : effects) {
                    PotionEffect potion = new PotionEffect(effect, Integer.MAX_VALUE,1);
                    monster.addPotionEffect(potion);
                }
            });
        }
    }

    public void death() {
        // TODO: cancel drops & add particle effects

        new BukkitRunnable() {

            @Override
            public void run() {
                if(monsters.size() <= 0) {

                }
            }
        }.runTaskTimerAsynchronously(Rifts.getInstance(), 0, 5);

        for (Monster monster : this.monsters) {
            monster.addPotionEffect(new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 4, false, false));
        }
        Bukkit.getScheduler().cancelTask(this.taskId);
        RiftManager.unregisterRift(this.riftId);
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (MobUtils.listContainsMonster(this.monsters, entity)) {
            this.monsters.remove(entity);
        }
    }

    // prevent creepers and charged creepers from destroying blocks
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (MobUtils.listContainsMonster(this.monsters, event.getEntity())) {
            event.blockList().clear();
        }
    }
}
