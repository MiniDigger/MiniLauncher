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

import com.alemcode.HexEditor.HexEditor;
import com.minecraft.moonlake.nbt.NBTBase;
import com.minecraft.moonlake.nbt.NBTTagCompound;
import com.minecraft.moonlake.nbt.NBTUtil;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.zeroturnaround.zip.commons.FileUtils.copy;

/**
 * @author ammar
 */
class Utils {

    //String versions_linux = getMineCraftLocation("Linux") + "/versions";
    public String getMineCraftLocation(String OS) {
        switch (OS) {
            case "Windows":
                return (System.getenv("APPDATA") + "/.minecraft");
            case "Linux":
                return (System.getProperty("user.home") + "/.minecraft");
            case "Mac":
                return (System.getProperty("user.home") + "/Library/Application Support/minecraft");
            default:
                return "N/A";
        }
    }

    public String getMineCraft_APIMeta(String OS) {
        return (getMineCraftLocation(OS) + "/api_meta");
    }

    public String getMineCraft_ServersDat(String OS) {
        return (getMineCraftLocation(OS) + "/servers.dat");
    }

    public String getMineCraftVersionsLocation(String OS) {
        return (getMineCraftLocation(OS) + "/versions");
    }

    public String getMineCraftTmpLocation(String OS) {
        return (getMineCraftLocation(OS) + "/tmp");
    }

    public String getMineCraft_Launcherlogs_txt(String OS) {
        return (getMineCraftLocation(OS) + "/Launcherlogs.txt");
    }

    public String getMineCraftLibrariesLocation(String OS) {
        return (getMineCraftLocation(OS) + "/libraries");
    }

    public String getMineCraftLibrariesComMojangNettyLocation(String OS) {
        return (getMineCraftLibrariesLocation(OS) + "/com/mojang/netty");
    }

    public String getMineCraftTmpIoNettyBootstrapLocation(String OS) {
        return (getMineCraftTmpLocation(OS) + "/io/netty/bootstrap");
    }

    public String getMineCraftTmpIoNettyBootstrapBootstrap_class(String OS) {
        return (getMineCraftTmpIoNettyBootstrapLocation(OS) + "/Bootstrap.class");
    }

    public Map<String, String> getMineCraftLibrariesComMojangNetty_jar(String OS) {
        Map<String, String> results = new HashMap<>();

        Utils utils = new Utils();
        File[] directories = new File(getMineCraftLibrariesComMojangNettyLocation(OS)).listFiles(File::isDirectory);
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
    }

