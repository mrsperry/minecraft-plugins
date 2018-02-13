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
        Entity entity = event.getEntity();
        if (this.isDungeonMonster(entity)) {
            monsters.remove(entity);
        }
    }

    // prevent creepers and charged creepers from destroying blocks
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (this.isDungeonMonster(event.getEntity())) {
            event.blockList().clear();
        }
    }

     private boolean isDungeonMonster(Entity entity) {
         if (entity instanceof Monster) {
             return this.monsters.contains(entity);
         }
         return false;
     }
}
