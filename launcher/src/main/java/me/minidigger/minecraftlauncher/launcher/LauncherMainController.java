/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.minidigger.minecraftlauncher.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import me.minidigger.minecraftlauncher.api.LauncherAPI;

/**
 * @author ammar
 */
public class LauncherMainController implements Initializable {

    private static Stage applicationOptionStage;
    ArrayList<String> backgroundList = new ArrayList<>();

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private ImageView playerAvatarImage;
    @FXML
    private Tooltip tt_username;
    @FXML
    private Label launcherStatus;
    @FXML
    private AnchorPane mainBackground;
    @FXML
    private Tooltip tt_version;
    @FXML
    private Tooltip tt_play;
    @FXML
    private Tooltip tt_options;

    private void setApplicationOptionStage(Stage stage) {
        LauncherMainController.applicationOptionStage = stage;
    }

    static public Stage getApplicationOptionStage() {
        return LauncherMainController.applicationOptionStage;
    }

    private Label label;
    @FXML
    private TextField username;
    @FXML
    private Button launch;
    @FXML
    private ComboBox version;
    @FXML
    private Button minimize;
    @FXML
    private Button exit;
    @FXML
    private Button options;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setToolTips();
        setTextBoxMax();
        runBackground();

        new Thread(this::loadPlayerAvatar).start();
        new Thread(this::checkLatestVersion).start();
        LauncherAPI API = new LauncherAPI();

        username.setText(LauncherSettings.playerUsername);

        for (Object ob : API.getInstalledVersionsList()) {
            version.getItems().addAll(ob.toString());

        }
        for (Object ob : API.getInstalledVersionsList()) {
            if (ob.equals(LauncherSettings.playerVersion)) {
                version.setValue(LauncherSettings.playerVersion);
            }
        }

