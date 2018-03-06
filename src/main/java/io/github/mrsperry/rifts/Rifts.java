package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.GeneralConfig;
import io.github.mrsperry.rifts.rifts.IRift;

import io.github.mrsperry.rifts.rifts.RiftSize;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Random;

public class Rifts extends JavaPlugin {
    private static Rifts instance;

    private static Random random = new Random();

    private static HashSet<IRift> rifts = new HashSet<IRift>();

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

    public static HashSet<IRift> getRifts() {
        return rifts;
    }
}
