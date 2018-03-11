package io.github.mrsperry.rifts.configs;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Set;

public interface IConfig {
    boolean load();
    boolean reload();
    int getInt(String path, int defaultValue);
    boolean getBoolean(String path, boolean defaultValue);
    String getString(String path, String defaultValue);
    List<String> getStringList(String path);
    Set<String> getKeys(String path, boolean deep);
    ConfigurationSection getConfigurationSection(String path);
}
