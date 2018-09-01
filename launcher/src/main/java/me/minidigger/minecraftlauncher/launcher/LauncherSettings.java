package me.minidigger.minecraftlauncher.launcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javafx.scene.Scene;
import me.minidigger.minecraftlauncher.api.LauncherAPI;

/**
 * @author Mathew
 */
public class LauncherSettings {

    static public final String updateURL = "https://raw.githubusercontent.com/MiniDigger/MinecraftLauncher/master/_html_/latestVersion";
    static public final String serverIP = "localhost";
    static public final String serverName = "Localhost";
    static public String launcherVersion = "2.0";
    static public String playerUsername = "";
    static public String playerVersion = "-1";
    static public boolean firstStart = true;
    static public boolean refreshVersionList = false;
    static public boolean bypassBlacklist = true;
    static public boolean keepLauncherOpen = false;
    static public boolean showDebugStatus = false;
    static public boolean fastStartUp = false;
    static public String selectedTheme = "";

    static public String resolutionWidth = "854";
    static public String resolutionHeight = "480";
    static public String ramAllocationMin = "1024";
    static public String ramAllocationMax = "1024";
    static public String javaPath = "";
    static public String jvmArguments = "";

    public enum Status {
        IDLE {
            @Override
            public String toString() {
                return "Idle";
            }
        },
        DOWNLOADING {
            @Override
            public String toString() {
                return "Checking & Downloading Files";
            }
        },
        DOWNLOADING_N {
            @Override
            public String toString() {
                return "Downloading File #";
            }
        },
        DOWNLOADING_M {
            @Override
            public String toString() {
                return "Downloading The Minecraft Client";
            }
        },
        DOWNLOADING_LM {
            @Override
            public String toString() {
                return "Downloading Version Data From Mojang";
            }
        },
        DOWNLOADING_L {
            @Override
            public String toString() {
                return "Downloading Required Libraries";
            }
        },
        DOWNLOAD_COMPLETE {
            @Override
            public String toString() {
                return "Downloading Completed";
            }
        },
        FINALIZING {
            @Override
            public String toString() {
                return "Finalizing The Installation";
            }
        },
        ERROR {
            @Override
            public String toString() {
                return "Error";
            }
        },
        CHECKING {
            @Override
            public String toString() {
                return "Checking Latest Versions Available";
            }
        },
        EXTRACTING {
            @Override
            public String toString() {
                return "Extracting & Installing Files";
            }
        },
        APILOG {
            @Override
            public String toString() {
                LauncherAPI API = new LauncherAPI();
                return API.getLog();
            }
        },
    }

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
            System.out.println("File not found" + ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("File not found" + e);
                }
            }
        }
    }

    public static void setTheme(Scene sceneOptions) {
        sceneOptions.getStylesheets().clear();
        if (LauncherSettings.selectedTheme.equals("purple") || LauncherSettings.selectedTheme.equals("")) {
            sceneOptions.getStylesheets().add("/css/purple.css");
        }
        if (LauncherSettings.selectedTheme.equals("red")) {
            sceneOptions.getStylesheets().add("/css/red.css");
        }
        if (LauncherSettings.selectedTheme.equals("green")) {
            sceneOptions.getStylesheets().add("/css/green.css");
        }
        if (LauncherSettings.selectedTheme.equals("blue")) {
            sceneOptions.getStylesheets().add("/css/blue.css");
        }
        if (LauncherSettings.selectedTheme.equals("gray")) {
            sceneOptions.getStylesheets().add("/css/gray.css");
        }
        if (LauncherSettings.selectedTheme.equals("white")) {
            sceneOptions.getStylesheets().add("/css/white.css");
        }
    }

}
