package me.bogger.mapp;

import me.bogger.mapp.managers.PlayerManager;
import me.bogger.mapp.tasks.PublishPlayerData;
import me.bogger.mapp.utils.AnsiColor;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;


public final class Main extends JavaPlugin {

    public MappConfig mappConfig;

    @Override
    public void onEnable() {
        mappConfig = new MappConfig(this);
        PlayerManager playerManager = new PlayerManager(this);
        MappAPIServer mappAPIServer = new MappAPIServer(this, mappConfig);

        int publishPlayerDataPeriod = mappConfig.getConfig().getInt("players-data-publish-period");
        if (publishPlayerDataPeriod == 0) {
            log(Level.WARNING, "Config field <players-data-publish-period> is empty");
            return;
        }

        getServer().getScheduler().runTaskLaterAsynchronously(this, () -> {
            log(Level.INFO, "Validating credentials...");
            if (!mappAPIServer.checkCredentials()) return;

            PublishPlayerData publishPlayerData = new PublishPlayerData(this, mappConfig, mappAPIServer, playerManager);
            publishPlayerData.runTaskTimerAsynchronously(this, 20, publishPlayerDataPeriod);
        }, 0);
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
