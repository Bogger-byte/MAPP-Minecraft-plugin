package me.bogger.mapp.renderer;

import me.bogger.mapp.objects.RegionFile;
import me.bogger.mapp.objects.RegionImage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public abstract class Renderer {
    public abstract @Nullable RegionImage render(RegionFile region) throws IOException;

    public abstract RegionImage[] renderQueue(RegionFile[] regions);
}
