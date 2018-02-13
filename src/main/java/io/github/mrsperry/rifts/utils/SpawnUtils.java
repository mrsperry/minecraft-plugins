package io.github.mrsperry.rifts.utils;

import io.github.mrsperry.rifts.Rifts;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

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

    public static Location toLocation(String location, World world) {
        String[] locationPart = location.split(",");

        return new Location(
                world,
                Double.parseDouble(locationPart[0]),
                Double.parseDouble(locationPart[1]),
                Double.parseDouble(locationPart[2])
        );
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
