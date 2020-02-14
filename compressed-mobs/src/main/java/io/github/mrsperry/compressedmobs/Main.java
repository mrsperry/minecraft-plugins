package io.github.mrsperry.compressedmobs;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {
    private Random random;

    private double chance;
    private int minYield;
    private int maxYield;

    private HashSet<EntityType> blacklist;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.random = new Random();

        FileConfiguration config = this.getConfig();
        this.chance = config.getDouble("global-chance", 100.0);
        this.minYield = config.getInt("global-min-yield", 3);
        this.maxYield = config.getInt("global-max-yield", 5);

        this.blacklist = new HashSet<>();
        if (config.isList("blacklist")) {
            for (String mob : config.getStringList("blacklist")) {
                try {
                    this.blacklist.add(EntityType.valueOf(mob.toUpperCase()));
                } catch (Exception ex) {
                    this.getLogger().severe("Invalid entity type: " + mob);
                }
            }
        }
        this.getLogger().info("Found " + this.blacklist.size() + " types on the blacklist");

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!this.blacklist.contains(event.getEntityType())) {
            Entity mob = event.getEntity();
            PersistentDataContainer container = mob.getPersistentDataContainer();

            if (container.has(new NamespacedKey(this, "compressed"), PersistentDataType.BYTE)) {
                int range = this.maxYield - this.minYield;
                int total = this.minYield + (range > 0 ? this.random.nextInt(range) : 0);
                for (int amount = 0; amount < total; amount++) {
                    Entity entity = mob.getWorld().spawnEntity(mob.getLocation(), mob.getType());
                    entity.setVelocity(new Vector(
                            (this.random.nextDouble() * 2) - 1,
                            (this.random.nextDouble() / 2),
                            (this.random.nextDouble() * 2) - 1));
                }
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!this.blacklist.contains(event.getEntityType())) {
            Entity creature = event.getEntity();
            PersistentDataContainer container = creature.getPersistentDataContainer();

            if (container.has(new NamespacedKey(this, "compressed"), PersistentDataType.BYTE)) {
                return;
            }

            if (this.random.nextDouble() * this.chance <= 1) {
                container.set(new NamespacedKey(this, "compressed"), PersistentDataType.BYTE, (byte) 1);

                creature.setCustomName(ChatColor.GRAY + "Compressed " + creature.getName());
                creature.setCustomNameVisible(true);
            }
        }
    }
}
