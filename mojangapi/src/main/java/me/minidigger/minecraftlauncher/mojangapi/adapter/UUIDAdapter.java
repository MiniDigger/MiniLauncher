package me.minidigger.minecraftlauncher.mojangapi.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.util.UUID;

import me.minidigger.minecraftlauncher.mojangapi.Util;

public class UUIDAdapter {
    @ToJson
    String toJson(UUID uuid) {
        return Util.normalUUIDtoMojang(uuid);
    }

    @FromJson
    UUID fromJson(String uuid) {
        return UUID.fromString(Util.mojangUUIDtoNormal(uuid));
    }
}
