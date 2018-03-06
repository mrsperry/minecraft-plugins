package io.github.mrsperry.rifts.rifts;

import io.github.mrsperry.rifts.Rifts;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class RiftSize {
    static RiftSize self;
    Map<String, CustomRiftSize> sizes;

    private RiftSize() {
        sizes = new HashMap<>();
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

        sizes.put(name.toLowerCase(), new CustomRiftSize(enabled, maxMonsters, length, radius));
    }

    public CustomRiftSize get(String name) {
        return sizes.containsKey(name.toLowerCase()) ? sizes.get(name.toLowerCase()) : null;
    }

    public CustomRiftSize getRandom() {
        List<CustomRiftSize> valid = new ArrayList<>();
        for(CustomRiftSize size : sizes.values()) {
            if(size.enabled()) {
                valid.add(size);
            }
        }

        return valid.get(Rifts.getRandom().nextInt(valid.size()));
    }

    public List<CustomRiftSize> list() {
        return new ArrayList<>(sizes.values());
    }

    public class CustomRiftSize {
        private boolean enabled;
        private int maxMonsters;
        private double length;
        private int radius;

        public CustomRiftSize(boolean enabled, int maxMonsters, double length, int radius) {
            this.enabled = enabled;
            this.maxMonsters = maxMonsters;
            this.length = length;
            this.radius = radius;
        }

        public boolean enabled() {
            return this.enabled;
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
    }
}
