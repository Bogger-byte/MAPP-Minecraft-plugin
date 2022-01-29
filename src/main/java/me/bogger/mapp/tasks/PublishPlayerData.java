package me.bogger.mapp.tasks;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import me.bogger.mapp.Main;
import me.bogger.mapp.MappAPIServer;
import me.bogger.mapp.MappConfig;
import me.bogger.mapp.managers.PlayerManager;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.logging.Level;

public class PublishPlayerData extends BukkitRunnable {

    private final Main plugin;
    private final PlayerManager playerManager;
    private final MappAPIServer mappAPIServer;

    private final int maxPublishAttempts;

    public PublishPlayerData(Main plugin,
                             @NotNull MappConfig mappConfig,
                             @NotNull MappAPIServer mappAPIServer,
                             @NotNull PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.mappAPIServer = mappAPIServer;
        this.maxPublishAttempts = mappConfig.getConfig().getInt("max-publish-attempts");
        if (this.maxPublishAttempts == 0) {
            plugin.log(Level.WARNING, "Config field <max-publish-attempts> is empty");
        }
    }

    private int publishAttempts = 0;

    @Override
    public void run() {
        JsonObject playerDataObject = playerManager.gatherPlayersData();
        try {
            StatusLine responseStatus = mappAPIServer.publishPlayersData(playerDataObject);
            if (responseStatus.getStatusCode() == 401) {
                plugin.log(Level.SEVERE, "Authentication failure. Check if field values of <api-key> and <server-ip> are valid");
                cancel();
            }
        } catch (ClientProtocolException e) {
            publishAttempts += 1;
            plugin.log(Level.INFO, "[Attempt " + publishAttempts + "] Trying to reconnect");
            if (publishAttempts >= maxPublishAttempts) {
                plugin.log(Level.WARNING, "Disabling due MAPP server problems");
                cancel();
            }
        } catch (IOException e) {
            plugin.log(Level.WARNING, "Failed to publish players data");
            plugin.log(Level.INFO, "Error message: " + e.getMessage());
        }
    }
}