        if (LauncherSettings.firstStart) {
            showFirstTimeMessage();
            LauncherSettings.firstStart = false;
            LauncherSettings.userSettingsSave();
        }
    }

    public void showFirstTimeMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Minecraft Launcher - Welcome");
        alert.setHeaderText("Greetings Player...");
        alert.setContentText(""
                + "It looks like this is your first time using our launcher and we wanted to provide you with some help.\n\n"
                + "By default, the launcher doesn't come with any versions of Minecraft installed so you will need to download the version you wish to play via the options menu.\n\n"
                + "Once in the options menu, click the dropdown menu under the Version Settings title and choose the version you want. When you have chosen, click the download button and wait for the launcher to download all the necessary files.");
        alert.initStyle(StageStyle.UTILITY);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("/css/purple.css");
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        alert.show();
    }

    @FXML
    private void launchMineCraft(ActionEvent event) {
        if (username.getText().equals("")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Minecraft Launcher - Error");
            alert.setHeaderText("A username is required.");
            alert.setContentText("Please create a username prior to starting Minecraft.");
            alert.initStyle(StageStyle.UTILITY);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/css/purple.css");
            alert.show();
            return;
        }

        LauncherSettings.playerUsername = username.getText();
        LauncherSettings.playerVersion = version.getValue().toString();
        LauncherSettings.userSettingsSave();

        options.setDisable(true);
        launch.setDisable(true);
        version.setDisable(true);
        username.setDisable(true);
        new Thread(this::loadPlayerAvatar).start();

        LauncherAPI API = new LauncherAPI();

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            //add server
            List ip = new ArrayList(API.getServersIPList());
            if (ip.isEmpty() || !ip.contains(LauncherSettings.serverIP)) {
                API.addServerToServersDat(LauncherSettings.serverName, LauncherSettings.serverIP);
            }

            API.downloadProfile(username.getText());
            API.syncVersions();

            if (!LauncherSettings.fastStartUp) { //NOT faststartup
                API.downloadMinecraft((String) version.getValue(), false);
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
                API.setVersionData("Hi " + LauncherSettings.playerUsername + "!");
            } else {
                API.setVersionData("#MiniDigger is awesome");
            }

            Boolean nettyPatch = LauncherSettings.bypassBlacklist;
            if (LauncherSettings.fastStartUp) {
                API.runMinecraft(username.getText(), (String) version.getValue(), false, nettyPatch);
            } else {
                API.runMinecraft(username.getText(), (String) version.getValue(), true, nettyPatch);
            }

            return null;
        });
        executor.shutdown();

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    if (LauncherSettings.showDebugStatus) {
                        Platform.runLater(() -> launcherStatus.setText(API.getLog()));
                    } else {
                        if (API.getLog().startsWith("[dl] DOWNLOADING...")) {
                            Platform.runLater(() -> launcherStatus.setText("Status: Checking installed " + LauncherSettings.playerVersion + " files."));
                        }
                        if (API.getLog().startsWith("[rl] KEY:")) {
                            Platform.runLater(() -> launcherStatus.setText("Status: Preparing to start Minecraft."));
                        }
                        if (API.getLog().startsWith("[rl] Starting")) {
                            Platform.runLater(() -> launcherStatus.setText("Status: Starting Minecraft " + LauncherSettings.playerVersion + "."));
                        }
                    }

                    Thread.sleep(10);

                    if (API.getLog().equals("[rl] Minecraft Initialized!")) {
                        LauncherSettings.playerUsername = username.getText();
                        LauncherSettings.playerVersion = version.getValue().toString();
                        LauncherSettings.userSettingsSave();

                        if (!LauncherSettings.keepLauncherOpen) {
                            Platform.runLater(() -> launcherStatus.setText("Status: Minecraft started, now closing launcher. Have fun!"));
                            API.dumpLogs();
                            System.exit(0);
                        } else {
                            new Thread(this::checkLatestVersion).start();
                        }
                        return;

                    } else if (API.getLog().equals("[el] Minecraft Corruption found!")) {
                        Platform.runLater(() -> {
                            //STATUS status.setText(API.getLog());
                            launcherStatus.setText("Status: Error. Minecraft file corruption detected!");
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Minecraft Launcher - Error");
                            alert.setHeaderText("Version: " + version.getValue() + " failed to initialize!");
                            alert.setContentText("The game failed to initialize as data corruption \nwas found! Press re-Download game with \n*Force Download* checked in the options menu.");
                            alert.initStyle(StageStyle.UTILITY);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add("/css/purple.css");
                            alert.showAndWait();
                            API.dumpLogs();
                            username.setDisable(false);
                            options.setDisable(false);
                            launch.setDisable(false);
                            version.setDisable(false);
                        });
                        return;
                    }
                } catch (Exception e) {
                }
            }
        });
        t.start();

    }

    @FXML
    private void launchMinimize(ActionEvent event) {
        Stage stage = LauncherMain.getApplicationMainStage();
        stage.setIconified(true);
    }

    @FXML
    private void launchExit(ActionEvent event) {
        LauncherSettings.playerUsername = username.getText();
        if (version.getValue() != null) {
            LauncherSettings.playerVersion = version.getValue().toString();
        }
        LauncherSettings.userSettingsSave();
        Stage stage = LauncherMain.getApplicationMainStage();
        stage.close();
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
            stage.setTitle("Minecraft Launcher - Options");
            Scene sceneOptions = new Scene(optionsGUI);
            stage.setMinWidth(400);
            stage.setMinHeight(500);
            stage.setMaxWidth(400);
            stage.setMaxHeight(500);
            stage.setResizable(false);

            stage.setScene(sceneOptions);
            LauncherSettings.setTheme(sceneOptions);
            setApplicationOptionStage(stage);

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
                LauncherAPI API = new LauncherAPI();

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

    private void loadPlayerAvatar() {
        Image image = new Image("http://minotar.net/avatar/" + LauncherSettings.playerUsername + "/100");
        if (!image.isError()) //incase someone adds random shit or changes URL. Will show steve not a blank area.
        {
            playerAvatarImage.setImage(image);
        }
    }

    private void setToolTips() {
        Image infoIMG = new Image(getClass().getResourceAsStream("/images/m_info.png"));

        tt_username.setText(
                ""
                        + "Your Username\n"
                        + "The name must be between 4 and 16 characters long and can only contain letters, numbers and underscores.\n"
        );
        tt_username.setGraphic(new ImageView(infoIMG));

        tt_version.setText(
                ""
                        + "Minecraft Version\n"
                        + "Select what version of minecraft you wish to play.\n"
                        + "You can download new versions via the options menu.\n"
        );
        tt_version.setGraphic(new ImageView(infoIMG));

        tt_options.setText(
                ""
                        + "The Options Menu\n"
                        + "A menu that allows you to tweak various settings for the launcher and Minecraft.\n"
        );
        tt_options.setGraphic(new ImageView(infoIMG));
        tt_play.setText(
                ""
                        + "Play Minecraft\n"
                        + "Launches Minecraft with the version and settings you have chosen.\n"
        );
        tt_play.setGraphic(new ImageView(infoIMG));
    }

    @FXML
    private void kt_username(KeyEvent event) {
        if (!event.getCharacter().matches("[A-Za-z0-9\b_]")) {
            //Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
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

    private void checkLatestVersion() {
        try {
            URL versionLastesturl = new URL(LauncherSettings.updateURL);
            URLConnection con = versionLastesturl.openConnection();
            con.setUseCaches(false); //had to as it was caching it.

            BufferedReader in = new BufferedReader(new InputStreamReader(versionLastesturl.openStream()));
            String line;

            while ((line = in.readLine()) != null) {
                if (LauncherSettings.launcherVersion.equals(line)) {
                    //TODO better update check, check semver
                    Platform.runLater(() -> launcherStatus.setText("Status: Your launcher is up to date!"));
                } else {
                    Platform.runLater(() -> launcherStatus.setText("Status: Your launcher is outdated! Please update it."));
                }
            }
            in.close();

        } catch (IOException e) {
            Platform.runLater(() -> launcherStatus.setText("Status: Unable to check for latest version!"));

        }
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
}
