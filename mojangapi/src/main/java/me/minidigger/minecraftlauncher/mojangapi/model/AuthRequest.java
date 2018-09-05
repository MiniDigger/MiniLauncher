package me.minidigger.minecraftlauncher.mojangapi.model;

import me.minidigger.minecraftlauncher.mojangapi.AuthenticationEndpoint;

public class AuthRequest {

	// agent is ommited

	private String username;
	private String password;
	// optional
	private String clientToken;
	private boolean requestUser;

	public AuthRequest(String username, String password, boolean requestUser) {
		super();
		this.username = username;
		this.password = password;
		this.clientToken = AuthenticationEndpoint.clientToken.toString();
		this.requestUser = requestUser;
	}

	public AuthRequest(String username, String password) {
		this(username, password, false);
	}
}
