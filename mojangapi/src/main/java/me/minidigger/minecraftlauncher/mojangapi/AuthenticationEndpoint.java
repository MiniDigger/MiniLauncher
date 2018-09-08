package me.minidigger.minecraftlauncher.mojangapi;

import com.squareup.moshi.JsonAdapter;

import java.io.IOException;
import java.util.UUID;

import me.minidigger.minecraftlauncher.mojangapi.model.AuthRequest;
import me.minidigger.minecraftlauncher.mojangapi.model.AuthResponse;
import me.minidigger.minecraftlauncher.mojangapi.model.PlayerProfile;
import me.minidigger.minecraftlauncher.mojangapi.model.RefreshRequest;
import me.minidigger.minecraftlauncher.mojangapi.model.TokenRequest;
import okhttp3.Response;

public class AuthenticationEndpoint extends AbstractEndpoint {
    // TODO we want to store this one somewhere on the disk, else all tokens get
    // invalidated all the time (needs to be auto generated on first start)
    public static final UUID clientToken = UUID.fromString("2c771d16-f492-4f7a-adb7-61c38a9c9bfb");

    private static final String baseURL = "https://authserver.mojang.com";

    public static AuthResponse authenticate(AuthRequest request) throws IOException {
        // POST https://authserver.mojang.com/authenticate
        // body authrequest
        // response authresponse

        JsonAdapter<AuthResponse> jsonAdapter = moshi.adapter(AuthResponse.class);

        return jsonAdapter
                .fromJson(read(post(baseURL + "/authenticate", APPLICATION_JSON, AuthRequest.class, request)));
    }

    public static AuthResponse refresh(RefreshRequest request) throws IOException {
        // POST https://authserver.mojang.com/refresh
        // body refreshrequest
        // response authresponse

        JsonAdapter<AuthResponse> jsonAdapter = moshi.adapter(AuthResponse.class);

        return jsonAdapter.fromJson(read(post(baseURL + "/refresh", APPLICATION_JSON, RefreshRequest.class, request)));
    }

    public static boolean validate(TokenRequest request) throws IOException {
        // POST https://authserver.mojang.com/validate
        // body tokenrequest

        Response resp = post(baseURL + "/validate", APPLICATION_JSON, TokenRequest.class, request);
        if (resp.code() == 204) {
            return true;
        } else if (resp.code() == 403) {
            return false;
        } else {
            // TODO properly read error message if present
            throw new RuntimeException("error (" + resp.code() + "):" + resp.body().string());
        }
    }

    public static void signout(String username, String password) throws IOException {
        throw new UnsupportedOperationException("Not supported, DO NOT STORE USERS PASSWORDS, ONLY THE AUTH TOKEN!");
    }

    public static void invalidate(TokenRequest request) throws IOException {
        // POST https://authserver.mojang.com/invalidate
        // body tokenrequest

        read(post(baseURL + "/invalidate", APPLICATION_JSON, TokenRequest.class, request));
    }

    // TODO https://wiki.vg/Protocol_Encryption#Authentication
    public static void join() throws IOException {
        // POST https://sessionserver.mojang.com/session/minecraft/join
        // body accessToken, selectedprofile, serverId

        throw new UnsupportedOperationException("Not implemented");
    }

    public static PlayerProfile hasJoined() throws IOException {
        // GET
        // https://sessionserver.mojang.com/session/minecraft/hasJoined?username=username&serverId=hash&ip=ip
        // result player profile with skin

        throw new UnsupportedOperationException("Not implemented");
    }
}