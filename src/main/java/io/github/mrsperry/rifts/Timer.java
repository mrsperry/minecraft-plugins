package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.GeneralConfig;
import io.github.mrsperry.rifts.configs.RiftConfig;
import io.github.mrsperry.rifts.meta.Rift;
import io.github.mrsperry.rifts.utils.SpawnUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class Timer implements Runnable {
    public void run() {
        if (RiftManager.getActiveRifts().size() < GeneralConfig.getMaxRifts()) {
            if (Main.getRandom().nextInt(100) + 1 <= GeneralConfig.getRiftChance()) {
                spawnRandomRift();
            }
        }
    }

    public static boolean spawnRift(Player player, RiftConfig config) {
        Location location = SpawnUtils.getValidLocation(player.getLocation(), GeneralConfig.getMinArea(), GeneralConfig.getMaxArea());
        if (location != null) {
            Bukkit.broadcastMessage("Location: " + location + "  Distance: " + player.getLocation().distance(location));
            new Rift(location, config);
        }
        return true;
    }

    public static void spawnRandomRift() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (players.size() != 0) {
            Player player = (Player) players.toArray()[Main.getRandom().nextInt(players.size())];
            spawnRift(player, RiftManager.getRandomRiftConfig());
        }
    }
}
