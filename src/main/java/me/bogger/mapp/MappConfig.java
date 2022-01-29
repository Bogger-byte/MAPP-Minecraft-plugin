package me.bogger.mapp;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MappConfig {

    private final String fileName = "mappConfig.yml";

    private final Plugin plugin;

    public MappConfig(Plugin plugin) {
        this.plugin = plugin;
        createConfig();
    }

    private FileConfiguration config;
    private File configFile;

    public void reloadConfig() {
        if (configFile == null)
            configFile = new File(plugin.getDataFolder(), fileName);

        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultStream = plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream));
            config.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) createConfig();
        return this.config;
    }

    private void createConfig() {
        configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
