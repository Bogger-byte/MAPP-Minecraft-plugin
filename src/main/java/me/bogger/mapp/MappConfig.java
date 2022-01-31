package me.bogger.mapp;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class MappConfig {

    private static final String fileName = "mappConfig.yml";

    private final Main plugin;

    public MappConfig(Main plugin) {
        this.plugin = plugin;

        tryCreateConfig();
    }

    private FileConfiguration config;
    private File configFile;

    public FileConfiguration getConfig() {
        if (config == null) tryCreateConfig();
        return this.config;
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.log(Level.CONFIG, "Failed to update config file");
        }
    }

    private void tryCreateConfig() {
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

    public boolean checkConfigForRequiredFields() {
        if (!configFile.exists()) return false;

        boolean success = true;
        if (config.get("mapp-root-host") == null) {
            plugin.log(Level.WARNING, "Required config field <mapp-root-host> is empty");
            success = false;
        }
        if (config.get("server-ip") == null) {
            plugin.log(Level.WARNING, "Required config field <server-ip> is empty");
            success = false;
        }
        if (config.get("api-key") == null) {
            plugin.log(Level.WARNING, "Required config field <api-key> is empty");
            success = false;
        }
        if (config.get("players-data-publish-period") == null) {
            plugin.log(Level.WARNING, "Required config field <players-data-publish-period> is empty");
            success = false;
        }
        if (config.get("max-publish-attempts") == null) {
            plugin.log(Level.WARNING, "Required config field <max-publish-attempts> is empty");
            success = false;
        }
        if (config.get("region-images-publish-period") == null) {
            plugin.log(Level.WARNING, "Required config field <region-images-publish-period> does not exist");
        }
        if (config.getList("force-visible-players") == null) {
            plugin.log(Level.WARNING, "Required config field <force-visible-players> does not exist");
            success = false;
        }
        return success;
    }
}
