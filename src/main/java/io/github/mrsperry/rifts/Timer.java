package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.rifts.Rift;

import io.github.mrsperry.rifts.rifts.RiftSize;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Timer implements Runnable {
    private int chance;

    public Timer(int chance) {
        this.chance = chance;
    }

    public void run() {
        if (Rifts.getRandom().nextInt(100) + 1 <= this.chance) {
            new Rift(new Location(Bukkit.getWorld("world"), 0, 30, 0), RiftSize.Small);
            Bukkit.broadcastMessage("Creating new rift...");
        }
    }
}
