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

package me.minidigger.minecraftlauncher.launcher.tasks;

import org.to2mbn.jmccc.mcdownloader.download.concurrent.CallbackAdapter;

import java.util.ResourceBundle;
import java.util.function.Consumer;

public class JMCCCCallback<R> extends CallbackAdapter<R> {
    private Consumer<String> statusConsumer;
    private ResourceBundle resourceBundle;

    public JMCCCCallback(Consumer<String> statusConsumer, ResourceBundle resourceBundle) {
        this.statusConsumer = statusConsumer;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public void done(R result) {
        statusConsumer.accept(resourceBundle.getString("status.done"));
    }

    @Override
    public void failed(Throwable e) {
        statusConsumer.accept(resourceBundle.getString("status.download_failed"));
    }

    @Override
    public void cancelled() {
        statusConsumer.accept(resourceBundle.getString("status.cancelled"));
    }

    @Override
    public void updateProgress(long done, long total) {
        statusConsumer.accept(String.format(resourceBundle.getString("status.progress"), done, total));
    }

    @Override
    public void retry(Throwable e, int current, int max) {
        statusConsumer.accept(String.format(resourceBundle.getString("status.retry"), current, max));
    }
}
