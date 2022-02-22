package me.bogger.mapp.plugin.listeners;

import me.bogger.mapp.plugin.managers.RegionsManager;
import me.bogger.mapp.plugin.Main;
import me.bogger.mapp.plugin.events.RegionUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionUpdateListener implements Listener {

    private final RegionsManager regionsManager;

    public RegionUpdateListener() {
        this.regionsManager = Main.getInstance().getRegionsManager();
    }

    @EventHandler
    public void handleRegionUpdateEvent(RegionUpdateEvent event) {
        regionsManager.addUpdatedRegion(event.getRegion());
    }
}
