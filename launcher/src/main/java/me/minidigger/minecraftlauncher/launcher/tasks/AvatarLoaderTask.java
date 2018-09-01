package me.minidigger.minecraftlauncher.launcher.tasks;

import java.util.function.Consumer;

import javafx.scene.image.Image;
import me.minidigger.minecraftlauncher.launcher.LauncherSettings;

public class AvatarLoaderTask extends Thread {

    public Consumer<Image> callback;

    public AvatarLoaderTask(Consumer<Image> callback) {
        this.callback = callback;
        setName("AvatarLoaderTask");
    }

    public void run() {
        Image image = new Image("http://minotar.net/avatar/" + LauncherSettings.playerUsername + "/100");
        if (!image.isError()) //incase someone adds random shit or changes URL. Will show steve not a blank area.
        {
            callback.accept(image);
        }
    }
}
