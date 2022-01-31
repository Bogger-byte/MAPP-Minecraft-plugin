package me.bogger.mapp;

import java.io.File;

public final class MappRegion {

    private final File file;
    private final String worldName;
    private final int x;
    private final int z;

    public MappRegion(File file, String worldName, int x, int z) {
        this.file = file;
        this.worldName = worldName;
        this.x = x;
        this.z = z;
    }

    public File getFile() {
        return file;
    }

    public String getWorldName() {
        return worldName;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
