package me.bogger.mapp.plugin.tasks;

import me.bogger.mapp.objects.RegionImage;
import me.bogger.mapp.plugin.managers.RegionsManager;
import me.bogger.mapp.plugin.Main;
import me.bogger.mapp.objects.RegionFile;
import me.bogger.mapp.renderer.Renderer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PublishUpdatedRegionsTask extends BukkitRunnable {

    private final Plugin plugin;
    private final RegionsManager regionsManager;
    private final Renderer renderer;

    public PublishUpdatedRegionsTask() {
        this.plugin = Main.getInstance();
        this.regionsManager = Main.getInstance().getRegionsManager();
        this.renderer = Main.getInstance().getRenderer();
    }

    @Override
    public void run() {
        RegionFile[] regionArray = regionsManager.getUpdatedRegions();
        if (regionArray.length == 0) {
            return;
        }

        RegionImage[] images = renderer.renderQueue(regionArray);

        new PublishRegionImagesTask(images).runTaskAsynchronously(plugin);
    }
}
