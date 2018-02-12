package io.github.mrsperry.rifts.rifts;

import io.github.mrsperry.rifts.Rifts;

import io.github.mrsperry.rifts.utils.SpawnUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

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
                    if (this.isValidLocation(current)) {
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

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Monster) {
            if (this.monsters.contains(entity)) {
                this.monsters.remove(entity);
            }
        }
    }

    public boolean isValidLocation(Location location) {
        Block block = location.getBlock();
        if (block.getType() == Material.AIR) {
            Block below = block.getWorld().getBlockAt(location.subtract(0, 1, 0));
            Block above = block.getWorld().getBlockAt(location.add(0, 1, 1));
            return below.getType().isSolid() && !above.getType().isSolid(); // require 2 blocks of air & solid material below
        }
        return false;
    }

    public void death() {
        for (Monster monster : monsters) {
            monster.remove();
        }
        Bukkit.getScheduler().cancelTask(this.id);
    }
}
