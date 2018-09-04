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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Minecraft Launcher profile
 *
 * @author Mark Vainomaa
 */
public class LauncherProfile {
    /*
     * This is written against:
     * * name -> 1.6.89-j
     * * format -> 21
     * * profilesFormat -> 1
     *
     * Profile was generated using latest Mojang Java based launcher
     */
    @Json(name = "clientToken")
    private UUID clientToken;

    @Json(name = "selectedUser")
    private String selectedUser;

    @Json(name = "selectedProfile")
    private String selectedProfile;

    @Json(name = "profiles")
    private Map<String, LauncherProfile> profiles;

    @Json(name = "authenticationDatabase")
    private Map<String, AuthenticationDatabaseEntry> authenticationDatabase;

    @Json(name = "launcherVersion")
    private Version launcherVersion;

    @NonNull
    public UUID getClientToken() {
        return clientToken;
    }

    @NonNull
    public String getSelectedUser() {
        return selectedUser;
    }

    @NonNull
    public String getSelectedProfile() {
        return selectedProfile;
    }

    @NonNull
    public Map<String, LauncherProfile> getProfiles() {
        return profiles;
    }

    @NonNull
    public Map<String, AuthenticationDatabaseEntry> getAuthenticationDatabase() {
        return authenticationDatabase;
    }

    @NonNull
    public Version getLauncherVersion() {
        return launcherVersion;
    }

    public static class Version {
        @Json(name = "name")
        private String name;

        @Json(name = "format")
        private int format;

        @Json(name = "profilesFormat")
        private int profilesFormat;

        @NonNull
        public String getName() {
            return name;
        }

        public int getFormat() {
            return format;
        }

        public int getProfilesFormat() {
            return profilesFormat;
        }
    }

    public static class LaunchProfile {
        // Always present
        @Json(name = "name")
        private String name;

        // Absent if "Use Latest Version" is set
        @Json(name = "lastVersionId")
        private String lastVersionId;

        @Json(name = "gameDir")
        private String gameDir;

        @Json(name = "javaDir")
        private String javaDir;

        // Why not string list... :(
        @Json(name = "javaArgs")
        private String javaArgs;

        @Json(name = "allowedReleaseTypes")
        private Set<VersionType> allowedReleaseTypes;

        @Json(name = "launcherVisibilityOnGameClose")
        private LauncherVisibility launcherVisibilityOnGameClose;

        public enum LauncherVisibility {
            @Json(name = "close launcher when game starts")
            CLOSE_ON_GAME_START,

            @Json(name = "hide launcher and re-open when game closes")
            HIDE_UNTIL_GAME_CLOSE,

            @Json(name = "keep the launcher open")
            KEEP_OPEN,
        }

        @NonNull
        public String getName() {
            return name;
        }

        public void setName(@Nullable String name) {
            this.name = name;
        }

        @Nullable
        public String getLastVersionId() {
            return lastVersionId;
        }

        public void setLastVersionId(@Nullable String lastVersionId) {
            this.lastVersionId = lastVersionId;
        }

        @Nullable
        public String getGameDir() {
            return gameDir;
        }

        public void setGameDir(@Nullable String gameDir) {
            this.gameDir = gameDir;
        }

        @Nullable
        public String getJavaDir() {
            return javaDir;
        }

        public void setJavaDir(@Nullable String javaDir) {
            this.javaDir = javaDir;
        }

        @Nullable
        public String getJavaArgs() {
            return javaArgs;
        }

        public void setJavaArgs(@Nullable String javaArgs) {
            this.javaArgs = javaArgs;
        }

        @NonNull
        public Set<VersionType> getAllowedReleaseTypes() {
            return allowedReleaseTypes != null ? allowedReleaseTypes : Collections.singleton(VersionType.RELEASE);
        }

        @NonNull
        public LauncherVisibility getLauncherVisibilityOnGameClose() {
            return launcherVisibilityOnGameClose != null ? launcherVisibilityOnGameClose : LauncherVisibility.CLOSE_ON_GAME_START;
        }

        public void setLauncherVisibilityOnGameClose(@Nullable LauncherVisibility launcherVisibilityOnGameClose) {
            this.launcherVisibilityOnGameClose = launcherVisibilityOnGameClose;
        }
    }

    public static class AuthenticationDatabaseEntry {
        @Json(name = "displayName")
        private String displayName;

        @Json(name = "accessToken")
        private String accessToken;

        @Json(name = "userid")
        private String userid;

        @Json(name = "uuid")
        private UUID uuid;

        @Json(name = "username")
        private String username;

        @NonNull
        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(@NonNull String displayName) {
            this.displayName = displayName;
        }

        @Nullable
        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(@Nullable String accessToken) {
            this.accessToken = accessToken;
        }

        @NonNull
        public String getUserid() {
            return userid;
        }

        public void setUserid(@NonNull String userid) {
            this.userid = userid;
        }

        @NonNull
        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(@NonNull UUID uuid) {
            this.uuid = uuid;
        }

        @NonNull
        public String getUsername() {
            return username;
        }

        public void setUsername(@NonNull String username) {
            this.username = username;
        }
    }
}
