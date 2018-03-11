package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.rifts.Rift;
import io.github.mrsperry.rifts.configs.RiftConfig;

import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class RiftManager {
    private static int currentRiftId = 0;

    private static HashMap<String, RiftConfig> riftConfigs = new HashMap<>();

    private static HashMap<Integer, Rift> activeRifts = new HashMap<>();

    public static RiftConfig getRiftConfig(String id) {
        return null;
    }

    public static RiftConfig getRandomRiftConfig() {
        int index = Rifts.getRandom().nextInt(riftConfigs.values().size());
        return (RiftConfig) riftConfigs.values().toArray()[index];
    }

    public static int registerRift(Rift rift) {
        activeRifts.put(++currentRiftId, rift);
        return currentRiftId;
    }

    public static void unregisterRift(int id) {
        if (activeRifts.containsKey(id)) {
            activeRifts.remove(id);
        }
    }

    public static void loadConfigs() {
        File base = new File(Rifts.getInstance().getDataFolder(), "configs");

        int count = 0;
        for(File riftFile : getFileList(base)) {
            RiftConfig config = new RiftConfig("configs/" + riftFile.getName());
            if(config.loadValues()) {
                String id = config.getRiftID();
                if (riftConfigs.keySet().contains(id)) {
                    Bukkit.getLogger().warning("Rift ID for two config files are identical: " + id);
                    break;
                }
                riftConfigs.put(id, config);
            } else {
                Bukkit.getLogger().warning("Could not load rift config: " + riftFile.getName());
            }
        }
        Bukkit.getLogger().info("Loaded " + count + " rift config file(s)");
    }

    private static File[] getFileList(File folder) {
        if(!folder.exists()) {
            Bukkit.getLogger().info(folder.getName() + " config folder not found, creating...");
            folder.mkdirs();
        }

        return folder.listFiles();
    }

    public static Rift getRiftById(int id) {
        return activeRifts.getOrDefault(id, null);
    }

    public static HashSet<Rift> getActiveRifts() {
        HashSet<Rift> rifts = new HashSet<>();
        rifts.addAll(activeRifts.values());
        return rifts;
    }

    public static int getCurrentRiftId() {
        return currentRiftId;
    }
}
