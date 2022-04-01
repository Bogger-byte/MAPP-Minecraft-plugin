package me.bogger.mapp.plugin.tasks;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.bogger.mapp.plugin.Main;
import me.bogger.mapp.plugin.utils.logger.Level;
import me.bogger.mapp.plugin.utils.logger.Logger;
import me.bogger.mapp.service.MappAPI;
import org.apache.http.auth.AuthenticationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class PublishServerInfoTask extends BukkitRunnable {
    private final Plugin plugin;
    private final MappAPI mappAPI;

    public PublishServerInfoTask() {
        this.plugin = Main.getInstance();
        this.mappAPI = Main.getInstance().getMappAPI();
    }

    @Override
    public void run() {
        JsonObject serverInfoObject = new JsonObject();
        serverInfoObject.addProperty("is_enabled", plugin.isEnabled());
        int playersOnline = plugin.getServer().getOnlinePlayers().size();
        serverInfoObject.addProperty("players_online", playersOnline);

        try {
            mappAPI.publishServerInfo(serverInfoObject);
        } catch (IOException e) {
            Logger.log(Level.WARNING, "Failed to connect to MAPP server");
            Logger.log(Level.INFO, "Error message: " + e.getMessage());
        } catch (AuthenticationException e) {
            Logger.log(Level.WARNING, "Authentication failure. Check if field values of <api-key> and <server-ip> are valid");
            cancel();
        } catch (JsonSyntaxException e) {
            Logger.log(Level.WARNING, "Failed to parse response from MAPP server");
        }
    }
}
