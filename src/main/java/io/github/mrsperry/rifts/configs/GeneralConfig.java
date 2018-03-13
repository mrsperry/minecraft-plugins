package io.github.mrsperry.rifts.configs;

import com.google.common.collect.Lists;

import io.github.mrsperry.rifts.meta.RiftSize;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class GeneralConfig {
    private static boolean riftsEnabled;

    private static String joinMessage;
    private static ArrayList<String> startMessages;
    private static ArrayList<String> stopMessages;

    private static Sound startSound;
    private static float startVolume;
    private static Sound stopSound;
    private static float stopVolume;

    private static int minArea;
    private static int maxArea;
    private static int chance;
    private static int tries;
    private static int frequency;
    private static int max;

    public static void initialize(FileConfiguration config) {
        riftsEnabled = config.getBoolean("enabled", true);

        joinMessage = config.getString("join", "");
        startMessages = Lists.newArrayList();
        if (config.isList("messages.start-messages")) {
            startMessages.addAll(config.getStringList("messages.start-messages"));
        }
        stopMessages = Lists.newArrayList();
        if (config.isList("messages.stop-messages")) {
            stopMessages.addAll(config.getStringList("messages.stop-messages"));
        }

        String[] startSoundString = config.getString("sounds.start", "block_end_portal_spawn:0.5").split(":");
        try {
            startSound = Sound.valueOf(startSoundString[0].toUpperCase());
            startVolume = Float.parseFloat(startSoundString[1]);
        } catch (Exception ex) {
            Bukkit.getLogger().warning("Error loading rift start sound or volume!");
        }
        String[] stopSoundString = config.getString("sounds.stop", "block_portal_travel:0.5").split(":");
        try {
            stopSound = Sound.valueOf(stopSoundString[0].toUpperCase());
            stopVolume = Float.parseFloat(stopSoundString[1]);
        } catch (Exception ex) {
            Bukkit.getLogger().warning("Error loading rift stop sound or volume!");
        }

        minArea = config.getInt("min-area", 50);
        maxArea = config.getInt("max-area", 100);
        if (minArea > maxArea) {
            minArea = 50;
            maxArea = 100;
            Bukkit.getLogger().warning("Maximum area is less than the minimum area a rift can spawn in; setting defaults");
        }
        chance = config.getInt("chance", 25);
        tries = config.getInt("tries", 100);
        frequency = config.getInt("frequency", 60);
        max = config.getInt("max", 3);

        for(String key : config.getConfigurationSection("sizes").getKeys(false)) {
            RiftSize.getInstance().register(key, config.getConfigurationSection("sizes." + key));
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

    public static ArrayList<String> getStartMessages() {
        return startMessages;
    }

    public static ArrayList<String> getStopMessages() {
        return stopMessages;
    }

    public static Sound getStartSound() {
        return startSound;
    }

    public static float getStartVolume() {
        return startVolume;
    }

    public static Sound getStopSound() {
        return stopSound;
    }

    public static float getStopVolume() {
        return stopVolume;
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
