package me.minidigger.minecraftlauncher.launcher.tasks;

import org.to2mbn.jmccc.auth.Authenticator;
import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.launch.ProcessListener;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;

import java.io.IOException;

import me.minidigger.minecraftlauncher.launcher.LauncherSettings;

public class MinecraftStartTask extends Thread {

    private Runnable onCorruped;
    private Runnable onStarted;
    private MinecraftDirectory minecraftDirectory;

    public MinecraftStartTask(Runnable onCorruped, Runnable onStarted, MinecraftDirectory minecraftDirectory) {
        this.onCorruped = onCorruped;
        this.onStarted = onStarted;
        this.minecraftDirectory = minecraftDirectory;
        setName("MinecraftStartTask");
    }

    @Override
    public void run() {
        try {
            Launcher launcher = LauncherBuilder.buildDefault();
            Authenticator authenticator = new OfflineAuthenticator(LauncherSettings.playerUsername);
            LaunchOption option = new LaunchOption(LauncherSettings.playerVersion, authenticator, minecraftDirectory);
            launcher.launch(option, new ProcessListener() {
                @Override
                public void onLog(String log) {
                    System.out.println("[LOG] " + log);
                }

                @Override
                public void onErrorLog(String log) {
                    System.out.println("[ERROR-LOG] " + log);
                }

                @Override
                public void onExit(int code) {
                    System.out.println("EXITED " + code);
                }
            });
        } catch (LaunchException | IOException e) {
            e.printStackTrace();
        }
    }

    public void old() {
//        //add server
//        List<String> ip = API.getServersIPList().stream().map(ServerListEntry::getIp).collect(Collectors.toList());
//        if (ip.isEmpty() || !ip.contains(LauncherSettings.serverIP)) {
//            API.addServerToServersDat(LauncherSettings.serverName, LauncherSettings.serverIP);
//        }
//
//        API.downloadProfile(username.getText());
//        API.syncVersions();
//
//        if (!LauncherSettings.fastStartUp) { //NOT faststartup
//            API.downloadMinecraft(version.getValue(), false);
//        }
//
//        API.setMinMemory(Integer.parseInt(LauncherSettings.ramAllocationMin));
//        API.setMemory(Integer.parseInt(LauncherSettings.ramAllocationMax));
//        API.setHeight(Integer.parseInt(LauncherSettings.resolutionHeight));
//        API.setWidth(Integer.parseInt(LauncherSettings.resolutionWidth));
//
//        if (!LauncherSettings.javaPath.equals("")) {
//            API.setJavaPath(LauncherSettings.javaPath);
//        }
//        if (!LauncherSettings.jvmArguments.equals("")) {
//            API.setJVMArgument(LauncherSettings.jvmArguments);
//        }
//        if (!LauncherSettings.playerUsername.equals("")) {
//            API.setVersionData(MessageFormat.format(resourceBundle.getString("versiondata.name"), LauncherSettings.playerUsername));
//        } else {
//            API.setVersionData(resourceBundle.getString("versiondata.default"));
//        }
//
//        boolean nettyPatch = LauncherSettings.bypassBlacklist;
//        if (LauncherSettings.fastStartUp) {
//            API.runMinecraft(username.getText(), version.getValue(), false, nettyPatch);
//        } else {
//            API.runMinecraft(username.getText(), version.getValue(), true, nettyPatch);
//        }
    }
}
