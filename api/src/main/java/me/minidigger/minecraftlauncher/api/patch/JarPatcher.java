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

package me.minidigger.minecraftlauncher.api.patch;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JarPatcher {

    private Map<String, String> classFileMap = new HashMap<>();
    private Map<String, String> titleMap = new HashMap<>();

    public JarPatcher() {
        classFileMap.put("1.13", "cfs.class");
        titleMap.put("1.13", "Minecraft 1.13");
    }

    public static void main(String[] args) {
        Path jar = Paths.get("C:\\Users\\Martin\\AppData\\Roaming\\.minecraft\\versions\\1.13\\1.13.jar");
        new JarPatcher().patchWindowTitle(jar, "1.13", "This is so cool");
    }


    public void patchWindowTitle(Path jar, String versionName, String replacement) {
        if (!classFileMap.containsKey(versionName) || !titleMap.containsKey(versionName)) {
            throw new RuntimeException("Can't patch window title: " + versionName + " is not supported");
        }

        patch(jar, classFileMap.get(versionName), (cw) -> new WindowTitleTransformer(cw, titleMap.get(versionName), replacement));
    }

    private void patch(Path jar, String classFile, Function<ClassWriter, ClassVisitor> transformerSupplier) {
        Map<String, String> jarProperties = new HashMap<>();
        jarProperties.put("create", "false");
        jarProperties.put("encoding", "UTF-8");

        URI jarFile = URI.create("jar:file:" + jar.toUri().getPath());
        try (FileSystem jarFS = FileSystems.newFileSystem(jarFile, jarProperties)) {
            Path rootPath = jarFS.getPath("/");
            Path classFilePath = rootPath.resolve(classFile).toAbsolutePath();

            byte[] content = applyTransformer(Files.newInputStream(classFilePath), transformerSupplier);

            Files.write(classFilePath, content, StandardOpenOption.WRITE);

            // cleanup jar
            Path sf = rootPath.resolve("META-INF").resolve("MOJANGCS.SF");
            Path rsa = rootPath.resolve("META-INF").resolve("MOJANGCS.RSA");
            Files.deleteIfExists(sf);
            Files.deleteIfExists(rsa);

            Path manifest = rootPath.resolve("META-INF").resolve("MANIFEST.MF");
            BufferedWriter writer = Files.newBufferedWriter(manifest, StandardOpenOption.WRITE);
            writer.write("Manifest-Version: 1.0\n" +
                    "Main-Class: net.minecraft.client.Main");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private byte[] applyTransformer(InputStream in, Function<ClassWriter, ClassVisitor> transformerSupplier) throws IOException {
        final ClassReader classReader = new ClassReader(in);
        final ClassWriter cw = new ClassWriter(classReader, 0);
        classReader.accept(transformerSupplier.apply(cw), ClassReader.EXPAND_FRAMES);
        in.close();
        return cw.toByteArray();
    }
}
