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

    public static Hashtable<String, String> VersionHashTable;

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

            }
        }, CacheOption.CACHE);
    }
}
