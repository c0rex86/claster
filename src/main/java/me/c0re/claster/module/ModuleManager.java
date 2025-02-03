package me.c0re.claster.module;

import me.c0re.claster.ClasterClient;
import me.c0re.claster.module.combat.KillAura;
import me.c0re.claster.module.movement.Speed;
import me.c0re.claster.module.movement.Flight;
import me.c0re.claster.module.movement.AutoSprint;
import me.c0re.claster.module.render.ESP;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        ClasterClient.LOGGER.info("Initializing ModuleManager...");
        
        // Регистрация модулей
        // Combat
        modules.add(new KillAura());
        
        // Movement
        modules.add(new Speed());
        modules.add(new Flight());
        modules.add(new AutoSprint());
        
        // Render
        modules.add(new ESP());
        
        ClasterClient.LOGGER.info("ModuleManager initialized with {} modules", modules.size());
    }

    public void onTick() {
        modules.stream()
                .filter(Module::isEnabled)
                .forEach(Module::onTick);
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Category category) {
        return modules.stream()
                .filter(module -> module.getCategory() == category)
                .collect(Collectors.toList());
    }

    public Module getModuleByName(String name) {
        return modules.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
} 