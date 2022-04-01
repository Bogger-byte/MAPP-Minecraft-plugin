package me.bogger.mapp.plugin;

import com.google.gson.JsonSyntaxException;
import me.bogger.mapp.plugin.commands.GeneralMappCommand;
import me.bogger.mapp.plugin.commands.subCommands.ForceVisibleSC;
import me.bogger.mapp.plugin.listeners.RegionUpdateEventProducer;
import me.bogger.mapp.plugin.listeners.RegionUpdateListener;
import me.bogger.mapp.plugin.managers.PlayersManager;
import me.bogger.mapp.plugin.managers.RegionsManager;
import me.bogger.mapp.plugin.tasks.PublishPlayerDataTask;
import me.bogger.mapp.plugin.tasks.PublishServerInfoTask;
import me.bogger.mapp.plugin.tasks.PublishUpdatedRegionsTask;
import me.bogger.mapp.plugin.utils.logger.Level;
import me.bogger.mapp.plugin.commands.subCommands.PublishAllRegionsSC;
import me.bogger.mapp.plugin.managers.ConfigManager;
import me.bogger.mapp.plugin.utils.logger.Logger;
import me.bogger.mapp.renderer.Renderer;
import me.bogger.mapp.renderer.Renderer_1_16_5;
import me.bogger.mapp.service.MappAPI;
import me.bogger.mapp.service.MappAPIv1;
import org.apache.http.auth.AuthenticationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public final class Main extends JavaPlugin {

    private ConfigManager configManager;
    private PlayersManager playersManager;

    private MappAPI mappAPI;
    private Renderer renderer;

    private RegionsManager regionsManager;

    private RegionUpdateEventProducer regionUpdateEventProducer;
    private RegionUpdateListener regionUpdateListener;

    @Override
    public void onLoad() {
        configManager = new ConfigManager();

        mappAPI = new MappAPIv1(
                configManager.getConfig().getString("mapp-root-host"),
                configManager.getConfig().getString("username"),
                configManager.getConfig().getString("password"),
                configManager.getConfig().getString("client-id"),
                configManager.getConfig().getString("client-secret"));
        renderer = new Renderer_1_16_5();
        regionsManager = new RegionsManager();
        playersManager = new PlayersManager();
        regionUpdateEventProducer = new RegionUpdateEventProducer();
        regionUpdateListener = new RegionUpdateListener();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(regionUpdateEventProducer, this);
        getServer().getPluginManager().registerEvents(regionUpdateListener, this);

        GeneralMappCommand generalMappCommand = new GeneralMappCommand(new PublishAllRegionsSC(), new ForceVisibleSC());
        Objects.requireNonNull(getCommand("mapp")).setExecutor(generalMappCommand);


        List<String> missingFields = configManager.getRequiredMissingFields();
        if (!missingFields.isEmpty()) {
            missingFields.forEach(field -> Logger.log(Level.WARNING, "Required config field <" + field + "> is empty"));
            return;
        }

        try {
            mappAPI.ping();
            Logger.log(Level.FINE, "Connected to MAPP server");
        } catch (IOException e) {
            Logger.log(Level.WARNING, "Failed to connect to MAPP server");
            return;
        }

        try {
            mappAPI.authorize();
            Logger.log(Level.FINER, "Successfully authorized as " + mappAPI.getClientData());
        } catch (IOException e) {
            Logger.log(Level.WARNING, "Failed to connect to MAPP server");
            return;
        } catch (AuthenticationException e) {
            Logger.log(Level.WARNING, "Authentication failure. Check if field values of <api-key> and <server-ip> are valid");
            return;
        } catch (JsonSyntaxException e) {
            Logger.log(Level.WARNING, "Failed to parse response from MAPP server");
            e.printStackTrace();
            return;
        }

        if (configManager.getConfig().getBoolean("publish-players-data")) {
            new PublishPlayerDataTask().runTaskTimerAsynchronously(this, 20,
                    configManager.getConfig().getInt("players-data-publish-period"));
        }
        if (configManager.getConfig().getBoolean("publish-regions-data")) {
            new PublishUpdatedRegionsTask().runTaskTimerAsynchronously(this, 20,
                    configManager.getConfig().getInt("region-images-publish-period"));
        }
        if (configManager.getConfig().getBoolean("publish-server-info")) {
            new PublishServerInfoTask().runTaskTimerAsynchronously(this, 20,
                    configManager.getConfig().getInt("server-info-publish-period"));
        }

        Logger.log(Level.FINE, "Publishing started");
    }

    @NotNull
    public static Main getInstance() {
        return Main.getPlugin(Main.class);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayersManager getPlayersManager() {
        return playersManager;
    }

    public MappAPI getMappAPI() {
        return mappAPI;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public RegionsManager getRegionsManager() {
        return regionsManager;
    }

}
