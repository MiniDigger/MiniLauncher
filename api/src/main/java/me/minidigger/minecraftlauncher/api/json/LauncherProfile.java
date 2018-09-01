package me.minidigger.minecraftlauncher.api.json;

import com.google.gson.annotations.SerializedName;
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
    @SerializedName("clientToken")
    private UUID clientToken;

    @SerializedName("selectedUser")
    private String selectedUser;

    @SerializedName("selectedProfile")
    private String selectedProfile;

    @SerializedName("profiles")
    private Map<String, LauncherProfile> profiles;

    @SerializedName("authenticationDatabase")
    private Map<String, AuthenticationDatabaseEntry> authenticationDatabase;

    @SerializedName("launcherVersion")
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
        @SerializedName("name")
        private String name;

        @SerializedName("format")
        private int format;

        @SerializedName("profilesFormat")
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
        @SerializedName("name")
        private String name;

        // Absent if "Use Latest Version" is set
        @SerializedName("lastVersionId")
        private String lastVersionId;

        @SerializedName("gameDir")
        private String gameDir;

        @SerializedName("javaDir")
        private String javaDir;

        // Why not string list... :(
        @SerializedName("javaArgs")
        private String javaArgs;

        @SerializedName("allowedReleaseTypes")
        private Set<VersionManifest.VersionType> allowedReleaseTypes;

        @SerializedName("launcherVisibilityOnGameClose")
        private LauncherVisibility launcherVisibilityOnGameClose;

        public enum LauncherVisibility {
            @SerializedName("close launcher when game starts")
            CLOSE_ON_GAME_START,

            @SerializedName("hide launcher and re-open when game closes")
            HIDE_UNTIL_GAME_CLOSE,

            @SerializedName("keep the launcher open")
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
        public Set<VersionManifest.VersionType> getAllowedReleaseTypes() {
            return allowedReleaseTypes != null ? allowedReleaseTypes : Collections.singleton(VersionManifest.VersionType.RELEASE);
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
        @SerializedName("displayName")
        private String displayName;

        @SerializedName("accessToken")
        private String accessToken;

        @SerializedName("userid")
        private String userid;

        @SerializedName("uuid")
        private UUID uuid;

        @SerializedName("username")
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
