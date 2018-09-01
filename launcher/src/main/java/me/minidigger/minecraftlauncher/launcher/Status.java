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