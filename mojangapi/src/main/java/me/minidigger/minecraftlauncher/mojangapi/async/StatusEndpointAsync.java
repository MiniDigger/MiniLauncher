package me.minidigger.minecraftlauncher.mojangapi.async;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import me.minidigger.minecraftlauncher.mojangapi.StatusEndpoint;
import me.minidigger.minecraftlauncher.mojangapi.model.Status;

public class StatusEndpointAsync extends AbstractAsyncEndpoint {

    public static CompletableFuture<Map<String, Status>> check() {
        return get(StatusEndpoint::check);
    }
}
