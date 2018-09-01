package me.minidigger.minecraftlauncher.launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author ammar
 */
public class LauncherMain extends Application {
    private static Stage applicationMainStage;
    private double xOffset = 0;
    private double yOffset = 0;

    private void setApplicationMainStage(Stage stage) {
        LauncherMain.applicationMainStage = stage;
    }

    static public Stage getApplicationMainStage() {
        return LauncherMain.applicationMainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LauncherSettings.userSettingsLoad();

        //Launcher_Main_Background.setBackgroundImages();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Launcher_Main_GUI.fxml"));
        Scene scene = new Scene(root);
        initApplicationSettings(stage, scene);

        stage.setScene(scene);
        stage.show();
    }

    public void initApplicationSettings(Stage stage, Scene scene) {
        setApplicationMainStage(stage);

        stage.getIcons().add(new Image(LauncherMain.class.getResourceAsStream("/images/app_icon_1.png")));
        stage.setTitle("Minecraft Launcher");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMinWidth(450);
        stage.setMinHeight(450);
        stage.setMaxWidth(450);
        stage.setMaxHeight(450);
        stage.setResizable(false);
        LauncherSettings.setTheme(scene);


        scene.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }
}