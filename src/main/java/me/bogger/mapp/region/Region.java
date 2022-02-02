package me.bogger.mapp.region;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Region {

    private final File file;
    private final String name;

    public Region(@NotNull World world, int x, int z) {
        this.file = new File(getRegionsFolderPath(world).toString(), "r." + x + "." + z + ".mca");
        this.name = world.getName() + "_" + "r." + x + "." + z + ".mca";
    }

    public Region(World world, File regionFile) {
        this.file = regionFile;
        this.name = world.getName() + "_" + regionFile.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final Region other = (Region) obj;
        if (other.file == null) {
            return false;
        }
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

    public String getName() {
        return name;
    }
}

