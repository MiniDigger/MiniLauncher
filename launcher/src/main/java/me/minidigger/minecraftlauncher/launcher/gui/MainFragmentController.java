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

import org.to2mbn.jmccc.auth.AuthenticationException;
import org.to2mbn.jmccc.auth.Authenticator;
import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.auth.yggdrasil.YggdrasilAuthenticator;
import org.to2mbn.jmccc.version.parsing.Versions;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.minidigger.minecraftlauncher.launcher.LauncherSettings;
import me.minidigger.minecraftlauncher.launcher.tasks.MinecraftStartTask;
import me.minidigger.minecraftlauncher.launcher.tasks.VersionCheckerTask;
import me.minidigger.minecraftlauncher.launcher.tasks.VersionListUpdaterTask;

public class MainFragmentController extends FragmentController {

    public static Stage applicationOptionStage;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Tooltip optionsTooltip;
    @FXML
    private Tooltip playTooltip;
    @FXML
    private Tooltip usernameTooltip;
    @FXML
    private Tooltip passwordTooltip;
    @FXML
    private Tooltip versionTooltip;
    @FXML
    private Label label;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private ComboBox<String> version;
    @FXML
    private Button launch;
    @FXML
    private Button options;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setToolTips();
        setTextBoxMax();

        username.setText(LauncherSettings.playerUsername);

        new VersionListUpdaterTask(minecraftDownloader, version, this::setStatusText, null, null, null, () -> {
            // remove all not downloaded
            version.getItems().removeIf(((v) -> {
                try {
                    return Versions.resolveVersion(minecraftDirectory, v) == null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return true;
                }
            }));
            // select the right one
            for (String ob : version.getItems()) {
                if (ob.equals(LauncherSettings.playerVersion)) {
                    Platform.runLater(() -> version.setValue(LauncherSettings.playerVersion));
                }
            }
            System.out.println("done");
        }).start();
    }

    @FXML
    private void launchMineCraft(ActionEvent event) {
        if (username.getText() == null || username.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("alert.username_missing.title"));
            alert.setHeaderText(resourceBundle.getString("alert.username_missing.header"));
            alert.setContentText(resourceBundle.getString("alert.username_missing.content"));
            alert.initStyle(StageStyle.UTILITY);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/css/purple.css");

            alert.show();
            return;
        } else if (version.getValue() == null || version.getValue().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("alert.version_missing.title"));
            alert.setHeaderText(resourceBundle.getString("alert.version_missing.header"));
            alert.setContentText(resourceBundle.getString("alert.version_missing.content"));
            alert.initStyle(StageStyle.UTILITY);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/css/purple.css");

            alert.show();
            return;
        }

        LauncherSettings.playerUsername = username.getText();
        LauncherSettings.playerVersion = version.getValue();
        LauncherSettings.userSettingsSave();

        disable(true);

        getMainFrame().loadAvatar();


        Authenticator authenticator;
        if (password.getText() == null || password.getText().equals("")) {
            authenticator = new OfflineAuthenticator(LauncherSettings.playerUsername);
        } else {
            try {
                authenticator = YggdrasilAuthenticator.password(LauncherSettings.playerUsername, password.getText());
            } catch (AuthenticationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("alert.invalidpw.title"));
                alert.setHeaderText(MessageFormat.format(resourceBundle.getString("alert.invalidpw.header"), version.getValue()));
                alert.setContentText(resourceBundle.getString("alert.invalidpw.content").replace("{msg}", e.getMessage()));
                alert.initStyle(StageStyle.UTILITY);

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add("/css/purple.css");
                alert.showAndWait();

                disable(false);

                e.printStackTrace();
                return;
            }
        }
        new MinecraftStartTask(this::onGameCorrupted, this::onGameStarted, authenticator, minecraftDirectory).start();
    }

    @FXML
    private void launchOptions(ActionEvent event) {
        getMainFrame().load(FrameController.Screen.OPTION);
    }

    public void onGameStarted() {
        LauncherSettings.playerUsername = username.getText();
        LauncherSettings.playerVersion = version.getValue();
        LauncherSettings.userSettingsSave();

        if (!LauncherSettings.keepLauncherOpen) {
            setStatusText(resourceBundle.getString("status.minecraft_started"));
            System.exit(0);
        } else {
            new VersionCheckerTask(this::setStatusText).start();
        }

        disable(false);
    }

    public void onGameCorrupted() {
        Platform.runLater(() -> {
            setStatusText(resourceBundle.getString("status.corrupted"));

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("alert.corruption.title"));
            alert.setHeaderText(MessageFormat.format(resourceBundle.getString("alert.corruption.header"), version.getValue()));
            alert.setContentText(resourceBundle.getString("alert.corruption.content"));
            alert.initStyle(StageStyle.UTILITY);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/css/purple.css");
            alert.showAndWait();

            disable(false);
        });
    }

    private void setToolTips() {
        Image infoIMG = new Image(getClass().getResourceAsStream("/images/m_info.png"));

        usernameTooltip.setText(resourceBundle.getString("mainscreen.tooltip.username"));
        usernameTooltip.setGraphic(new ImageView(infoIMG));

        passwordTooltip.setText(resourceBundle.getString("mainscreen.tooltip.password"));
        passwordTooltip.setGraphic(new ImageView(infoIMG));

        versionTooltip.setText(resourceBundle.getString("mainscreen.tooltip.version"));
        versionTooltip.setGraphic(new ImageView(infoIMG));

        optionsTooltip.setText(resourceBundle.getString("mainscreen.tooltip.options"));
        optionsTooltip.setGraphic(new ImageView(infoIMG));

        playTooltip.setText(resourceBundle.getString("mainscreen.tooltip.play"));
        playTooltip.setGraphic(new ImageView(infoIMG));
    }

    private void setTextBoxMax() {
        username.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (username.getText().length() > 16 && !username.getText().contains("@")) {
                    username.setText(username.getText().substring(0, 16));
                    //Toolkit.getDefaultToolkit().beep();
                }
            }
        });
    }

    @FXML
    private void kt_username(KeyEvent event) {
        if (!event.getCharacter().matches("[A-Za-z0-9_.@]")) {
            //Toolkit.getDefaultToolkit().beep();
            event.consume();
        }
    }

    @FXML
    private void kt_password(KeyEvent event) {

    }

    @Override
    public void onClose() {

    }

    private void disable(boolean b) {
        username.setDisable(b);
        password.setDisable(b);
        options.setDisable(b);
        launch.setDisable(b);
        version.setDisable(b);
    }
}
