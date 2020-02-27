package io.github.mrsperry.worldhandler;

import com.google.common.collect.Lists;
import io.github.mrsperry.mcutils.ConfigManager;
import io.github.mrsperry.worldhandler.listeners.EntityListener;
import io.github.mrsperry.worldhandler.listeners.TeleportListener;
import io.github.mrsperry.worldhandler.listeners.WeatherListener;
import io.github.mrsperry.worldhandler.portals.PortalCommands;
import io.github.mrsperry.worldhandler.portals.PortalHandler;
import io.github.mrsperry.worldhandler.worlds.WorldCommands;
import io.github.mrsperry.worldhandler.worlds.WorldHandler;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;

public class Main extends JavaPlugin {
    private static Main instance;

    private final Random random = new Random();
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        Main.instance = this;

        this.saveDefaultConfig();

        WorldHandler.initialize(this.getConfig());
        PortalHandler.reloadPortals();

        this.configManager = new ConfigManager(this, Lists.newArrayList("inventories"));

        Objects.requireNonNull(this.getCommand("world")).setExecutor(new WorldCommands());
        Objects.requireNonNull(this.getCommand("portal")).setExecutor(new PortalCommands());

        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new EntityListener(), this);
        manager.registerEvents(new WeatherListener(), this);
        manager.registerEvents(new TeleportListener(this.configManager), this);
    }

    @Override
    public void onDisable() {
        WorldHandler.save(this.getConfig());
        this.configManager.saveAllConfigs();
        this.saveConfig();
    }

    public static Main getInstance() {
        return Main.instance;
    }

    public Random getRandom() {
        return this.random;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }
}
