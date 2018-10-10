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

import org.to2mbn.jmccc.mcdownloader.CacheOption;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloader;
import org.to2mbn.jmccc.mcdownloader.RemoteVersionList;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.CombinedDownloadCallback;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.DownloadCallback;
import org.to2mbn.jmccc.mcdownloader.download.tasks.DownloadTask;

import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.scene.control.ComboBox;

public class VersionListUpdaterTask extends Thread {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("minilauncher");
    private MinecraftDownloader minecraftDownloader;

    public static Hashtable<String, String> VersionHashTable = new Hashtable<>();

    private ComboBox<String> optionsSelectVersion;
    private Consumer<String> statusConsumer;
    private Runnable enable;
    private Runnable disable;
    private Runnable callback;

    public VersionListUpdaterTask(MinecraftDownloader minecraftDownloader, ComboBox<String> optionsSelectVersion, Consumer<String> statusConsumer,
                                  Hashtable<String, String> versionHashTable, Runnable enable, Runnable disable, Runnable callback) {
        this.minecraftDownloader = minecraftDownloader;
        this.optionsSelectVersion = optionsSelectVersion;
        this.statusConsumer = statusConsumer;
        this.enable = enable;
        this.disable = disable;
        this.callback = callback;
        if (versionHashTable != null) {
            VersionHashTable = versionHashTable;
        }
        setName("VersionListUpdaterTask");
    }

    @Override
    public void run() {
        minecraftDownloader.fetchRemoteVersionList(new CombinedDownloadCallback<RemoteVersionList>() {
            @Override
            public <R> DownloadCallback<R> taskStart(DownloadTask<R> task) {
                statusConsumer.accept(resourceBundle.getString("status.getting_latest_version"));
                optionsSelectVersion.setDisable(true);
                if(disable != null) disable.run();

                return new JMCCCCallback<>(statusConsumer, resourceBundle);
            }

            @Override
            public void done(RemoteVersionList result) {
                for (String key : result.getVersions().keySet()) {
                    optionsSelectVersion.getItems().addAll(key);
                    VersionHashTable.put(key, result.getVersions().get(key).getVersion());
                }

                optionsSelectVersion.setDisable(false);
                if(enable != null) enable.run();

                callback.run();
            }

            @Override
            public void failed(Throwable e) {
                statusConsumer.accept(resourceBundle.getString("status.getting_latest_version_failed"));
                e.printStackTrace();
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled");
            }
        }, CacheOption.CACHE);
    }
}
