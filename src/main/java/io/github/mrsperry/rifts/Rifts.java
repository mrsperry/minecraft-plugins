package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.GeneralConfig;

import io.github.mrsperry.rifts.rifts.RiftCommands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Rifts extends JavaPlugin {
    private static Rifts instance;
    private static Random random = new Random();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        GeneralConfig.initialize(this.getConfig());
        RiftManager.loadConfigs();

        getCommand("spawnrift").setExecutor(new RiftCommands());
        getCommand("endrift").setExecutor(new RiftCommands());
        getCommand("riftids").setExecutor(new RiftCommands());

        if (GeneralConfig.areRiftsEnabled()) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this,
                    new Timer(GeneralConfig.getRiftArea(),
                            GeneralConfig.getRiftChance(),
                            GeneralConfig.getMaxRifts()),
                            0, GeneralConfig.getRiftFrequency() * 20);
        }
    }

    @Override
    public void onDisable() {

    }

    public static Rifts getInstance() {
        return instance;
    }

    public static Random getRandom() {
        return random;
    }
}
