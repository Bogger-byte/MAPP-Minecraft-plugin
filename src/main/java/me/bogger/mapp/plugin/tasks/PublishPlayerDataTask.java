package me.bogger.mapp.plugin.tasks;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.bogger.mapp.plugin.Main;
import me.bogger.mapp.plugin.managers.PlayersManager;
import me.bogger.mapp.plugin.utils.logger.Level;
import me.bogger.mapp.plugin.utils.logger.Logger;
import me.bogger.mapp.service.MappAPI;
import org.apache.http.auth.AuthenticationException;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;


public class PublishPlayerDataTask extends BukkitRunnable {

    private final PlayersManager playersManager;
    private final MappAPI mappAPI;

    public PublishPlayerDataTask() {
        this.playersManager = Main.getInstance().getPlayersManager();
        this.mappAPI = Main.getInstance().getMappAPI();
    }

    private int publishAttempts = 0;

    @Override
    public void run() {
        JsonObject playersDataObject = playersManager.gatherPlayersData();
        try {
            mappAPI.publishPlayersData(playersDataObject);
        } catch (IOException e) {
            publishAttempts += 1;
            Logger.log(Level.WARNING, "Failed to connect to MAPP server");
            Logger.log(Level.INFO, "Error message: " + e.getMessage());
            Logger.log(Level.INFO, "[Attempt " + publishAttempts + "] Trying to connect again...");
        } catch (AuthenticationException e) {
            Logger.log(Level.WARNING, "Authentication failure. Check if field values of <api-key> and <server-ip> are valid");
            cancel();
        } catch (JsonSyntaxException e) {
            Logger.log(Level.WARNING, "Failed to parse response from MAPP server");
        }
    }
}
