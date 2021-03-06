package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.GeneralConfig;
import io.github.mrsperry.rifts.commands.RiftCommands;
import io.github.mrsperry.rifts.meta.Rift;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Main extends JavaPlugin {
    private static Main instance;
    private static boolean riftsEnabled;
    private static Random random = new Random();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        GeneralConfig.initialize(this.getConfig());
        RiftManager.loadConfigs();

        riftsEnabled = GeneralConfig.areRiftsEnabled();

        getCommand("rifts").setExecutor(new RiftCommands());

        if (riftsEnabled) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new Timer(), 0, GeneralConfig.getRiftFrequency() * 20);
        }
    }

    @Override
    public void onDisable() {
        for (Rift rift : RiftManager.getActiveRifts()) {
            rift.end();
        }

        getConfig().set("enabled", riftsEnabled);
        saveConfig();
    }

    public static Main getInstance() {
        return instance;
    }

    public static boolean areRiftsEnabled() {
        return riftsEnabled;
    }

    public static void setRiftsEnabled(boolean isEnabled) {
        riftsEnabled = isEnabled;
    }

    public static Random getRandom() {
        return random;
    }
}
