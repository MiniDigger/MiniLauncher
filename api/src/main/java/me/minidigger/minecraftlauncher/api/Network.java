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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

/**
 * @author ammar
 */
class Network {
    private final static Logger logger = LoggerFactory.getLogger(Network.class);

    public final static String minecraftLibrariesUrl = "https://libraries.minecraft.net";
    public final static String minecraftAssetsUrl = "http://resources.download.minecraft.net";
    public final static String minecraftVersionsJsonUrl = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    public final static String minecraftProfilesUrl = "https://api.mojang.com/users/profiles/minecraft";

    private Network() {}

    public static void downloadProfile(String _username) {
        try {
            URL url = new URL(minecraftProfilesUrl + "/" + _username);
            File file = new File(Utils.getMinecraftDataDirectory() + "/" + _username + ".json");
            if (file.exists()) {
                //do not download..
                logger.debug("Profile for user {} already exists", _username);
            } else {
                FileUtils.copyURLToFile(url, file);
            }
        } catch (Exception e) {
            logger.warn("Failed to download profile for {}", _username, e);
        }
    }

    public static void downloadLibraries(String _url, String _path, Boolean ForceDownload) {
        try {
            URL url = new URL(_url);
            File file = new File(Utils.getMineCraftLibrariesLocation() + "/" + _path);
            if (ForceDownload) {
                FileUtils.copyURLToFile(url, file);
            } else if (file.exists()) {
                logger.debug("File Exists! - Skipping download");
            } else {
                FileUtils.copyURLToFile(url, file);
            }
        } catch (Exception e) {
            logger.warn("Failed to download libraries", e);
        }
    }

    public static void downloadAssetsObjects(String folder, String _hash) {
        //resources.download.minecraft.net/4b/4b90ff3a9b1486642bc0f15da0045d83a91df82e
        try {
            URL url = new URL(minecraftAssetsUrl + "/" + folder + "/" + _hash);
            File file = new File(Utils.getMineCraftAssetsObjectsLocation() + "/" + folder + "/" + _hash);
            if (file.exists() && Utils.getSHA_1(file.toString()).equals(_hash)) {
                //do not download..
                logger.debug("File Exists!");
                logger.debug("Hash Verified!");
            } else {
                //System.out.println("Calculated Hash:" + Utils.getSHA_1(file.toString()));
                FileUtils.copyURLToFile(url, file);
            }
        } catch (Exception e) {
            logger.warn("Failed to download assets", e);
        }
    }

    public static void downloadLaunchermeta(String _url, String version, Boolean ForceDownload) {
        try {
            URL url = new URL(_url);
            File file = Utils.getMineCraftAssetsIndexes_X_json(version).toFile();
            if (ForceDownload) {
                FileUtils.copyURLToFile(url, file);
            } else if (file.exists()) {
                //do not download..
                logger.debug("File Exists! - Skipping download");
            } else {
                FileUtils.copyURLToFile(url, file);
            }
        } catch (Exception e) {
            logger.warn("Failed to download launcher meta", e);
        }
    }

    public static void downloadMinecraftJar(String version, Boolean ForceDownload) {
        try {
            URL url = new URL(minecraftVersionsJsonUrl + "/" + version + "/" + version + ".jar");
            File file = Utils.getMineCraft_Versions_X_X_jar_Location(version).toFile();
            if (ForceDownload) {
                FileUtils.copyURLToFile(url, file);
            } else if (file.exists()) {
                //do not download..
                logger.debug("File Exists! - Skipping download");
            } else {
                FileUtils.copyURLToFile(url, file);
            }
        } catch (Exception e) {
            logger.warn("Failed to download Minecraft jar", e);
        }
    }

    public static void downloadVersionManifest(Path _filepath) {
        try {
            URL url = new URL(minecraftVersionsJsonUrl);
            File file = _filepath.toFile();
            FileUtils.copyURLToFile(url, file);
        } catch (Exception e) {
            logger.warn("Failed to download version manifest", e);
        }
    }

    public static void downloadVersionJson(String _url, String versionnumber) {
        try {
            URL url = new URL(_url);
            File file = Utils.getMineCraft_Versions_X_X_json(versionnumber).toFile();
            /*if (file.exists()){
                //do not download..
                System.out.println("File Exists!");
            } else {
                FileUtils.copyURLToFile(url, file);
            }*/
            FileUtils.copyURLToFile(url, file);

        } catch (Exception e) {
            logger.warn("Failed to download version json", e);
        }
    }
}
