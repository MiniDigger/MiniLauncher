package me.minidigger.minecraftlauncher.mojangapi.model;

import me.minidigger.minecraftlauncher.mojangapi.AuthenticationEndpoint;

public class RefreshRequest {

    private String accessToken;
    private String clientToken;
    private boolean requestUser;

    public RefreshRequest(String accessToken, boolean requestUser) {
        this.accessToken = accessToken;
        this.clientToken = AuthenticationEndpoint.clientToken.toString();
        this.requestUser = requestUser;
    }

    public RefreshRequest(String accessToken) {
        this(accessToken, false);
    }
}
