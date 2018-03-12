package io.github.mrsperry.rifts.configs;

import io.github.mrsperry.rifts.meta.RiftSize;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class GeneralConfig {
    private static boolean riftsEnabled;

    private static String joinMessage;
    private static String createMessage;
    private static String deathMessage;

    private static int minArea;
    private static int maxArea;
    private static int chance;
    private static int tries;
    private static int frequency;
    private static int max;

    public static void initialize(FileConfiguration config) {
        riftsEnabled = config.getBoolean("rifts.enabled", true);

        joinMessage = config.getString("rifts.messages.join", "");
        createMessage = config.getString("rifts.messages.create", "");
        deathMessage = config.getString("rifts.messages.death", "");

        minArea = config.getInt("rifts.spawning.min-area", 50);
        maxArea = config.getInt("rifts.spawning.max-area", 100);
        if (minArea > maxArea) {
            minArea = 50;
            maxArea = 100;
            Bukkit.getLogger().warning("Maximum area is less than the minimum area a rift can spawn in; setting defaults");
        }
        chance = config.getInt("rifts.spawning.chance", 25);
        tries = config.getInt("rifts.spawning.tries", 100);
        frequency = config.getInt("rifts.spawning.frequency", 60);
        max = config.getInt("rifts.spawning.max", 3);

        for(String key : config.getConfigurationSection("rifts.spawning.size").getKeys(false)) {
            RiftSize.getInstance().register(key, config.getConfigurationSection("rifts.spawning.size." + key));
        }

        RiftSize.getInstance().list().forEach(size ->
            Bukkit.getLogger().info(size.toString())
        );
    }

    public static boolean areRiftsEnabled() {
        return riftsEnabled;
    }

    public static String getJoinMessage() {
        return joinMessage;
    }

    public static String getCreateMessage() {
        return createMessage;
    }

    public static String getDeathMessage() {
        return deathMessage;
    }

    public static int getMinArea() {
        return minArea;
    }

    public static int getMaxArea() {
        return maxArea;
    }

    public static int getRiftChance() {
        return chance;
    }

    public static int getRiftTries() {
        return tries;
    }

    public static int getRiftFrequency() {
        return frequency;
    }

    public static int getMaxRifts() {
        return max;
    }
}
