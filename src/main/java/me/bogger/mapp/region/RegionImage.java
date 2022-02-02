package me.bogger.mapp.region;

import me.bogger.mapp.Main;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RegionImage {

    private final Main plugin;

    public RegionImage(Main plugin) {
        this.plugin = plugin;
    }

    private File createFakeImageFile(Region region) throws IOException {
        File regionFile = region.getFile();
        File imageFile = File.createTempFile(
                "tmp-",
                "=" + region.getName() + ".png",
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

    private File compileRegionToImage(Region region) {
        try {
            Thread.sleep(1000); // making fake activity
            return createFakeImageFile(region);
        } catch (IOException | InterruptedException e) {
            return region.getFile();
        }
    }

    public List<File> compileRegionsAsynchronously(List<Region> regionList) {
        List<CompletableFuture<File>> futureList = regionList.stream().map(
                region -> CompletableFuture.supplyAsync(
                        () -> compileRegionToImage(region)))
                .collect(Collectors.toList());
        return futureList.stream().map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}