    public List<String> getMineCraftServerDatNBTIP(String OS) {
        Utils utils = new Utils();
        List<String> ip = new ArrayList<>();
        try {
            File file = new File(utils.getMineCraft_ServersDat(OS));
            NBTTagCompound root = NBTUtil.readFile(file, false);
            for (NBTBase server : root.getList("servers")) {
                if (server instanceof NBTTagCompound) {
                    NBTTagCompound serverNBT = (NBTTagCompound) server;
                    ip.add(serverNBT.getString("ip"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public List<String> getMineCraftServerDatNBTName(String OS) {
        Utils utils = new Utils();
        List<String> name = new ArrayList<>();
        try {
            File file = new File(utils.getMineCraft_ServersDat(OS));
            NBTTagCompound root = NBTUtil.readFile(file, false);
            for (NBTBase server : root.getList("servers")) {
                if (server instanceof NBTTagCompound) {
                    NBTTagCompound serverNBT = (NBTTagCompound) server;
                    name.add(serverNBT.getString("name"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void injectPatchy(String OS) {
        Utils utils = new Utils();
        Map<String, String> map = new HashMap<>(utils.getMineCraftLibrariesComMojangPatchy_jar(OS));

        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                File file_to_delete = new File(entry.getKey());
                if (entry.getValue().startsWith("mod_")) {
                    file_to_delete.delete();
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }


        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!entry.getValue().startsWith("mod_")) {
                System.out.println(entry.getKey());
                //time to extract all files inside.
                try {
                    utils.extractJarContent(utils.getMineCraftTmpLocation(OS), entry.getKey());
                    System.out.println("Jar extracted!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //file extracted! now to modify it...
                try {
                    String Path = getMineCraftTmpIoPatchyBootstrapBootstrap_class(OS);
                    HexEditor HexEditor = new HexEditor(Path);
                    System.out.println(HexEditor.file_hex_string);
                    HexEditor.replace("73657373696f6e7365727665722e6d6f6a616e672e636f6d", "73657373696f6e7365727665722e6d6f6b616e672e636f6d");
                    //sessionserver.mojang.com
                    //sessionserver.mokang.com    

                    HexEditor.save();
                    System.out.println("Class modified!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                /*
                //delete original file.
                try {
                    File file_to_delete = new File(entry.getKey());
                    file_to_delete.delete();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                */

                String mod_netty = entry.getKey().replace(entry.getValue(), "mod_" + entry.getValue());

                try {
                    File file_to_delete = new File(mod_netty);
                    file_to_delete.delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //file modified. now we compress it again.
                try {
                    System.out.println("1: " + getMineCraftTmpLocation(OS));
                    System.out.println("2: " + entry.getKey());
                    System.out.println("3: " + entry.getValue());
                    System.out.println("4: " + mod_netty);

                    compressJarContent(new File(getMineCraftTmpLocation(OS)), new File(mod_netty));
                    System.out.println("Compressed file to jar");
                } catch (Exception ex) {
                    System.out.println(ex);
                }

                //cleanup. delete tmp folder
                try {
                    FileUtils.deleteDirectory(new File(utils.getMineCraftTmpLocation(OS)));
                    System.out.println("Cleanup directory");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void injectNetty(String OS) {
        Utils utils = new Utils();
        Map<String, String> map = new HashMap<>(utils.getMineCraftLibrariesComMojangNetty_jar(OS));

        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                File file_to_delete = new File(entry.getKey());
                if (entry.getValue().startsWith("mod_")) {
                    file_to_delete.delete();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!entry.getValue().startsWith("mod_")) {
                System.out.println(entry.getKey());
                //time to extract all files inside.
                try {
                    utils.extractJarContent(utils.getMineCraftTmpLocation(OS), entry.getKey());
                    System.out.println("Jar extracted!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //file extracted! now to modify it...
                try {
                    String Path = getMineCraftTmpIoNettyBootstrapBootstrap_class(OS);
                    HexEditor HexEditor = new HexEditor(Path);
                    System.out.println(HexEditor.file_hex_string);
                    HexEditor.replace("73657373696f6e7365727665722e6d6f6a616e672e636f6d", "73657373696f6e7365727665722e6d6f6b616e672e636f6d");
                    //sessionserver.mojang.com
                    //sessionserver.mokang.com    

                    HexEditor.save();
                    System.out.println("Class modified!");
                } catch (Exception ex) {
                    System.out.println(ex);
                }

                /*
                //delete original file.
                try {
                    File file_to_delete = new File(entry.getKey());
                    file_to_delete.delete();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                */

                String mod_netty = entry.getKey().replace(entry.getValue(), "mod_" + entry.getValue());

                try {
                    File file_to_delete = new File(mod_netty);
                    file_to_delete.delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //file modified. now we compress it again.
                try {
                    System.out.println("1: " + getMineCraftTmpLocation(OS));
                    System.out.println("2: " + entry.getKey());
                    System.out.println("3: " + entry.getValue());
                    System.out.println("4: " + mod_netty);

                    compressJarContent(new File(getMineCraftTmpLocation(OS)), new File(mod_netty));
                    System.out.println("Compressed file to jar");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //cleanup. delete tmp folder
                try {
                    FileUtils.deleteDirectory(new File(utils.getMineCraftTmpLocation(OS)));
                    System.out.println("Cleanup directory");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void compressJarContent(File directory, File zipfile) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipfile);
        Closeable res = out;
        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name.endsWith("/") ? name : name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    } else {
                        zout.putNextEntry(new ZipEntry(name));
                        copy(kid, zout);
                        zout.closeEntry();
                    }
                }
            }
        } finally {
            res.close();
        }
    }

    public void extractJarContent(String destinationDir, String jarPath) throws IOException {
        File file = new File(jarPath);
        JarFile jar = new JarFile(file);

        // fist get all directories,
        // then make those directory on the destination Path
        for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements(); ) {
            JarEntry entry = enums.nextElement();

            String fileName = destinationDir + File.separator + entry.getName();
            File f = new File(fileName);

            if (fileName.endsWith("/")) {
                f.mkdirs();
            }

        }

        //now create all files
        for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements(); ) {
            JarEntry entry = enums.nextElement();

            String fileName = destinationDir + File.separator + entry.getName();
            File f = new File(fileName);

            if (!fileName.endsWith("/")) {
                InputStream is = jar.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(f);

                // write contents of 'is' to 'fos'
                while (is.available() > 0) {
                    fos.write(is.read());
                }

                fos.close();
                is.close();
            }
        }
    }

    public String getMineCraftLibrariesComMojangPatchyLocation(String OS) {
        return (getMineCraftLibrariesLocation(OS) + "/com/mojang/patchy");
    }

    public String getMineCraftTmpIoPatchyBootstrapBootstrap_class(String OS) {
        return (getMineCraftTmpIoNettyBootstrapLocation(OS) + "/Bootstrap.class");
    }

    public Map<String, String> getMineCraftLibrariesComMojangPatchy_jar(String OS) {
        Map<String, String> results = new HashMap<>();

        Utils utils = new Utils();
        File[] directories = new File(getMineCraftLibrariesComMojangPatchyLocation(OS)).listFiles(File::isDirectory);
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
    }


    public String getMineCraft_Version_Manifest_json(String OS) {
        return (getMineCraftLocation(OS) + "/version_manifest.json");

    }

    public String getMineCraft_Launcher_Profiles_json(String OS) {
        return (getMineCraftLocation(OS) + "/launcher_profiles.json");
    }

    public String getMineCraft_Version_Json(String OS, String VersionNumber) {
        return (getMineCraftVersionsLocation(OS) + "/" + VersionNumber + "/" + VersionNumber + ".json");

    }

    public String getMineCraft_Versions_X_X_json(String OS, String VersionNumber) {
        return (getMineCraftVersionsLocation(OS) + "/" + VersionNumber + "/" + VersionNumber + ".json");

    }

    public String getMineCraft_Versions_X_X_jar(String OS, String VersionNumber) {
        return (getMineCraftVersionsLocation(OS) + "/" + VersionNumber + "/" + VersionNumber + ".jar");

    }

    public String getMineCraft_Versions_X_X_jar_Location(String OS, String VersionNumber) {
        return (getMineCraftVersionsLocation(OS) + "/" + VersionNumber + "/" + VersionNumber + ".jar");

    }

    public String getMineCraftAssetsRootLocation(String OS) {
        return (getMineCraftLocation(OS) + "/assets");

    }

    public String getMineCraft_Versions_X_Natives_Location(String OS, String VersionNumber) {
        return (getMineCraftVersionsLocation(OS) + "/" + VersionNumber + "/natives");

    }

    public String getMineCraft_Versions_X_Natives(String OS, String VersionNumber) {
        return (getMineCraftVersionsLocation(OS) + "/" + VersionNumber + "/natives");

    }

    public String getMineCraftAssetsIndexes_X_json(String OS, String VersionNumber) {

        return (getMineCraftAssetsIndexesLocation(OS) + "/" + VersionNumber + ".json");
    }

    public String getMineCraft_X_json(String OS, String Username) {

        return (getMineCraftLocation(OS) + "/" + Username + ".json");

    }

    public String getMineCraftAssetsIndexesLocation(String OS) {
        return (getMineCraftAssetsLocation(OS) + "/indexes");

    }

    public String getMineCraftAssetsLocation(String OS) {
        return (getMineCraftLocation(OS) + "/assets");

    }

    public String getMineCraftAssetsObjectsLocation(String OS) {
        return (getMineCraftAssetsLocation(OS) + "/objects");
    }

    public String setMineCraft_Versions_X_NativesLocation(String OS, String _path) {
        Utils utils = new Utils();
        return (utils.getMineCraftLibrariesLocation(OS) + "/" + _path);

    }

    public String setMineCraft_librariesLocation(String OS, String _path) {
        Utils utils = new Utils();
        return (utils.getMineCraftLibrariesLocation(OS) + "/" + _path);
    }

    public String getArgsDiv(String OS) {
        if (OS.equals("Windows")) {
            return (";");
        }
        if (OS.equals("Linux")) {
            return (":");
        }
        if (OS.equals("Mac")) {
            return (":");
        }

        return "N/A";
    }

    @SuppressWarnings("empty-statement")
    public String getSHA_1(String _path) {

        try {

            MessageDigest md = MessageDigest.getInstance("SHA1");
            FileInputStream fis = new FileInputStream(_path);
            byte[] dataBytes = new byte[1024];

            int nread;

            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            ;

            byte[] mdbytes = md.digest();

            //convert the byte to hex format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            return (sb.toString());

        } catch (NoSuchAlgorithmException | IOException ex) {
            System.out.println(ex);
            return "N/A";
        }

    }

    public void jarExtract(String OS, String _jarFile, String destDir) {
        try {
            Utils utils = new Utils();
            _jarFile = utils.setMineCraft_Versions_X_NativesLocation(OS, _jarFile);
            //_jarFile = _jarFile.replace("https://libraries.minecraft.net", "/home/ammar/NetBeansProjects/TagAPI_3/testx/libraries");
            File dir = new File(destDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File jarFile = new File(_jarFile);

            java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile);
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
            System.out.println(e);
        }
    }

    public String getMineCraftAssetsVirtualLocation(String OS) {
        return (getMineCraftAssetsLocation(OS) + "/virtual");
    }

    public String getMineCraftAssetsVirtualLegacyLocation(String OS) {
        return (getMineCraftAssetsVirtualLocation(OS) + "/legacy");
    }

    public void copyToVirtual(String OS, String folder, String _hash, String virtualfolder) {
        try {
            Utils utils = new Utils();
            File source = new File(utils.getMineCraftAssetsObjectsLocation(OS) + "/" + folder + "/" + _hash);
            File dest = new File(utils.getMineCraftAssetsVirtualLegacyLocation(OS) + "/" + virtualfolder.replaceFirst("minecraft/", ""));
            //Files.copy(source.toPath(), dest.toPath());
            FileUtils.copyFile(source, dest);

        } catch (Exception ex) {
            System.out.println("File Exists! " + ex.getMessage());
        }

    }

    public String getOS() {
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if ((OS.contains("mac")) || (OS.contains("darwin"))) {
            return ("Mac");
        } else if (OS.contains("win")) {
            return ("Windows");
        } else if (OS.contains("nux")) {
            return ("Linux");
        } else {
            //bring support to other OS.
            //we will assume that the OS is based on linux.
            return ("Linux");
        }
    }

    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }

    public List<String> removeDuplicates(List<String> list) {

        // Store unique items in result.
        List<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        Set<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

    public void writeLogs(String OS, ArrayList list) {
        try {
            Utils utils = new Utils();
            //get the entire list and append it to string

            File file = new File(utils.getMineCraft_Launcherlogs_txt(OS));

            //recreate the file no matter what
            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuilder content = new StringBuilder("Logs: \n");
            for (Object item : list) {
                content.append(item).append("\n");
            }

            bw.write(content.toString());
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
