package io.github.mrsperry.rifts.dungeons;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashSet;

public abstract class Dungeon implements Listener {
    private DungeonConfig config;

    private HashSet<Monster> monsters;
    private Monster boss;

    public Dungeon(DungeonConfig config, Player keyholder) {
        this.config = config;

        this.monsters = new HashSet<Monster>();
        this.boss = null;
    }

    public void teleport(Player keyholder, String message) {
        HashSet<Player> nearby = new HashSet<Player>();
        nearby.add(keyholder);
        for (Player player : keyholder.getWorld().getPlayers()) {
            if (keyholder.getLocation().distance(player.getLocation()) <= 5) {
                nearby.add(player);
            }
        }

        for (Player player : nearby) {
            player.teleport(config.getStartingLocation());
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

    }
}
