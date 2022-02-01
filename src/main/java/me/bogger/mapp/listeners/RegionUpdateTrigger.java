package me.bogger.mapp.listeners;

import me.bogger.mapp.Main;
import me.bogger.mapp.Region;
import me.bogger.mapp.events.RegionUpdateEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class RegionUpdateTrigger implements Listener {

    private final Main plugin;

    public RegionUpdateTrigger(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleBlockBreakEvent(BlockBreakEvent event) {
        Location loc = Region.getLocation(event.getBlock().getChunk());
        RegionUpdateEvent regionUpdateEvent = new RegionUpdateEvent(
                new Region(event.getBlock().getWorld(), loc.getX(), loc.getZ()));
        plugin.getServer().getPluginManager().callEvent(regionUpdateEvent);
    }

    @EventHandler
    public void handleBlockPlaceEvent(BlockPlaceEvent event) {
        Location loc = Region.getLocation(event.getBlock().getChunk());
        RegionUpdateEvent regionUpdateEvent = new RegionUpdateEvent(
                new Region(event.getBlock().getWorld(), loc.getX(), loc.getZ()));
        plugin.getServer().getPluginManager().callEvent(regionUpdateEvent);
    }

    @EventHandler
    public void handleChunkLoadEvent(ChunkLoadEvent event) {
        if (!event.isNewChunk()) return;

        Location loc = Region.getLocation(event.getChunk());
        RegionUpdateEvent regionUpdateEvent = new RegionUpdateEvent(
                new Region(event.getWorld(), loc.getX(), loc.getZ()));
        plugin.getServer().getPluginManager().callEvent(regionUpdateEvent);
    }
}
