package me.bogger.mapp.tasks;

import com.google.gson.JsonObject;
import me.bogger.mapp.Main;
import me.bogger.mapp.MappAPIServer;
import me.bogger.mapp.managers.PlayersManager;
import org.apache.http.auth.AuthenticationException;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Level;

public class PublishPlayerData extends BukkitRunnable {

    private final Main plugin;
    private final PlayersManager playerManager;
    private final MappAPIServer mappAPIServer;

    public PublishPlayerData(Main plugin,
                             @NotNull MappAPIServer mappAPIServer,
                             @NotNull PlayersManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.mappAPIServer = mappAPIServer;
    }

    private int publishAttempts = 0;

    @Override
    public void run() {
        JsonObject playersDataObject = playerManager.gatherPlayersData();
        try {
            mappAPIServer.publishPlayersData(playersDataObject);
        } catch (AuthenticationException e) {
            plugin.log(Level.WARNING, "Authentication failure. Check if field values of <api-key> and <server-ip> are valid");
            cancel();
        } catch (IOException e) {
            publishAttempts += 1;
            plugin.log(Level.WARNING, "Failed to publish players data");
            plugin.log(Level.INFO, "Error message: " + e.getMessage());
            plugin.log(Level.INFO, "[Attempt " + publishAttempts + "] Trying to reconnect");
        }
    }
}
