package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.BossConfig;
import io.github.mrsperry.rifts.dungeons.Dungeon;
import io.github.mrsperry.rifts.configs.DungeonConfig;
import io.github.mrsperry.rifts.rifts.Rift;
import io.github.mrsperry.rifts.configs.RiftConfig;

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
}
