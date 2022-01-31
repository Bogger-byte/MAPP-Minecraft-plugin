package me.bogger.mapp;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MappImage {

    private final Main plugin;

    public MappImage(Main plugin) {
        this.plugin = plugin;
    }

    public File compileRegionToImage(File regionFile) {
        String renderFolder = plugin.getDataFolder() + "/render";
        return regionFile;
    }

    public List<File> compileUpdatedRegionsToImage(List<File> updatedRegionFiles) {
        if (updatedRegionFiles.isEmpty()) return Collections.emptyList();
        return updatedRegionFiles.stream().map(this::compileRegionToImage).collect(Collectors.toList());
    }

}
