package me.bogger.mapp.plugin.listeners;

import me.bogger.mapp.plugin.Main;
import me.bogger.mapp.plugin.events.RegionUpdateEvent;
import me.bogger.mapp.objects.RegionFile;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;

public class RegionUpdateEventProducer implements Listener {

    private final Plugin plugin;

    public RegionUpdateEventProducer() {
        this.plugin = Main.getInstance();
    }

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {
        Location loc = RegionFile.getLocation(event.getBlock().getChunk());
        RegionUpdateEvent regionUpdateEvent = new RegionUpdateEvent(
                new RegionFile(event.getBlock().getWorld(), (int) loc.getX(), (int) loc.getZ()));
        plugin.getServer().getPluginManager().callEvent(regionUpdateEvent);
    }

    @EventHandler
    public void handleBlockPlaceEvent(BlockPlaceEvent event) {
        Location loc = RegionFile.getLocation(event.getBlock().getChunk());
        RegionUpdateEvent regionUpdateEvent = new RegionUpdateEvent(
                new RegionFile(event.getBlock().getWorld(), (int) loc.getX(), (int) loc.getZ()));
        plugin.getServer().getPluginManager().callEvent(regionUpdateEvent);
    }

    @EventHandler
    public void handleChunkLoadEvent(ChunkLoadEvent event) {
        if (!event.isNewChunk()) return;

        Location loc = RegionFile.getLocation(event.getChunk());
        RegionUpdateEvent regionUpdateEvent = new RegionUpdateEvent(
                new RegionFile(event.getWorld(), (int) loc.getX(), (int) loc.getZ()));
        plugin.getServer().getPluginManager().callEvent(regionUpdateEvent);
    }
}
