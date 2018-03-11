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
    private int area;
    private int chance;
    private int max;

    public Timer(int area, int chance, int max) {
        this.area = area;
        this.chance = chance;
        this.max = max;
    }

    public void run() {
        if (RiftManager.getActiveRifts().size() < this.max) {
            if (Rifts.getRandom().nextInt(100) + 1 <= this.chance) {
                spawnRandomRift(this.area);
            }
        }
    }

    public static boolean spawnRift(Player player, int area, RiftConfig config) {
        Location location = SpawnUtils.getValidLocation(player.getLocation(), area, area, area);
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
            new Rift(location, config);
        }
        return true;
    }

    public static void spawnRandomRift(int area) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (players.size() != 0) {
            Player player = (Player) players.toArray()[Rifts.getRandom().nextInt(players.size())];
            spawnRift(player, area, RiftManager.getRandomRiftConfig());
        }
    }
}
