package me.bogger.mapp.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.bogger.mapp.MappConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayersManager {

    private final Plugin plugin;

    private final MappConfig config;

    public PlayersManager(Plugin plugin, MappConfig config) {
        this.plugin = plugin;

        this.config = config;
    }

    private Set<UUID> getForceVisiblePlayersList() {
        List<String> forceVisiblePlayersList = config.getConfig().getStringList("force-visible-players");
        return forceVisiblePlayersList.stream().map(UUID::fromString).collect(Collectors.toSet());
    }

    public JsonObject gatherPlayersData() {
        JsonArray playerDataList = new JsonArray();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            JsonObject playerData = getPlayerData(player);
            playerDataList.add(playerData);
        }
        JsonObject playerDataObject = new JsonObject();
        playerDataObject.add("players_data", playerDataList);
        return playerDataObject;
    }

    public JsonObject getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isForceVisible = getForceVisiblePlayersList().contains(uuid);
        String nickname = player.getName();

        Location playerLoc = player.getLocation();
        int xLoc = (int) playerLoc.getX();
        int yLoc = (int) playerLoc.getY();
        int zLoc = (int) playerLoc.getZ();

        JsonObject coordinates = new JsonObject();
        coordinates.addProperty("x", xLoc);
        coordinates.addProperty("y", yLoc);
        coordinates.addProperty("z", zLoc);

        JsonObject playerData = new JsonObject();
        playerData.addProperty("uuid", uuid.toString());
        playerData.addProperty("is_force_visible", isForceVisible);
        playerData.addProperty("name", nickname);
        playerData.add("coordinates", coordinates);

        return playerData;
    }
}
