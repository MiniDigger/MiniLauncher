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

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.minidigger.minecraftlauncer.renderer.SkinCanvas;
import me.minidigger.minecraftlauncer.renderer.SkinCanvasMouseHandler;
import me.minidigger.minecraftlauncer.renderer.model.SkinCube;

/**
 * @author ammar
 */
public class LauncherMain extends Application {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("minilauncher");

    private static Stage applicationMainStage;

    private double xOffset = 0;
    private double yOffset = 0;

    private boolean dragStartedOnSkin = false;

    static public Stage getApplicationMainStage() {
        return LauncherMain.applicationMainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LauncherSettings.userSettingsLoad();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Launcher_Main_GUI.fxml"));
        Scene scene = new Scene(root);
        initApplicationSettings(stage, scene);

        stage.setScene(scene);
        stage.show();
    }

    public void initApplicationSettings(Stage stage, Scene scene) {
        applicationMainStage = stage;

        stage.getIcons().add(new Image(LauncherMain.class.getResourceAsStream("/images/app_icon_1.png")));
        stage.setTitle(resourceBundle.getString("mainscreen.title"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMinWidth(450);
        stage.setMinHeight(450);
        stage.setMaxWidth(450);
        stage.setMaxHeight(450);
        stage.setResizable(false);
        LauncherSettings.setTheme(scene);

        SkinCanvasMouseHandler mouseHandler = new SkinCanvasMouseHandler(.5);

        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (isSkin(event.getPickResult().getIntersectedNode())) {
                dragStartedOnSkin = true;
                mouseHandler.press(event);
                return;
            }
            dragStartedOnSkin = false;
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (dragStartedOnSkin) {
                mouseHandler.drag(event);
                return;
            }
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            if (dragStartedOnSkin) {
                mouseHandler.scroll(event);
                return;
            }
        });
    }

    private boolean isSkin(Node node) {
        if (node == null) {
            return false;
        } else if (node instanceof SkinCube || node instanceof SkinCanvas) {
            return true;
        } else if (node instanceof Pane) {
            return false;//TODO check pane
        } else {
            System.out.println("check " + node.getClass().getName());
            return false;//TODO check pane
        }
    }
}