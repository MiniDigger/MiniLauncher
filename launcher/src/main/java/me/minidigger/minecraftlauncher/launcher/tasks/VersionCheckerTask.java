/*
 * MIT License
 *
 * Copyright (c) 2018 Ammar Ahmad
 * Copyright (c) 2018 Martin Benndorf
 * Copyright (c) 2018 Mark Vainomaa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
