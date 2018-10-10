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
    private Authenticator authenticator;
    private MinecraftDirectory minecraftDirectory;

    public MinecraftStartTask(Runnable onCorruped, Runnable onStarted, Authenticator authenticator, MinecraftDirectory minecraftDirectory) {
        this.onCorruped = onCorruped;
        this.onStarted = onStarted;
        this.authenticator = authenticator;
        this.minecraftDirectory = minecraftDirectory;
        setName("MinecraftStartTask");
    }

    @Override
    public void run() {
        try {
            Launcher launcher = LauncherBuilder.buildDefault();
            LaunchOption option = new LaunchOption(LauncherSettings.playerVersion, authenticator, minecraftDirectory);
            launcher.launch(option, new ProcessListener() {

                private boolean firstStart = true;

                @Override
                public void onLog(String log) {
                    if (firstStart) {
                        firstStart = false;
                        onStarted.run();
                    }
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
