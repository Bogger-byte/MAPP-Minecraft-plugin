package me.bogger.mapp;

import me.bogger.mapp.managers.PlayerManager;
import me.bogger.mapp.tasks.PublishPlayerData;
import me.bogger.mapp.utils.AnsiColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;


public final class Main extends JavaPlugin {

    public MappConfig mappConfig;

    @Override
    public void onEnable() {
        mappConfig = new MappConfig(this);
        PlayerManager playerManager = new PlayerManager(this);
        MappAPIServer mappAPIServer = new MappAPIServer(this, mappConfig);
        if (!mappAPIServer.checkCredentials()) return;

        PublishPlayerData publishPlayerData = new PublishPlayerData(this, mappConfig, mappAPIServer, playerManager);
        publishPlayerData.runTaskTimerAsynchronously(this, 0, 60);
    }

    @Override
    public void onDisable() {

    }

    public void log(Level level, String msg) {
        String ansiColor = "";
        switch (level.getName()) {
            case "OFF":
            case "WARNING":
            case "SEVERE":
                ansiColor = AnsiColor.RED;
                break;
            case "INFO":
            case "CONFIG":
                ansiColor = AnsiColor.YELLOW;
                break;
            case "FINE":
                ansiColor = AnsiColor.GREEN;
                break;
            case "FINER":
            case "FINEST":
                ansiColor = AnsiColor.GREEN + AnsiColor.Bold;
                break;
            case "ALL":
                ansiColor = AnsiColor.YELLOW + AnsiColor.Bold;
                break;
        }
        getServer().getLogger().log(level, "[mApp] " + ansiColor + msg + AnsiColor.RESET);
    }
}
