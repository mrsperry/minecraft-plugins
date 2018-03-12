package io.github.mrsperry.rifts.meta;

import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class RiftSize {
    public static RiftSize self;

    private Map<String, CustomRiftSize> sizes;

    private RiftSize() {
        this.sizes = new HashMap<>();
    }

    public static RiftSize getInstance() {
        if(self == null) {
            self = new RiftSize();
        }
        return self;
    }

    public void register(String name, ConfigurationSection section) {
        boolean enabled = section.getBoolean("enabled", true);
        int maxMonsters = section.getInt("max-monsters", 25);
        double length = section.getDouble("length", 5);
        int radius = section.getInt("radius", 25);

        this.sizes.put(name.toLowerCase(), new CustomRiftSize(name, enabled, maxMonsters, length, radius));
    }

    public CustomRiftSize get(String name) {
        return this.sizes.getOrDefault(name, null);
    }

    public List<CustomRiftSize> list() {
        return new ArrayList<>(this.sizes.values());
    }

    public class CustomRiftSize {
        private String name;
        private boolean enabled;
        private int maxMonsters;
        private double length;
        private int radius;

        public CustomRiftSize(String name, boolean enabled, int maxMonsters, double length, int radius) {
            this.name = name;
            this.enabled = enabled;
            this.maxMonsters = maxMonsters;
            this.length = length;
            this.radius = radius;
        }

        public int maxMonsters() {
            return this.maxMonsters;
        }

        public double length() {
            return this.length;
        }

        public int radius() {
            return this.radius;
        }

        @Override
        public String toString() {
            return  "[" + this.name + ","
                    + " [Enabled: " + this.enabled + ","
                    + " Max Monsters: " + this.maxMonsters + ","
                    + " Length: " + this.length + ","
                    + " Radius: " + this.radius + "]]";
        }
    }
}
