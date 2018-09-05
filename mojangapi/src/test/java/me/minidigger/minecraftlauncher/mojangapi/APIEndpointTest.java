package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import me.minidigger.minecraftlauncher.mojangapi.APIEndpoint;
import me.minidigger.minecraftlauncher.mojangapi.model.PlayerProfile;
import me.minidigger.minecraftlauncher.mojangapi.model.SalesStatType;
import me.minidigger.minecraftlauncher.mojangapi.model.SalesStats;

public class APIEndpointTest {

	private String TESTUSER = "MiniDigger";
	private UUID TESTUUID = UUID.fromString("2c771d16-f492-4f7a-adb7-61c38a9c9bfb");

	@Test
	public void test_getUUID() throws IOException {
		UUID uuid = APIEndpoint.getUUID(TESTUSER);
		assertNotNull(uuid);
		assertEquals(TESTUUID, uuid);
	}

	@Test
	public void test_getUUID_timestamp() throws IOException {
		UUID uuid = APIEndpoint.getUUID(TESTUSER, System.currentTimeMillis());
		assertNotNull(uuid);
		assertEquals(TESTUUID, uuid);
	}

	@Test
	@Ignore // TODO fix name history endpoint
	public void test_getNameHistory() throws IOException {
		Map<String, Long> nameHistory = APIEndpoint.getNameHistory(TESTUUID);
		assertNotNull(nameHistory);
		assertTrue(nameHistory.size() == 0);

		nameHistory = APIEndpoint.getNameHistory(UUID.fromString("5a745bdb-0e99-430d-a35d-7598b4c74bf3"));
		assertNotNull(nameHistory);
		assertTrue(nameHistory.size() > 0);
	}

	@Test
	public void test_getPlayerProfiles() throws IOException {
		List<PlayerProfile> playerProfiles = APIEndpoint
				.getPlayerProfiles(Arrays.asList(TESTUSER.toLowerCase(), "Notch"));
		assertNotNull(playerProfiles);
		assertTrue(playerProfiles.size() == 2);
		assertEquals(TESTUUID, playerProfiles.get(1).getUuid());
		assertEquals(TESTUSER, playerProfiles.get(1).getUsername());
	}

	@Test
	public void test_getSalesStats() throws IOException {
		SalesStats stats = APIEndpoint.getSalesStats(SalesStatType.ITEM_SOLD_MINECRAFT,
				SalesStatType.PREPAID_CARD_REDEEMED_MINECRAFT);
		assertTrue(stats.getTotal() > 0);
		assertTrue(stats.getLast24h() > 0);
		assertTrue(stats.getSaleVelocityPerSeconds() > 0);
	}
}
