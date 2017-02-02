package com.rayzr522.requestit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.rayzr522.requestit.data.Messages;
import com.rayzr522.requestit.data.Request;

/**
 * @author Rayzr
 */
public class RequestIt extends JavaPlugin {

    private Messages messages;
    private List<Request> requests = new ArrayList<>();

    @Override
    public void onEnable() {
        reload();

        getCommand("request").setExecutor(new ComamndRequest(this));
    }

    @Override
    public void onDisable() {
        save();
    }

    @SuppressWarnings("unchecked")
    public void reload() {
        reloadConfig();

        messages = new Messages();
        messages.load(getConfig("messages.yml"));

        requests.clear();

        YamlConfiguration requestConfig = getConfig("requests.yml");
        requestConfig.getMapList("requests").forEach(map -> {
            requests.add(Request.deserialize((Map<String, Object>) map));
        });
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("requests", requests.stream().map(r -> r.serialize()).collect(Collectors.toList()));
        try {
            config.save(getFile("requests.yml"));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to save requests.yml file!", e);
        }
    }

    public List<Request> getRequests() {
        return requests;
    }

    public YamlConfiguration getConfig(String path) {
        if (!getFile(path).exists() && getResource(path) != null) {
            saveResource(path, false);
        }
        return YamlConfiguration.loadConfiguration(getFile(path));
    }

    public File getFile(String path) {
        return new File(getDataFolder(), path.replace('/', File.pathSeparatorChar));
    }

    public String tr(String key, Object... objects) {
        return messages.tr(key, objects);
    }

    public String trRaw(String key, Object... objects) {
        return messages.trRaw(key, objects);
    }

}
