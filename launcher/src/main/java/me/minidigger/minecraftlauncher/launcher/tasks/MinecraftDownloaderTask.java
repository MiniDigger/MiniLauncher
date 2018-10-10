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

import org.to2mbn.jmccc.mcdownloader.AssetOption;
import org.to2mbn.jmccc.mcdownloader.ChecksumOption;
import org.to2mbn.jmccc.mcdownloader.MavenOption;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloadOption;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloader;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.CallbackAdapter;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.CombinedDownloadCallback;
import org.to2mbn.jmccc.mcdownloader.download.concurrent.DownloadCallback;
import org.to2mbn.jmccc.mcdownloader.download.tasks.DownloadTask;
import org.to2mbn.jmccc.option.MinecraftDirectory;
import org.to2mbn.jmccc.version.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MinecraftDownloaderTask extends Thread {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("minilauncher");

    private boolean force;
    private String version;
    private MinecraftDownloader minecraftDownloader;
    private MinecraftDirectory minecraftDirectory;
    private Consumer<String> statusConsumer;
    private Consumer<Version> callback;

    public MinecraftDownloaderTask(boolean force, String version, MinecraftDownloader minecraftDownloader,
                                   MinecraftDirectory minecraftDirectory, Consumer<String> statusConsumer, Consumer<Version> callback) {
        this.force = force;
        this.version = version;
        this.minecraftDownloader = minecraftDownloader;
        this.minecraftDirectory = minecraftDirectory;
        this.statusConsumer = statusConsumer;
        this.callback = callback;
        setName("MinecraftDownloaderTask");
    }

    @Override
    public void run() {
        List<MinecraftDownloadOption> options = new ArrayList<>();
        if (force) {
            options.add(AssetOption.FORCIBLY_DOWNLOAD);
            options.add(MavenOption.UPDATE_SNAPSHOTS);
            options.add(ChecksumOption.CHECK_ASSETS);
            options.add(ChecksumOption.CHECK_LIBRARIES);
        }

        minecraftDownloader.downloadIncrementally(minecraftDirectory, version, new CombinedDownloadCallback<Version>() {
            @Override
            public <R> DownloadCallback<R> taskStart(DownloadTask<R> task) {
                return new JMCCCCallback<>(statusConsumer, resourceBundle);
            }

            @Override
            public void done(Version result) {
                statusConsumer.accept(resourceBundle.getString("status.done"));
                callback.accept(result);
            }

            @Override
            public void failed(Throwable e) {
                statusConsumer.accept(resourceBundle.getString("status.download_failed"));
                e.printStackTrace();
            }

            @Override
            public void cancelled() {
                statusConsumer.accept(resourceBundle.getString("status.cancelled"));
            }
        }, options.toArray(new MinecraftDownloadOption[0]));
    }
}
