package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.RiftConfig;
import io.github.mrsperry.rifts.rifts.Rift;
import io.github.mrsperry.rifts.utils.SpawnUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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

    public static void spawnRift(Player player, int area, RiftConfig config) {
        ArrayList<Location> valids = SpawnUtils.getValidLocations(player.getLocation(), area, area, area);
        Location location = valids.get(Rifts.getRandom().nextInt(valids.size()));
        new Rift(location, config);
    }

    public static void spawnRandomRift(int area) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        Player player = (Player) players.toArray()[Rifts.getRandom().nextInt(players.size())];
        spawnRift(player, area, RiftManager.getRandomRiftConfig());
    }
}
