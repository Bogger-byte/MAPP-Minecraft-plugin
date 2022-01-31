package me.bogger.mapp.listeners;

import me.bogger.mapp.events.RegionUpdateEvent;
import me.bogger.mapp.managers.RegionsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionUpdateListener implements Listener {

    private final RegionsManager regionsManager;

    public RegionUpdateListener(RegionsManager regionsManager) {
        this.regionsManager = regionsManager;
    }

    @EventHandler
    public void handleRegionUpdateEvent(RegionUpdateEvent event) {
        regionsManager.addUpdatedRegion(event.getWorld(), event.getX(), event.getZ());
    }
}
