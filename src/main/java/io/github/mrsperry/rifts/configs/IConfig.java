package io.github.mrsperry.rifts.configs;

import java.util.List;

public interface IConfig {

    boolean load();
    boolean reload();
    int getInt(String path, int defaultValue);
    String getString(String path, String defaultValue);
    Double getDouble(String path, Double defaultValue);
    List<String> getStringList(String path);
}
