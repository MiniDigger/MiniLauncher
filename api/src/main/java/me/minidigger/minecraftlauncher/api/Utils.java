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

import me.minidigger.minecraftlauncher.api.platform.EnvironmentInfo;
import net.kyori.nbt.CompoundTag;
import net.kyori.nbt.ListTag;
import net.kyori.nbt.TagIO;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ammar
 */
class Utils {
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);
    private final static SecureRandom random = new SecureRandom();

    private Utils() {}

    @Deprecated
    public static Path getMinecraftDataDirectory() {
        return EnvironmentInfo.getMinecraftDataDirectory();
    }

    @Deprecated
    public static Path getMinecraftServersList() {
        return EnvironmentInfo.getMinecraftServersList();
    }

    @Deprecated
    public static Path getMineCraftVersionsLocation() {
        return EnvironmentInfo.getMinecraftVersionsDirectory();
    }

    @Deprecated
    public static Path getMineCraftLibrariesLocation() {
        return EnvironmentInfo.getMinecraftLibrariesDirectory();
    }

    @NonNull
    public static List<ServerListEntry> getMinecraftClientServerList() {
        Path serversFile = getMinecraftServersList();

        if(Files.notExists(serversFile))
            return Collections.emptyList();

        CompoundTag tag;
        try {
            tag = TagIO.readPath(serversFile);
        } catch (IOException e) {
            logger.error("Failed to read {}", serversFile, e);
            return Collections.emptyList();
        }

        List<ServerListEntry> servers = new ArrayList<>();
        ListTag serversList = tag.getList("servers");
        serversList.stream()
                .filter(CompoundTag.class::isInstance)
                .map(CompoundTag.class::cast)
                .map(ServerListEntry::fromNbt)
                .forEach(servers::add);

        return servers;
    }

    public static void setMinecraftClientServerList(@NonNull List<ServerListEntry> servers) {
        Path serversFile = getMinecraftServersList();

        CompoundTag tag = new CompoundTag();
        ListTag serversTag = new ListTag();

        servers.stream().map(ServerListEntry::toNbt).forEach(serversTag::add);

        tag.put("servers", serversTag);

        try {
            TagIO.writePath(tag, serversFile);
        } catch (IOException e) {
            logger.error("Failed to write {}", serversFile, e);
        }
    }

    public static void injectPatchy() {

    }

    public static void injectNetty() {

    }

    public static Path getMineCraftLibrariesComMojangPatchyLocation() {
        return getMineCraftLibrariesLocation().resolve("com/mojang/patchy");
    }

    /*public static Path getMineCraftTmpIoPatchyBootstrapBootstrap_class() {
        return (getMineCraftTmpIoNettyBootstrapLocation().resolve("Bootstrap.class");
    }

    public static Map<String, String> getMineCraftLibrariesComMojangPatchy_jar() {
        Map<String, String> results = new HashMap<>();

        File[] directories = new File(getMineCraftLibrariesComMojangPatchyLocation()).listFiles(File::isDirectory);
        for (File en : directories) {
            //check if file exists.
            File[] files = new File(en.toString()).listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    if (file.toString().endsWith(".jar")) {
                        results.put(file.getPath(), file.getName());
                    }
                }
            }
        }

        return (results);
    }*/


    public static Path getMineCraft_Version_Manifest_json() {
        return getMinecraftDataDirectory().resolve("version_manifest.json");

    }

    public static Path getMineCraft_Launcher_Profiles_json() {
        return getMinecraftDataDirectory().resolve("launcher_profiles.json");
    }

    public static Path getMineCraft_Version_Json(String VersionNumber) {
        return getMineCraftVersionsLocation().resolve("" + VersionNumber + "/" + VersionNumber + ".json");
    }

    public static Path getMineCraft_Versions_X_X_json(String VersionNumber) {
        return getMineCraftVersionsLocation().resolve("" + VersionNumber + "/" + VersionNumber + ".json");
    }

    public static Path getMineCraft_Versions_X_X_jar(String VersionNumber) {
        return getMineCraftVersionsLocation().resolve("" + VersionNumber + "/" + VersionNumber + ".jar");
    }

    public static Path getMineCraft_Versions_X_X_jar_Location(String VersionNumber) {
        return getMineCraftVersionsLocation().resolve("" + VersionNumber + "/" + VersionNumber + ".jar");
    }

    public static Path getMineCraftAssetsRootLocation() {
        return getMinecraftDataDirectory().resolve("assets");
    }

    public static Path getMineCraft_Versions_X_Natives_Location(String VersionNumber) {
        return getMineCraftVersionsLocation().resolve("" + VersionNumber + "/natives");
    }

    public static Path getMineCraft_Versions_X_Natives(String VersionNumber) {
        return getMineCraftVersionsLocation().resolve("" + VersionNumber + "/natives");
    }

    public static Path getMineCraftAssetsIndexes_X_json(String VersionNumber) {
        return getMineCraftAssetsIndexesLocation().resolve("" + VersionNumber + ".json");
    }

    public static Path getMineCraft_X_json(String Username) {
        return getMinecraftDataDirectory().resolve("" + Username + ".json");
    }

    public static Path getMineCraftAssetsIndexesLocation() {
        return getMineCraftAssetsLocation().resolve("indexes");
    }

    public static Path getMineCraftAssetsLocation() {
        return getMinecraftDataDirectory().resolve("assets");
    }

    public static Path getMineCraftAssetsObjectsLocation() {
        return getMineCraftAssetsLocation().resolve("objects");
    }

    public static Path setMineCraft_Versions_X_NativesLocation(String _path) {
        return getMineCraftLibrariesLocation().resolve(_path);
    }

    public static Path setMineCraft_librariesLocation(String _path) {
        return getMineCraftLibrariesLocation().resolve("" + _path);
    }


    public static String getArgsDiv() {
        return EnvironmentInfo.getClasspathArgsDivider();
    }

    @Nullable
    public static String getSha1Sum(@NonNull Path path) {
        try {
            InputStream fis = Files.newInputStream(path, StandardOpenOption.READ);
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] dataBytes = new byte[1024];

            int nread;
            while((nread = fis.read(dataBytes)) != -1)
                md.update(dataBytes, 0, nread);

            byte[] digestBytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for(byte b : digestBytes)
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

            return sb.toString();

        } catch (NoSuchAlgorithmException | IOException ex) {
            logger.error("Failed to get SHA1 {}", path, ex);
            return null;
        }
    }

    public static void jarExtract(Path _jarFile, Path destDir) {
        try {
            _jarFile = setMineCraft_Versions_X_NativesLocation(_jarFile.toString());
            //_jarFile = _jarFile.replace("https://libraries.minecraft.net", "/home/ammar/NetBeansProjects/TagAPI_3/testx/libraries");
            if (Files.notExists(destDir)) {
                Files.createDirectories(destDir);
            }

            java.util.jar.JarFile jar = new java.util.jar.JarFile(_jarFile.toFile());
            java.util.Enumeration enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
                java.io.File f = new java.io.File(destDir + java.io.File.separator + file.getName());
                if (file.isDirectory()) { // if its a directory, create it
                    f.mkdirs();
                    continue;
                }
                if (!f.exists()) {
                    java.io.InputStream is = jar.getInputStream(file); // get the input stream
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                    while (is.available() > 0) {  // write contents of 'is' to 'fos'
                        fos.write(is.read());
                    }
                    fos.close();
                    is.close();

                }
            }
        } catch (Exception e) {
            logger.warn("Failed to extract JAR {}", _jarFile, e);
        }
    }

    public static Path getMineCraftAssetsVirtualLocation() {
        return getMineCraftAssetsLocation().resolve("virtual");
    }

    public static Path getMineCraftAssetsVirtualLegacyLocation() {
        return getMineCraftAssetsVirtualLocation().resolve("legacy");
    }

    public static void copyToVirtual(String folder, String sha1hash, String virtualfolder) {
        try {
            Path source = getMineCraftAssetsObjectsLocation().resolve(folder + "/" + sha1hash);
            Path dest = getMineCraftAssetsVirtualLegacyLocation().resolve(virtualfolder.replaceFirst("minecraft/", ""));
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            logger.warn("Failed to copy", ex);
        }

    }

    public static EnvironmentInfo.OperatingSystem getOS() {
        return EnvironmentInfo.getCurrentOperatingSystem();
    }

    public static String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}
