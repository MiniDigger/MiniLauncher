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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.minidigger.minecraftlauncher.launcher.LauncherMain;
import me.minidigger.minecraftlauncher.launcher.LauncherSettings;
import me.minidigger.minecraftlauncher.launcher.Status;
import me.minidigger.minecraftlauncher.launcher.tasks.MinecraftDownloaderTask;
import me.minidigger.minecraftlauncher.launcher.tasks.VersionListUpdaterTask;

/**
 * FXML Controller class
 *
 * @author Mathew
 */
public class OptionFragmentController extends FragmentController {

    private final static Logger logger = LoggerFactory.getLogger(OptionFragmentController.class);

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
    private ComboBox<String> optionsSelectVersion;
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
    @FXML
    private RadioButton optionsDebugMode;
    @FXML
    private ComboBox<String> themeType;
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

    private Hashtable<String, String> VersionHashTable = new Hashtable<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setToolTips();
        setTextBoxMax();
        themeType.getItems().addAll("Purple", "Gray", "Red", "Green", "Blue", "White");

        loadOptionsData();

        new VersionListUpdaterTask(minecraftDownloader, optionsSelectVersion, optionsSelectVersionInstall,
                this::setStatusText, VersionHashTable, this::onDownloadComplete).start();
    }

    @Override
    public void onClose() {
        saveOptionsData();
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
            alert.setTitle(resourceBundle.getString("alert.no_version.title"));
            alert.setHeaderText(resourceBundle.getString("alert.no_version.header"));
            alert.setContentText(resourceBundle.getString("alert.no_version.content"));
            alert.initStyle(StageStyle.UTILITY);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/css/purple.css");
            alert.show();
            return;
        }

        optionsSelectVersionInstall.setDisable(true);
        optionsSelectVersion.setDisable(true);

        new MinecraftDownloaderTask(optionsSelectVersionForce.isSelected(), optionsSelectVersion.getValue(),
                minecraftDownloader, minecraftDirectory, this::setStatusText, (version) -> {
            optionsSelectVersionInstall.setDisable(false);
            optionsSelectVersion.setDisable(false);
        }).start();
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
        launcherVersion.setText(MessageFormat.format(resourceBundle.getString("optionscreen.version"), LauncherSettings.launcherVersion));

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
        validate(event);
    }

    @FXML
    private void kt_optionsRamAllocationMin(KeyEvent event) {
        validate(event);
    }

    @FXML
    private void kt_optionsResolutionMax(KeyEvent event) {
        validate(event);
    }

    @FXML
    private void kt_optionsRamAllocationMax(KeyEvent event) {
        validate(event);
    }

    private void validate(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9\b]")) {
            Toolkit.getDefaultToolkit().beep();
            event.consume();
        }
    }

    @FXML
    private void _themeType(ActionEvent event) {
        LauncherSettings.selectedTheme = themeType.getValue().toLowerCase();
        LauncherSettings.userSettingsSave();

        Stage stage = LauncherMain.getApplicationMainStage();
        LauncherSettings.setTheme(stage.getScene());
    }

    @FXML
    private void _useThemeType(ActionEvent event) {
        if (useThemeType.isSelected()) {
            themeType.setDisable(false);
            if (themeType.getValue() != null) {
                LauncherSettings.selectedTheme = themeType.getValue().toLowerCase();
                LauncherSettings.userSettingsSave();
            }
        } else {
            themeType.setDisable(true);
            LauncherSettings.selectedTheme = "";
        }
        Stage gui_options = MainFragmentController.applicationOptionStage;
        Stage gui_main = LauncherMain.getApplicationMainStage();

        LauncherSettings.setTheme(gui_options.getScene());
        LauncherSettings.setTheme(gui_main.getScene());
    }

    private void setToolTips() {
        Image infoIMG = new Image(getClass().getResourceAsStream("/images/m_info.png"));

        tt_keepLauncherOpen.setText(resourceBundle.getString("optionscreen.tooltip.keep_launcher_open"));
        tt_keepLauncherOpen.setGraphic(new ImageView(infoIMG));

        tt_customTheme.setText(resourceBundle.getString("optionscreen.tooltip.custom_theme"));
        tt_customTheme.setGraphic(new ImageView(infoIMG));

        tt_resolution.setText(resourceBundle.getString("optionscreen.tooltip.resolution"));
        tt_resolution.setGraphic(new ImageView(infoIMG));

        tt_ramAllocation.setText(resourceBundle.getString("optionscreen.tooltip.ram"));
        tt_ramAllocation.setGraphic(new ImageView(infoIMG));

        tt_fastStartup.setText(resourceBundle.getString("optionscreen.tooltip.faststartup"));
        tt_fastStartup.setGraphic(new ImageView(infoIMG));

        tt_bypassBlacklist.setText(resourceBundle.getString("optionscreen.tooltip.bypass_blacklist"));
        tt_bypassBlacklist.setGraphic(new ImageView(infoIMG));

        tt_selectVersion.setText(resourceBundle.getString("optionscreen.tooltip.select_version"));
        tt_selectVersion.setGraphic(new ImageView(infoIMG));

        tt_selectVersionInstall.setText(resourceBundle.getString("optionscreen.tooltip.select_version_install"));
        tt_selectVersionInstall.setGraphic(new ImageView(infoIMG));

        tt_forceDownload.setText(resourceBundle.getString("optionscreen.tooltip.force_download"));
        tt_forceDownload.setGraphic(new ImageView(infoIMG));

        tt_javaVersion.setText(resourceBundle.getString("optionscreen.tooltip.java_location"));
        tt_javaVersion.setGraphic(new ImageView(infoIMG));

        tt_jvmArgs.setText(resourceBundle.getString("optionscreen.tooltip.jvm_args"));
        tt_jvmArgs.setGraphic(new ImageView(infoIMG));

        tt_launcherVersion.setText(resourceBundle.getString("optionscreen.tooltip.launcher_version"));
        tt_launcherVersion.setGraphic(new ImageView(infoIMG));

        tt_debugMode.setText(resourceBundle.getString("optionscreen.tooltip.debug_mode"));
        tt_debugMode.setGraphic(new ImageView(infoIMG));
    }

    @FXML
    private void mc_launcherVersion(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("alert.credits.title"));
        alert.setHeaderText(resourceBundle.getString("alert.credits.header"));
        alert.setContentText(resourceBundle.getString("alert.credits.content"));

        alert.initStyle(StageStyle.UTILITY);
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                .forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("/css/purple.css");
        alert.show();
    }

    private void setTextBoxMax() {
        optionsResolutionMin.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (optionsResolutionMin.getText().length() > 4) {
                    optionsResolutionMin.setText(optionsResolutionMin.getText().substring(0, 4));
                }
            }
        });

        optionsResolutionMax.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (optionsResolutionMax.getText().length() > 4) {
                    optionsResolutionMax.setText(optionsResolutionMax.getText().substring(0, 4));
                }
            }
        });

        optionsRamAllocationMin.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (optionsRamAllocationMin.getText().length() > 4) {
                    optionsRamAllocationMin.setText(optionsRamAllocationMin.getText().substring(0, 4));
                }
            }
        });

        optionsRamAllocationMax.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (optionsRamAllocationMax.getText().length() > 4) {
                    optionsRamAllocationMax.setText(optionsRamAllocationMax.getText().substring(0, 4));
                }
            }
        });
    }

    @FXML
    private void mp_optionsJavaVersionInput(MouseEvent event) {

        if (optionsJavaVersionInput.getText().equals("")) {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(resourceBundle.getString("filechooser.title"));
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

    public void onDownloadComplete() {
        Platform.runLater(() -> {
            setStatus(Status.DOWNLOAD_COMPLETE);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("alert.download_success.title"));
            alert.setHeaderText(resourceBundle.getString("alert.download_success.header"));
            alert.setContentText(MessageFormat.format(resourceBundle.getString("alert.download_success.content"), optionsSelectVersion.getValue()));
            alert.initStyle(StageStyle.UTILITY);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/css/purple.css");

            setStatus(Status.IDLE);
            optionsSelectVersionInstall.setDisable(false);
            optionsSelectVersion.setDisable(false);
            optionsSelectFastStart.setSelected(false);

            LauncherSettings.refreshVersionList = true;
            LauncherSettings.fastStartUp = false;

            alert.showAndWait();
        });
    }
}
