package io.github.mrsperry.rifts.utils;

import io.github.mrsperry.rifts.Main;
import io.github.mrsperry.rifts.configs.GeneralConfig;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

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
            int x = random.nextInt((difference * 2) + 1) - difference;
            int z = random.nextInt((difference * 2) + 1) - difference;
            location = new Location(center.getWorld(),
                    center.getBlockX() + (x + (x >= 0 ? + minArea : - minArea)),
                    center.getWorld().getHighestBlockYAt(center) + 1,
                    center.getBlockZ() + (z + (z >= 0 ? + minArea : - minArea)));
            tries++;
            original.put(location, location.getBlock().getType());
            location.getBlock().setType(Material.WOOL);
        } while (!isValidLocation(location) && tries <= GeneralConfig.getRiftTries());
        cleanupdebug(original);
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

    private static void cleanupdebug(Map<Location, Material> org) {
        new BukkitRunnable() {

            @Override
            public void run() {
                for(Location loc : org.keySet()) {
                    loc.getBlock().setType(org.get(loc));
                }
            }
        }.runTaskLater(Main.getInstance(), 100);
    }
}
