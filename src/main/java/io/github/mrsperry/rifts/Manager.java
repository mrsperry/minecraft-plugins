package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.BossConfig;
import io.github.mrsperry.rifts.dungeons.Dungeon;
import io.github.mrsperry.rifts.configs.DungeonConfig;
import io.github.mrsperry.rifts.rifts.Rift;
import io.github.mrsperry.rifts.configs.RiftConfig;

import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;

public class Manager {
    private static int currentDungeonId = 0;
    private static int currentRiftId = 0;

    private static HashMap<String, BossConfig> bossConfigs = new HashMap<>();
    private static HashMap<String, DungeonConfig> dungeonConfigs = new HashMap<>();
    private static HashMap<String, RiftConfig> riftConfigs = new HashMap<>();

    private static HashMap<Integer, Dungeon> activeDungeons = new HashMap<>();
    private static HashMap<Integer, Rift> activeRifts = new HashMap<>();

    public static BossConfig getBossConfig(String id) {
        return null;
    }

    public static DungeonConfig getDungeonConfig(String id) {
        return null;
    }

    public static RiftConfig getRiftConfig(String id) {
        return null;
    }

    public static int registerDungeon(Dungeon dungeon) {
        activeDungeons.put(++currentDungeonId, dungeon);
        return currentDungeonId;
    }

    public static int registerRift(Rift rift) {
        activeRifts.put(++currentRiftId, rift);
        return currentRiftId;
    }

    public static void unregisterDungeon(int id) {
        if (activeDungeons.containsKey(id)) {
            activeDungeons.remove(id);
            currentDungeonId--;
        }
    }

    public static void unregisterRift(int id) {
        if (activeRifts.containsKey(id)) {
            activeRifts.remove(id);
            currentRiftId--;
        }
    }

    public static void loadConfigs() {
        File base = new File(Rifts.getInstance().getDataFolder(), "configs");
        File riftConfigFolder = new File(base, "rifts");
        File dungeonConfigFolder = new File(base, "dungeons");
        File bossConfigFolder = new File(base, "bosses");

        //Load rift configs
        for(File riftFile : getFileList(riftConfigFolder)) {
            RiftConfig config = new RiftConfig("configs/rifts/" + riftFile.getName());
            if(config.loadValues()) {
                riftConfigs.put(config.getRiftID(), config);
                Bukkit.getLogger().info("\n\nLoaded rift config " + riftFile.getName());
                Bukkit.getLogger().info(config.toString());
            } else {
                Bukkit.getLogger().warning("Could not load Rift config " + riftFile.getName());
            }
        }

        //Load dungeon configs
        for(File dungeonFile : getFileList(dungeonConfigFolder)) {
            DungeonConfig config = new DungeonConfig("configs/dungeons/" + dungeonFile.getName());
            if(config.loadValues()) {
                dungeonConfigs.put(config.getDungeonID(), config);
                Bukkit.getLogger().info("\n\nLoaded rift config " + dungeonFile.getName());
                Bukkit.getLogger().info(config.toString());
            } else {
                Bukkit.getLogger().warning("Could not load Rift config " + dungeonFile.getName());
            }
        }

        //Load boss configs
        for(File bossFile : getFileList(bossConfigFolder)) {
            BossConfig config = new BossConfig("configs/bosses/" + bossFile.getName());
            if(config.loadValues()) {
                bossConfigs.put(config.getBossID(), config);
                Bukkit.getLogger().info("\n\nLoaded rift config " + bossFile.getName());
                Bukkit.getLogger().info(config.toString());
            } else {
                Bukkit.getLogger().warning("Could not load Rift config " + bossFile.getName());
            }
        }
    }

    private static File[] getFileList(File folder) {
        if(!folder.exists()) {
            Bukkit.getLogger().info(folder.getName() + " config folder not found, creating...");
            folder.mkdirs();
        }

        return folder.listFiles();
    }

    public static int getCurrentRiftId() {
        return currentRiftId;
    }
}
