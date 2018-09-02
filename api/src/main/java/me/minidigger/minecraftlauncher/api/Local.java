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

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author ammar
 */
class Local {
    private final static Logger logger = LoggerFactory.getLogger(Local.class);

    List<String> versions_json_path_list = new ArrayList<>(); //gets the path of all json files
    List<String> versions_list = new ArrayList<>();           //just gets the versions available on the system
    List<String> version_url_list = new ArrayList<>();        //gets url of all the libraries
    List<String> HALF_URL_version_url_list = new ArrayList<>();// this is the half url. it needs to be fixed first in order to be used

    List<String> version_path_list = new ArrayList<>();       //%new added... This is for direct paths
    List<String> version_name_list = new ArrayList<>();       //%new added... This is for direct names

    List<String> objects_hash = new ArrayList<>();            //gets objects hash
    List<String> objects_KEY = new ArrayList<>();             //gets objects keys

    List<String> profiles_lastVersionId = new ArrayList<>();   //gets profiles lastVersionId
    List<String> profiles_KEY = new ArrayList<>();             //gets profiles keys

    List<String> version_url_list_natives = new ArrayList<>();    //gets url of all the natives
    List<String> version_path_list_natives = new ArrayList<>();    //%gets url of all the natives
    List<String> version_name_list_natives = new ArrayList<>(); //EXP CODE!

    List<String> libraries_path = new ArrayList<>();          //gets path to all the libraries
    //List natives_path = new ArrayList();            //_NOT NEEDED_ gets path to all the natives

    List<String> version_manifest_versions_id = new ArrayList<>();
    List<String> version_manifest_versions_type = new ArrayList<>();
    List<String> version_manifest_versions_url = new ArrayList<>();

