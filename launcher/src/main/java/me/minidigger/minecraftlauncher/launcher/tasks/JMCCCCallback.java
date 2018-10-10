package me.minidigger.minecraftlauncher.launcher.tasks;

import org.to2mbn.jmccc.mcdownloader.download.concurrent.CallbackAdapter;

import java.util.ResourceBundle;
import java.util.function.Consumer;

public class JMCCCCallback<R> extends CallbackAdapter<R> {
    private Consumer<String> statusConsumer;
    private ResourceBundle resourceBundle;

    public JMCCCCallback(Consumer<String> statusConsumer, ResourceBundle resourceBundle) {
        this.statusConsumer = statusConsumer;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void done(R result) {
        statusConsumer.accept(resourceBundle.getString("status.done"));
    }

    @Override
    public void failed(Throwable e) {
        statusConsumer.accept(resourceBundle.getString("status.download_failed"));
    }

    @Override
    public void cancelled() {
        statusConsumer.accept(resourceBundle.getString("status.cancelled"));
    }

    @Override
    public void updateProgress(long done, long total) {
        statusConsumer.accept(String.format(resourceBundle.getString("status.progress"), done, total));
    }

    @Override
    public void retry(Throwable e, int current, int max) {
        statusConsumer.accept(String.format(resourceBundle.getString("status.retry"), current, max));
    }
}
