package me.bogger.mapp;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MappImage {

    private final Main plugin;

    public MappImage(Main plugin) {
        this.plugin = plugin;
    }

    private File createFakeImageFile(Region region) throws IOException {
        File regionFile = region.getFile();
        if (region.getLocation().getWorld() == null) return null;
        String worldName = region.getLocation().getWorld().getName();
        String regionName = worldName + "_" + regionFile.getName() + ".png";
        File imageFile = File.createTempFile(
                "tmp-",
                "=" + regionName,
                Paths.get(plugin.getDataFolder().getPath() + "/render/").toFile());
        imageFile.deleteOnExit();

        InputStream in = new BufferedInputStream(new FileInputStream(regionFile));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFile));
        byte[] buffer = new byte[1024];
        int lengthRead;
        while ((lengthRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, lengthRead);
            out.flush();
        }
        return imageFile;
    }

    public File compileRegionToImage(Region region) {
        try {
            return createFakeImageFile(region);
        } catch (IOException e) {
            return null;
        }
    }

    public List<File> compileRegionsToImages(List<Region> regionList) {
        if (regionList.isEmpty()) return Collections.emptyList();
        List<File> imageList = regionList.stream().map(this::compileRegionToImage).collect(Collectors.toList());
        imageList.removeAll(Collections.singleton(null));
        return imageList;
    }

}
