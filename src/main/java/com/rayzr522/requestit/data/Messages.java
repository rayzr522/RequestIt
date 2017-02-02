/**
 * 
 */
package com.rayzr522.requestit.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.rayzr522.creativelynamedlib.utils.text.TextUtils;

/**
 * @author Rayzr
 *
 */
public class Messages {
    private Map<String, String> messages = new HashMap<>();
    private String prefix = "";

    public void load(ConfigurationSection section) {
        for (String key : section.getKeys(true)) {
            if (key.equalsIgnoreCase("prefix")) {
                prefix = TextUtils.colorize(section.getString(key));
                continue;
            }
            messages.put(key, section.getString(key));
        }
    }

    public String tr(String key, Object... objects) {
        return prefix + trRaw(key, objects);
    }

    public String trRaw(String key, Object... objects) {
        String message = messages.getOrDefault(key, key);
        for (int i = 0; i < objects.length; i++) {
            message = message.replaceAll(String.format("\\{%d\\}", i), objects[i].toString());
        }
        return TextUtils.colorize(message);
    }

}
