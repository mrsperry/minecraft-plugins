package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.GeneralConfig;

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

        getServer().getScheduler().scheduleSyncRepeatingTask(this,
                new Timer(GeneralConfig.getRiftArea(),
                    GeneralConfig.getRiftChance(),
                    GeneralConfig.getMaxRifts()),
                    0, GeneralConfig.getRiftFrequency() * 20);
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
