package me.minidigger.minecraftlauncher.launcher.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import me.minidigger.minecraftlauncer.renderer.SkinCanvas;
import me.minidigger.minecraftlauncer.renderer.animation.animations.RunningAnimation;
import me.minidigger.minecraftlauncher.launcher.LauncherSettings;

public class SkinFragmentController extends FragmentController {

    @FXML
    private Pane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainPane.getChildren().clear();
        try {
            SkinCanvas canvas = new SkinCanvas(LauncherSettings.playerUsername, 250, 200, true);
            canvas.getAnimationPlayer().addSkinAnimation(
//                new MagmaArmsAnimation(100, 500, 90, canvas));
//                new WavingArmsAnimation(100, 500, 90, canvas));
                    new RunningAnimation(100, 800, 30, canvas));
            mainPane.getChildren().add(canvas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose() {

    }
}
