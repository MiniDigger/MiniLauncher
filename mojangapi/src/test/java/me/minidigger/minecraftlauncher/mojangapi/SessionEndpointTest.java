package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import me.minidigger.minecraftlauncher.mojangapi.SessionEndpoint;
import me.minidigger.minecraftlauncher.mojangapi.model.PlayerProfile;

public class SessionEndpointTest {
	
	private String TESTUSER = "MiniDigger";
	private UUID TESTUUID = UUID.fromString("2c771d16-f492-4f7a-adb7-61c38a9c9bfb");

	@Test
	public void test_getPlayerProfile() throws IOException	{
		PlayerProfile profile = SessionEndpoint.getPlayerProfile(TESTUUID);
		assertNotNull(profile);
		assertEquals(TESTUUID, profile.getUuid());
		assertEquals(TESTUSER, profile.getUsername());
		assertNotNull(profile.getProperties());
		assertTrue(profile.getProperties().size() == 1);
		assertTrue(profile.getProperties().get(0).getSignature() == null);
		System.out.println(profile);
	}
	
	@Test
	public void test_getPlayerProfile_withSignature() throws IOException	{
		PlayerProfile profile = SessionEndpoint.getPlayerProfile(TESTUUID, true);
		assertNotNull(profile);
		assertEquals(TESTUUID, profile.getUuid());
		assertEquals(TESTUSER, profile.getUsername());
		assertNotNull(profile.getProperties());
		assertTrue(profile.getProperties().size() == 1);
		assertNotNull(profile.getProperties().get(0).getSignature());
		System.out.println(profile);
	}
	
	
	@Test
	public void test_getBlockedServers() throws IOException	{
		List<String> blockedServers = SessionEndpoint.getBlockedServers();
		assertNotNull(blockedServers);
		assertTrue(blockedServers.size() > 0);
	}
}
