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

package me.minidigger.minecraftlauncher.api;

import net.kyori.nbt.CompoundTag;
import net.kyori.nbt.ListTag;
import net.kyori.nbt.TagIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author ammar
 */
public class LauncherAPI {

    public String getAPIVersion() {
        return "v0.10-alpha";
    }

    public String getUpdateStatus() {
        //download the file..
        Network network = new Network();
        Utils utils = new Utils();
        Local local = new Local();
        String OperatingSystemToUse = utils.getOS();
        network.downloadAPIMeta(OperatingSystemToUse);
        int versionBehind = 0;
        if (!local.getAPIMetaList(OperatingSystemToUse).contains(getAPIVersion())) {
            return "Unknown";
        } else {
            for (Object val : local.getAPIMetaList(OperatingSystemToUse)) {
                if (!getAPIVersion().equals(val)) {
                    versionBehind = versionBehind + 1;
                } else if (getAPIVersion().equals(val)) {
                    break;
                }
            }
        }
        return String.valueOf(versionBehind);
    }

    private String runLogs;

    //run logs getter/setter
    private String getRunLogs() {
        return runLogs; //run logs
    }

    private void setRunLogs(String runLogs_) {
        System.out.println(runLogs_);
        this.setLog("[rl] " + runLogs_);
        runLogs = "[rl] " + runLogs_;
    }

    private String downloadLogs;

    //download logs getter/setter
    private String getDownloadLogs() {
        return downloadLogs; //download logs
    }

    private void setDownloadLogs(String downloadLogs_) {
        System.out.println(downloadLogs_);
        this.setLog("[dl] " + downloadLogs_);
        downloadLogs = "[dl] " + downloadLogs_;
    }

    private String errorLogs;

    //last error logs getter/setter
    private String getErrorLogs() {
        return errorLogs;
    }

    private void setErrorLogs(String errorLogs_) {
        System.out.println(errorLogs_);
        this.setLog("[el] " + errorLogs_);
        errorLogs = "[el] " + errorLogs_;
    }

    private String log;

    //interface for log
    public String getLog() {
        return log;
    }

    private void setLog(String log_) {
        this.setLogs(log_);
        log = log_;
    }

    //interface for full logs
    private List<String> logs = new ArrayList<>();

    public List<String> getLogs() {
        return logs;
    }

    private void setLogs(String logs_) {
        logs.add(logs_);
    }

    public void dumpLogs() {
        Utils utils = new Utils();
        String OperatingSystemToUse = utils.getOS();
        utils.writeLogs(OperatingSystemToUse, (ArrayList) logs);
    }

    public List<String> getInstallableVersionsList() {
        Local local = new Local();
        Utils utils = new Utils();
        String OperatingSystemToUse = utils.getOS();
        local.readJson_versions_id(utils.getMineCraft_Version_Manifest_json(OperatingSystemToUse));
        local.readJson_versions_type(utils.getMineCraft_Version_Manifest_json(OperatingSystemToUse));
        //local.version_manifest_versions_id;
        List<String> InstallableVersionsList = new ArrayList<>();

        if (local.version_manifest_versions_id.size() == local.version_manifest_versions_type.size()) {
            //we can merge them..
            for (int i = 0; i < local.version_manifest_versions_id.size(); i++) {
                InstallableVersionsList.add(local.version_manifest_versions_id.get(i) + " % " + local.version_manifest_versions_type.get(i));
            }
        } else {
            //don't merge them..

            InstallableVersionsList.addAll(local.version_manifest_versions_id);
        }
        return InstallableVersionsList;
    }

    private List<String> getProfileInstalledVersionsList() {
        Utils utils = new Utils();
        Local local = new Local();
        String OperatingSystemToUse = utils.getOS();
        local.readJson_profiles_KEY(utils.getMineCraft_Launcher_Profiles_json(OperatingSystemToUse));
        local.readJson_profiles_KEY_lastVersionId(utils.getMineCraft_Launcher_Profiles_json(OperatingSystemToUse));
        return local.profiles_lastVersionId;
    }

