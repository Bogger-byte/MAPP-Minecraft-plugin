package me.bogger.mapp.managers;

import me.bogger.mapp.Region;

import java.util.ArrayList;
import java.util.List;

public class RegionsManager {

    private final List<Region> updatedRegionList;

    public RegionsManager() {
        this.updatedRegionList = new ArrayList<>();
    }

    public void addUpdatedRegion(Region region) {
        if (updatedRegionList.contains(region)) return;
        updatedRegionList.add(region);
    }

    public List<Region> getUpdatedRegionList() {
        List<Region> temp = new ArrayList<>(updatedRegionList);
        updatedRegionList.clear();
        return temp;
    }

}
