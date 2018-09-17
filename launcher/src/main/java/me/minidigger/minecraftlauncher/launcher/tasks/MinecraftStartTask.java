package me.minidigger.minecraftlauncher.launcher.tasks;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import me.minidigger.minecraftlauncher.launcher.LauncherSettings;

public class MinecraftStartTask extends Thread {

    private Runnable onCorruped;
    private Runnable onStarted;

    public MinecraftStartTask(Runnable onCorruped, Runnable onStarted) {
        this.onCorruped = onCorruped;
        this.onStarted = onStarted;
        setName("MinecraftStartTask");
    }

    @Override
    public void run() {
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
    }
}
