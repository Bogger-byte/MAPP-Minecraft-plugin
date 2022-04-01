package me.bogger.mapp.plugin.commands.subCommands;

import me.bogger.mapp.plugin.Main;
import me.bogger.mapp.plugin.utils.Messenger;
import me.bogger.mapp.plugin.commands.SubCommand;
import me.bogger.mapp.plugin.tasks.PublishRegionsTask;
import me.bogger.mapp.objects.RegionFile;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PublishAllRegionsSC extends SubCommand {

    private final Plugin plugin;

    public PublishAllRegionsSC() {
        setName("publish-all-regions");
        this.plugin = Main.getInstance();
    }

    @Override
    public void onCommand(@NotNull CommandSender sender,
                          @NotNull Command command,
                          @NotNull String[] args) {
        if (args.length != 1) {
            Messenger.sendTo(sender, ChatColor.RED + "Arguments required");
            return;
        }

        String worldName = args[0];
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            Messenger.sendTo(sender, ChatColor.RED + "This world does not exist");
            return;
        }

        File[] regionFiles = RegionFile.getRegionsFolderPath(world).toFile().listFiles();
        if (regionFiles == null) {
            return;
        }

        List<File> regionFileList = Arrays.asList(regionFiles);
        RegionFile[] regionFileArray = regionFileList.stream()
                .map(file -> new RegionFile(world, file))
                .toArray(RegionFile[]::new);

        new PublishRegionsTask(regionFileArray).runTaskAsynchronously(plugin);
    }

    @Override
    public String getPermission() {
        return null;
    }
}
