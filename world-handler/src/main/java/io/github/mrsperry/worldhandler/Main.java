package io.github.mrsperry.worldhandler;

import io.github.mrsperry.worldhandler.listeners.EntityListener;
import io.github.mrsperry.worldhandler.listeners.WeatherListener;
import io.github.mrsperry.worldhandler.portals.PortalCommands;
import io.github.mrsperry.worldhandler.portals.PortalHandler;
import io.github.mrsperry.worldhandler.worlds.WorldCommands;

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

        this.saveDefaultConfig();

        PortalHandler.reloadPortals();

        Objects.requireNonNull(this.getCommand("world")).setExecutor(new WorldCommands());
        Objects.requireNonNull(this.getCommand("portal")).setExecutor(new PortalCommands());

        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new EntityListener(), this);
        manager.registerEvents(new WeatherListener(), this);
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
