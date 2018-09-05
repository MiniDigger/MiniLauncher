package me.minidigger.minecraftlauncher.mojangapi.model;

import java.util.List;

public class AuthResponse {

	private String accessToken;
	private String clientToken;
	// only if agent was send
	private List<PlayerProfile> availableProfiles;
	// only if agent was send
	private PlayerProfile selectedProfile;
	// only if requestuser was true
	private PlayerProfile user;
	
	@Override
	public String toString() {
		return "AuthResponse [accessToken=" + accessToken + ", clientToken=" + clientToken + ", availableProfiles="
				+ availableProfiles + ", selectedProfile=" + selectedProfile + ", user=" + user + "]";
	}
}
