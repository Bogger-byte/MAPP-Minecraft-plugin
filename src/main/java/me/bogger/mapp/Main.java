package me.bogger.mapp;

import me.bogger.mapp.commands.ForceVisibleCommand;
import me.bogger.mapp.listeners.RegionUpdateListener;
import me.bogger.mapp.listeners.RegionUpdateTrigger;
import me.bogger.mapp.managers.CommandsManager;
import me.bogger.mapp.managers.PlayersManager;
import me.bogger.mapp.managers.RegionsManager;
import me.bogger.mapp.tasks.PublishPlayerData;
import me.bogger.mapp.tasks.PublishRegionImages;
import me.bogger.mapp.utils.AnsiColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;


public final class Main extends JavaPlugin {

    public MappConfig mappConfig;

    private PlayersManager playersManager;
    private MappAPIServer mappAPIServer;

    private RegionsManager regionsManager;
    private MappImage mappImage;
    private CommandsManager commandsManager;

    private RegionUpdateTrigger regionUpdateTrigger;
    private RegionUpdateListener regionUpdateListener;

    @Override
    public void onLoad() {
        mappConfig = new MappConfig(this);

        mappAPIServer = new MappAPIServer(this, mappConfig);
        mappImage = new MappImage(this);

        regionsManager = new RegionsManager();
        playersManager = new PlayersManager(this, mappConfig);
        commandsManager = new CommandsManager(new ForceVisibleCommand(mappConfig));

        regionUpdateTrigger = new RegionUpdateTrigger(this);
        regionUpdateListener = new RegionUpdateListener(regionsManager);
    }

    @Override
    public void onEnable() {
        getCommand("force-visible").setExecutor(commandsManager);

        getServer().getPluginManager().registerEvents(regionUpdateTrigger, this);
        getServer().getPluginManager().registerEvents(regionUpdateListener, this);

        if (!mappConfig.checkConfigForRequiredFields()) return;
        int publishPlayerDataPeriod = mappConfig.getConfig().getInt("players-data-publish-period");
        int publishRegionImagesPeriod = mappConfig.getConfig().getInt("region-images-publish-period");

        log(Level.INFO, "Validating credentials...");
        if (!mappAPIServer.checkCredentials()) return;

        log(Level.INFO, "Starting data publishing tasks...");
        new PublishPlayerData(this, mappAPIServer, playersManager)
                .runTaskTimerAsynchronously(this, 20, publishPlayerDataPeriod);
        new PublishRegionImages(this, mappAPIServer, mappImage, regionsManager)
                .runTaskTimerAsynchronously(this, 20, publishRegionImagesPeriod);
    }

    @Override
    public void onDisable() {

    }

    public void log(Level level, String msg) {
        String messageColor = "";
        switch (level.getName()) {
            case "OFF":
            case "WARNING":
                messageColor = AnsiColor.RED;
                break;
            case "SEVERE":
                messageColor = AnsiColor.BACKGROUND_RED;
                break;
            case "INFO":
            case "CONFIG":
                messageColor = AnsiColor.YELLOW;
                break;
            case "FINE":
                messageColor = AnsiColor.GREEN;
                break;
            case "FINER":
            case "FINEST":
                messageColor = AnsiColor.GREEN + AnsiColor.HIGH_INTENSITY;
                break;
            case "ALL":
                messageColor = AnsiColor.BACKGROUND_YELLOW;
                break;
        }
        ConsoleCommandSender sender = getServer().getConsoleSender();
        sender.sendMessage("[mApp] " + messageColor + msg + AnsiColor.RESET);
    }
}
