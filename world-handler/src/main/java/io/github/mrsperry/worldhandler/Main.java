package io.github.mrsperry.worldhandler;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Main extends JavaPlugin {
    private static Main instance;

    private final Random random = new Random();

    @Override
    public void onEnable() {
        Main.instance = this;
    }

    @Override
    public void onDisable() {
        
    }

    public static Main getInstance() {
        return Main.instance;
    }

    public Random getRandom() {
        return this.random;
    }
}
