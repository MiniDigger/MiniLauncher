package me.minidigger.minecraftlauncher.launcher;

import me.minidigger.minecraftlauncher.api.LauncherAPI;

public enum Status {
    IDLE {
        @Override
        public String toString() {
            return "Idle";
        }
    },
    DOWNLOADING {
        @Override
        public String toString() {
            return "Checking & Downloading Files";
        }
    },
    DOWNLOADING_N {
        @Override
        public String toString() {
            return "Downloading File #";
        }
    },
    DOWNLOADING_M {
        @Override
        public String toString() {
            return "Downloading The Minecraft Client";
        }
    },
    DOWNLOADING_LM {
        @Override
        public String toString() {
            return "Downloading Version Data From Mojang";
        }
    },
    DOWNLOADING_L {
        @Override
        public String toString() {
            return "Downloading Required Libraries";
        }
    },
    DOWNLOAD_COMPLETE {
        @Override
        public String toString() {
            return "Downloading Completed";
        }
    },
    FINALIZING {
        @Override
        public String toString() {
            return "Finalizing The Installation";
        }
    },
    ERROR {
        @Override
        public String toString() {
            return "Error";
        }
    },
    CHECKING {
        @Override
        public String toString() {
            return "Checking Latest Versions Available";
        }
    },
    EXTRACTING {
        @Override
        public String toString() {
            return "Extracting & Installing Files";
        }
    },
}