    public void fixLauncherProfiles() {
        //this is where we will check if file is available or not. 
        //if the file is available, we do not need to fix anything...
        //else we need to fix it using the new version.
        try {

            // TODO we also need to fix this
            String content = "{\"profiles\":{\"TagCraftMC\":{\"name\":\"TagCraftMC\"}},\"selectedProfile\":\"TagCraftMC\",\"clientToken\":\"dc291e47-a41f-4bc8-8ec2-563195188db2\",\"authenticationDatabase\":{\"4db2fbf560f355492dea6962e103f1d2\":{\"displayName\":\"TagCraftMC\",\"userProperties\":[{\"name\":\"twitch_access_token\",\"value\":\"e4u4updugw2h7pn7psy3u4u4u7p8raq\"}],\"accessToken\":\"c8c8358cac8a43c896ec85ee3c807c8e\",\"userid\":\"793addf9682c8e04c8cc823db79c8c85\",\"uuid\":\"4db2fbf5-60f3-5549-2dea-6962e103f1d2\",\"username\":\"support@tagcraftmc.com\"}},\"selectedUser\":\"4db2fbf560f355492dea6962e103f1d2\",\"launcherVersion\":{\"name\":\"1.6.61\",\"format\":18}}";

            File file = Utils.getMineCraft_Launcher_Profiles_json().toFile();

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
                logger.info("LauncherProfiles.json Created!");
            } else {
                logger.debug("LauncherProfiles.json not created! File exists!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readJson_profiles_KEY(Path path) {
        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object jsonfile;

            jsonfile = readMCJSONFiles.parse(Files.newBufferedReader(path));

            JSONObject jsonObject = (JSONObject) jsonfile;
            JSONObject profiles = (JSONObject) jsonObject.get("profiles");

            Set fileCheckObjects = profiles.keySet();
            Iterator a = fileCheckObjects.iterator();
            while (a.hasNext()) {
                String fileName = (String) a.next();
                profiles_KEY.add(fileName);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }

    }

    public void readJson_profiles_KEY_lastVersionId(Path path) {
        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object jsonfile;

            jsonfile = readMCJSONFiles.parse(Files.newBufferedReader(path));

            JSONObject jsonObject = (JSONObject) jsonfile;
            JSONObject profiles = (JSONObject) jsonObject.get("profiles");

            Set fileCheckObjects = profiles.keySet();
            for (Object fileCheckObject : fileCheckObjects) {
                String fileName = (String) fileCheckObject;

                JSONObject fileNameObject = (JSONObject) profiles.get(fileName);
                String lastVersionId = (String) fileNameObject.get("lastVersionId");
                profiles_lastVersionId.add(lastVersionId);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }

    }

    /*
    public void writeJson_launcher_profiles_Sync() {
        //step 1 would be to populate the version list.
        //step 2 would be to load json and read profiles{ {val} { lastVersionId } }
    }
     */
    public void writeJson_launcher_profiles(String profile, String version) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(Utils.getMineCraft_Launcher_Profiles_json().toFile()));
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject profiles = (JSONObject) jsonObject.get("profiles");
            String selectedProfile = (String) jsonObject.get("selectedProfile");
            String clientToken = (String) jsonObject.get("clientToken");
            JSONObject authenticationDatabase = (JSONObject) jsonObject.get("authenticationDatabase");
            JSONObject selectedUser = (JSONObject) jsonObject.get("selectedUser");
            JSONObject launcherVersion = (JSONObject) jsonObject.get("launcherVersion");

            JSONObject params = new JSONObject();

            params.put("lastVersionId", version);
            params.put("name", profile);
            profiles.put(profile, params);

            JSONObject lp_json = new JSONObject();
            lp_json.put("profiles", profiles);
            lp_json.put("selectedProfile", selectedProfile);
            lp_json.put("clientToken", clientToken);
            lp_json.put("authenticationDatabase", authenticationDatabase);
            lp_json.put("selectedUser", selectedUser);
            lp_json.put("launcherVersion", launcherVersion);
            Files.write(Utils.getMineCraft_Launcher_Profiles_json(), lp_json.toJSONString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE);

        } catch (Exception e) {
            logger.warn("Failed to write launcher profiles", e);
        }
    }

    public void readJson_versions_id(Path path) {
        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object object = readMCJSONFiles.parse(new FileReader(path.toFile()));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray versions = (JSONArray) jsonObject.get("versions");
            for (JSONObject version : (Iterable<JSONObject>) versions) {
                version_manifest_versions_id.add((String) version.get("id"));
            }
        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse version ids", e);
        }
    }

    public void readJson_versions_type(Path path) {
        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object object = readMCJSONFiles.parse(new FileReader(path.toFile()));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray versions = (JSONArray) jsonObject.get("versions");
            for (JSONObject version : (Iterable<JSONObject>) versions) {
                version_manifest_versions_type.add((String) version.get("type"));
            }
        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse version types", e);
        }
    }

    public void readJson_versions_url(Path path) {
        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object object = readMCJSONFiles.parse(new FileReader(path.toFile()));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray versions = (JSONArray) jsonObject.get("versions");
            for (JSONObject version : (Iterable<JSONObject>) versions) {
                version_manifest_versions_url.add((String) version.get("url"));
            }
        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse version urls", e);
        }
    }

    public void readJson_libraries_name(Path path) {
        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object object = readMCJSONFiles.parse(new FileReader(path.toFile()));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray versions = (JSONArray) jsonObject.get("libraries");
            for (JSONObject version : (Iterable<JSONObject>) versions) {
                version_name_list.add((String) version.get("name"));
            }
        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse library names", e);
        }
    }

    public void readJson_libraries_downloads_artifact_url(Path path) {

        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object object = readMCJSONFiles.parse(Files.newBufferedReader(path));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray msg = (JSONArray) jsonObject.get("libraries");
            for (JSONObject aMsg : (Iterable<JSONObject>) msg) {
                JSONObject downloads = (JSONObject) aMsg.get("downloads");
                if (downloads.get("artifact") != null) {
                    JSONObject artifact = (JSONObject) downloads.get("artifact");
                    if (artifact.get("url") != null) {
                        String url = (String) artifact.get("url");
                        version_url_list.add(url);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse library download urls", e);
        }

    }

    public void readJson_libraries_downloads_artifact_path(Path path) {

        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object object = readMCJSONFiles.parse(Files.newBufferedReader(path));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray msg = (JSONArray) jsonObject.get("libraries");
            for (JSONObject aMsg : (Iterable<JSONObject>) msg) {
                JSONObject downloads = (JSONObject) aMsg.get("downloads");
                if (downloads.get("artifact") != null) {
                    JSONObject artifact = (JSONObject) downloads.get("artifact");
                    if (artifact.get("path") != null) {
                        String _path = (String) artifact.get("path");
                        version_path_list.add(_path);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse library download paths", e);
        }

    }

    //edit this function to add more operating systems
    public void readJson_libraries_downloads_classifiers_natives_X(Path path) {

        try {
            String natives_OS = null;
            switch(Utils.getOS()) {
                case LINUX:
                    natives_OS = "natives-linux";
                    break;
                case WINDOWS:
                    natives_OS = "natives-windows";
                    break;
                case MAC:
                    natives_OS = "natives-osx";
                    break;
            }

            String content = new Scanner(path.toFile()).useDelimiter("\\Z").next();
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            try {

                //TODO figure out wtf this script is doing
                String script_js = "var getJsonLibrariesDownloadsClassifiersNativesX=function(r,s){var a=r,e=JSON.parse(a),n=\"\",t=0;for(i=0;i<500;i++)try{n=n+e.libraries[t].downloads.classifiers[s].url+\"\\n\",t+=1}catch(o){t+=1}return n},getJsonLibrariesDownloadsClassifiersNativesY=function(r,s){var a=r,e=JSON.parse(a),n=\"\",t=0;for(i=0;i<500;i++)try{n=n+e.libraries[t].downloads.classifiers[s].path+\"\\n\",t+=1}catch(o){t+=1}return n},getJsonLibrariesDownloadsClassifiersNativesZ=function(r){var s=r,a=JSON.parse(s),e=\"\",n=0;for(i=0;i<500;i++)try{a.libraries[n].natives?(e=e+a.libraries[n].name+\"\\n\",n+=1):n+=1}catch(t){n+=1}return e};";

                File file = new File("./.script.js");
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(script_js);
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            engine.eval(new FileReader("./.script.js"));

            Invocable invocable = (Invocable) engine;

            Object result = invocable.invokeFunction("getJsonLibrariesDownloadsClassifiersNativesX", content, natives_OS);

            version_url_list_natives.addAll(Arrays.asList(result.toString().split("\n")));
        } catch (FileNotFoundException | ScriptException | NoSuchMethodException ex) {
            logger.warn("Failed to parse native library classifiers", ex);
        }
    }

    public void readJson_libraries_downloads_classifiers_natives_Y(Path path) {

        try {
            String natives_OS = null;
            switch(Utils.getOS()) {
                case LINUX:
                    natives_OS = "natives-linux";
                    break;
                case WINDOWS:
                    natives_OS = "natives-windows";
                    break;
                case MAC:
                    natives_OS = "natives-osx";
                    break;
            }

            String content = new Scanner(path.toFile()).useDelimiter("\\Z").next();
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            try {

                //TODO figure out wtf to do with this script
                String script_js = "var getJsonLibrariesDownloadsClassifiersNativesX=function(r,s){var a=r,e=JSON.parse(a),n=\"\",t=0;for(i=0;i<500;i++)try{n=n+e.libraries[t].downloads.classifiers[s].url+\"\\n\",t+=1}catch(o){t+=1}return n},getJsonLibrariesDownloadsClassifiersNativesY=function(r,s){var a=r,e=JSON.parse(a),n=\"\",t=0;for(i=0;i<500;i++)try{n=n+e.libraries[t].downloads.classifiers[s].path+\"\\n\",t+=1}catch(o){t+=1}return n},getJsonLibrariesDownloadsClassifiersNativesZ=function(r){var s=r,a=JSON.parse(s),e=\"\",n=0;for(i=0;i<500;i++)try{a.libraries[n].natives?(e=e+a.libraries[n].name+\"\\n\",n+=1):n+=1}catch(t){n+=1}return e};";

                File file = new File("./.script.js");
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(script_js);
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            engine.eval(new FileReader("./.script.js"));

            Invocable invocable = (Invocable) engine;

            Object result = invocable.invokeFunction("getJsonLibrariesDownloadsClassifiersNativesY", content, natives_OS);

            version_path_list_natives.addAll(Arrays.asList(result.toString().split("\n")));
        } catch (FileNotFoundException | ScriptException | NoSuchMethodException ex) {
            logger.warn("Failed to parse native library classifiers", ex);
        }
    }

    public void readJson_libraries_downloads_classifiers_natives_Z(Path path) {

        try {

            String content = new Scanner(path.toFile()).useDelimiter("\\Z").next();
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            try {

                String script_js = "var getJsonLibrariesDownloadsClassifiersNativesX=function(r,s){var a=r,e=JSON.parse(a),n=\"\",t=0;for(i=0;i<500;i++)try{n=n+e.libraries[t].downloads.classifiers[s].url+\"\\n\",t+=1}catch(o){t+=1}return n},getJsonLibrariesDownloadsClassifiersNativesY=function(r,s){var a=r,e=JSON.parse(a),n=\"\",t=0;for(i=0;i<500;i++)try{n=n+e.libraries[t].downloads.classifiers[s].path+\"\\n\",t+=1}catch(o){t+=1}return n},getJsonLibrariesDownloadsClassifiersNativesZ=function(r){var s=r,a=JSON.parse(s),e=\"\",n=0;for(i=0;i<500;i++)try{a.libraries[n].natives?(e=e+a.libraries[n].name+\"\\n\",n+=1):n+=1}catch(t){n+=1}return e};";

                File file = new File("./.script.js");
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(script_js);
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            engine.eval(new FileReader("./.script.js"));

            Invocable invocable = (Invocable) engine;

            Object result = invocable.invokeFunction("getJsonLibrariesDownloadsClassifiersNativesZ", content);

            version_name_list_natives.addAll(Arrays.asList(result.toString().split("\n")));
        } catch (FileNotFoundException | ScriptException | NoSuchMethodException ex) {
            logger.warn("Failed to parse native library classifiers", ex);
        }
    }

    public void readJson_objects_KEY(Path path) {
        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object jsonfile;

            jsonfile = readMCJSONFiles.parse(Files.newBufferedReader(path));

            JSONObject jsonObject = (JSONObject) jsonfile;
            JSONObject objects = (JSONObject) jsonObject.get("objects");

            Set fileCheckObjects = objects.keySet();
            for (Object fileCheckObject : fileCheckObjects) {
                String fileName = (String) fileCheckObject;
                objects_KEY.add(fileName);
            }
        } catch (IOException | ParseException ex) {
            logger.warn("Failed to parse JSON", ex);
        }

    }

    public void readJson_objects_KEY_hash(Path path) {
        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object jsonfile;

            jsonfile = readMCJSONFiles.parse(Files.newBufferedReader(path));

            JSONObject jsonObject = (JSONObject) jsonfile;
            JSONObject objects = (JSONObject) jsonObject.get("objects");

            Set fileCheckObjects = objects.keySet();
            for (Object fileCheckObject : fileCheckObjects) {
                String fileName = (String) fileCheckObject;

                JSONObject fileNameObject = (JSONObject) objects.get(fileName);
                String fileHash = (String) fileNameObject.get("hash");
                objects_hash.add(fileHash);
            }
        } catch (IOException | ParseException ex) {
            logger.warn("Failed to parse JSON", ex);
        }

    }

    public String readJson_assetIndex_url(Path path) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(Files.newBufferedReader(path));
            JSONObject structure = (JSONObject) jsonObject.get("assetIndex");
            return (String) (structure.get("url"));

        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse JSON", e);
        }
        return "N/A";
    }

    public String readJson_assetIndex_id(Path path) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(Files.newBufferedReader(path));
            JSONObject structure = (JSONObject) jsonObject.get("assetIndex");
            return (String) (structure.get("id"));

        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse JSON", e);
        }
        return "N/A";
    }

    public void generateVersionJsonPathList(Path path) {
        File root = path.toFile();
        String fileName = ".json";
        try {
            boolean recursive = true;

            Collection files = FileUtils.listFiles(root, null, recursive);

            for (Object file1 : files) {
                File file = (File) file1;
                if (file.getName().endsWith(fileName)) {
                    versions_json_path_list.add(file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to parse JSON", e);
        }
    }

    public void generateVersionList(Path path) {
        File root = path.toFile();
        String fileName = ".json";
        try {
            boolean recursive = true;

            Collection files = FileUtils.listFiles(root, null, recursive);

            for (Object file1 : files) {
                File file = (File) file1;
                if (file.getName().endsWith(fileName)) {
                    versions_list.add(file.getName().replace(".json", ""));
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to parse JSON", e);
        }
    }

    public String readJson_minecraftArguments_v2(Path path) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(Files.newBufferedReader(path));
            JSONObject jsonArgs = (JSONObject) jsonObject.get("arguments");
            String args = jsonArgs.get("game").toString();
            args = args.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", "").replaceAll("\"\"", " ").replaceAll("\"", "");
            String[] argsF = args.split("\\{rules");
            return (argsF[0]);

        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse JSON", e);
        }
        return "N/A";
    }

    public String readJson_minecraftArguments(Path path) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(Files.newBufferedReader(path));
            return (String) (jsonObject.get("minecraftArguments"));

        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse JSON", e);
        }
        return "N/A";
    }

    public String readJson_assets(Path path) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(Files.newBufferedReader(path));
            return (String) (jsonObject.get("assets"));

        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse JSON", e);
        }
        return "N/A";
    }

    public String readJson_id(Path path) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(Files.newBufferedReader(path));
            return (String) (jsonObject.get("id"));

        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse JSON", e);;
        }
        return "N/A";
    }

    public String readJson_mainClass(Path path) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(Files.newBufferedReader(path));
            return (String) (jsonObject.get("mainClass"));

        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse JSON", e);
        }
        return "N/A";
    }

    public String[] generateMinecraftArguments(String auth_player_name, String version_name, Path game_directory, Path assets_root, String assets_index_name, String auth_uuid, String auth_access_token, String user_properties, String user_type, String version_type, Path game_assets, String auth_session) {

        Local local = new Local();
        String cmdArgs = local.readJson_minecraftArguments(Utils.getMineCraft_Versions_X_X_json(version_name));
        if (cmdArgs == null) {
            //run v2
            cmdArgs = local.readJson_minecraftArguments_v2(Utils.getMineCraft_Versions_X_X_json(version_name));
        }

        //the arguments can start with -- or $
        cmdArgs = cmdArgs.replaceAll(" +", " ");
        //the above will change it to single space.
        //split it to String and move it to ArrayList
        String[] tempArgsSplit = cmdArgs.split(" ");
        for (int i = 0; i < tempArgsSplit.length; i++) {
            if (tempArgsSplit[i].equals("${auth_player_name}")) {
                tempArgsSplit[i] = auth_player_name;
            }
            if (tempArgsSplit[i].equals("${version_name}")) {
                tempArgsSplit[i] = version_name;
            }
            if (tempArgsSplit[i].equals("${game_directory}")) {
                tempArgsSplit[i] = game_directory.toString();
            }
            if (tempArgsSplit[i].equals("${assets_root}")) {
                tempArgsSplit[i] = assets_root.toString();
            }
            if (tempArgsSplit[i].equals("${assets_index_name}")) {
                tempArgsSplit[i] = assets_index_name;
            }
            if (tempArgsSplit[i].equals("${auth_uuid}")) {
                tempArgsSplit[i] = auth_uuid;
            }
            if (tempArgsSplit[i].equals("${auth_access_token}")) {
                tempArgsSplit[i] = auth_access_token;
            }
            if (tempArgsSplit[i].equals("${user_properties}")) {
                tempArgsSplit[i] = user_properties;
            }
            if (tempArgsSplit[i].equals("${user_type}")) {
                tempArgsSplit[i] = user_type;
            }
            if (tempArgsSplit[i].equals("${version_type}")) {
                tempArgsSplit[i] = version_type;
            }
            if (tempArgsSplit[i].equals("${game_assets}")) {
                tempArgsSplit[i] = game_assets.toString();
            }
            if (tempArgsSplit[i].equals("${auth_session}")) {
                tempArgsSplit[i] = auth_session;
            }
        }

        return tempArgsSplit;
    }

    public String generateLibrariesArguments() {
        StringBuilder cp = new StringBuilder();
        for (int i = 0; i < libraries_path.size(); i++) {
            if (i == libraries_path.size() - 1) {

                cp.append(libraries_path.get(i));

            } else {
                cp.append(libraries_path.get(i)).append(Utils.getArgsDiv());

            }
        }
        return cp.toString();
    }

    public String generateRunnableArguments(String Memory, String NativesDir, String FullLibraryArgument, String mainClass, String HalfArgument) {
        //unused function. Will be removed
        return ("-Xmx" + Memory + " -XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump -Djava.library.path=" + NativesDir + " -cp " + FullLibraryArgument + " " + mainClass + " " + HalfArgument);

    }

    // TODO why are these unused, this doesn't look to bad
    public String generateRunnableArguments(String Memory, String MinMemory, String NativesDir, String FullLibraryArgument, String mainClass, String HalfArgument) {
        //unused function. Will be removed
        return ("-Xms" + Memory + " -Xmx" + Memory + " -XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump -Djava.library.path=" + NativesDir + " -cp " + FullLibraryArgument + " " + mainClass + " " + HalfArgument);

    }

    public String generateLibrariesPath(String _name) {
        try {
            String fileName = _name;
            String[] colonSplit = fileName.split("\\:", 3);
            String[] folderSplit = colonSplit[0].split("\\.");

            String compileSplit = "";

            StringBuilder compileFolder = new StringBuilder();

            for (String aFolderSplit : folderSplit) {
                compileFolder.append(aFolderSplit).append("/");
            }
            compileSplit = compileFolder + "/" + colonSplit[1] + "/" + colonSplit[2] + "/" + colonSplit[1] + "-" + colonSplit[2] + ".jar";
            compileSplit = compileSplit.replace("//", "/");
            /*
                Downloading: https://libraries.minecraft.net/org/ow2/asm/asm-all/4.1/asm-all-4.1.jar
                org/ow2/asm/asm-all/4.1/asm-all-4.1.jar
                
             */
            //compileSplit = Utils.getMineCraftLibrariesLocation(_OS) + "/" + compileSplit;
            return (compileSplit);

        } catch (Exception e) {
            logger.warn("Failed to generate libraries path", e);
        }
        return "N/A";
    }

    public String generateNativesPath(String _name) {
        try {
            String natives_OS = null;
            switch(Utils.getOS()) {
                case LINUX:
                    natives_OS = "natives-linux";
                    break;
                case WINDOWS:
                    natives_OS = "natives-windows";
                    break;
                case MAC:
                    natives_OS = "natives-osx";
                    break;
            }

            String fileName = _name;
            String[] colonSplit = fileName.split("\\:", 3);
            String[] folderSplit = colonSplit[0].split("\\.");

            String compileSplit = "";

            StringBuilder compileFolder = new StringBuilder();

            for (int i = 0; i < folderSplit.length; i++) {
                compileFolder.append(folderSplit[i]).append("/");
            }

            compileSplit = compileFolder + "/" + colonSplit[1] + "/" + colonSplit[2] + "/" + colonSplit[1] + "-" + colonSplit[2] + "-" + natives_OS + ".jar";
            compileSplit = compileSplit.replace("//", "/");
            return (compileSplit);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    public Boolean checkIfVanillaMC(String version) {
        for (String aVersion_manifest_versions_id : version_manifest_versions_id) {
            if (aVersion_manifest_versions_id.equals(version)) {
                return true;
            }
        }
        //if nothing.. return false
        return false;
    }

    public void MOD_readJson_libraries_name_PLUS_url(Path path) {
        JSONParser readMCJSONFiles = new JSONParser();
        try {
            Object object = readMCJSONFiles.parse(Files.newBufferedReader(path));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray versions = (JSONArray) jsonObject.get("libraries");
            for (JSONObject version : (Iterable<JSONObject>) versions) {
                version_name_list.add((String) version.get("name"));
                if (version.get("url") == null) {
                    logger.error("Can't resolve url. Attempting to fix!");
                    HALF_URL_version_url_list.add(Network.minecraftLibrariesUrl);
                } else {
                    HALF_URL_version_url_list.add((String) version.get("url"));

                }
            }
        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse JSON", e);
        }
    }

    public String readJson_inheritsFrom(Path path) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(Files.newBufferedReader(path));
            return (String) (jsonObject.get("inheritsFrom"));

        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse JSON", e);
        }
        return "N/A";
    }

    public String readJson_jar(Path path) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(Files.newBufferedReader(path));
            return (String) (jsonObject.get("jar"));

        } catch (IOException | ParseException e) {
            logger.warn("Failed to parse JSON", e);
        }
        return "N/A";
    }
}
