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

package me.minidigger.minecraftlauncher.api;

import net.kyori.nbt.CompoundTag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Minecraft multiplayer server list entry
 *
 * @author Mark Vainomaa
 */
public class ServerListEntry {
    private String icon;
    private String name;
    private String ip;

    private ServerListEntry(@Nullable String icon, @NonNull String name, @NonNull String ip) {
        this.icon = icon;
        this.name = name;
        this.ip = ip;
    }

    /**
     * Gets base64 encoded server icon in PNG format. Can be absent.
     *
     * Note: Size must be 64x64
     *
     * @return Base64 encoded server icon
     */
    @Nullable
    public String getIcon() {
        return icon;
    }

    /**
     * Gets server name in server list
     *
     * @return Server name
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Gets server IP address
     *
     * @return Server IP address
     */
    @NonNull
    public String getIp() {
        return ip;
    }

    /**
     * Converts {@link ServerListEntry} to {@link CompoundTag} for use in
     * server list file
     *
     * @return {@link CompoundTag}
     */
    @NonNull
    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        if(icon != null) tag.putString("icon", icon);
        tag.putString("name", name);
        tag.putString("ip", ip);

        return tag;
    }

    /**
     * Converts {@link CompoundTag} with valid server info to {@link ServerListEntry}
     *
     * @param nbtTag {@link CompoundTag} where information should be read
     * @return Instance of {@link ServerListEntry} containing passed {@link CompoundTag} data
     */
    public static ServerListEntry fromNbt(@NonNull CompoundTag nbtTag) {
        String icon = nbtTag.getString("icon");
        return new ServerListEntry(
                icon.isEmpty() ? null : icon,
                nbtTag.getString("name"),
                nbtTag.getString("ip")
        );
    }

    /**
     * Creates a new {@link ServerListEntry}
     *
     * @param icon base64 encoded PNG data
     * @param name Server name
     * @param ip Server IP
     * @return Instance of {@link ServerListEntry}
     */
    public static ServerListEntry of(@Nullable String icon, @NonNull String name, @NonNull String ip) {
        return new ServerListEntry(icon, name, ip);
    }
}
