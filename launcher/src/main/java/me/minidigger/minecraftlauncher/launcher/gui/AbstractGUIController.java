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

package me.minidigger.minecraftlauncher.launcher.gui;

import org.checkerframework.checker.nullness.qual.NonNull;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import me.minidigger.minecraftlauncher.api.LauncherAPI;
import me.minidigger.minecraftlauncher.api.events.LauncherEventHandler;
import me.minidigger.minecraftlauncher.launcher.Status;

public abstract class AbstractGUIController implements Initializable, LauncherEventHandler {

    protected LauncherAPI API;

    public AbstractGUIController(){
        API = new LauncherAPI();
        API.setEventHandler(this);
    }

    @Override
    public void onDownload(@NonNull Downloadable downloadable) {
        // TODO: direct log message passing is not supported
        Platform.runLater(() -> {
            switch(downloadable) {
                case ASSETS:
                    setStatus(Status.DOWNLOADING_GAME_ASSETS);
                    break;
                case LAUNCHER_META:
                    setStatus(Status.DOWNLOADING_LAUNCHER_META);
                    break;
                case LIBRARIES:
                    setStatus(Status.DOWNLOADING_LIBRARIES);
                    break;
                case MINECRAFT:
                    setStatus(Status.DOWNLOADING_MINECRAFT);
                    break;
                case NATIVES:
                    setStatus(Status.FINALIZING);
                    break;
            }
        });
    }

    public abstract void setStatus(Status finalizing);
}
