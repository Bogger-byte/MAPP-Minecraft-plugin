package me.bogger.mapp.events;

import me.bogger.mapp.region.Region;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RegionUpdateEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final Region region;

    public RegionUpdateEvent(Region region) {
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

    public Region getRegion() {
        return region;
    }
}
