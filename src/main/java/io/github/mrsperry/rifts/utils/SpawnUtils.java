package io.github.mrsperry.rifts.utils;

import io.github.mrsperry.rifts.Main;
import io.github.mrsperry.rifts.configs.GeneralConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SpawnUtils {
    public interface ISpawn {
        void execute(Location location, int count);
    }

    public static void spawn(List<Location> validLocations, int maxCount, ISpawn operation){
        for (int count = 0; count < maxCount; count++) {
            Location location = validLocations.get(Main.getRandom().nextInt(validLocations.size()));
            if (location != null) {
                operation.execute(location, count);
            }
        }
    }

    public static Location getValidLocation(Location center, int minArea, int maxArea) {
        Map<Location, Material> original = new HashMap<>();
        Random random = Main.getRandom();
        Location location;
        int tries = 0;
        do {
            int difference = maxArea - minArea;

            //Finding point within a circle of radius 1
            double t = 2 * Math.PI * random.nextDouble();
            double u = random.nextDouble() + random.nextDouble();
            double r = (u > 1) ? 2-u : u;
            double dPrime;

            // x coordingate in circle radius 1
            double x = r * Math.cos(t);
            //x coordinate in circle radius (range)
            x = x * difference + center.getX();

            //z coordinate in circle radius 1
            double z = r * Math.sin(t);
            //z coordingate in circle radius (range)
            z = z * difference + center.getZ();

            //distance from point to center
            dPrime = center.distance(new Location(center.getWorld(), x, center.getY(), z));

            //getting point on line from center and generated point minArea away from generated point
            // 1 / dPrime * distance is the distance to make one step on this line, multiply by minArea to get atleast that far away from center
            x = x + (minArea / dPrime) * (x - center.getX());
            z = z + (minArea / dPrime) * (z - center.getZ());

            //get y value for that x,z coordinate
            double y = center.getWorld().getHighestBlockYAt((int)x, (int)z);

            location = new Location(center.getWorld(), x, y, z);
            tries++;
            original.put(location, location.getBlock().getType());
            //location.getBlock().setType(Material.WOOL);
        } while (!isValidLocation(location) && tries <= GeneralConfig.getRiftTries());
        return isValidLocation(location) ? location : null;
    }

    public static boolean isValidLocation(Location location) {
        Block block = location.getBlock();
        if (block.getType() == Material.AIR) {
            Block below = block.getWorld().getBlockAt(location.subtract(0, 1, 0));
            Block above = block.getWorld().getBlockAt(location.add(0, 1, 0));
            return below.getType().isSolid() && !above.getType().isSolid();
        }
        return false;
    }
}
