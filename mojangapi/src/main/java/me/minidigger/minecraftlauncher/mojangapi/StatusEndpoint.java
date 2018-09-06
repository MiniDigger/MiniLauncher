package me.minidigger.minecraftlauncher.mojangapi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;

import me.minidigger.minecraftlauncher.mojangapi.model.Status;

public class StatusEndpoint extends AbstractEndpoint {

    private static final String baseURL = "https://status.mojang.com";

    public static Map<String, Status> check() throws IOException {
        // GET https://status.mojang.com/check
        // response: list<map<system,status>>

        JsonAdapter<List<Map<String, Status>>> jsonAdapter = moshi.adapter(Types.newParameterizedType(List.class,
                Types.newParameterizedType(Map.class, String.class, Status.class)));

        List<Map<String, Status>> result = jsonAdapter.fromJson(read(get(baseURL + "/check")));

        return result.stream().collect(HashMap::new, Map::putAll, Map::putAll);
    }
}
