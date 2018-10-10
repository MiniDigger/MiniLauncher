package me.minidigger.minecraftlauncher.launcher.tasks;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;

import org.json.JSONObject;

import java.time.OffsetDateTime;

public class DiscordIntegrationTask extends Thread {

    private IPCClient client;
    private boolean connected;

    public DiscordIntegrationTask() {
        setName("DiscordThread");
    }

    @Override
    public void run() {
        client = new IPCClient(499578469412503572L);
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                RichPresence.Builder builder = new RichPresence.Builder();
                builder.setState("Playing on secret-test-server.dyescape.com")
//                        .setDetails("Frustration level: Over 9000")
                        .setStartTimestamp(OffsetDateTime.now())
                        .setLargeImage("canary-large", "Discord Canary")
//                        .setSmallImage("ptb-small", "Discord PTB")
                        .setParty("party1234", 1, 200)
                        .setMatchSecret("xyzzy")
                        .setJoinSecret("join")
                        .setSpectateSecret("look");
                sendRichPresence(builder.build());
            }

            @Override
            public void onActivityJoin(IPCClient client, String secret) {
                System.out.println("JOIN secret " + secret);
            }

            @Override
            public void onActivityJoinRequest(IPCClient client, String secret, User user) {
                System.out.println("JOIN secret " + secret + " user " + user.getName());
            }

            @Override
            public void onActivitySpectate(IPCClient client, String secret) {
                System.out.println("SPEC secret " + secret);
            }

            @Override
            public void onClose(IPCClient client, JSONObject json) {
                System.out.println("close");
                connected = false;
            }

            @Override
            public void onDisconnect(IPCClient client, Throwable t) {
                System.out.println("dc");
                connected = false;
            }
        });
        try {
            client.connect();
            connected = true;
        } catch (NoDiscordClientException e) {
            e.printStackTrace();
        }
    }

    public void sendRichPresence(RichPresence rp) {
        client.sendRichPresence(rp);
    }
}
