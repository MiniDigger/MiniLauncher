package me.minidigger.minecraftlauncher.mojangapi.test;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import me.minidigger.minecraftlauncher.mojangapi.StatusEndpoint;
import me.minidigger.minecraftlauncher.mojangapi.model.Status;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StatusEndpointTest {

    @Test
    public void test_check() throws IOException {
        Map<String, Status> status = StatusEndpoint.check();
        System.out.println(status);
        assertNotNull(status);
        assertTrue(status.size() > 0);
    }
}
