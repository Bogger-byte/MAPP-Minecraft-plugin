package me.bogger.mapp.plugin.managers;

import me.bogger.mapp.objects.RegionFile;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RegionsManager {

    private Set<RegionFile> blockingRegionSet;

    public RegionsManager() {
        this.blockingRegionSet = Collections.synchronizedSet(new HashSet<>());
    }

    public void addUpdatedRegion(RegionFile region) {
        blockingRegionSet.add(region);
    }

    public RegionFile[] getUpdatedRegions() {
        RegionFile[] regionArray = blockingRegionSet.stream().toArray(RegionFile[]::new);
        blockingRegionSet = Collections.synchronizedSet(new HashSet<>());
        return regionArray;
    }

}
