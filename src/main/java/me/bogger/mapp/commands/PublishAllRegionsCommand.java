package me.bogger.mapp.commands;

import me.bogger.mapp.Main;
import me.bogger.mapp.MappAPIServer;
import me.bogger.mapp.MappImage;
import me.bogger.mapp.Region;
import org.apache.http.auth.AuthenticationException;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PublishAllRegionsCommand implements CommandExecutor {

    private final Main plugin;

    private final MappAPIServer mappAPIServer;
    private final MappImage mappImage;

    public PublishAllRegionsCommand(Main plugin,
                                    @NotNull MappAPIServer mappAPIServer,
                                    @NotNull MappImage mappImage) {
        this.plugin = plugin;

        this.mappAPIServer = mappAPIServer;
        this.mappImage = mappImage;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        if (args.length != 1) return false;

        String worldName = args[0];
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) return false;

        File[] regionFiles = Region.getRegionsFolderPath(world).toFile().listFiles();
        if (regionFiles == null) return false;

        List<File> regionFileList = Arrays.asList(regionFiles);
        List<Region> regionList = regionFileList.stream().map(file -> new Region(world, file)).collect(Collectors.toList());
        List<File> regionImageList = mappImage.compileRegionsToImages(regionList);
        if (regionImageList.isEmpty()) return false;

        plugin.log(Level.INFO, "Sending data to MAPP server...");
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                mappAPIServer.publishRegionImages(regionImageList);
            } catch (AuthenticationException e) {
                plugin.log(Level.WARNING, "Authentication failure. Check if field values of <api-key> and <server-ip> are valid");
            } catch (IOException e) {
                e.printStackTrace();
                plugin.log(Level.WARNING, "Failed to publish region data");
                plugin.log(Level.INFO, "Error message: " + e.getMessage());
            }
        });
        return false;
    }
}
