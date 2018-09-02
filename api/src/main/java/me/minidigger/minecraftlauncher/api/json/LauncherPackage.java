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

package me.minidigger.minecraftlauncher.api.json;

import com.google.gson.annotations.SerializedName;
import me.minidigger.minecraftlauncher.api.LauncherAPI;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.URL;
import java.util.List;

/**
 * Launcher package object
 *
 * @author Mark Vainomaa
 */
public class LauncherPackage {
    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private VersionManifest.VersionType type;

    @SerializedName("assets")
    private String assets;

    @SerializedName("minimumLauncherVersion")
    private int minimumLauncherVersion;

    @SerializedName("arguments")
    private Arguments arguments;

    public static class Arguments {
        @SerializedName("game")
        private List<Argument> game;

        @SerializedName("jvm")
        private List<Argument> jvm;
    }

    public static class Library {
        @SerializedName("name")
        private String name;

        @SerializedName("downloads")
        private List<Artifact> downloads;

        public static class Artifact {
            @SerializedName("path")
            private String path;

            @SerializedName("sha1")
            private String sha1;

            @SerializedName("size")
            private long size;

            @SerializedName("url")
            private URL url;
        }
    }

    public interface Argument {
        @NonNull
        List<String> getValue();

        /**
         * Checks if this game or JVM argument is allowed in this environment
         *
         * @param launcherAPI Instance of {@link LauncherAPI} where to get information about the environment
         * @return Whether this game or JVM argument is allowed in this environment
         */
        boolean isAllowed(@NonNull LauncherAPI launcherAPI);
    }
}
