package io.github.mrsperry.rifts.configs;

import io.github.mrsperry.rifts.Rifts;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class BasicConfig implements IConfig {
    private Plugin riftsPlugin;
    private String path;
    private FileConfiguration fileConfiguration;

    public BasicConfig(String path) {
        this.path = path;
        this.riftsPlugin = Rifts.getInstance();
        load();
    }

    @Override
    public boolean load() {
        File file = new File(riftsPlugin.getDataFolder(), path);

        if(file.exists()) {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
            return true;
        }
        return false;
    }

    @Override
    public boolean reload() {
        this.fileConfiguration = null;
        return load();
    }

    @Override
    public int getInt(String path, int defaultValue) {
        return this.fileConfiguration.getInt(path, defaultValue);
    }

    @Override
    public String getString(String path, String defaultValue) {
        return this.fileConfiguration.getString(path, defaultValue);
    }

    @Override
    public List<String> getStringList(String path) {
        return this.fileConfiguration.getStringList(path);
    }
}
