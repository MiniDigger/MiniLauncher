package me.minidigger.minecraftlauncher.launcher.gui;

import org.checkerframework.checker.nullness.qual.NonNull;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import me.minidigger.minecraftlauncher.api.LauncherAPI;
import me.minidigger.minecraftlauncher.api.events.LauncherEventHandler;
import me.minidigger.minecraftlauncher.launcher.Status;

public abstract class AbstractGUIController implements Initializable, LauncherEventHandler {

    protected LauncherAPI API;

    public AbstractGUIController(){
        API = new LauncherAPI();
        API.setEventHandler(this);
    }

    @Override
    public void onDownload(@NonNull Downloadable downloadable) {
        // TODO: direct log message passing is not supported
        Platform.runLater(() -> {
            switch(downloadable) {
                case ASSETS:
                    setStatus(Status.DOWNLOADING);
                    break;
                case LAUNCHER_META:
                    setStatus(Status.DOWNLOADING_LM);
                    break;
                case LIBRARIES:
                    setStatus(Status.DOWNLOADING_L);
                    break;
                case MINECRAFT:
                    setStatus(Status.DOWNLOADING_M);
                    break;
                case NATIVES:
                    setStatus(Status.FINALIZING);
                    break;
            }
        });
    }

    public abstract void setStatus(Status finalizing);
}
