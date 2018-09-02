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
import me.minidigger.minecraftlauncher.api.json.RulesContainer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mark Vainomaa
 */
public class RulesDeserializer implements JsonDeserializer<RulesContainer> {
    @Override
    public RulesContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<String, String> collectedRules = new HashMap<>();

        for(JsonElement ruleElement : json.getAsJsonArray()) {
            JsonObject rule = ruleElement.getAsJsonObject();

            // TODO: no idea what to do with this
            // But I think that code behaves as this == "allowed"
            //String action = rule.get("action").getAsString();

            JsonObject features;
            if((features = rule.getAsJsonObject("features")) != null) {
                for(Map.Entry<String, JsonElement> featuresEntry : features.entrySet()) {
                    JsonElement entryValue = featuresEntry.getValue();
                    String conditionValue = entryValue.toString(); // TODO: might needs something sexier than toString()
                    collectedRules.put("feature." + featuresEntry.getKey(), conditionValue);
                }
            }

            JsonObject os;
            if((os = rule.getAsJsonObject("os")) != null) {
                String osName = os.get("name").getAsString();
                Object osVersion = os.getAsJsonPrimitive("version");
                Object arch = os.getAsJsonPrimitive("arch");

                if(osName != null) {
                    collectedRules.put("os.name", osName);
                }

                if(osVersion != null) {
                    collectedRules.put("os.version", osVersion.toString());
                }

                if(arch != null) {
                    collectedRules.put("os.arch", arch.toString());
                }
            }
        }

        return new RulesContainer(collectedRules);
    }
}
