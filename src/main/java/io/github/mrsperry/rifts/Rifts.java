package io.github.mrsperry.rifts;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Rifts extends JavaPlugin {
    private static Random random = new Random();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    public static Rifts getInstance() {
        return Rifts.getInstance();
    }

    public static Random getRandom() {
        return random;
    }
}
