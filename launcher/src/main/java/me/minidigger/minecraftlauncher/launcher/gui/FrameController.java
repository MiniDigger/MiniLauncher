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

package me.minidigger.minecraftlauncher.launcher.gui;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import me.minidigger.minecraftlauncher.launcher.LauncherMain;
import me.minidigger.minecraftlauncher.launcher.LauncherSettings;
import me.minidigger.minecraftlauncher.launcher.Status;
import me.minidigger.minecraftlauncher.launcher.tasks.VersionCheckerTask;

/**
 * @author ammar
 */
public class FrameController extends AbstractGUIController {

    private List<String> backgroundList = new ArrayList<>();
    private FragmentController currentFragment;

    enum Screen {
        MAIN, OPTION, SKIN
    }

    @FXML
    private AnchorPane mainBackground;
    @FXML
    private Button minimize;
    @FXML
    private Button exit;
    @FXML
    private Label launcherStatus;
    @FXML
    private Pane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        runBackground();

        new VersionCheckerTask((status) -> launcherStatus.setText(status)).start();

        if (LauncherSettings.firstStart) {
            showFirstTimeMessage();
            LauncherSettings.firstStart = false;
            LauncherSettings.userSettingsSave();
        }

        load(Screen.MAIN);
    }

    public void load(Screen screen) {
        String fxml = "";
        switch (screen) {
            case MAIN:
                fxml = "/fxml/MainScreen.fxml";
                break;
            case OPTION:
                throw new UnsupportedOperationException("Not implemented");
            case SKIN:
                throw new UnsupportedOperationException("Not implemented");
        }
        try (InputStream fxmlStream = getClass().getResource(fxml).openStream()) {
            contentPane.getChildren().clear();

            FXMLLoader loader = new FXMLLoader();
            Node node = loader.load(fxmlStream);
            currentFragment = loader.getController();
            currentFragment.setMainFrame(this);

            contentPane.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFirstTimeMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("alert.welcome.title"));
        alert.setHeaderText(resourceBundle.getString("alert.welcome.header"));
        alert.setContentText(resourceBundle.getString("alert.welcome.content"));
        alert.initStyle(StageStyle.UTILITY);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("/css/purple.css");

        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();

        alert.show();
    }

    @FXML
    private void launchMinimize(ActionEvent event) {
        Stage stage = LauncherMain.getApplicationMainStage();
        stage.setIconified(true);
    }

    @FXML
    private void launchExit(ActionEvent event) {
        saveSettings();
        Stage stage = LauncherMain.getApplicationMainStage();
        stage.close();
    }

    private void runBackground() {
        for (int i = 1; i < 11; i++) {
            backgroundList.add("background_" + i);
        }

        int randomBG = ThreadLocalRandom.current().nextInt(0, backgroundList.size());
        mainBackground.setStyle("-fx-background-image: url('/images/" + backgroundList.get(randomBG) + ".jpg')");

        Timeline rotateBackground = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            int randomBG1 = ThreadLocalRandom.current().nextInt(0, backgroundList.size());

            mainBackground.setStyle("-fx-background-image: url('/images/" + backgroundList.get(randomBG1) + ".jpg')");
        }));
        rotateBackground.setCycleCount(Timeline.INDEFINITE);
        rotateBackground.play();
    }

    @Override
    public void setStatus(Status status) {
        launcherStatus.setText(MessageFormat.format(resourceBundle.getString("status.generic"), status));
    }

    private void saveSettings() {
//        LauncherSettings.playerUsername = username.getText();
//        if (version.getValue() != null) {
//            LauncherSettings.playerVersion = version.getValue();
//        }
//        LauncherSettings.userSettingsSave();//TODO
    }

    /*
     * event handler
     */

    @Override
    public void setStatusText(String text) {
        launcherStatus.setText(text);
    }

    @Override
    public void onGameStart(@NonNull StartStatus status) {
        currentFragment.onGameStart(status);
        Platform.runLater(() -> {
            switch (status) {
                case VALIDATING:
                    setStatusText(MessageFormat.format(resourceBundle.getString("status.validating"), LauncherSettings.playerVersion));
                    break;
                case DOWNLOADING_NATIVES:
                    setStatusText(resourceBundle.getString("status.downloading_natives"));
                    break;
                case PATCHING_NETTY:
                    setStatusText(resourceBundle.getString("status.patching_netty"));
                    break;
                case STARTING:
                    setStatusText(MessageFormat.format(resourceBundle.getString("status.starting"), LauncherSettings.playerVersion));
                    break;
            }
        });
    }

    @Override
    public void onGameStarted() {
        currentFragment.onGameStarted();
    }

    @Override
    public void onGameCorrupted(int exitCode) {
        currentFragment.onGameCorrupted(exitCode);
    }

    @Override
    public void onDownloadComplete() {
        currentFragment.onDownloadComplete();
    }

    @Override
    public void onDownload(@NonNull DownloadingStatus downloadingStatus) {
        currentFragment.onDownload(downloadingStatus);
        // TODO: direct log message passing is not supported
        Platform.runLater(() -> {
            switch (downloadingStatus) {
                case ASSETS:
                    setStatus(Status.DOWNLOADING_GAME_ASSETS);
                    break;
                case LAUNCHER_META:
                    setStatus(Status.DOWNLOADING_LAUNCHER_META);
                    break;
                case LIBRARIES:
                    setStatus(Status.DOWNLOADING_LIBRARIES);
                    break;
                case MINECRAFT:
                    setStatus(Status.DOWNLOADING_MINECRAFT);
                    break;
                case NATIVES:
                    setStatus(Status.FINALIZING);
                    break;
            }
        });
    }
}
