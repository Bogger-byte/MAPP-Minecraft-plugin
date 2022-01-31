package me.bogger.mapp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;


public class MappAPIServer {

    private final Main plugin;

    private final String mappRootHost;
    private final String serverIP;
    private final String apiKey;

    public MappAPIServer(Main plugin, @NotNull MappConfig mappConfig) {
        this.plugin = plugin;

        this.mappRootHost = mappConfig.getConfig().getString("mapp-root-host");
        this.serverIP = mappConfig.getConfig().getString("server-ip");
        this.apiKey = mappConfig.getConfig().getString("api-key");
    }

    private JsonObject fetchJsonObject(HttpEntity entity) throws IOException {
        String jsonString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        Gson gson = new Gson();
        return gson.fromJson(jsonString, JsonObject.class);
    }

    public boolean checkCredentials() {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/server/" + serverIP + "/check-credentials";
        HttpGet request = new HttpGet(url);

        request.setHeader("api-key", apiKey);
        try {
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == 401) {
                plugin.log(Level.WARNING, "Authentication failure. Check if field values of <api-key> and <server-ip> are valid");
                return false;
            }

            JsonObject jsonResponse = fetchJsonObject(response.getEntity());
            String serverName = jsonResponse.get("name").getAsString();
            String serverID = jsonResponse.get("id").getAsString();
            plugin.log(Level.FINER, "Successfully authorized as '" + serverName + "' <" + serverID + ">");
            return true;
        } catch (IOException e) {
            plugin.log(Level.WARNING, "Failed to get request from MAPP server." + "\nError msg: " + e.getMessage());
            return false;
        }
    }

    public StatusLine publishPlayersData(@NotNull JsonObject jsonData) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/server/" + serverIP + "/players-data";
        HttpPost request = new HttpPost(url);

        String jsonString = jsonData.toString();
        StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        request.setHeader("api-key", apiKey);

        HttpResponse response = client.execute(request);
        client.close();
        return response.getStatusLine();
    }

    public StatusLine publishRegionImage(@NotNull File image,
                                         String worldName,
                                         int regionX,
                                         int regionZ) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/server/" + serverIP + "/upload-region-image" +
                "?world=" + worldName +
                "&region_x=" + regionX +
                "&region_z=" + regionZ;
        HttpPost request = new HttpPost(url);

        FileBody imageBody = new FileBody(image, ContentType.IMAGE_PNG);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.addPart("image", imageBody);

        request.setEntity(entityBuilder.build());
        request.setHeader("api-key", apiKey);

        HttpResponse response = client.execute(request);
        client.close();
        return response.getStatusLine();
    }
}
