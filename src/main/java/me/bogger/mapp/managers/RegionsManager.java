package me.bogger.mapp.managers;

import me.bogger.mapp.Main;
import me.bogger.mapp.MappRegion;
import org.bukkit.World;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegionsManager {

    private final List<MappRegion> updatedRegionList;

    public RegionsManager() {
        this.updatedRegionList = new ArrayList<>();
    }

    private boolean contains(List<MappRegion> list, MappRegion region) {
        return list.stream().anyMatch(r -> r.getFile().getName()
                .equals(region.getFile().getName()));
    }

    private String getRegionsFolderPath(World world) {
        String worldName = world.getName();
        if (worldName.endsWith("_nether")) {
            return world.getWorldFolder().getPath() + "/DIM-1/region";
        }
        if (worldName.endsWith("_the_end")) {
            return world.getWorldFolder().getPath() + "DIM1/region";
        }
        return world.getWorldFolder().getPath();
    }

    public void addUpdatedRegion(World world, int x, int z) {
        String regionPath = getRegionsFolderPath(world) + "/r." + x + "." + z + ".mca";
        MappRegion region = new MappRegion(new File(regionPath), world.getName() , x, z);
        if (contains(updatedRegionList, region)) return;
        updatedRegionList.add(region);
    }

    public List<MappRegion> getUpdatedRegionList() {
        List<MappRegion> temp = new ArrayList<>(updatedRegionList);
        updatedRegionList.clear();
        return temp;
    }

}
