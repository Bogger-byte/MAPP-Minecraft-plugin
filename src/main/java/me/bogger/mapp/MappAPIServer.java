package me.bogger.mapp;

import com.google.gson.JsonObject;
import com.sun.istack.internal.NotNull;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.logging.Level;


public class MappAPIServer {

    private final String mappRootHost;
    private final String serverIP;
    private final String apiKey;

    private final CloseableHttpClient client;

    public MappAPIServer(Main plugin, @NotNull MappConfig mappConfig) {
        this.mappRootHost = mappConfig.getConfig().getString("mapp-root-host");
        if (this.mappRootHost == null) {
            plugin.log(Level.WARNING, "mApp server field <mapp-root-host> is empty");
            plugin.selfDisable("Disabling due miss of required config values");
        }
        this.serverIP = mappConfig.getConfig().getString("server-ip");
        if (this.serverIP == null) {
            plugin.log(Level.WARNING, "Authentication field <server-ip> is empty");
            plugin.selfDisable("Disabling due miss of required config values");
        }
        this.apiKey = mappConfig.getConfig().getString("apiKey");
        if (this.apiKey == null) {
            plugin.log(Level.WARNING, "Authentication field <api-key> is empty");
            plugin.selfDisable("Disabling due miss of required config values");
        }

        this.client = HttpClients.createDefault();
    }

    public StatusLine publishPlayersData(@NotNull JsonObject jsonData) throws IOException {
        String url = mappRootHost + "/server/" + serverIP + "/players-data";
        HttpPost request = new HttpPost(url);

        String jsonString = jsonData.toString();
        StringEntity entity = new StringEntity(jsonString);
        request.setEntity(entity);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader("api-key", apiKey);

        CloseableHttpResponse response = client.execute(request);
        return response.getStatusLine();
    }
}
