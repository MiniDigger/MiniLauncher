package me.minidigger.minecraftlauncher.launcher;

public enum Status {
    CHECKING {
        @Override
        public String toString() {
            return "Checking Latest Versions Available";
        }
    },
    DOWNLOAD_COMPLETE {
        @Override
        public String toString() {
            return "Downloading Completed";
        }
    },
    DOWNLOADING_GAME_ASSETS {
        @Override
        public String toString() {
            return "Checking & Downloading Files";
        }
    },
    DOWNLOADING_LAUNCHER_META {
        @Override
        public String toString() {
            return "Downloading Version Data From Mojang";
        }
    },
    DOWNLOADING_LIBRARIES {
        @Override
        public String toString() {
            return "Downloading Required Libraries";
        }
    },
    DOWNLOADING_MINECRAFT {
        @Override
        public String toString() {
            return "Downloading The Minecraft Client";
        }
    },
    ERROR {
        @Override
        public String toString() {
            return "Error";
        }
    },
    EXTRACTING {
        @Override
        public String toString() {
            return "Extracting & Installing Files";
        }
    },
    FINALIZING {
        @Override
        public String toString() {
            return "Finalizing The Installation";
        }
    },
    IDLE {
        @Override
        public String toString() {
            return "Idle";
        }
    },
}