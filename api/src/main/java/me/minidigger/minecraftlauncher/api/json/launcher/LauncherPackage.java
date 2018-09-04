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

package me.minidigger.minecraftlauncher.api.json.launcher;

import com.squareup.moshi.Json;
import me.minidigger.minecraftlauncher.api.LauncherAPI;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Launcher package object
 *
 * @author Mark Vainomaa
 */
public class LauncherPackage {
    @Json(name = "id")
    private String id;

    @Json(name = "type")
    private VersionType type;

    @Json(name = "assets")
    private String assets;

    @Json(name = "assetIndex")
    private AssetIndexInfo assetIndex;

    @Json(name = "downloads")
    private Map<String, Downloadable> downloads;

    @Json(name = "libraries")
    private List<Library> libraries;

    @Json(name = "minimumLauncherVersion")
    private int minimumLauncherVersion;

    @Json(name = "arguments")
    private Arguments arguments;

    @Json(name = "minecraftArguments")
    private String minecraftArguments;

    @Json(name = "logging")
    private Map<String, Logging> logging;

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public VersionType getType() {
        return type;
    }

    @NonNull
    public String getAssets() {
        return assets;
    }

    @NonNull
    public AssetIndexInfo getAssetIndex() {
        return assetIndex;
    }

    @NonNull
    public Map<String, Downloadable> getDownloads() {
        return downloads;
    }

    @NonNull
    public List<Library> getLibraries() {
        return libraries;
    }

    public int getMinimumLauncherVersion() {
        return minimumLauncherVersion;
    }

    // If this isn't present, then minecraftArguments is
    @Nullable
    public Arguments getArguments() {
        return arguments;
    }

    @Nullable
    public List<String> getMinecraftArguments() {
        return minecraftArguments != null ? Arrays.asList(minecraftArguments.split(" ")) : null;
    }

    // Not present in older versions (TODO: which?)
    @Nullable
    public Map<String, Logging> getLogging() {
        return logging;
    }

    public static class Arguments {
        @Json(name = "game")
        private List<Argument> game;

        @Json(name = "jvm")
        private List<Argument> jvm;

        @NonNull
        public List<Argument> getGame() {
            return game;
        }

        @NonNull
        public List<Argument> getJvm() {
            return jvm;
        }
    }

    public static class AssetIndexInfo {
        @Json(name = "id")
        private String id;

        @Json(name = "sha1")
        private String sha1;

        @Json(name = "size")
        private long size;

        @Json(name = "totalSize")
        private long totalSize;

        @Json(name = "url")
        private URL url;

        @NonNull
        public String getId() {
            return id;
        }

        @NonNull
        public String getSha1() {
            return sha1;
        }

        public long getSize() {
            return size;
        }

        public long getTotalSize() {
            return totalSize;
        }

        @NonNull
        public URL getUrl() {
            return url;
        }
    }

    public static class Library {
        @Json(name = "name")
        private String name;

        @Json(name = "downloads")
        private Downloads downloads;

        @Json(name = "extract")
        private ExtractGuide extract;

        @Json(name = "natives")
        private Map<String, String> natives;

        @Json(name = "rules")
        private RulesContainer rules;

        @NonNull
        public String getName() {
            return name;
        }

        @NonNull
        public Downloads getDownloads() {
            return downloads;
        }

        @Nullable
        public ExtractGuide getExtract() {
            return extract;
        }

        @Nullable
        public Map<String, String> getNatives() {
            return natives;
        }

        @Nullable
        public RulesContainer getRules() {
            return rules;
        }

        public static class Downloads {
            @Json(name = "artifact")
            private Artifact artifact;

            @Json(name = "classifiers")
            private Map<String, Downloadable> classifiers;

            @NonNull
            public Artifact getArtifact() {
                return artifact;
            }

            @Nullable
            public Map<String, Downloadable> getClassifiers() {
                return classifiers;
            }
        }

        public static class Artifact {
            @Json(name = "path")
            private String path;

            @Json(name = "sha1")
            private String sha1;

            @Json(name = "size")
            private long size;

            @Json(name = "url")
            private URL url;

            @NonNull
            public String getPath() {
                return path;
            }

            @NonNull
            public String getSha1() {
                return sha1;
            }

            public long getSize() {
                return size;
            }

            @NonNull
            public URL getUrl() {
                return url;
            }
        }

        public static class ExtractGuide {
            @Json(name = "exclude")
            private List<String> exclude;

            @Nullable
            public List<String> getExclude() {
                return exclude;
            }
        }
    }

    public static class Logging {
        @Json(name = "argument")
        private String argument;

        @Json(name = "file")
        private Downloadable file;

        @Json(name = "type")
        private String type;

        @NonNull
        public String getArgument() {
            return argument;
        }

        @NonNull
        public Downloadable getFile() {
            return file;
        }

        @NonNull
        public String getType() {
            return type;
        }
    }

    public interface Argument {
        /**
         * Gets the argument value which should be appended to Minecraft game or JVM
         *
         * @return Argument value
         */
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
