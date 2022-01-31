package me.bogger.mapp.listeners;

import me.bogger.mapp.Main;
import me.bogger.mapp.events.RegionUpdateEvent;
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
        int regionX = event.getBlock().getChunk().getX() >> 5;
        int regionZ = event.getBlock().getChunk().getZ() >> 5;
        RegionUpdateEvent regionUpdateEvent = new RegionUpdateEvent(
                event.getBlock().getWorld(),
                regionX,
                regionZ);
        plugin.getServer().getPluginManager().callEvent(regionUpdateEvent);
    }

    @EventHandler
    public void handleBlockPlaceEvent(BlockPlaceEvent event) {
        int regionX = event.getBlock().getChunk().getX() >> 5;
        int regionZ = event.getBlock().getChunk().getZ() >> 5;
        RegionUpdateEvent regionUpdateEvent = new RegionUpdateEvent(
                event.getBlock().getWorld(),
                regionX,
                regionZ);
        plugin.getServer().getPluginManager().callEvent(regionUpdateEvent);
    }

    @EventHandler
    public void handleChunkLoadEvent(ChunkLoadEvent event) {
        if (!event.isNewChunk()) return;

        int regionX = event.getChunk().getX() >> 5;
        int regionZ = event.getChunk().getZ() >> 5;
        RegionUpdateEvent regionUpdateEvent = new RegionUpdateEvent(
                event.getWorld(),
                regionX,
                regionZ);
        plugin.getServer().getPluginManager().callEvent(regionUpdateEvent);
    }
}
