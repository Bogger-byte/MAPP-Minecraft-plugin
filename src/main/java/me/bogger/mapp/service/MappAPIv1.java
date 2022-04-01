package me.bogger.mapp.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.bogger.mapp.objects.RegionImage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class MappAPIv1 implements MappAPI {

    private final String mappRootHost;
    private final String username;
    private final String password;
    private final String clientID;
    private final String clientSecret;

    private TokenStorer tokenStorer;
    private String serverInfo;

    public MappAPIv1(
            String mappRootHost,
            String username,
            String password,
            String clientID,
            String clientSecret) {
        this.mappRootHost = mappRootHost;
        this.username = username;
        this.password = password;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
    }

    private JsonObject fetchJsonObject(String jsonString) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, JsonObject.class);
    }

    @Override
    public void ping()
            throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/";
        HttpGet request = new HttpGet(url);

        client.execute(request);
    }

    @Override
    public void authorize()
            throws IOException, AuthenticationException, JsonSyntaxException {
        this.tokenStorer = new TokenStorer(mappRootHost, username, password, clientID, clientSecret);

        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/servers/me";
        HttpGet request = new HttpGet(url);

        request.setHeader("Authorization", "Bearer " + tokenStorer.getAccessToken());
        HttpResponse response = client.execute(request);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            throw new AuthenticationException();
        }

        String jsonString = EntityUtils.toString(response.getEntity());
        JsonObject jsonResponse = fetchJsonObject(jsonString);
        JsonElement serverNameElement = jsonResponse.get("name");
        JsonElement serverIDElement = jsonResponse.get("id");

        if (serverNameElement == null || serverIDElement == null) {
            throw new IOException();
        }

        String serverName = serverNameElement.getAsString();
        String serverID = serverIDElement.getAsString();
        serverInfo = "'" + serverName + "' <" + serverID + ">";
    }

    @Override
    public StatusLine publishPlayersData(@NotNull JsonObject json)
            throws IOException, AuthenticationException, JsonSyntaxException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/servers/me/players-data";
        HttpPost request = new HttpPost(url);

        String jsonString = json.toString();
        StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        request.setHeader("Authorization", "Bearer " + tokenStorer.getAccessToken());

        HttpResponse response = client.execute(request);
        StatusLine responseStatus = response.getStatusLine();
        if (responseStatus.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            throw new AuthenticationException();
        }
        return responseStatus;
    }

    @Override
    public StatusLine publishRegionsData(@NotNull RegionImage[] images)
            throws IOException, AuthenticationException, JsonSyntaxException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/servers/me/regions";
        HttpPost request = new HttpPost(url);

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (RegionImage image : images) {
            entityBuilder.addBinaryBody("region_image_uploads",
                    image.getData(), ContentType.IMAGE_PNG, image.getName());
        }
        request.setEntity(entityBuilder.build());
        request.setHeader("Authorization", "Bearer " + tokenStorer.getAccessToken());

        HttpResponse response = client.execute(request);
        StatusLine responseStatus = response.getStatusLine();
        if (responseStatus.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            throw new AuthenticationException();
        }
        return responseStatus;
    }

    @Override
    public StatusLine publishServerInfo(@NotNull JsonObject json)
            throws IOException, AuthenticationException, JsonSyntaxException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/servers/me/info";
        HttpPost request = new HttpPost(url);

        String jsonString = json.toString();
        StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        request.setHeader("Authorization", "Bearer " + tokenStorer.getAccessToken());

        HttpResponse response = client.execute(request);
        StatusLine responseStatus = response.getStatusLine();
        if (responseStatus.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            throw new AuthenticationException();
        }
        return responseStatus;
    }

    @Override
    public String getClientData() {
        return serverInfo;
    }
}
