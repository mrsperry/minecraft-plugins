package io.github.mrsperry.rifts.utils;

import io.github.mrsperry.rifts.Rifts;
import io.github.mrsperry.rifts.configs.GeneralConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Random;

public class SpawnUtils {
    public interface ISpawn {
        void execute(Location location, int count);
    }

    public static void spawn(List<Location> validLocations, int maxCount, ISpawn operation){
        for (int count = 0; count < maxCount; count++) {
            Location location = validLocations.get(Rifts.getRandom().nextInt(validLocations.size()));
            if (location != null) {
                // TODO: spawn correct monster type/amount
                operation.execute(location, count);
            }
        }
    }

    public static Location getValidLocation(Location center, int xRadius, int yRadius, int zRadius) {
        Random random = Rifts.getRandom();
        Location location;
        int tries = 0;
        do {
            int x = random.nextInt((xRadius * 2) + 1);
            int y = random.nextInt((yRadius * 2) + 1);
            int z = random.nextInt((zRadius * 2) + 1);
            location = new Location(center.getWorld(),
                    center.getBlockX() + (x - xRadius),
                    center.getBlockY() + (y - yRadius),
                    center.getBlockZ() + (z - zRadius));
            tries++;
        } while (!isValidLocation(location) && tries <= GeneralConfig.getRiftTries());
        Bukkit.broadcastMessage("Valid: " + location);
        return location;
    }

    public static boolean isValidLocation(Location location) {
        Block block = location.getBlock();
        if (block.getType() == Material.AIR) {
            Block below = block.getWorld().getBlockAt(location.subtract(0, 1, 0));
            Block above = block.getWorld().getBlockAt(location.add(0, 1, 0));
            return below.getType().isSolid() && !above.getType().isSolid(); // require 2 blocks of air & solid material below
        }
        return false;
    }
}
