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
import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Minecraft Version Manifest object
 *
 * @author Mark Vainomaa
 */
public class VersionManifest {

    @SerializedName("latest")
    private LatestInfo latest;

    @SerializedName("versions")
    private List<VersionInfo> versions;

    /**
     * Gets latest release and snapshot versions
     *
     * @return Latest release and snapshot versions
     * @see LatestInfo
     */
    @NonNull
    public LatestInfo getLatest() {
        return latest;
    }

    /**
     * Gets downloadable versions
     *
     * @return Downloadable versions
     * @see VersionInfo
     */
    @NonNull
    public List<VersionInfo> getVersions() {
        return versions;
    }

    /**
     * Latest Minecraft version info in {@link VersionManifest}
     */
    public static class LatestInfo {
        @SerializedName("release")
        private String release;

        @SerializedName("snapshot")
        private String snapshot;

        @NonNull
        public String getRelease() {
            return release;
        }

        @NonNull
        public String getSnapshot() {
            return snapshot;
        }
    }

    /**
     * Minecraft Version info in {@link VersionManifest}
     */
    public static class VersionInfo implements Comparable<VersionInfo> {
        @SerializedName("id")
        private String id;

        @SerializedName("type")
        private VersionType type;

        @SerializedName("url")
        private URL url;

        @SerializedName("time")
        private Date time;

        @SerializedName("releaseTime")
        private Date releaseTime;

        /**
         * Gets Minecraft version represented by this {@link VersionInfo}
         *
         * @return Minecraft version
         */
        @NonNull
        public String getId() {
            return id;
        }

        /**
         * Gets released version type.
         *
         * @return Released version type
         */
        @NonNull
        public VersionType getType() {
            return type;
        }

        /**
         * Gets client asset index JSON url
         *
         * @return Client asset index JSON url
         */
        @NonNull
        public URL getUrl() {
            return url;
        }

        @Override
        public int compareTo(VersionInfo o) {
            return releaseTime.compareTo(o.releaseTime);
        }
    }

    /**
     * Minecraft release type
     */
    public enum VersionType {
        @SerializedName("old_alpha")
        OLD_ALPHA,

        @SerializedName("old_beta")
        OLD_BETA,

        @SerializedName("release")
        RELEASE(true),

        @SerializedName("snapshot")
        SNAPSHOT,
        ;

        private final boolean stable;

        VersionType(boolean stable) {
            this.stable = stable;
        }

        VersionType() {
            this.stable = false;
        }

        /**
         * Returns whether this version type is stable or not
         *
         * @return Whether this version type is stable or not
         */
        public boolean isStable() {
            return stable;
        }
    }
}
