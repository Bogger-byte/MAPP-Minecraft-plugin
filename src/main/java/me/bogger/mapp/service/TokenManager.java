package me.bogger.mapp.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TokenManager {
    private final String mappRootHost;

    private final String username;
    private final String password;
    private final String clientID;
    private final String clientSecret;

    private String accessToken;
    private String refreshToken;
    private Instant accessTokenExpireDate;
    private Instant refreshTokenExpireDate;

    public TokenManager(@NotNull String mappRootHost,
                        @NotNull String username,
                        @NotNull String password,
                        @NotNull String clientID,
                        @NotNull String clientSecret) {
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

    private void parseTokenResponse(JsonObject jsonResponse) throws JsonSyntaxException {
        JsonElement accessTokenElement = jsonResponse.get("access_token");
        JsonElement refreshTokenElement = jsonResponse.get("refresh_token");
        JsonElement accessTokenExpireDateElement = jsonResponse.get("access_token_expire");
        JsonElement refreshTokenExpireDateElement = jsonResponse.get("refresh_token_expire");

        if (accessTokenElement == null || refreshTokenElement == null ||
                accessTokenExpireDateElement == null || refreshTokenExpireDateElement == null) {
            throw new JsonSyntaxException("Missing required fields");
        }

        accessToken = accessTokenElement.getAsString();
        refreshToken = refreshTokenElement.getAsString();
        accessTokenExpireDate = Instant.now().plusSeconds(accessTokenExpireDateElement.getAsInt());
        refreshTokenExpireDate = Instant.now().plusSeconds(refreshTokenExpireDateElement.getAsInt());
    }

    private void authorize() throws IOException, AuthenticationException, JsonSyntaxException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/auth/jwt/token";
        HttpPost request = new HttpPost(url);

        List<BasicNameValuePair> payload = new ArrayList<>();
        payload.add(new BasicNameValuePair("grant_type", "password"));
        payload.add(new BasicNameValuePair("username", username));
        payload.add(new BasicNameValuePair("password", password));
        payload.add(new BasicNameValuePair("scope", "minecraft-server"));
        payload.add(new BasicNameValuePair("client_id", clientID));
        payload.add(new BasicNameValuePair("client_secret", clientSecret));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(payload);
        request.setEntity(urlEncodedFormEntity);

        HttpResponse response = client.execute(request);
        StatusLine responseStatus = response.getStatusLine();
        if (responseStatus.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            throw new AuthenticationException();
        }

        String jsonString = EntityUtils.toString(response.getEntity());
        JsonObject jsonResponse = fetchJsonObject(jsonString);
        parseTokenResponse(jsonResponse);
    }

    private void refreshAccessToken() throws IOException, AuthenticationException, JsonSyntaxException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url = mappRootHost + "/auth/jwt/refresh";
        HttpPost request = new HttpPost(url);

        request.setHeader("refresh-token", refreshToken);

        HttpResponse response = client.execute(request);
        StatusLine responseStatus = response.getStatusLine();
        if (responseStatus.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
            throw new AuthenticationException();
        }

        String jsonString = EntityUtils.toString(response.getEntity());
        JsonObject jsonResponse = fetchJsonObject(jsonString);
        parseTokenResponse(jsonResponse);
    }

    public String getAccessToken() throws IOException, AuthenticationException, JsonSyntaxException {
        if (areTokenFieldsNull() || isRefreshTokenExpired()) {
            authorize();
        }
        if (isAccessTokenExpired()) {
            refreshAccessToken();
        }
        return accessToken;
    }

    private boolean areTokenFieldsNull() {
        return accessToken == null || refreshToken == null || accessTokenExpireDate == null || refreshTokenExpireDate == null;
    }

    private boolean isRefreshTokenExpired() {
        return refreshTokenExpireDate != null && Instant.now().isAfter(refreshTokenExpireDate);
    }

    private boolean isAccessTokenExpired() {
        return accessTokenExpireDate != null && Instant.now().isAfter(accessTokenExpireDate);
    }
}
