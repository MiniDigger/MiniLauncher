package me.minidigger.minecraftlauncher.mojangapi.model;

import me.minidigger.minecraftlauncher.mojangapi.AuthenticationEndpoint;

public class TokenRequest {

	private String accessToken;
	private String clientToken;

	public TokenRequest(String accessToken) {
		super();
		this.accessToken = accessToken;
		this.clientToken = AuthenticationEndpoint.clientToken.toString();
	}
}
