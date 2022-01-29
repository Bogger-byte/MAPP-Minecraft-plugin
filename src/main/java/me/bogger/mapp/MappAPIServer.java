package me.bogger.mapp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
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
        if (this.mappRootHost == null) {
            plugin.log(Level.WARNING, "mApp server field <mapp-root-host> is empty");
        }
        this.serverIP = mappConfig.getConfig().getString("server-ip");
        if (this.serverIP == null) {
            plugin.log(Level.WARNING, "Authentication field <server-ip> is empty");
        }
        this.apiKey = mappConfig.getConfig().getString("api-key");
        if (this.apiKey == null) {
            plugin.log(Level.WARNING, "Authentication field <api-key> is empty");
        }
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
            CloseableHttpResponse response = client.execute(request);

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
            plugin.log(Level.WARNING, "Failed to get request from MAPP server. " + e.getMessage());
            return false;
        }
    }

    public StatusLine publishPlayersData(@NotNull JsonObject jsonData) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/server/" + serverIP + "/players-data";
        HttpPost request = new HttpPost(url);

        String jsonString = jsonData.toString();
        StringEntity entity = new StringEntity(jsonString);
        request.setEntity(entity);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader("api-key", apiKey);

        CloseableHttpResponse response = client.execute(request);
        client.close();
        return response.getStatusLine();
    }
}
