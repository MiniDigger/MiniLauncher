package me.minidigger.minecraftlauncher.mojangapi.async;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import me.minidigger.minecraftlauncher.mojangapi.APIEndpoint;
import me.minidigger.minecraftlauncher.mojangapi.model.PlayerProfile;
import me.minidigger.minecraftlauncher.mojangapi.model.SalesStatType;
import me.minidigger.minecraftlauncher.mojangapi.model.SalesStats;

public class APIEndpointAsync extends AbstractAsyncEndpoint {

	public static CompletableFuture<UUID> getUUID(String pUsername) {
		return get(pUsername, APIEndpoint::getUUID);
	}

	public static CompletableFuture<UUID> getUUID(String username, long timestamp) {
		return get(username, timestamp, APIEndpoint::getUUID);
	}

	public static CompletableFuture<Map<String, Long>> getNameHistory(UUID pId) {
		return get(pId, APIEndpoint::getNameHistory);
	}

	public static CompletableFuture<List<PlayerProfile>> getPlayerProfiles(List<String> usernames) {
		return get(usernames, APIEndpoint::getPlayerProfiles);
	}

	public static CompletableFuture<SalesStats> getSalesStats(SalesStatType... types) {
		return get(types, APIEndpoint::getSalesStats);
	}
}
