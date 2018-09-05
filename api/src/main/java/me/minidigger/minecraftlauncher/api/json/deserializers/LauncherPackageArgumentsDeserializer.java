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

package me.minidigger.minecraftlauncher.api.json.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import me.minidigger.minecraftlauncher.api.json.launcher.LauncherPackage;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mark Vainomaa
 */
public class LauncherPackageArgumentsDeserializer implements JsonDeserializer<LauncherPackage.Arguments> {
    @Override
    public LauncherPackage.Arguments deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        LauncherPackage.Arguments args = new LauncherPackage.Arguments();

        List<LauncherPackage.Argument> gameArgs = Collections.emptyList();
        List<LauncherPackage.Argument> jvmArgs = Collections.emptyList();

        // Legacy string arguments?
        if(json.isJsonPrimitive()) {
            // Game arguments should be always present
            gameArgs = Arrays.stream(json.getAsString().split(" "))
                    .map(LauncherPackageArgumentListDeserializer.BasicArgument::new)
                    .collect(Collectors.toList());

            // No jvm args
        } else if(json.isJsonObject()) {
            JsonObject base = json.getAsJsonObject();

            // New advanced (and cancerous...) arguments instead :o
            if(base.has("game"))
                gameArgs = context.deserialize(base.get("game"), LauncherPackageArgumentListDeserializer.ARGUMENT_LIST_TYPE.getType());

            if(base.has("jvm"))
                jvmArgs = context.deserialize(base.get("jvm"), LauncherPackageArgumentListDeserializer.ARGUMENT_LIST_TYPE.getType());
        } else {
            throw new IllegalStateException("Invalid json type: " + json);
        }

        setFieldValue(args, "game", gameArgs);
        setFieldValue(args, "jvm", jvmArgs);

        return args;
    }

    private static void setFieldValue(@NonNull Object instance, @NonNull String fieldName, @Nullable Object value) {
        try {
            Field field = instance.getClass().getField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
