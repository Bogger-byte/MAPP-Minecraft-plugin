package me.bogger.mapp;

import me.bogger.mapp.managers.PlayerManager;
import me.bogger.mapp.tasks.PublishPlayerData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;


public final class Main extends JavaPlugin {

    public MappConfig mappConfig;

    @Override
    public void onEnable() {
        mappConfig = new MappConfig(this);
        PlayerManager playerManager = new PlayerManager(this);
        MappAPIServer mappAPIServer = new MappAPIServer(this, mappConfig);

        PublishPlayerData publishPlayerData = new PublishPlayerData(this, mappConfig, mappAPIServer, playerManager);

        publishPlayerData.runTaskTimerAsynchronously(this, 0, 60);
    }

    @Override
    public void onDisable() {

    }

    public void selfDisable(String reason) {
        getServer().getLogger().log(Level.OFF, reason);
        getServer().getScheduler().cancelTasks(this);
    }

    public void log(Level level, String msg) {
        getServer().getLogger().log(level, msg);
    }
}
