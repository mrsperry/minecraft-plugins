package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.RiftConfig;
import io.github.mrsperry.rifts.rifts.Rift;
import io.github.mrsperry.rifts.utils.SpawnUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        Bukkit.broadcastMessage("Location: " + location.toString() + "  Player: " + player.getName() + "  Area: " + area + "  Rift config: " + config.getRiftID());
        new Rift(location, config);
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
