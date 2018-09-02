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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import me.minidigger.minecraftlauncher.api.LauncherAPI;
import me.minidigger.minecraftlauncher.api.json.LauncherPackage;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mark Vainomaa
 */
public class LauncherPackageArgumentsDeserializer implements JsonDeserializer<List<LauncherPackage.Argument>> {
    @Override
    public List<LauncherPackage.Argument> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<LauncherPackage.Argument> collectedArguments = new ArrayList<>();
        JsonArray base = json.getAsJsonArray();

        for(JsonElement element : base) {
            if(element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                JsonArray rules = object.getAsJsonArray("rules");
                JsonElement value = object.get("value");

                // Deal with rules
                Map<String, String> conditions = new HashMap<>();
                for(JsonElement ruleElement : rules) {
                    JsonObject rule = ruleElement.getAsJsonObject();

                    // TODO: no idea what to do with this
                    // But I think that code behaves as this == "allowed"
                    //String action = rule.get("action").getAsString();

                    JsonObject features;
                    if((features = rule.getAsJsonObject("features")) != null) {
                        for(Map.Entry<String, JsonElement> featuresEntry : features.entrySet()) {
                            JsonElement entryValue = featuresEntry.getValue();
                            String conditionValue = entryValue.toString(); // TODO: might needs something sexier than toString()
                            conditions.put("feature." + featuresEntry.getKey(), conditionValue);
                        }
                    }

                    JsonObject os;
                    if((os = rule.getAsJsonObject("os")) != null) {
                        String osName = os.get("name").getAsString();
                        Object osVersion = os.getAsJsonPrimitive("version");
                        Object arch = os.getAsJsonPrimitive("arch");

                        if(osName != null) {
                            conditions.put("os.name", osName);
                        }

                        if(osVersion != null) {
                            conditions.put("os.version", osVersion.toString());
                        }

                        if(arch != null) {
                            conditions.put("os.arch", arch.toString());
                        }
                    }
                }

                // Collect rule values
                List<String> values;
                if(value.isJsonArray()) {
                    // String array
                    values = new ArrayList<>();
                    for(JsonElement valueElement : value.getAsJsonArray()) {
                        values.add(valueElement.getAsString());
                    }
                } else {
                    values = Collections.singletonList(value.getAsString());
                }

                // Add to collected arguments
                collectedArguments.add(new ConditionalArgument(values, conditions));
            } else {
                String value = element.getAsString();
                collectedArguments.add(new BasicArgument(value, true));
            }
        }

        return collectedArguments;
    }

    public static class BasicArgument implements LauncherPackage.Argument {
        private final List<String> value;
        private final boolean allowed;

        public BasicArgument(@NonNull String value, boolean allowed) {
            this(Collections.singletonList(value), allowed);
        }

        public BasicArgument(@NonNull List<String> value, boolean allowed) {
            this.value = value;
            this.allowed = allowed;
        }

        @NonNull
        @Override
        public List<String> getValue() {
            return value;
        }

        @Override
        public boolean isAllowed(@NonNull LauncherAPI launcherAPI) {
            return allowed;
        }
    }

    public static class ConditionalArgument extends BasicArgument implements LauncherPackage.Argument {
        private final Map<String, String> conditions;

        public ConditionalArgument(@NonNull String value, @NonNull Map<String, String> conditions) {
            super(value, false);
            this.conditions = conditions;
        }

        public ConditionalArgument(@NonNull List<String> value, @NonNull Map<String, String> conditions) {
            super(value, false);
            this.conditions = conditions;
        }

        @Override
        public boolean isAllowed(@NonNull LauncherAPI launcherAPI) {
            // TODO!
            return false;
        }
    }
}
