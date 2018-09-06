package me.minidigger.minecraftlauncher.mojangapi.async;

import java.util.concurrent.CompletableFuture;

import me.minidigger.minecraftlauncher.mojangapi.AuthenticationEndpoint;
import me.minidigger.minecraftlauncher.mojangapi.model.AuthRequest;
import me.minidigger.minecraftlauncher.mojangapi.model.AuthResponse;
import me.minidigger.minecraftlauncher.mojangapi.model.PlayerProfile;
import me.minidigger.minecraftlauncher.mojangapi.model.RefreshRequest;
import me.minidigger.minecraftlauncher.mojangapi.model.TokenRequest;

public class AuthenticationEndpointAsync extends AbstractAsyncEndpoint {

	public static CompletableFuture<AuthResponse> authenticate(AuthRequest request) {
		return get(request, AuthenticationEndpoint::authenticate);
	}

	public static CompletableFuture<AuthResponse> refresh(RefreshRequest request) {
		return get(request, AuthenticationEndpoint::refresh);
	}

	public static CompletableFuture<Boolean> validate(TokenRequest request) {
		return get(request, AuthenticationEndpoint::validate);
	}

	public static CompletableFuture<Void> signout(String username, String password) {
		return getVoid(username, password, AuthenticationEndpoint::signout);
	}

	public static CompletableFuture<Void> invalidate(TokenRequest request) {
		return getVoid(request, AuthenticationEndpoint::invalidate);
	}

	// TODO https://wiki.vg/Protocol_Encryption#Authentication
	public static void join() {
		throw new UnsupportedOperationException("Not implemented");
	}

	public static PlayerProfile hasJoined() {
		throw new UnsupportedOperationException("Not implemented");
	}
}
