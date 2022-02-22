package me.bogger.mapp.plugin.managers;

import me.bogger.mapp.plugin.utils.logger.Logger;
import me.bogger.mapp.plugin.Main;
import me.bogger.mapp.plugin.utils.logger.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigManager {

    private static final String fileName = "mappConfig.yml";

    private final Plugin plugin;
    private final List<String> requiredFields = Arrays.asList(
            "mapp-root-host",
            "username",
            "password",
            "client-id",
            "client-secret",
            "publish-players-data",
            "players-data-publish-period",
            "publish-regions-data",
            "region-images-publish-period"
    );

    public ConfigManager() {
        this.plugin = Main.getInstance();
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
            Logger.log(Level.CONFIG, "Failed to update config file");
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

    public List<String> getRequiredMissingFields() {
        if (!configFile.exists()) {
            return Collections.emptyList();
        }

        return requiredFields.stream()
                .filter(field -> config.get(field) == null)
                .collect(Collectors.toList());
    }
}
