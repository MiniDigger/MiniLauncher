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

package me.minidigger.minecraftlauncher.api.events;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An interface to handle launch events
 *
 * @author Mark Vainomaa
 */
public interface LauncherEventHandler {
    /**
     * This method is invoked when {@link me.minidigger.minecraftlauncher.api.LauncherAPI} is downloading something
     *
     * @param downloadable {@link Downloadable}
     */
    default void onDownload(@NonNull Downloadable downloadable) {
    }

    /**
     * This method is invoked when download is complete
     */
    default void onDownloadComplete() {
    }

    /**
     * This method is invoked when the game is starting
     * @param status the current status
     */
    default void onGameStart(@NonNull StartStatus status) {
    }

    /**
     * This method is invoked when the game has started
     */
    default void onGameStarted() {
    }

    /**
     * This method is invoked when the game couldn't start
     */
    default void onGameCorrupted(){
    }

    /**
     * Downloadable items
     */
    enum Downloadable {
        ASSETS,
        LAUNCHER_META,
        LIBRARIES,
        MINECRAFT,
        NATIVES,
    }

    enum StartStatus {
        VALIDATING,
        DOWNLOADING_NATIVES,
        PATCHING_NETTY,
        STARTING,
    }
}
