package me.minidigger.minecraftlauncher.mojangapi.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;

import me.minidigger.minecraftlauncher.mojangapi.AuthenticationEndpoint;
import me.minidigger.minecraftlauncher.mojangapi.model.AuthRequest;
import me.minidigger.minecraftlauncher.mojangapi.model.AuthResponse;
import me.minidigger.minecraftlauncher.mojangapi.model.RefreshRequest;
import me.minidigger.minecraftlauncher.mojangapi.model.TokenRequest;

public class AuthenticationEndpointTest {

	private String TESTUSER = "MiniDigger";
	private UUID TESTUUID = UUID.fromString("2c771d16-f492-4f7a-adb7-61c38a9c9bfb");

	private String TESTMAIL = "";
	private String TESTPASS = "";
	private String TESTTOKEN = "";

	@Test
	public void test_authenticate() throws IOException {
		AuthResponse resp = AuthenticationEndpoint.authenticate(new AuthRequest(TESTMAIL, TESTPASS));
		System.out.println(resp);
	}

	@Test
	public void test_authenticate_withUser() throws IOException {
		AuthResponse resp = AuthenticationEndpoint.authenticate(new AuthRequest(TESTMAIL, TESTPASS, true));
		System.out.println(resp);
	}

	@Test
	public void test_refresh() throws IOException {
		AuthResponse resp = AuthenticationEndpoint.refresh(new RefreshRequest(TESTTOKEN));
		System.out.println(resp);
	}

	@Test
	public void test_refresh_withUser() throws IOException {
		AuthResponse resp = AuthenticationEndpoint.refresh(new RefreshRequest(TESTTOKEN, true));
		System.out.println(resp);
	}

	@Test
	public void test_validate() throws IOException {
		boolean result = AuthenticationEndpoint.validate(new TokenRequest(TESTTOKEN));
		assertTrue(result);
	
		result = AuthenticationEndpoint.validate(new TokenRequest(TESTTOKEN+ "222"));
		assertFalse(result);
	}

	@Test
	public void test_invalidate() throws IOException {
		AuthenticationEndpoint.invalidate(new TokenRequest(TESTTOKEN));
		assertFalse(AuthenticationEndpoint.validate(new TokenRequest(TESTTOKEN)));
	}

	@Test
	public void test_join() throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Test
	public void test_hasJoined() throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}
}