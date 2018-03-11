package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.RiftConfig;
import io.github.mrsperry.rifts.rifts.Rift;
import io.github.mrsperry.rifts.rifts.RiftEffect;
import io.github.mrsperry.rifts.utils.SpawnUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class Timer implements Runnable {
    private int minArea;
    private int maxArea;
    private int chance;
    private int max;

    public Timer(int minArea, int maxArea, int chance, int max) {
        this.minArea = minArea;
        this.maxArea = maxArea;
        this.chance = chance;
        this.max = max;
    }

    public void run() {
        if (RiftManager.getActiveRifts().size() < this.max) {
            if (Rifts.getRandom().nextInt(100) + 1 <= this.chance) {
                spawnRandomRift(this.minArea, this.maxArea);
            }
        }
    }

    public static boolean spawnRift(Player player, int minArea, int maxArea, RiftConfig config) {
        Location location = SpawnUtils.getValidLocation(player.getLocation(), minArea, maxArea);
        if (location != null) {
            for (Location core : RiftEffect.getCoreLocations(location)) {
                if (core.getBlock().getType() != Material.AIR) {
                    return false;
                }
            }
            for (Location secondary : RiftEffect.getSecondaryLocations(location)) {
                if (secondary.getBlock().getType() != Material.AIR) {
                    return false;
                }
            }
            Bukkit.broadcastMessage("Location: " + location + "  Distance: " + player.getLocation().distance(location));
            new Rift(location, config);
        }
        return true;
    }

    public static void spawnRandomRift(int minArea, int maxArea) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (players.size() != 0) {
            Player player = (Player) players.toArray()[Rifts.getRandom().nextInt(players.size())];
            spawnRift(player, minArea, maxArea, RiftManager.getRandomRiftConfig());
        }
    }
}
