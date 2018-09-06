package me.minidigger.minecraftlauncher.mojangapi.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import me.minidigger.minecraftlauncher.mojangapi.StatusEndpoint;
import me.minidigger.minecraftlauncher.mojangapi.model.Status;

public class StatusEndpointTest {

	@Test
	public void test_check() throws IOException{
		Map<String, Status> status = StatusEndpoint.check();
		System.out.println(status);
		assertNotNull(status);
		assertTrue(status.size() > 0);
	}
}
