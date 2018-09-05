package me.minidigger.minecraftlauncher.launcher.gui;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.minidigger.minecraftlauncer.renderer.SkinCanvas;
import me.minidigger.minecraftlauncer.renderer.animation.animations.RunningAnimation;
import me.minidigger.minecraftlauncher.api.ServerListEntry;
import me.minidigger.minecraftlauncher.launcher.LauncherMain;
import me.minidigger.minecraftlauncher.launcher.LauncherSettings;
import me.minidigger.minecraftlauncher.launcher.Status;
import me.minidigger.minecraftlauncher.launcher.tasks.AvatarLoaderTask;
import me.minidigger.minecraftlauncher.launcher.tasks.VersionCheckerTask;

public class MainScreenController extends AbstractGUIController {

    public static Stage applicationOptionStage;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private ImageView playerAvatarImage;
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
    @FXML
    private Pane formPane;

    @Override
    public void setStatus(Status finalizing) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setToolTips();
        setTextBoxMax();

        new AvatarLoaderTask((image) -> playerAvatarImage.setImage(image)).start();

        username.setText(LauncherSettings.playerUsername);

        version.getItems().addAll(API.getInstalledVersionsList());

        for (String ob : API.getInstalledVersionsList()) {
            if (ob.equals(LauncherSettings.playerVersion)) {
                version.setValue(LauncherSettings.playerVersion);
            }
        }

        playerAvatarImage.setOnMouseClicked(event -> {
            LauncherSettings.playerUsername = username.getText();
            new AvatarLoaderTask((image) -> playerAvatarImage.setImage(image)).start();
            formPane.getChildren().clear();
            try {
                SkinCanvas canvas = new SkinCanvas(LauncherSettings.playerUsername, 250, 200, true);
                canvas.getAnimationPlayer().addSkinAnimation(
//                new MagmaArmsAnimation(100, 500, 90, canvas));
//                new WavingArmsAnimation(100, 500, 90, canvas));
                        new RunningAnimation(100, 800, 30, canvas));
                formPane.getChildren().add(canvas);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void launchMineCraft(ActionEvent event) {
        if (username.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("alert.username_missing.title"));
            alert.setHeaderText(resourceBundle.getString("alert.username_missing.header"));
            alert.setContentText(resourceBundle.getString("alert.username_missing.content"));
            alert.initStyle(StageStyle.UTILITY);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/css/purple.css");

            alert.show();
            return;
        }

        LauncherSettings.playerUsername = username.getText();
        LauncherSettings.playerVersion = version.getValue();
        LauncherSettings.userSettingsSave();

        options.setDisable(true);
        launch.setDisable(true);
        version.setDisable(true);
        username.setDisable(true);
        password.setDisable(true);

        new AvatarLoaderTask((image) -> playerAvatarImage.setImage(image)).start();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            //add server
            List<String> ip = API.getServersIPList().stream().map(ServerListEntry::getIp).collect(Collectors.toList());
            if (ip.isEmpty() || !ip.contains(LauncherSettings.serverIP)) {
                API.addServerToServersDat(LauncherSettings.serverName, LauncherSettings.serverIP);
            }

            API.downloadProfile(username.getText());
            API.syncVersions();

            if (!LauncherSettings.fastStartUp) { //NOT faststartup
                API.downloadMinecraft(version.getValue(), false);
            }

            API.setMinMemory(Integer.parseInt(LauncherSettings.ramAllocationMin));
            API.setMemory(Integer.parseInt(LauncherSettings.ramAllocationMax));
            API.setHeight(Integer.parseInt(LauncherSettings.resolutionHeight));
            API.setWidth(Integer.parseInt(LauncherSettings.resolutionWidth));

            if (!LauncherSettings.javaPath.equals("")) {
                API.setJavaPath(LauncherSettings.javaPath);
            }
            if (!LauncherSettings.jvmArguments.equals("")) {
                API.setJVMArgument(LauncherSettings.jvmArguments);
            }
            if (!LauncherSettings.playerUsername.equals("")) {
                API.setVersionData(MessageFormat.format(resourceBundle.getString("versiondata.name"), LauncherSettings.playerUsername));
            } else {
                API.setVersionData(resourceBundle.getString("versiondata.default"));
            }

            boolean nettyPatch = LauncherSettings.bypassBlacklist;
            if (LauncherSettings.fastStartUp) {
                API.runMinecraft(username.getText(), version.getValue(), false, nettyPatch);
            } else {
                API.runMinecraft(username.getText(), version.getValue(), true, nettyPatch);
            }
        });
        executor.shutdown();
    }

