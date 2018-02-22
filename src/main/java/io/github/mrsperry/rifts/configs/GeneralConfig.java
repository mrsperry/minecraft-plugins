package io.github.mrsperry.rifts.configs;

import io.github.mrsperry.rifts.rifts.RiftSize;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class GeneralConfig {
    private static boolean riftsEnabled;
    private static int area;
    private static int chance;
    private static int frequency;
    private static int max;
    // TODO: implement dynamic sizes
    private static ArrayList<RiftSize> sizes = new ArrayList<>();

    public static void initialize(FileConfiguration config) {
        riftsEnabled = config.getBoolean("rifts.enabled", true);
        area = config.getInt("rifts.area", 50);
        chance = config.getInt("rifts.chance", 25);
        frequency = config.getInt("rifts.frequency", 60);
        max = config.getInt("rifts.max", 3);
        sizes.add(RiftSize.Small);
        sizes.add(RiftSize.Medium);
        sizes.add(RiftSize.Large);
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

    public static ArrayList<RiftSize> getRiftSizes() {
        return sizes;
    }
}
