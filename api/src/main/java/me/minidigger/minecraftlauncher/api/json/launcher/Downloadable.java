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

import java.net.URL;

/**
 * Generic downloadable info used in Launcher package JSON
 *
 * @author Mark Vainomaa
 */
public class Downloadable {
    @Json(name = "id")
    private String id;

    @Json(name = "sha1")
    private String sha1;

    @Json(name = "size")
    private long size;

    @Json(name = "url")
    private URL url;

    @Nullable
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

    @NonNull
    public URL getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Downloadable{" +
                "id='" + id + '\'' +
                ", sha1='" + sha1 + '\'' +
                ", size=" + size +
                ", url=" + url +
                '}';
    }
}
