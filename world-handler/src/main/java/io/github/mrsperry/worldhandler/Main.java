package io.github.mrsperry.worldhandler;

import io.github.mrsperry.worldhandler.listeners.EntityListener;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;

public class Main extends JavaPlugin {
    private static Main instance;

    private final Random random = new Random();

    @Override
    public void onEnable() {
        Main.instance = this;

        Objects.requireNonNull(this.getCommand("worlds")).setExecutor(new Commands());

        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new EntityListener(), this);
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