    public List<String> getInstalledVersionsList() {
        Utils utils = new Utils();
        Local local = new Local();
        String OperatingSystemToUse = utils.getOS();
        local.generateVersionJsonPathList(utils.getMineCraftVersionsLocation(OperatingSystemToUse));
        local.generateVersionList(utils.getMineCraftVersionsLocation(OperatingSystemToUse));

        return local.versions_list;
    }

    public List<String> getServersIPList() {
        Utils utils = new Utils();
        String OperatingSystemToUse = utils.getOS();
        return utils.getMineCraftServerDatNBTIP(OperatingSystemToUse);
    }

    public List<String> getServersNameList() {
        Utils utils = new Utils();
        String OperatingSystemToUse = utils.getOS();
        return utils.getMineCraftServerDatNBTName(OperatingSystemToUse);
    }

    public void addServerToServersDat(String Name, String IP) {
        Utils utils = new Utils();
        String OperatingSystemToUse = utils.getOS();
        CompoundTag root = new CompoundTag();
        ListTag servers = new ListTag();
        CompoundTag data = new CompoundTag();

        List<String> names = new ArrayList<>(utils.getMineCraftServerDatNBTName(OperatingSystemToUse));
        List<String> ips = new ArrayList<>(utils.getMineCraftServerDatNBTIP(OperatingSystemToUse));
        data.putString("name", Name);
        data.putString("ip", IP);
        servers.add(data);
        try {
            for (int i = 0; i < ips.size(); i++) {
                data = new CompoundTag();
                data.putString("name", names.get(i));
                data.putString("ip", ips.get(i));
                servers.add(data);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        root.put("servers", servers);
        //System.out.println(root.toString());
        try {
            TagIO.writePath(root, new File(utils.getMineCraft_ServersDat(OperatingSystemToUse)).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncVersions() {
        Utils utils = new Utils();
        Local local = new Local();
        String OperatingSystemToUse = utils.getOS();
        //this function is used to sync json and file system versions together.
        local.fixLauncherProfiles(OperatingSystemToUse); //<-- just fix it!
        LauncherAPI api_Interface = new LauncherAPI();

        List<String> ProfileInstalledVersionsList;    //json
        List<String> InstalledVersionsList;           //filesys

        InstalledVersionsList = api_Interface.getInstalledVersionsList();    //get json
        ProfileInstalledVersionsList = api_Interface.getProfileInstalledVersionsList();    //get filesys

        List<String> union = new ArrayList<>(InstalledVersionsList);
        union.addAll(ProfileInstalledVersionsList);
        // Prepare an intersection
        List<String> intersection = new ArrayList<>(InstalledVersionsList);
        intersection.retainAll(ProfileInstalledVersionsList);
        // Subtract the intersection from the union
        union.removeAll(intersection);
        union.removeAll(ProfileInstalledVersionsList); //this is required so that we can get rid of redundant versions
        // Print the result
        if (!union.isEmpty()) {
            for (Object n : union) {
                //add these versions to the system.
                if (n != null) {
                    System.out.println(n);
                    local.writeJson_launcher_profiles(OperatingSystemToUse, n.toString() + "_Cracked_" + utils.nextSessionId(), n.toString());
                }
            }
        }
    }

    private void injectNetty() {
        Utils utils = new Utils();
        String OperatingSystemToUse = utils.getOS();
        try {
            utils.injectNetty(OperatingSystemToUse);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {
            utils.injectPatchy(OperatingSystemToUse);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void runMinecraft(String UsernameToUse, String VersionToUse, Boolean HashCheck, Boolean injectNetty) {
        Utils utils = new Utils();
        Local local = new Local();
        Network network = new Network();
        String OperatingSystemToUse = utils.getOS();
        //get list of all 
        local.readJson_versions_id(utils.getMineCraft_Version_Manifest_json(OperatingSystemToUse));
        local.readJson_versions_type(utils.getMineCraft_Version_Manifest_json(OperatingSystemToUse));
        local.readJson_versions_url(utils.getMineCraft_Version_Manifest_json(OperatingSystemToUse));

        //inject netty
        if (injectNetty) {
            injectNetty();
        }

        //declaration for mods
        String MOD_inheritsFrom = null;
        String MOD_jar = null;
        String MOD_assets = null;
        String MOD_minecraftArguments;
        String MOD_mainClass = null;
        String MOD_id = null;
        //check if it is vanilla or not
        if (local.checkIfVanillaMC(VersionToUse).equals(true)) {
            this.setRunLogs("Vanilla Minecraft found!");
        } else {
            this.setRunLogs("Modded Minecraft found!");
            local.MOD_readJson_libraries_name_PLUS_url(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            for (int i = 0; i < local.version_name_list.size(); i++) {
                this.setRunLogs(local.version_name_list.get(i));
                this.setRunLogs(local.HALF_URL_version_url_list.get(i));
            }

            this.setRunLogs("Fixing url using name.");
            for (int i = 0; i < local.version_name_list.size(); i++) {
                local.version_path_list.add(local.generateLibrariesPath(OperatingSystemToUse, local.version_name_list.get(i)));

            }

            for (int i = 0; i < local.version_name_list.size(); i++) {
                local.version_url_list.add(local.HALF_URL_version_url_list.get(i) + "/" + local.version_path_list.get(i));
            }

            MOD_inheritsFrom = local.readJson_inheritsFrom(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setRunLogs("inheritsFrom: " + MOD_inheritsFrom);

            MOD_jar = local.readJson_jar(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setRunLogs("jar: " + MOD_jar);

            MOD_assets = local.readJson_assets(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setRunLogs("assets: " + MOD_assets);

            MOD_minecraftArguments = local.readJson_minecraftArguments(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setRunLogs("minecraftArguments: " + MOD_minecraftArguments);

            MOD_mainClass = local.readJson_mainClass(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setRunLogs("mainClass: " + MOD_mainClass);

            MOD_id = local.readJson_id(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setRunLogs("id: " + MOD_id);
        }

        if (MOD_inheritsFrom == null) {
            this.setRunLogs("Using: " + VersionToUse);

        } else {
            VersionToUse = MOD_inheritsFrom;
            this.setRunLogs("Using: " + VersionToUse);

        }

        //incase the url is empty.. we have to assume that the user has old path system.
        for (int i = 0; i < local.version_manifest_versions_id.size(); i++) {
            this.setRunLogs(local.version_manifest_versions_id.get(i));
            this.setRunLogs(local.version_manifest_versions_type.get(i));
            this.setRunLogs(local.version_manifest_versions_url.get(i));
        }

        //download 1.7.10.json_libs
        try {
            for (int i = 0; i < local.version_manifest_versions_id.size(); i++) {
                if (local.version_manifest_versions_id.get(i).equals(VersionToUse)) {
                    //we will download versionjson everytime.
                    if (HashCheck) {
                        network.downloadVersionJson(OperatingSystemToUse, local.version_manifest_versions_url.get(i), local.version_manifest_versions_id.get(i));
                    }
                    break;
                } else {
                    //do nothing...
                }
            }

        } catch (Exception e) {
            this.setErrorLogs("Something went wrong downloadVersionJson" + e);
        }

        this.setRunLogs(utils.getMineCraftLocation(OperatingSystemToUse));

        local.generateVersionJsonPathList(utils.getMineCraftVersionsLocation(OperatingSystemToUse));
        local.generateVersionList(utils.getMineCraftVersionsLocation(OperatingSystemToUse));

        for (int i = 0; i < local.versions_json_path_list.size(); i++) {
            this.setRunLogs(local.versions_json_path_list.get(i));
        }

        for (int i = 0; i < local.versions_list.size(); i++) {
            this.setRunLogs(local.versions_list.get(i));
        }

        this.setRunLogs(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));

        try {
            local.readJson_libraries_downloads_artifact_url(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));

        } catch (Exception ex) {
            this.setErrorLogs("Unable to get libraries_downloads_artifact_url " + ex);
        }
        try {
            local.readJson_libraries_downloads_artifact_path(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));

        } catch (Exception ex) {
            this.setErrorLogs("Unable to get libraries_downloads_artifact_path " + ex);
        }
        try {
            local.readJson_libraries_name(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));

        } catch (Exception ex) {
            this.setErrorLogs("Unable to get libraries_name " + ex);
        }

        try {
            this.setRunLogs(local.readJson_assetIndex_url(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse)));

        } catch (Exception ex) {
            this.setErrorLogs("Unable to get assetIndex_url" + ex);
        }
        try {
            this.setRunLogs(local.readJson_assetIndex_id(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse)));
        } catch (Exception ex) {
            this.setErrorLogs("Unable to get assetIndex_id" + ex);
        }

        this.setRunLogs(utils.getMineCraftAssetsIndexes_X_json(OperatingSystemToUse, VersionToUse));

        try {
            local.readJson_objects_KEY(utils.getMineCraftAssetsIndexes_X_json(OperatingSystemToUse, local.readJson_assetIndex_id(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse))));

        } catch (Exception e) {
            this.setErrorLogs("Error reading objects KEY" + e);
        }
        try {
            local.readJson_objects_KEY_hash(utils.getMineCraftAssetsIndexes_X_json(OperatingSystemToUse, local.readJson_assetIndex_id(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse))));

        } catch (Exception e) {
            this.setErrorLogs("Error reading objects KEY_hash" + e);

        }

        if (HashCheck) {
            try {
                for (int i = 0; i < local.objects_hash.size(); i++) {
                    this.setRunLogs("HASH: " + local.objects_hash.get(i));
                    this.setRunLogs("FOLDER: " + local.objects_hash.get(i).substring(0, 2));
                    this.setRunLogs("KEY: " + local.objects_KEY.get(i));
                    utils.copyToVirtual(OperatingSystemToUse, local.objects_hash.get(i).substring(0, 2), local.objects_hash.get(i), local.objects_KEY.get(i));
                    //generate virtual folder as well.

                }

            } catch (Exception e) {
                this.setErrorLogs("Error reading objects KEY + KEY_hash" + e);

            }
        }


        this.setRunLogs("Getting NATIVES URL");
        local.readJson_libraries_downloads_classifiers_natives_X(utils.getMineCraft_Versions_X_X_json(OperatingSystemToUse, VersionToUse), OperatingSystemToUse);
        this.setRunLogs("Getting NATIVES PATH");
        local.readJson_libraries_downloads_classifiers_natives_Y(utils.getMineCraft_Versions_X_X_json(OperatingSystemToUse, VersionToUse), OperatingSystemToUse);

        for (int i = 0; i < local.version_url_list_natives.size(); i++) {
            this.setRunLogs("NATIVE URL: " + local.version_url_list_natives.get(i));
            //extract them here..
            this.setRunLogs("Extracting...");
            this.setRunLogs(local.version_url_list_natives.get(i));
            this.setRunLogs(utils.getMineCraft_Versions_X_Natives_Location(OperatingSystemToUse, VersionToUse));

            utils.jarExtract(OperatingSystemToUse, local.version_path_list_natives.get(i), utils.getMineCraft_Versions_X_Natives_Location(OperatingSystemToUse, VersionToUse));

        }

        //String HalfArgumentTemplate = local.readJson_minecraftArguments(utils.getMineCraft_Versions_X_X_json(OperatingSystemToUse, VersionToUse));
        int Xmx = this.getMemory();
        int Xms = this.getMinMemory();
        int Width = this.getWidth();
        int Height = this.getHeight();
        String JavaPath = this.getJavaPath();
        String JVMArgument = this.getJVMArgument();


        String mainClass;
        if (MOD_mainClass == null) {
            mainClass = local.readJson_mainClass(utils.getMineCraft_Versions_X_X_json(OperatingSystemToUse, VersionToUse));

        } else {
            mainClass = MOD_mainClass;
        }

        String NativesDir = utils.getMineCraft_Versions_X_Natives(OperatingSystemToUse, VersionToUse);
        String assetsIdexId;
        if (MOD_assets == null) {
            assetsIdexId = local.readJson_assets(utils.getMineCraft_Versions_X_X_json(OperatingSystemToUse, VersionToUse));

        } else {
            assetsIdexId = MOD_assets;
        }
        if (assetsIdexId == null) {
            assetsIdexId = "NULL";
        }

        String gameDirectory = utils.getMineCraftLocation(OperatingSystemToUse);
        String AssetsRoot = utils.getMineCraftAssetsRootLocation(OperatingSystemToUse);

        String versionName;
        if (MOD_id == null) {
            versionName = local.readJson_id(utils.getMineCraft_Versions_X_X_json(OperatingSystemToUse, VersionToUse));
        } else {
            versionName = MOD_id;
        }

        String authuuid = local.readJson_id(utils.getMineCraft_X_json(OperatingSystemToUse, UsernameToUse));
        String Username = UsernameToUse;
        String MinecraftJar;
        if (MOD_jar == null) {
            MinecraftJar = utils.getMineCraft_Versions_X_X_jar(OperatingSystemToUse, VersionToUse);

        } else {
            MinecraftJar = utils.getMineCraft_Versions_X_X_jar(OperatingSystemToUse, MOD_jar);
        }

        String VersionType = this.getVersionData();
        String AuthSession = "OFFLINE";

        String GameAssets = utils.getMineCraftAssetsVirtualLegacyLocation(OperatingSystemToUse);
        System.out.println("NativesPath: " + NativesDir);

        for (int i = 0; i < local.version_path_list.size(); i++) {
            local.libraries_path.add(utils.setMineCraft_librariesLocation(OperatingSystemToUse, local.version_path_list.get(i)));
            System.out.println(local.libraries_path.get(i));
        }

        String HalfLibraryArgument = local.generateLibrariesArguments(OperatingSystemToUse);
        String FullLibraryArgument = local.generateLibrariesArguments(OperatingSystemToUse) + utils.getArgsDiv(OperatingSystemToUse) + MinecraftJar;
        System.out.println("HalfLibraryArgument: " + HalfLibraryArgument);
        System.out.println("FullLibraryArgument: " + FullLibraryArgument);

        //argument patch for netty and patchy comes here
        if (injectNetty) {
            System.out.println("Netty/Patchy Patch Detected!");

            String patchy_mod = "";
            String patchy = "";

            String netty_mod = "";
            String netty = "";


            try {
                Map<String, String> patchyMAP = new HashMap<>(utils.getMineCraftLibrariesComMojangPatchy_jar(OperatingSystemToUse));
                for (Map.Entry<String, String> entry : patchyMAP.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (value.startsWith("mod_")) {
                        patchy_mod = value;
                    } else {
                        patchy = value;
                    }

                    System.out.println("KEY:::::" + key);
                    System.out.println("VALUE:::::" + value);
                }
                HalfLibraryArgument = HalfLibraryArgument.replace(patchy, patchy_mod);
                FullLibraryArgument = FullLibraryArgument.replace(patchy, patchy_mod);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                Map<String, String> nettyMAP = new HashMap<>(utils.getMineCraftLibrariesComMojangNetty_jar(OperatingSystemToUse));
                for (Map.Entry<String, String> entry : nettyMAP.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (value.startsWith("mod_")) {
                        netty_mod = value;
                    } else {
                        netty = value;
                    }

                    System.out.println("KEY:::::" + key);
                    System.out.println("VALUE:::::" + value);
                }
                HalfLibraryArgument = HalfLibraryArgument.replace(netty, netty_mod);
                FullLibraryArgument = FullLibraryArgument.replace(netty, netty_mod);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        //argument patch netty and patchy ends here

        String[] HalfArgument = local.generateMinecraftArguments(OperatingSystemToUse, Username, versionName, gameDirectory, AssetsRoot, assetsIdexId, authuuid, "aeef7bc935f9420eb6314dea7ad7e1e5", "{\"twitch_access_token\":[\"emoitqdugw2h8un7psy3uo84uwb8raq\"]}", "mojang", VersionType, GameAssets, AuthSession);
        //System.out.println("HalfArgument: " + HalfArgument);
        for (String HalfArgsVal : HalfArgument) {
            System.out.println("HalfArg: " + HalfArgsVal);
        }
        System.out.println("Minecraft.jar: " + MinecraftJar);

        this.setRunLogs("username: " + Username);
        this.setRunLogs("version number: " + versionName);
        this.setRunLogs("game directory: " + gameDirectory);
        this.setRunLogs("assets root directory: " + AssetsRoot);
        this.setRunLogs("assets Index Id: " + assetsIdexId);
        this.setRunLogs("assets legacy directory: " + GameAssets);
        //won't be using this
        //this.setRunLogs(local.generateRunnableArguments(Xmx, NativesDir, FullLibraryArgument, mainClass, HalfArgument));

        try {
            String cmds[] = {"-Xms" + Xms + "M", "-Xmx" + Xmx + "M", "-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump", "-Djava.library.path=" + NativesDir, "-cp", FullLibraryArgument, mainClass, "--width", String.valueOf(Width), "--height", String.valueOf(Height)};
            //put jvm arguments here
            String[] JVMArguments = JVMArgument.split(" ");
            //we now have all the arguments. merge cmds with JVMArguments
            if (!JVMArgument.isEmpty()) {
                //no need to join.
                cmds = Stream.concat(Arrays.stream(JVMArguments), Arrays.stream(cmds)).toArray(String[]::new);
            }
            String javaPathArr[] = {JavaPath};
            //merge javapath back to cmds
            cmds = Stream.concat(Arrays.stream(javaPathArr), Arrays.stream(cmds)).toArray(String[]::new);

            String[] finalArgs = Stream.concat(Arrays.stream(cmds), Arrays.stream(HalfArgument)).toArray(String[]::new);
            for (String finalArgs_ : finalArgs) {
                this.setRunLogs(finalArgs_);
            }
            this.setRunLogs("Starting game... Please wait....");
            Process process = Runtime.getRuntime().exec(finalArgs);

            try {
                process.waitFor(10, TimeUnit.SECONDS);
                //process.waitFor();
                if (process.exitValue() != 0) {
                    //something went wrong.
                    this.setErrorLogs("Minecraft Corruption found!");
                }

            } catch (Exception ex) {
                this.setRunLogs("Minecraft Initialized!");
                //nothing to print.. everythimg went fine.
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String jvmArgument = "";

    public void setJVMArgument(String jvmArgument_) {
        jvmArgument = jvmArgument_;
    }

    private String getJVMArgument() {
        return jvmArgument;
    }

    private String javaPath = "java";

    public void setJavaPath(String javaPath_) {
        javaPath = javaPath_;
    }

    private String getJavaPath() {
        return javaPath;
    }

    private int width = 854;

    public void setWidth(int width_) {
        width = width_;
    }

    private int getWidth() {
        return width;
    }

    private int height = 480;

    public void setHeight(int height_) {
        height = height_;
    }

    private int getHeight() {
        return height;
    }

    private int memory = 1024;

    public void setMemory(int memory_) {
        memory = memory_;
    }

    private int getMemory() {
        return memory;
    }

    private int minMemory = 1024;

    public void setMinMemory(int memory_) {
        minMemory = memory_;
    }

    private int getMinMemory() {
        return minMemory;
    }

    private String versionData = "#ammarbless";

    public void setVersionData(String versionData_) {
        versionData = versionData_;
    }

    private String getVersionData() {
        return versionData;
    }

    public void downloadVersionManifest() {
        Utils utils = new Utils();
        Network network = new Network();
        System.out.println("Downloading: version_manifest.json");
        String OperatingSystemToUse = utils.getOS();
        network.downloadVersionManifest(utils.getMineCraft_Version_Manifest_json(OperatingSystemToUse));

    }

    public void downloadProfile(String UsernameToUse) {
        Utils utils = new Utils();
        Network network = new Network();
        String OperatingSystemToUse = utils.getOS();
        System.out.println("Downloading: " + UsernameToUse + ".json");
        network.downloadProfile(OperatingSystemToUse, UsernameToUse);

    }

    public void downloadMinecraft(String VersionToUse, Boolean ForceDownload) {
        Utils utils = new Utils();
        Local local = new Local();
        Network network = new Network();
        String OperatingSystemToUse = utils.getOS();
        this.setDownloadLogs("Downlaoding: " + VersionToUse);

        //add version in launcher_profiles.json
        local.writeJson_launcher_profiles(OperatingSystemToUse, "_Cracked_" + utils.nextSessionId() + "_" + VersionToUse, VersionToUse);

        //get list of all 
        local.readJson_versions_id(utils.getMineCraft_Version_Manifest_json(OperatingSystemToUse));
        local.readJson_versions_type(utils.getMineCraft_Version_Manifest_json(OperatingSystemToUse));
        local.readJson_versions_url(utils.getMineCraft_Version_Manifest_json(OperatingSystemToUse));

        //declaration for mods
        String MOD_inheritsFrom = null;
        String MOD_jar = null;
        String MOD_assets;
        String MOD_minecraftArguments;
        String MOD_mainClass;
        String MOD_id;
        //check if it is vanilla or not
        if (local.checkIfVanillaMC(VersionToUse).equals(true)) {
            this.setRunLogs("Vanilla Minecraft found!");

        } else {
            this.setRunLogs("Modded Minecraft found!");
            local.MOD_readJson_libraries_name_PLUS_url(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            for (int i = 0; i < local.version_name_list.size(); i++) {
                System.out.println(local.version_name_list.get(i));
                System.out.println(local.HALF_URL_version_url_list.get(i));
            }

            this.setDownloadLogs("Fixing url using name.");
            for (int i = 0; i < local.version_name_list.size(); i++) {
                local.version_path_list.add(local.generateLibrariesPath(OperatingSystemToUse, local.version_name_list.get(i)));

            }

            for (int i = 0; i < local.version_name_list.size(); i++) {
                local.version_url_list.add(local.HALF_URL_version_url_list.get(i) + "/" + local.version_path_list.get(i));
            }
            for (int i = 0; i < local.version_name_list.size(); i++) {
                this.setDownloadLogs("Downloading: " + local.version_url_list.get(i));
                network.downloadLibraries(OperatingSystemToUse, local.version_url_list.get(i), local.version_path_list.get(i), ForceDownload);

            }

            MOD_inheritsFrom = local.readJson_inheritsFrom(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setDownloadLogs("inheritsFrom: " + MOD_inheritsFrom);

            MOD_jar = local.readJson_jar(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setDownloadLogs("jar: " + MOD_jar);

            MOD_assets = local.readJson_assets(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setDownloadLogs("assets: " + MOD_assets);

            MOD_minecraftArguments = local.readJson_minecraftArguments(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setDownloadLogs("minecraftArguments: " + MOD_minecraftArguments);

            MOD_mainClass = local.readJson_mainClass(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setDownloadLogs("mainClass: " + MOD_mainClass);

            MOD_id = local.readJson_id(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));
            this.setDownloadLogs("id: " + MOD_id);
        }

        if (MOD_inheritsFrom == null) {
            this.setDownloadLogs("Using: " + VersionToUse);

        } else {
            VersionToUse = MOD_inheritsFrom;
            this.setDownloadLogs("Using: " + VersionToUse);

        }

        //incase the url is empty.. we have to assume that the user has old path system.
        for (int i = 0; i < local.version_manifest_versions_id.size(); i++) {
            this.setDownloadLogs("ID: " + local.version_manifest_versions_id.get(i));
            this.setDownloadLogs("TYPE: " + local.version_manifest_versions_type.get(i));
            this.setDownloadLogs("URL: " + local.version_manifest_versions_url.get(i));
        }

        //download 1.7.10.json_libs
        try {
            for (int i = 0; i < local.version_manifest_versions_id.size(); i++) {
                if (local.version_manifest_versions_id.get(i).equals(VersionToUse)) {
                    network.downloadVersionJson(OperatingSystemToUse, local.version_manifest_versions_url.get(i), local.version_manifest_versions_id.get(i));
                    break;
                } else {
                    //do nothing...
                }
            }

        } catch (Exception e) {
            this.setErrorLogs("Something went wrong getting version json" + e);
        }

        this.setRunLogs(utils.getMineCraftLocation(OperatingSystemToUse));

        local.generateVersionJsonPathList(utils.getMineCraftVersionsLocation(OperatingSystemToUse));
        local.generateVersionList(utils.getMineCraftVersionsLocation(OperatingSystemToUse));

        try {
            local.readJson_libraries_downloads_artifact_url(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));

        } catch (Exception ex) {
            this.setErrorLogs("Exception" + ex);

        }
        try {
            local.readJson_libraries_downloads_artifact_path(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));

        } catch (Exception ex) {
            this.setErrorLogs("Exception" + ex);

        }
        try {
            local.readJson_libraries_name(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse));

        } catch (Exception ex) {
            this.setErrorLogs("Exception" + ex);

        }
        ///************************************************************
        for (int i = 0; i < local.version_url_list.size(); i++) {
            this.setDownloadLogs("Downloading: " + local.version_url_list.get(i));
            try {
                network.downloadLibraries(OperatingSystemToUse, local.version_url_list.get(i), local.version_path_list.get(i), ForceDownload);

            } catch (Exception ex) {
                this.setErrorLogs("Due to: " + ex + " " + local.generateLibrariesPath(OperatingSystemToUse, local.version_name_list.get(i)));
                local.version_path_list.add(local.generateLibrariesPath(OperatingSystemToUse, local.version_name_list.get(i)));
                network.downloadLibraries(OperatingSystemToUse, local.version_url_list.get(i), local.generateLibrariesPath(OperatingSystemToUse, local.version_name_list.get(i)), ForceDownload);

            }
        }

        //this may need to be edited!*************//
        this.setRunLogs(local.readJson_assetIndex_url(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse)));
        this.setRunLogs(local.readJson_assetIndex_id(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse)));
        //get assets index id!
        network.downloadLaunchermeta(OperatingSystemToUse, local.readJson_assetIndex_url(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse)), local.readJson_assetIndex_id(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse)), ForceDownload);

        this.setRunLogs(utils.getMineCraftAssetsIndexes_X_json(OperatingSystemToUse, VersionToUse));

        local.readJson_objects_KEY(utils.getMineCraftAssetsIndexes_X_json(OperatingSystemToUse, local.readJson_assetIndex_id(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse))));
        local.readJson_objects_KEY_hash(utils.getMineCraftAssetsIndexes_X_json(OperatingSystemToUse, local.readJson_assetIndex_id(utils.getMineCraft_Version_Json(OperatingSystemToUse, VersionToUse))));

        for (int i = 0; i < local.objects_hash.size(); i++) {
            this.setDownloadLogs("HASH: " + local.objects_hash.get(i));
            this.setDownloadLogs("FOLDER: " + local.objects_hash.get(i).substring(0, 2));
            this.setDownloadLogs("KEY: " + local.objects_KEY.get(i));

            this.setDownloadLogs("DOWNLOADING..." + "HASH: " + local.objects_hash.get(i));
            network.downloadAssetsObjects(OperatingSystemToUse, local.objects_hash.get(i).substring(0, 2), local.objects_hash.get(i));
            utils.copyToVirtual(OperatingSystemToUse, local.objects_hash.get(i).substring(0, 2), local.objects_hash.get(i), local.objects_KEY.get(i));
            //generate virtual folder as well.

        }

        this.setDownloadLogs("DOWNLOADING MINECRAFT JAR " + VersionToUse);
        if (MOD_jar == null) {
            network.downloadMinecraftJar(OperatingSystemToUse, VersionToUse, ForceDownload);

        } else {
            network.downloadMinecraftJar(OperatingSystemToUse, MOD_jar, ForceDownload);

        }

        //would have tp edit this line as we also need natives paths!
        this.setDownloadLogs("Getting NATIVES URL");
        local.readJson_libraries_downloads_classifiers_natives_X(utils.getMineCraft_Versions_X_X_json(OperatingSystemToUse, VersionToUse), OperatingSystemToUse);
        this.setDownloadLogs("Getting NATIVES PATH");
        local.readJson_libraries_downloads_classifiers_natives_Y(utils.getMineCraft_Versions_X_X_json(OperatingSystemToUse, VersionToUse), OperatingSystemToUse);

        for (int i = 0; i < local.version_url_list_natives.size(); i++) {
            this.setDownloadLogs("NATIVE URL: " + local.version_url_list_natives.get(i));
            network.downloadLibraries(OperatingSystemToUse, local.version_url_list_natives.get(i), local.version_path_list_natives.get(i), ForceDownload);
            //extract them here..
            this.setRunLogs("Extracting...");
            this.setRunLogs(local.version_url_list_natives.get(i));
            this.setRunLogs(utils.getMineCraft_Versions_X_Natives_Location(OperatingSystemToUse, VersionToUse));

            utils.jarExtract(OperatingSystemToUse, local.version_path_list_natives.get(i), utils.getMineCraft_Versions_X_Natives_Location(OperatingSystemToUse, VersionToUse));

        }
        this.setDownloadLogs("Download Complete!");
    }

}
