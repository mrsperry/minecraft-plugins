package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.rifts.IRift;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Random;

public class Rifts extends JavaPlugin {
    private static Random random = new Random();

    private static HashSet<IRift> rifts = new HashSet<IRift>();

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

    public static HashSet<IRift> getRifts() {
        return rifts;
    }
}
