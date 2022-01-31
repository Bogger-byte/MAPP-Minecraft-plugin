package me.bogger.mapp.events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RegionUpdateEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final World world;
    private final int x;
    private final int z;

    public RegionUpdateEvent(World world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public World getWorld() {
        return world;
    }
}
