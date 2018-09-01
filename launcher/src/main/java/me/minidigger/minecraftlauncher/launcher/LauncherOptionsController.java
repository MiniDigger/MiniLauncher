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

package me.minidigger.minecraftlauncher.launcher;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.minidigger.minecraftlauncher.api.LauncherAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author Mathew
 */
public class LauncherOptionsController implements Initializable {
    private final static Logger logger = LoggerFactory.getLogger(LauncherOptionsController.class);

    @FXML
    private Button optionsExit;
    @FXML
    private Button optionsClose;
    @FXML
    private RadioButton optionsKeepLauncherOpen;
    @FXML
    private RadioButton optionDisableAutoUpdates;
    @FXML
    private RadioButton optionsResolution;
    @FXML
    private RadioButton optionsRamAllocation;
    @FXML
    private RadioButton optionsBypassBlacklist;
    @FXML
    private TextField optionsResolutionMin;
    @FXML
    private TextField optionsRamAllocationMin;
    @FXML
    private TextField optionsResolutionMax;
    @FXML
    private TextField optionsRamAllocationMax;
    @FXML
    private ComboBox optionsSelectVersion;
    @FXML
    private Button optionsSelectVersionInstall;
    @FXML
    private RadioButton optionsSelectVersionForce;
    @FXML
    private RadioButton optionsJavaVersion;
    @FXML
    private RadioButton optionsJVMArguments;
    @FXML
    private TextField optionsJavaVersionInput;
    @FXML
    private TextField optionsJVMArgumentsInput;

    Hashtable<String, String> VersionHashTable = new Hashtable<>();
    @FXML
    private Label optionStatus;
    @FXML
    private RadioButton optionsDebugMode;
    @FXML
    private ComboBox themeType;
    @FXML
    private RadioButton useThemeType;
    @FXML
    private Tooltip tt_keepLauncherOpen;
    @FXML
    private Tooltip tt_customTheme;
    @FXML
    private Tooltip tt_resolution;
    @FXML
    private Tooltip tt_ramAllocation;
    @FXML
    private Tooltip tt_bypassBlacklist;
    @FXML
    private Tooltip tt_selectVersion;
    @FXML
    private Tooltip tt_selectVersionInstall;
    @FXML
    private Tooltip tt_forceDownload;
    @FXML
    private Tooltip tt_javaVersion;
    @FXML
    private Tooltip tt_jvmArgs;
    @FXML
    private Label launcherVersion;
    @FXML
    private Tooltip tt_launcherVersion;
    @FXML
    private RadioButton optionsSelectFastStart;
    @FXML
    private Tooltip tt_fastStartup;
    @FXML
    private Tooltip tt_debugMode;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setToolTips();
        setTextBoxMax();
        themeType.getItems().addAll("Purple", "Gray", "Red", "Green", "Blue", "White");

        loadOptionsData();

        LauncherAPI API = new LauncherAPI();
        ExecutorService executor1 = Executors.newCachedThreadPool();
        executor1.submit(() -> {
            if (API.getUpdateStatus().equals("0")) {
                logger.info("You are running the latest API version");
            } else {
                logger.info("You are " + API.getUpdateStatus() + " versions behind");
            }
            return null;
        });
        executor1.shutdown();

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            optionStatus.setText("Status: Getting latest versions");
            optionsSelectVersion.setDisable(true);
            optionsSelectVersionInstall.setDisable(true);
            API.downloadVersionManifest();

