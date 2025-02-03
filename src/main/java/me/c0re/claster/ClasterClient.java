package me.c0re.claster;

import me.c0re.claster.module.ModuleManager;
import me.c0re.claster.ui.ClickGUI;
import me.c0re.claster.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasterClient implements ClientModInitializer {
    public static final String MOD_ID = "claster";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static ClasterClient INSTANCE;
    
    private ModuleManager moduleManager;
    private ClickGUI clickGUI;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        LOGGER.info("Initializing Claster Client...");
        
        this.moduleManager = new ModuleManager();
        this.clickGUI = new ClickGUI();
        
        // Загружаем конфигурацию
        Config.loadConfig();
        
        // Регистрируем хук для сохранения конфигурации при выходе
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Saving configuration...");
            Config.saveConfig();
        }));
        
        LOGGER.info("Claster Client initialized successfully!");
    }

    public static ClasterClient getInstance() {
        return INSTANCE;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ClickGUI getClickGUI() {
        return clickGUI;
    }
} 