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

package me.minidigger.minecraftlauncher.api.platform;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * @author Mark Vainomaa
 */
public class EnvironmentInfo {
    private final static OperatingSystem currentOS;
    private final static String classpathArgsDivider;
    private final static Path minecraftDataDirectory;

    @NonNull
    public static Path getMinecraftDataDirectory() {
        if(minecraftDataDirectory == null)
            throw new IllegalStateException("Unsupported OS");

        return minecraftDataDirectory;
    }

    @NonNull
    public static Path getMinecraftServersList() {
        return getMinecraftDataDirectory().resolve("servers.dat");
    }

    @NonNull
    public static Path getMinecraftVersionsDirectory() {
        return getMinecraftDataDirectory().resolve("versions");
    }

    @NonNull
    public static Path getMinecraftLibrariesDirectory() {
        return getMinecraftDataDirectory().resolve("libraries");
    }

    @NonNull
    public static OperatingSystem getCurrentOperatingSystem() {
        return currentOS;
    }

    @NonNull
    public static String getClasspathArgsDivider() {
        if(classpathArgsDivider == null)
            throw new IllegalStateException("Unsupported OS");

        return classpathArgsDivider;
    }

    public enum OperatingSystem {
        LINUX,
        MAC,
        WINDOWS,
        UNSUPPORTED,
    }

    static {
        // Cache current OS and data directory
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

        if ((OS.contains("mac")) || (OS.contains("darwin"))) {
            currentOS = OperatingSystem.MAC;
            minecraftDataDirectory = Paths.get(System.getProperty("user.home") + "Library/Application Support/minecraft");
            classpathArgsDivider = ":";
        } else if (OS.contains("win")) {
            currentOS = OperatingSystem.WINDOWS;
            minecraftDataDirectory = Paths.get(System.getenv("APPDATA"),  ".minecraft");
            classpathArgsDivider = ";";
        } else if (OS.contains("nux")) {
            currentOS = OperatingSystem.LINUX;
            minecraftDataDirectory = Paths.get(System.getProperty("user.home"), ".minecraft");
            classpathArgsDivider = ":";
        } else {
            currentOS = OperatingSystem.UNSUPPORTED;
            minecraftDataDirectory = null;
            classpathArgsDivider = null;
        }
    }
}
