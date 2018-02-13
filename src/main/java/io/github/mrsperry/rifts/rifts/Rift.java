package io.github.mrsperry.rifts.rifts;

import io.github.mrsperry.rifts.Rifts;

import io.github.mrsperry.rifts.utils.MobUtils;
import io.github.mrsperry.rifts.utils.SpawnUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public abstract class Rift implements IRift, Runnable, Listener {
    private Location center;

    private int radius;
    private int timer;

    private ArrayList<Monster> monsters;
    private ArrayList<Location> validLocations;

    private int id;

    public Rift(Location location, RiftSize size) {
        this.center = location;

        this.radius = size.radius;
        this.timer = size.timer;

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

        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Rifts.getInstance(), this, 0, 20); // 20 ticks == 1 second
    }

    public void run() {
        this.timer--;
        if (this.timer == 0) {
            this.death();
        }

        this.center.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, this.center, 1); // mark the center of the rift
        
        SpawnUtils.spawn(this.validLocations, 10, (location, count) -> {
            location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 1);
        });

        SpawnUtils.spawn(this.validLocations, 3, (location, count) -> {
            location.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
        });

    }

    public void death() {
        // TODO: cancel drops & add particle effects
        for (Monster monster : this.monsters) {
            monster.addPotionEffect(new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 4, false, false));
        }
        Bukkit.getScheduler().cancelTask(this.id);
    }

    // TODO: repetitive event handlers with dungeons -- can we merge them somewhere?
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
