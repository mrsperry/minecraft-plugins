package io.github.mrsperry.rifts.dungeons;

import io.github.mrsperry.rifts.configs.DungeonConfig;
import io.github.mrsperry.rifts.utils.SpawnUtils;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Dungeon implements Listener {
    private DungeonConfig config;

    private World dungeonWorld;
    private ArrayList<Monster> monsters;
    private Monster boss;

    public Dungeon(DungeonConfig config) {
        this.config = config;

        this.monsters = new ArrayList<>();
        this.boss = null;
    }

    public void teleport(Player keyholder, String message) {
        HashSet<Player> nearby = new HashSet<>();
        nearby.add(keyholder);
        for (Player player : keyholder.getWorld().getPlayers()) {
            if (keyholder.getLocation().distance(player.getLocation()) <= 5) {
                nearby.add(player);
            }
        }

        for (Player player : nearby) {
            player.teleport(SpawnUtils.toLocation(this.config.getStartingLocation(), keyholder.getWorld()));
            player.sendMessage(message);
        }
    }

    public void spawnChest() {

    }

    public void spawnMonsters() {

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        
    }

    // prevent creepers and charged creepers from destroying blocks
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Monster) {
            if (this.monsters.contains(entity)) {
                event.blockList().clear();
            }
        }
     }
}
