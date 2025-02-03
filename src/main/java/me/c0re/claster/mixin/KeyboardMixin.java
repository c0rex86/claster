package me.c0re.claster.mixin;

import me.c0re.claster.ClasterClient;
import me.c0re.claster.module.Module;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (window == MinecraftClient.getInstance().getWindow().getHandle()) {
            if (action == 1) { // Клавиша нажата
                // Проверяем клавишу для ClickGUI (по умолчанию - правый Shift)
                if (key == 344) { // GLFW_KEY_RIGHT_SHIFT
                    ClasterClient.getInstance().getClickGUI().toggle();
                    return;
                }

                // Проверяем привязки клавиш для модулей
                for (Module module : ClasterClient.getInstance().getModuleManager().getModules()) {
                    if (module.getKeyBind() == key) {
                        module.toggle();
                    }
                }
            }
        }
    }
} 