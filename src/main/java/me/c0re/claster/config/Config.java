package me.c0re.claster.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.c0re.claster.ClasterClient;
import me.c0re.claster.module.Module;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("claster.json");

    public static void saveConfig() {
        JsonObject config = new JsonObject();
        JsonObject modules = new JsonObject();

        for (Module module : ClasterClient.getInstance().getModuleManager().getModules()) {
            JsonObject moduleObj = new JsonObject();
            moduleObj.addProperty("enabled", module.isEnabled());
            moduleObj.addProperty("keyBind", module.getKeyBind());
            modules.add(module.getName(), moduleObj);
        }

        config.add("modules", modules);

        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            ClasterClient.LOGGER.error("Failed to save config", e);
        }
    }

    public static void loadConfig() {
        if (!Files.exists(CONFIG_PATH)) {
            saveConfig();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            JsonObject config = GSON.fromJson(reader, JsonObject.class);
            JsonObject modules = config.getAsJsonObject("modules");

            for (Module module : ClasterClient.getInstance().getModuleManager().getModules()) {
                JsonObject moduleObj = modules.getAsJsonObject(module.getName());
                if (moduleObj != null) {
                    if (moduleObj.has("enabled") && moduleObj.get("enabled").getAsBoolean()) {
                        module.setEnabled(true);
                    }
                    if (moduleObj.has("keyBind")) {
                        module.setKeyBind(moduleObj.get("keyBind").getAsInt());
                    }
                }
            }
        } catch (IOException e) {
            ClasterClient.LOGGER.error("Failed to load config", e);
        }
    }
} 