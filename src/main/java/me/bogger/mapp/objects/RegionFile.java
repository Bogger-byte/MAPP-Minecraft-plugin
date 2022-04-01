package me.bogger.mapp.objects;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RegionFile extends File {
    private final String name;

    public RegionFile(@NotNull World world, int x, int z) {
        super(getRegionsFolderPath(world).toString(), "r." + x + "." + z + ".mca");
        this.name = world.getName() + "_" + "r." + x + "." + z + ".mca";
    }

    public RegionFile(World world, File regionFile) {
        super(getRegionsFolderPath(world).toString(), regionFile.getName());
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
        final RegionFile other = (RegionFile) obj;
        if (!other.exists()) {
            return false;
        }
        return other.getName().equals(this.getName());
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

    public String getFullName() {
        return name;
    }

    public byte[] getBytes() throws IOException {
        return Files.readAllBytes(Paths.get(getPath()));
    }
}
