package me.minidigger.minecraftlauncher.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MinecraftLogThread extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(MinecraftLogThread.class);

    private InputStream inputStream;

    public MinecraftLogThread(InputStream inputStream) {
        this.inputStream = inputStream;
        setName("MinecraftLogThread");
        start();
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                logger.info("MINECRAFT: " + buffer);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
