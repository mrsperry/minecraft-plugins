package io.github.mrsperry.rifts.utils;

import io.github.mrsperry.rifts.Rifts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

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

    public static ArrayList<Location> getValidLocations(Location center, int xRadius, int yRadius, int zRadius) {
        ArrayList<Location> valids = new ArrayList<>();
        for (int x = center.subtract(xRadius, 0, 0).getBlockX(); x <= (xRadius * 2) + 1; x++) {
            for (int y = center.subtract(0, yRadius, 0).getBlockY(); y <= (yRadius * 2) + 1; y++) {
                for (int z = center.subtract(0, 0, zRadius).getBlockZ(); z <= (zRadius * 2) + 1; z++) {
                    Location location = new Location(center.getWorld(), x, y, z);
                    if (isValidLocation(location)) {
                        valids.add(location);
                    }
                }
            }
        }
        return valids;
    }

    public static boolean isValidLocation(Location location) {
        Block block = location.getBlock();
        if (block.getType() == Material.AIR) {
            Block below = block.getWorld().getBlockAt(location.subtract(0, 1, 0));
            Block above = block.getWorld().getBlockAt(location.add(0, 1, 1));
            return below.getType().isSolid() && !above.getType().isSolid(); // require 2 blocks of air & solid material below
        }
        return false;
    }
}
