package me.bogger.mapp.plugin.events;

import me.bogger.mapp.objects.RegionFile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RegionUpdateEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final RegionFile region;

    public RegionUpdateEvent(RegionFile region) {
        this.region = region;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public RegionFile getRegion() {
        return region;
    }
}
