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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javafx.scene.Scene;

/**
 * @author Mathew
 */
public class LauncherSettings {
    private final static Logger logger = LoggerFactory.getLogger(LauncherSettings.class);

    public static final String updateURL = "https://raw.githubusercontent.com/MiniDigger/MinecraftLauncher/master/_html_/latestVersion";
    public static final String serverIP = "localhost";
    public static final String serverName = "Localhost";
    public static String launcherVersion = "2.0";
    public static String playerUsername = "";
    public static String playerVersion = "-1";
    public static boolean firstStart = true;
    public static boolean refreshVersionList = false;
    public static boolean bypassBlacklist = true;
    public static boolean keepLauncherOpen = false;
    public static boolean showDebugStatus = false;
    public static boolean fastStartUp = false;
    public static String selectedTheme = "";

    public static String resolutionWidth = "854";
    public static String resolutionHeight = "480";
    public static String ramAllocationMin = "1024";
    public static String ramAllocationMax = "1024";
    public static String javaPath = "";
    public static String jvmArguments = "";

    public static void userSettingsSave() {
        if (LauncherSettings.resolutionWidth.equals("") || LauncherSettings.resolutionWidth.equals("0")) {
            LauncherSettings.resolutionWidth = "854";
        }
        if (LauncherSettings.resolutionHeight.equals("") || LauncherSettings.resolutionHeight.equals("0")) {
            LauncherSettings.resolutionHeight = "480";
        }
        if (LauncherSettings.ramAllocationMin.equals("") || LauncherSettings.ramAllocationMin.equals("0")) {
            LauncherSettings.ramAllocationMin = "1024";
        }
        if (LauncherSettings.ramAllocationMax.equals("") || LauncherSettings.ramAllocationMax.equals("0")) {
            LauncherSettings.ramAllocationMax = "1024";
        }
        if (LauncherSettings.playerUsername.equals("")) {
            LauncherSettings.playerUsername = "Username";
        }

        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");
            prop.setProperty("username", LauncherSettings.playerUsername);
            prop.setProperty("version", LauncherSettings.playerVersion);
            prop.setProperty("theme", LauncherSettings.selectedTheme);
            prop.setProperty("firststart", String.valueOf(LauncherSettings.firstStart));
            prop.setProperty("faststartup", String.valueOf(LauncherSettings.fastStartUp));
            prop.setProperty("bypassblacklist", String.valueOf(LauncherSettings.bypassBlacklist));
            prop.setProperty("keeplauncheropen", String.valueOf(LauncherSettings.keepLauncherOpen));
            prop.setProperty("resolutionwidth", LauncherSettings.resolutionWidth);
            prop.setProperty("resolutionheight", LauncherSettings.resolutionHeight);
            prop.setProperty("ramAllocationmin", LauncherSettings.ramAllocationMin);
            prop.setProperty("ramAllocationmax", LauncherSettings.ramAllocationMax);
            prop.setProperty("javapath", LauncherSettings.javaPath);
            prop.setProperty("jvmarguments", LauncherSettings.jvmArguments);
            prop.setProperty("debugMode", String.valueOf(LauncherSettings.showDebugStatus));
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void userSettingsLoad() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);

            if (prop.getProperty("username") != null) {
                LauncherSettings.playerUsername = prop.getProperty("username");
            }

            if (prop.getProperty("version") != null) {
                LauncherSettings.playerVersion = prop.getProperty("version");
            }

            if (prop.getProperty("firststart") != null) {
                LauncherSettings.firstStart = Boolean.parseBoolean(prop.getProperty("firststart"));
            }

            if (prop.getProperty("faststartup") != null) {
                LauncherSettings.fastStartUp = Boolean.parseBoolean(prop.getProperty("faststartup"));
            }

            if (prop.getProperty("bypassblacklist") != null) {
                LauncherSettings.bypassBlacklist = Boolean.parseBoolean(prop.getProperty("bypassblacklist"));
            }

            if (prop.getProperty("keeplauncheropen") != null) {
                LauncherSettings.keepLauncherOpen = Boolean.parseBoolean(prop.getProperty("keeplauncheropen"));
            }

            if (prop.getProperty("resolutionwidth") != null) {
                LauncherSettings.resolutionWidth = prop.getProperty("resolutionwidth");
            }

            if (prop.getProperty("resolutionheight") != null) {
                LauncherSettings.resolutionHeight = prop.getProperty("resolutionheight");
            }

            if (prop.getProperty("ramAllocationmin") != null) {
                LauncherSettings.ramAllocationMin = prop.getProperty("ramAllocationmin");
            }

            if (prop.getProperty("ramAllocationmax") != null) {
                LauncherSettings.ramAllocationMax = prop.getProperty("ramAllocationmax");
            }

            if (prop.getProperty("javapath") != null) {
                LauncherSettings.javaPath = prop.getProperty("javapath");
            }

            if (prop.getProperty("jvmarguments") != null) {
                LauncherSettings.jvmArguments = prop.getProperty("jvmarguments");
            }

            if (prop.getProperty("debugMode") != null) {
                LauncherSettings.showDebugStatus = Boolean.parseBoolean(prop.getProperty("debugMode"));
            }

            if (prop.getProperty("theme") != null) {
                LauncherSettings.selectedTheme = prop.getProperty("theme");
            }

        } catch (IOException ex) {
            logger.warn("File not found", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.warn("File not found", e);
                }
            }
        }
    }

    public static void setTheme(Scene sceneOptions) {
        sceneOptions.getStylesheets().clear();
        switch(LauncherSettings.selectedTheme) {
            case "red":
                sceneOptions.getStylesheets().add("/css/red.css");
                break;
            case "green":
                sceneOptions.getStylesheets().add("/css/green.css");
                break;
            case "blue":
                sceneOptions.getStylesheets().add("/css/blue.css");
                break;
            case "gray":
                sceneOptions.getStylesheets().add("/css/gray.css");
                break;
            case "white":
                sceneOptions.getStylesheets().add("/css/white.css");
                break;
            case "purple":
            default:
                sceneOptions.getStylesheets().add("/css/purple.css");
                break;
        }
    }
}
