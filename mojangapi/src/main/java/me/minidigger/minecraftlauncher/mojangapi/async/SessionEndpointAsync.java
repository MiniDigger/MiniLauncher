package me.minidigger.minecraftlauncher.mojangapi.async;

import java.awt.Image;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import me.minidigger.minecraftlauncher.mojangapi.SessionEndpoint;
import me.minidigger.minecraftlauncher.mojangapi.model.PlayerProfile;

public class SessionEndpointAsync extends AbstractAsyncEndpoint {

	public static CompletableFuture<PlayerProfile> getPlayerProfile(UUID uuid) {
		return get(uuid, SessionEndpoint::getPlayerProfile);
	}

	public static CompletableFuture<PlayerProfile> getPlayerProfile(UUID uuid, boolean signature) {
		return get(uuid, signature, SessionEndpoint::getPlayerProfile);
	}

	public static CompletableFuture<Void> changeSkin(String accessToken, UUID uuid, String url, boolean slim) {
		throw new UnsupportedOperationException("Not implemented"); //TODO
	}

	public static CompletableFuture<Void> uploadSkin(String accessToken, UUID uuid, Image image, boolean slim) {
		throw new UnsupportedOperationException("Not implemented"); //TODO
	}

	public static CompletableFuture<Void> resetSkin(String accessTokein, UUID uuid) {
		return getVoid(accessTokein, uuid, SessionEndpoint::resetSkin);
	}

	public static CompletableFuture<List<String>> getBlockedServers() {
		return get(SessionEndpoint::getBlockedServers);
	}
}
