package me.bogger.mapp.tasks;

import me.bogger.mapp.Main;
import me.bogger.mapp.MappAPIServer;
import me.bogger.mapp.MappImage;
import me.bogger.mapp.Region;
import me.bogger.mapp.managers.RegionsManager;
import org.apache.http.auth.AuthenticationException;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class PublishUpdatedRegions extends BukkitRunnable {

    private final Main plugin;
    private final MappAPIServer mappAPIServer;
    private final MappImage mappImage;
    private final RegionsManager regionsManager;

    public PublishUpdatedRegions(Main plugin,
                                 @NotNull MappAPIServer mappAPIServer,
                                 @NotNull MappImage mappImage,
                                 @NotNull RegionsManager regionsManager) {
        this.plugin = plugin;
        this.mappAPIServer = mappAPIServer;
        this.mappImage = mappImage;
        this.regionsManager = regionsManager;
    }

    private int publishAttempts = 0;

    @Override
    public void run() {
        List<Region> regionList = regionsManager.getUpdatedRegionList();

        if (regionList.isEmpty()) return;

        List<File> regionImageList = mappImage.compileRegionsToImages(regionList);
        if (regionImageList.isEmpty()) return;

        try {
            mappAPIServer.publishRegionImages(regionImageList);
        } catch (AuthenticationException e) {
            plugin.log(Level.WARNING, "Authentication failure. Check if field values of <api-key> and <server-ip> are valid");
            cancel();
        } catch (IOException e) {
            publishAttempts += 1;
            plugin.log(Level.WARNING, "Failed to publish region data");
            plugin.log(Level.INFO, "Error message: " + e.getMessage());
            plugin.log(Level.INFO, "[Attempt " + publishAttempts + "] Trying to reconnect");
        }
    }
}
