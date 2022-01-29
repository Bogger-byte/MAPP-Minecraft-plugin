package me.bogger.mapp.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.UUID;

public class PlayerManager {

    private final Plugin plugin;

    private final HashSet<UUID> forceVisiblePlayers;

    public PlayerManager(Plugin plugin) {
        this.plugin = plugin;

        this.forceVisiblePlayers = new HashSet<>();
    }

    public void markAsForceVisible(UUID playerUuid) {
        if (forceVisiblePlayers.contains(playerUuid)) return;
        forceVisiblePlayers.add(playerUuid);
    }

    public void markAsDefault(UUID playerUuid) {
        forceVisiblePlayers.remove(playerUuid);
    }

    public JsonObject gatherPlayersData() {
        JsonArray playerDataList = new JsonArray();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            JsonObject playerData = getPlayerData(player);
            playerDataList.add(playerData);
        }
        JsonObject playerDataObject = new JsonObject();
        playerDataObject.add("players_data", playerDataList);
        System.out.println(playerDataObject);
        return playerDataObject;
    }

    public JsonObject getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        boolean isForceVisible = forceVisiblePlayers.contains(uuid);
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