    @FXML
    private void launchOptions(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Launcher_Options_GUI.fxml"));
            Parent optionsGUI = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.getIcons().add(new Image(LauncherMain.class.getResourceAsStream("/images/app_icon_1.png")));
            stage.setTitle(resourceBundle.getString("optionscreen.title"));
            stage.setMinWidth(400);
            stage.setMinHeight(500);
            stage.setMaxWidth(400);
            stage.setMaxHeight(500);
            stage.setResizable(false);

            Scene sceneOptions = new Scene(optionsGUI);
            stage.setScene(sceneOptions);
            LauncherSettings.setTheme(sceneOptions);
            applicationOptionStage = stage;

            sceneOptions.setOnMousePressed(event_ -> {
                xOffset = stage.getX() - event_.getScreenX();
                yOffset = stage.getY() - event_.getScreenY();
            });

            sceneOptions.setOnMouseDragged(event_ -> {
                stage.setX(event_.getScreenX() + xOffset);
                stage.setY(event_.getScreenY() + yOffset);
            });

            stage.setOnHiding(event_ -> {
                //if (LauncherSettings.refreshVersionList == true) { //Just refesh it anyway.
                version.getItems().removeAll(version.getItems());
                for (Object ob : API.getInstalledVersionsList()) {
                    version.getItems().addAll(ob.toString());
                }

                if (!LauncherSettings.playerVersion.equals("-1")) {
                    version.setValue(LauncherSettings.playerVersion);
                }
                //}
            });
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LauncherMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onGameStarted() {
        LauncherSettings.playerUsername = username.getText();
        LauncherSettings.playerVersion = version.getValue();
        LauncherSettings.userSettingsSave();

        if (!LauncherSettings.keepLauncherOpen) {
            Platform.runLater(() -> setStatusText(resourceBundle.getString("status.minecraft_started")));
            System.exit(0);
        } else {
            new VersionCheckerTask(this::setStatusText).start();
        }

        username.setDisable(false);
        password.setDisable(false);
        options.setDisable(false);
        launch.setDisable(false);
        version.setDisable(false);
    }

    @Override
    public void onGameCorrupted(int exitCode) {
        Platform.runLater(() -> {
            setStatusText(resourceBundle.getString("status.corrupted"));

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("alert.corruption.title"));
            alert.setHeaderText(MessageFormat.format(resourceBundle.getString("alert.corruption.header"), version.getValue()));
            alert.setContentText(MessageFormat.format(resourceBundle.getString("alert.corruption.content"), exitCode));
            alert.initStyle(StageStyle.UTILITY);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/css/purple.css");
            alert.showAndWait();

            username.setDisable(false);
            password.setDisable(false);
            options.setDisable(false);
            launch.setDisable(false);
            version.setDisable(false);
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
                if (username.getText().length() > 16) {
                    username.setText(username.getText().substring(0, 16));
                    //Toolkit.getDefaultToolkit().beep();
                }
            }
        });
    }

    @FXML
    private void kt_username(KeyEvent event) {
        if (!event.getCharacter().matches("[A-Za-z0-9\b_]")) {
            //Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
    }

    public void setStatusText(String text){
//        launcherStatus.setText(text);//TODO
    }
}
