package me.minidigger.minecraftlauncher.api;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class JarPatcher {

    public void patch(Path jar, String classFile, String value, String replacement) {
        Map<String, String> jarProperties = new HashMap<>();
        jarProperties.put("create", "false");
        jarProperties.put("encoding", "UTF-8");

        URI zipFile = URI.create("jar:file:" + jar.toUri().getPath());

        try (FileSystem jarFS = FileSystems.newFileSystem(zipFile, jarProperties)) {
            Path rootPath = jarFS.getPath("/");
            Path classFilePath = rootPath.resolve(classFile).toAbsolutePath();

            // read
            byte[] content = Files.readAllBytes(classFilePath);
            // mod
            content = filter(content, value, replacement);
            // write
            Files.write(classFilePath, content, StandardOpenOption.WRITE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public byte[] filter(byte[] contentBytes, String value, String replacement) {
        byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        byte[] replacementBytes = replacement.getBytes(StandardCharsets.UTF_8);
        byte[] newContent = new byte[contentBytes.length - value.length() + replacement.length()];

        int valueIndex = 0;
        int startIndex = -1;

        for (int index = 0; index < contentBytes.length; index++) {
            byte curr = contentBytes[index];

            if (curr == valueBytes[valueIndex]) {
                if (valueIndex == 0) {
                    startIndex = index;
                } else if (valueIndex + 1 == valueBytes.length) {
                    break;
                }
                valueIndex++;
            } else {
                startIndex = -1;
                valueIndex = 0;
            }
        }

        if (startIndex == -1) {
            throw new RuntimeException("Could not patch file, could not find value to replace!");
        } else {
            System.arraycopy(contentBytes, 0, newContent, 0, startIndex);
            System.arraycopy(replacementBytes, 0, newContent, startIndex, replacementBytes.length);
            System.arraycopy(contentBytes, startIndex + valueBytes.length, newContent,
                    startIndex + replacementBytes.length, contentBytes.length - startIndex - replacementBytes.length);
        }

        return newContent;
    }

    public static void main(String[] args) {
//        new JarPatcher().filter(new byte[0], "", "");

        Path orig = Paths.get("C:\\Users\\Martin\\AppData\\Roaming\\.minecraft\\versions\\1.13\\1.13.jar");
        Path jar = Paths.get("C:\\Users\\Martin\\AppData\\Roaming\\.minecraft\\versions\\1.13\\1.13.mod.jar");

        try {
            Files.copy(orig, jar, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String classFile = "cfs.class";
        String value = "Minecraft 1.13";
        String replacement = "Minecraft 1.13 - Test!";
        new JarPatcher().patch(jar, classFile, value, replacement);
    }
}
