package me.minidigger.minecraftlauncher.mojangapi;

import java.awt.Image;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.squareup.moshi.JsonAdapter;

import me.minidigger.minecraftlauncher.mojangapi.model.PlayerProfile;

public class SessionEndpoint extends AbstractEndpoint {

    private static final String baseURL = "https://sessionserver.mojang.com";

    public static PlayerProfile getPlayerProfile(UUID uuid) throws IOException {
        return getPlayerProfile(uuid, false);
    }

    public static PlayerProfile getPlayerProfile(UUID uuid, boolean signature) throws IOException {
        // GET https://sessionserver.mojang.com/session/minecraft/profile/<uuid>

        JsonAdapter<PlayerProfile> jsonAdapter = moshi.adapter(PlayerProfile.class);

        PlayerProfile result = jsonAdapter.fromJson(read(get(
                baseURL + "/session/minecraft/profile/" + Util.normalUUIDtoMojang(uuid) + "?unsigned=" + !signature)));

        return result;
    }

    public static void changeSkin(String accessToken, UUID uuid, String url, boolean slim) throws IOException {
        // POST https://api.mojang.com/user/profile/<uuid>/skin
        // content model=<""/"slim">&url=<skin url>
        // needs token
    }

    public static void uploadSkin(String accessToken, UUID uuid, Image image, boolean slim) throws IOException {
        // PUT https://api.mojang.com/user/profile/<uuid>/skin
        // content multipart, model slim/"", image file
    }

    public static void resetSkin(String accessTokein, UUID uuid) throws IOException {
        // DELETE https://api.mojang.com/user/profile/<uuid>/skin
    }

    public static List<String> getBlockedServers() throws IOException {
        // GET https://sessionserver.mojang.com/blockedservers
        // response: raw text
        String content = read(get(baseURL + "/blockedservers"));
        String[] blockedServers = content.split("\n");
        return Arrays.asList(blockedServers);
    }
}
