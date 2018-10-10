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
