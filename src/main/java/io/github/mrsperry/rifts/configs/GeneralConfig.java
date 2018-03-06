package io.github.mrsperry.rifts.configs;

import io.github.mrsperry.rifts.rifts.RiftSize;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class GeneralConfig {
    private static boolean riftsEnabled;
    private static int area;
    private static int chance;
    private static int frequency;
    private static int max;

    public static void initialize(FileConfiguration config) {
        riftsEnabled = config.getBoolean("rifts.enabled", true);
        area = config.getInt("rifts.spawning.area", 50);
        chance = config.getInt("rifts.spawning.chance", 25);
        frequency = config.getInt("rifts.spawning.frequency", 60);
        max = config.getInt("rifts.spawning.max", 3);

        for(String key : config.getConfigurationSection("rifts.spawning.size").getKeys(false)) {
            RiftSize.getInstance().register(key, config.getConfigurationSection("rifts.spawning.size." + key));
        }

        //Testing to make sure loading works
        RiftSize.getInstance().list().forEach(size -> {
            Bukkit.getLogger().info(size.toString());
        });
    }

    public static boolean areRiftsEnabled() {
        return riftsEnabled;
    }

    public static int getRiftArea() {
        return area;
    }

    public static int getRiftChance() {
        return chance;
    }

    public static int getRiftFrequency() {
        return frequency;
    }

    public static int getMaxRifts() {
        return max;
    }
}
