package me.bogger.mapp;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Region {

    private final File file;
    private final Location location;
    private final Path folder;

    public Region(World world, double x, double z) {
        this.file = new File(getRegionsFolderPath(world).toString(), "/r." + x + "." + z + ".mca");
        this.location = new Location(world, x, 0, z);
        this.folder = getRegionsFolderPath(world);
    }

    public Region(World world, File regionFile) {
        this.file = regionFile;

        String[] split = regionFile.getName().split("\\.");
        int regionX = Integer.parseInt(split[1]);
        int regionZ = Integer.parseInt(split[2]);
        this.location = new Location(world, regionX, 0, regionZ);
        this.folder = getRegionsFolderPath(world);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        final Region other = (Region) obj;
        if (other.file == null) return false;
        return other.file.getName().equals(this.file.getName());
    }

    public static Path getRegionsFolderPath(World world) {
        String worldName = world.getName();
        if (worldName.endsWith("_nether")) {
            return Paths.get(world.getWorldFolder().getPath() + "/DIM-1/region");
        }
        if (worldName.endsWith("_the_end")) {
            return Paths.get(world.getWorldFolder().getPath() + "/DIM1/region");
        }
        return Paths.get(world.getWorldFolder().getPath() + "/region");
    }

    public static Location getLocation(Chunk chunk) {
        int regionX = chunk.getX() >> 5;
        int regionZ = chunk.getZ() >> 5;
        return new Location(chunk.getWorld(), regionX, 0, regionZ);
    }

    public File getFile() {
        return file;
    }

    public Location getLocation() {
        return location;
    }

    public Path getFolder() {
        return folder;
    }
}
