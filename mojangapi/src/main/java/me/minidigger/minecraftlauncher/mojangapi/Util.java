package me.minidigger.minecraftlauncher.mojangapi;

import java.util.UUID;

public class Util {

    public static String mojangUUIDtoNormal(String uuid) {
        return uuid.substring(0, 8) +
                "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) +
                "-" + uuid.substring(20, 32);
    }

    public static String normalUUIDtoMojang(String uuid) {
        return uuid.replace("-", "");
    }

    public static String normalUUIDtoMojang(UUID uuid) {
        return uuid.toString().replace("-", "");
    }
}
