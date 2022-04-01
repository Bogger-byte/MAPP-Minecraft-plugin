package me.bogger.mapp.plugin.tasks;

import me.bogger.mapp.plugin.managers.RegionsManager;
import me.bogger.mapp.plugin.Main;
import me.bogger.mapp.objects.RegionFile;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PublishUpdatedRegionsTask extends BukkitRunnable {

    private final Plugin plugin;
    private final RegionsManager regionsManager;

    public PublishUpdatedRegionsTask() {
        this.plugin = Main.getInstance();
        this.regionsManager = Main.getInstance().getRegionsManager();
    }

    @Override
    public void run() {
        RegionFile[] regionArray = regionsManager.getUpdatedRegions();
        if (regionArray.length == 0) {
            return;
        }
        new PublishRegionsTask(regionArray).runTaskAsynchronously(plugin);
    }
}
