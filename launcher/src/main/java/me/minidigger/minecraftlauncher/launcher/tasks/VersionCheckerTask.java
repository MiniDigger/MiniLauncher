package me.minidigger.minecraftlauncher.launcher.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

import javafx.application.Platform;
import me.minidigger.minecraftlauncher.launcher.LauncherSettings;

public class VersionCheckerTask extends Thread {

    private Consumer<String> statusConsumer;

    public VersionCheckerTask(Consumer<String> statusConsumer){
        this.statusConsumer = statusConsumer;
        setName("VersionCheckerTask");
    }

    @Override
    public void run() {
        try {
            Platform.runLater(() -> statusConsumer.accept("Status: Checking launcher version."));
            URL versionLastesturl = new URL(LauncherSettings.updateURL);
            URLConnection con = versionLastesturl.openConnection();
            con.setUseCaches(false); //had to as it was caching it.

            BufferedReader in = new BufferedReader(new InputStreamReader(versionLastesturl.openStream()));
            String line;

            while ((line = in.readLine()) != null) {
                if (LauncherSettings.launcherVersion.equals(line)) {
                    //TODO better update check, check semver
                    Platform.runLater(() -> statusConsumer.accept("Status: Your launcher is up to date!"));
                } else {
                    Platform.runLater(() -> statusConsumer.accept("Status: Your launcher is outdated! Please update it."));
                }
            }
            in.close();

        } catch (IOException e) {
            Platform.runLater(() -> statusConsumer.accept("Status: Unable to check for latest version!"));
        }
    }
}