            for (Object ob : API.getInstallableVersionsList()) {
                String[] prsntAry = ob.toString().split(" % ");
                optionsSelectVersion.getItems().addAll(prsntAry[0]);
                VersionHashTable.put(prsntAry[0], prsntAry[1]);

            }
            if (!API.getInstalledVersionsList().isEmpty()) {

                for (Object ob_ : API.getInstalledVersionsList()) {
                    if (!VersionHashTable.containsKey(ob_)) {
                        optionsSelectVersion.getItems().addAll(ob_);
                        VersionHashTable.put((String) ob_, "Unknown");
                    }
                }

            }
            optionsSelectVersion.setDisable(false);
            optionsSelectVersionInstall.setDisable(false);
            try {
                Platform.runLater(() -> optionStatus.setText("Status: Idle"));
            } catch (Exception e) {
            }
            return null;
        });
        executor.shutdown();
    }

    @FXML
    private void _optionsClose(ActionEvent event) {
        saveOptionsData();
        Stage stage = LauncherMainController.getApplicationOptionStage();
        stage.close();
    }

    @FXML
    private void _optionsExit(ActionEvent event) {
        saveOptionsData();
        Stage stage = LauncherMainController.getApplicationOptionStage();
        stage.close();
    }

    @FXML
    private void _optionsResolution(ActionEvent event) {
        if (optionsResolution.isSelected()) {
            optionsResolutionMin.setDisable(false);
            optionsResolutionMax.setDisable(false);

        } else {
            optionsResolutionMin.setDisable(true);
            optionsResolutionMax.setDisable(true);
            optionsResolutionMin.setText("854");
            optionsResolutionMax.setText("480");
        }
    }

    @FXML
    private void _optionsRamAllocation(ActionEvent event) {
        if (optionsRamAllocation.isSelected()) {
            optionsRamAllocationMin.setDisable(false);
            optionsRamAllocationMax.setDisable(false);
        } else {
            optionsRamAllocationMin.setDisable(true);
            optionsRamAllocationMax.setDisable(true);
            optionsRamAllocationMin.setText("1024");
            optionsRamAllocationMax.setText("1024");
        }
    }

    @FXML
    private void _optionsJavaVersion(ActionEvent event) {
        if (optionsJavaVersion.isSelected()) {
            optionsJavaVersionInput.setDisable(false);
        } else {
            optionsJavaVersionInput.setDisable(true);
            optionsJavaVersionInput.setText("");
        }
    }

    @FXML
    private void _optionsJVMArguments(ActionEvent event) {
        if (optionsJVMArguments.isSelected()) {
            optionsJVMArgumentsInput.setDisable(false);
        } else {
            optionsJVMArgumentsInput.setDisable(true);
            optionsJVMArgumentsInput.setText("");
        }
    }

    @FXML
    private void _optionsKeepLauncherOpen(ActionEvent event) {
        LauncherSettings.keepLauncherOpen = optionsKeepLauncherOpen.isSelected();

    }

    @FXML
    private void _optionDisableAutoUpdates(ActionEvent event) {
    }

    @FXML
    private void _optionsBypassBlacklist(ActionEvent event) {
    }

    @FXML
    private void _optionsSelectVersion(ActionEvent event) {
    }

    @FXML
    private void _optionsSelectVersionInstall(ActionEvent event) {
        if (optionsSelectVersion.getValue() == null || optionsSelectVersion.getValue().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Minecraft Launcher - Error");
            alert.setHeaderText("Invaild selection.");
            alert.setContentText("Please select a version to install prior to installing.");
            alert.initStyle(StageStyle.UTILITY);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/css/purple.css");
            alert.show();
            return;
        }

        optionsSelectVersionInstall.setDisable(true);
        optionsExit.setDisable(true);
        optionsClose.setDisable(true);
        optionsSelectVersion.setDisable(true);

        if (optionsSelectVersionForce.isSelected()) {
            logger.info("Selected!");
        } else {
            logger.info("NOT Selected!");

        }
        LauncherAPI API = new LauncherAPI();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            API.downloadMinecraft((String) optionsSelectVersion.getValue(), optionsSelectVersionForce.isSelected());
            return null;
        });
        executor.shutdown();

        // TODO: this is shit, really shit.
        Thread t = new Thread(() -> {
            while (true) {
                try {

                    Platform.runLater(() -> {

                        if (LauncherSettings.showDebugStatus) {
                            optionStatus.setText(API.getLog());
                        } else {
                            if (API.getLog().startsWith("[dl] URL: https://launchermeta")) {
                                optionStatus.setText("Status: " + LauncherSettings.Status.DOWNLOADING_LM);
                            }
                            if (API.getLog().startsWith("[dl] DOWNLOADING...HASH:")) {
                                optionStatus.setText("Status: " + LauncherSettings.Status.DOWNLOADING);
                            }
                            if (API.getLog().startsWith("[dl] DOWNLOADING MINECRAFT JAR")) {
                                optionStatus.setText("Status: " + LauncherSettings.Status.DOWNLOADING_M);
                            }
                            if (API.getLog().startsWith("[dl] Downloading: https://libraries")) {
                                optionStatus.setText("Status: " + LauncherSettings.Status.DOWNLOADING_L);
                            }
                            if (API.getLog().startsWith("[dl] Getting NATIVES URL")) {
                                optionStatus.setText("Status: " + LauncherSettings.Status.FINALIZING);
                            }
                        }

                    });

                    Thread.sleep(10);

                    if (API.getLog().equals("[dl] Download Complete!")) {
                        Platform.runLater(() -> {
                            optionStatus.setText("Status: " + LauncherSettings.Status.DOWNLOAD_COMPLETE);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Minecraft Launcher - Success");
                            alert.setHeaderText("Download Complete.");
                            alert.initStyle(StageStyle.UTILITY);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add("/css/purple.css");
                            alert.setContentText("Version: " + optionsSelectVersion.getValue() + " has been downloaded & installed!");
                            ;
                            optionStatus.setText("Status: " + LauncherSettings.Status.IDLE);
                            optionsSelectVersionInstall.setDisable(false);
                            optionsExit.setDisable(false);
                            optionsClose.setDisable(false);
                            optionsSelectVersion.setDisable(false);
                            optionsSelectFastStart.setSelected(false);
                            LauncherSettings.refreshVersionList = true;
                            LauncherSettings.fastStartUp = false;
                            alert.showAndWait();

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
    private void _optionsSelectVersionForce(ActionEvent event) {
    }

    @FXML
    private void _optionsDebugMode(ActionEvent event) {
        LauncherSettings.showDebugStatus = !LauncherSettings.showDebugStatus;

    }

    private void saveOptionsData() {
        LauncherSettings.bypassBlacklist = optionsBypassBlacklist.isSelected();
        LauncherSettings.keepLauncherOpen = optionsKeepLauncherOpen.isSelected();
        LauncherSettings.fastStartUp = optionsSelectFastStart.isSelected();
        LauncherSettings.resolutionWidth = optionsResolutionMin.getText();
        LauncherSettings.resolutionHeight = optionsResolutionMax.getText();
        LauncherSettings.ramAllocationMin = optionsRamAllocationMin.getText();
        LauncherSettings.ramAllocationMax = optionsRamAllocationMax.getText();
        LauncherSettings.javaPath = optionsJavaVersionInput.getText();
        LauncherSettings.jvmArguments = optionsJVMArgumentsInput.getText();
        LauncherSettings.showDebugStatus = optionsDebugMode.isSelected();
        LauncherSettings.userSettingsSave();
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private void loadOptionsData() {

        launcherVersion.setText("Version: " + LauncherSettings.launcherVersion);

        optionsResolutionMin.setText(LauncherSettings.resolutionWidth);
        optionsResolutionMax.setText(LauncherSettings.resolutionHeight);
        if (!LauncherSettings.resolutionWidth.equals("854") || !LauncherSettings.resolutionHeight.equals("480")) {
            optionsResolution.setSelected(true);
            optionsResolutionMin.setDisable(false);
            optionsResolutionMax.setDisable(false);
        }

        optionsRamAllocationMin.setText(LauncherSettings.ramAllocationMin);
        optionsRamAllocationMax.setText(LauncherSettings.ramAllocationMax);
        if (!LauncherSettings.ramAllocationMin.equals("1024") || !LauncherSettings.ramAllocationMax.equals("1024")) {
            optionsRamAllocation.setSelected(true);
            optionsRamAllocationMin.setDisable(false);
            optionsRamAllocationMax.setDisable(false);
        }

        if (LauncherSettings.fastStartUp) {
            optionsSelectFastStart.setSelected(true);
        }

        if (LauncherSettings.bypassBlacklist) {
            optionsBypassBlacklist.setSelected(true);
        }

        if (LauncherSettings.showDebugStatus) {
            optionsDebugMode.setSelected(true);
        }

        optionsJavaVersionInput.setText(LauncherSettings.javaPath);
        if (!LauncherSettings.javaPath.equals("")) {
            optionsJavaVersion.setSelected(true);
            optionsJavaVersionInput.setDisable(false);
        }

        optionsJVMArgumentsInput.setText(LauncherSettings.jvmArguments);
        if (!LauncherSettings.jvmArguments.equals("")) {
            optionsJVMArguments.setSelected(true);
            optionsJVMArgumentsInput.setDisable(false);
        }

        if (!LauncherSettings.selectedTheme.equals("")) {
            useThemeType.setSelected(true);
            themeType.setDisable(false);
            themeType.setValue(capitalize(LauncherSettings.selectedTheme));
        }

        if (LauncherSettings.keepLauncherOpen) {
            optionsKeepLauncherOpen.setSelected(true);
        }

    }

    @FXML
    private void kt_optionsResolutionMin(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9\b]")) {
            Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
    }

    @FXML
    private void kt_optionsRamAllocationMin(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9\b]")) {
            Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
    }

    @FXML
    private void kt_optionsResolutionMax(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9\b]")) {
            Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
    }

    @FXML
    private void kt_optionsRamAllocationMax(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9\b]")) {
            Toolkit.getDefaultToolkit().beep();
            event.consume();

        }
    }

    @FXML
    private void _themeType(ActionEvent event) {
        LauncherSettings.selectedTheme = themeType.getValue().toString().toLowerCase();
        LauncherSettings.userSettingsSave();

        Stage gui_options = LauncherMainController.getApplicationOptionStage();
        Stage gui_main = LauncherMain.getApplicationMainStage();

        LauncherSettings.setTheme(gui_options.getScene());
        LauncherSettings.setTheme(gui_main.getScene());
    }

    @FXML
    private void _useThemeType(ActionEvent event) {
        if (useThemeType.isSelected()) {
            themeType.setDisable(false);
            if (themeType.getValue() != null) {
                LauncherSettings.selectedTheme = themeType.getValue().toString().toLowerCase();
                LauncherSettings.userSettingsSave();
            }
        } else {
            themeType.setDisable(true);
            LauncherSettings.selectedTheme = "";
        }
        Stage gui_options = LauncherMainController.getApplicationOptionStage();
        Stage gui_main = LauncherMain.getApplicationMainStage();

        LauncherSettings.setTheme(gui_options.getScene());
        LauncherSettings.setTheme(gui_main.getScene());
    }

    private void setToolTips() {
        Image infoIMG = new Image(getClass().getResourceAsStream("/images/m_info.png"));

        tt_keepLauncherOpen.setText(
                ""
                        + "Keep The Launcher Open\n"
                        + "Keeps the launcher open after Minecraft has started.\n"
        );
        tt_keepLauncherOpen.setGraphic(new ImageView(infoIMG));

        tt_customTheme.setText(
                ""
                        + "Choose A Custom Theme\n"
                        + "Change the launchers theme to a diffrent color.\n"
        );
        tt_customTheme.setGraphic(new ImageView(infoIMG));

        tt_resolution.setText(
                ""
                        + "Set Minecraft's Resolution\n"
                        + "Set the height and width of the Minecraft client.\n"
        );
        tt_resolution.setGraphic(new ImageView(infoIMG));

        tt_ramAllocation.setText(
                ""
                        + "Set Minecraft's Ram Usage\n"
                        + "Set the minimum and maximum ram that the Minecraft client will use.\n"
                        + "WARNING: It's best to leave this option alone unless you know what you're doing. Altering this beyond what your computer can handle will cause Minecraft to not open.\n"
        );
        tt_ramAllocation.setGraphic(new ImageView(infoIMG));

        tt_fastStartup.setText(
                ""
                        + "Fast Startup\n"
                        + "Lets you bypass the integrity check when launching Minecraft.\n"
                        + "Should only be used if you're using vanilla minecraft or the mod doesn't need to download third party API/Files.\n"
        );
        tt_fastStartup.setGraphic(new ImageView(infoIMG));

        tt_bypassBlacklist.setText(
                ""
                        + "Bypass The Blacklist\n"
                        + "Let you bypass the EULA blacklist when enabled.\n"
        );
        tt_bypassBlacklist.setGraphic(new ImageView(infoIMG));

        tt_selectVersion.setText(
                ""
                        + "Select A Version\n"
                        + "Choose a version of Minecraft to download and install.\n"
        );
        tt_selectVersion.setGraphic(new ImageView(infoIMG));

        tt_selectVersionInstall.setText(
                ""
                        + "Install and Download\n"
                        + "Install and download the version of Minecraft selected.\n"
        );
        tt_selectVersionInstall.setGraphic(new ImageView(infoIMG));

        tt_forceDownload.setText(
                ""
                        + "Force Download\n"
                        + "When enabled, this tells the launcher to re-download all files for the selected version.\n"
                        + "This can fix issues with Minecraft not starting due to corruption or missing files.\n"
        );
        tt_forceDownload.setGraphic(new ImageView(infoIMG));

        tt_javaVersion.setText(
                ""
                        + "Java Location\n"
                        + "When enabled, this changes the location of where the launcher gets Java from.\n"
                        + "WARNING: It's best to leave this option alone unless you know what you're doing. Altering this may cause Minecraft to not open.\n"
        );
        tt_javaVersion.setGraphic(new ImageView(infoIMG));

        tt_jvmArgs.setText(
                ""
                        + "Extra JVM Arguments\n"
                        + "When enabled, this allows you to insert extra JVM arguments when Minecraft launches.\n"
                        + "WARNING: It's best to leave this option alone unless you know what you're doing. Altering this may cause Minecraft to not open.\n"
        );
        tt_jvmArgs.setGraphic(new ImageView(infoIMG));

        tt_launcherVersion.setText(
                ""
                        + "Current Launcher Version\n"
                        + "This is the current launcher version.\n"
                        + "Click to view the launcher credits.\n"
        );
        tt_launcherVersion.setGraphic(new ImageView(infoIMG));

        tt_debugMode.setText(
                ""
                        + "Debug Mode\n"
                        + "When enabled, this tells the launcher to output raw logs in the logger.\n"
        );
        tt_debugMode.setGraphic(new ImageView(infoIMG));
    }

    @FXML
    private void mc_launcherVersion(MouseEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
        alert.setTitle("Minecraft Launcher - Credits");
        alert.setHeaderText("So long, and thanks for all the fish.");
        alert.initStyle(StageStyle.UTILITY);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("/css/purple.css");
        alert.setContentText(""
                + "Minotar.net: API used for the Avatars.\n"
                + "Mojang: A little thing called Minecraft.\n"
                + "maxd @ Github: Great dark Modena theme.\n"
                + "Ammar_Ahmad: The brains behind the code.\n"
                + "Chalkie: Pressed 'compile' that one time.\n\n"
                + "© TagCraftMC.com & TerraPrimal.com\n");
        alert.show();
    }

    private void setTextBoxMax() {
        optionsResolutionMin.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (optionsResolutionMin.getText().length() > 4) {
                    optionsResolutionMin.setText(optionsResolutionMin.getText().substring(0, 4));
                    //Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        optionsResolutionMax.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (optionsResolutionMax.getText().length() > 4) {
                    optionsResolutionMax.setText(optionsResolutionMax.getText().substring(0, 4));
                    //Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        optionsRamAllocationMin.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (optionsRamAllocationMin.getText().length() > 4) {
                    optionsRamAllocationMin.setText(optionsRamAllocationMin.getText().substring(0, 4));
                    //Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        optionsRamAllocationMax.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (optionsRamAllocationMax.getText().length() > 4) {
                    optionsRamAllocationMax.setText(optionsRamAllocationMax.getText().substring(0, 4));
                    //Toolkit.getDefaultToolkit().beep();
                }
            }
        });
    }

    @FXML
    private void mp_optionsJavaVersionInput(MouseEvent event) {

        if (optionsJavaVersionInput.getText().equals("")) {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Your Java Location");
            //removed! because this will not work if you are on MAC or Linux.
            //fileChooser.getExtensionFilters().add(new ExtensionFilter("Java Version", "*.exe"));

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                optionsJavaVersionInput.clear();
                optionsJavaVersionInput.setText(selectedFile.getAbsolutePath());
            }
        }

    }

    @FXML
    private void _optionsSelectFastStart(ActionEvent event) {
        LauncherSettings.fastStartUp = !LauncherSettings.fastStartUp;
    }
}
