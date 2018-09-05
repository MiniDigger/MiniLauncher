package me.minidigger.minecraftlauncher.mojangapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;

import me.minidigger.minecraftlauncher.mojangapi.model.PlayerProfile;
import me.minidigger.minecraftlauncher.mojangapi.model.SalesStatType;
import me.minidigger.minecraftlauncher.mojangapi.model.SalesStats;

public class APIEndpoint extends AbstractEndpoint {

	private static final String baseURL = "https://api.mojang.com";

	public static UUID getUUID(String username) throws IOException {
		// GET https://api.mojang.com/users/profiles/minecraft/<username>
		// 204? -> unknown user
		// 400? -> error, errorMessage
		// 200? -> id, name

		JsonAdapter<Map<String, String>> jsonAdapter = moshi
				.adapter(Types.newParameterizedType(Map.class, String.class, String.class));

		Map<String, String> result = jsonAdapter.fromJson(read(get(baseURL + "/users/profiles/minecraft/" + username)));

		return UUID.fromString(Util.mojangUUIDtoNormal(result.get("id")));
	}

	public static UUID getUUID(String username, long timestamp) throws IOException {
		// GET https://api.mojang.com/users/profiles/minecraft/<username>?at=<timestamp>
		// 204? -> unknown user
		// 400? -> error, errorMessage
		// 200? -> id, name

		JsonAdapter<Map<String, String>> jsonAdapter = moshi
				.adapter(Types.newParameterizedType(Map.class, String.class, String.class));

		Map<String, String> result = jsonAdapter
				.fromJson(read(get(baseURL + "/users/profiles/minecraft/" + username + "?at=" + timestamp)));

		return UUID.fromString(Util.mojangUUIDtoNormal(result.get("id")));
	}

	public static Map<String, Long> getNameHistory(UUID id) throws IOException {
		// GET https://api.mojang.com/user/profiles/<uuid>/names
		// result: array of name, changedToAt (optional)

		JsonAdapter<List<Map<String, Long>>> jsonAdapter = moshi.adapter(Types.newParameterizedType(List.class,
				Types.newParameterizedType(Map.class, String.class, Long.class)));

		List<Map<String, Long>> result = jsonAdapter
				.fromJson(read(get(baseURL + "/users/profiles/" + Util.normalUUIDtoMojang(id) + "/names")));

		return result.stream().collect(HashMap::new, Map::putAll, Map::putAll);
	}

	public static List<PlayerProfile> getPlayerProfiles(List<String> usernames) throws IOException {
		// POST https://api.mojang.com/profiles/minecraft
		// content application/json, array of username
		// result list playerprofile

		JsonAdapter<List<PlayerProfile>> jsonAdapter = moshi
				.adapter(Types.newParameterizedType(List.class, PlayerProfile.class));

		List<PlayerProfile> result = jsonAdapter.fromJson(read(post(baseURL + "/profiles/minecraft", APPLICATION_JSON,
				Types.newParameterizedType(List.class, String.class), usernames)));

		return result;
	}
	
	public static SalesStats getSalesStats(SalesStatType... types) throws IOException {
		// POST https://api.mojang.com/orders/statistics
		// content application/json, metricKeys (list of all types)
		
		Map<String, SalesStatType[]> body = new HashMap<>();
		body.put("metricKeys", types);
		
		JsonAdapter<SalesStats> jsonAdapter = moshi
				.adapter(SalesStats.class);
		
		SalesStats result = jsonAdapter.fromJson(read(post(baseURL + "/orders/statistics", APPLICATION_JSON,
				Types.newParameterizedType(Map.class, String.class, Types.arrayOf(SalesStatType.class)), body)));
		
		return result;
	}
}
