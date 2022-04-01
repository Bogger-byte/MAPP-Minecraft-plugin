package me.bogger.mapp.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.bogger.mapp.objects.RegionFile;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface MappAPI {
    void ping() throws IOException;
    void authorize() throws IOException, AuthenticationException, JsonSyntaxException;

    StatusLine publishPlayersData(@NotNull JsonObject json)
            throws IOException, AuthenticationException, JsonSyntaxException;
    StatusLine publishRegionsData(@NotNull RegionFile[] regions)
            throws IOException, AuthenticationException, JsonSyntaxException;

    String getServerInfo();
}
