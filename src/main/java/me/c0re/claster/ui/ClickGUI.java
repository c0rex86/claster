package me.c0re.claster.ui;

import me.c0re.claster.ClasterClient;
import me.c0re.claster.module.Category;
import me.c0re.claster.module.Module;
import me.c0re.claster.ui.theme.Theme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.HashMap;
import java.util.Map;

public class ClickGUI extends Screen {
    private static final Identifier LOGO = new Identifier("claster", "textures/logo.png");
    private boolean visible = false;
    private final Map<Category, Integer> categoryYOffset = new HashMap<>();
    private Category selectedCategory = null;
    private int startX = 5;
    private float logoScale = 0.5f;

    public ClickGUI() {
        super(Text.literal("Claster Client"));
        for (Category category : Category.values()) {
            categoryYOffset.put(category, 30);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Theme.updateGlow();
        
        // Фон
        context.fill(0, 0, this.width, this.height, Theme.BACKGROUND_DARK);
        
        // Отрисовка лого в правом верхнем углу
        int logoSize = (int)(128 * logoScale);
        context.drawTexture(LOGO, this.width - logoSize - 10, 10, 0, 0, logoSize, logoSize, logoSize, logoSize);

        // Отрисовка категорий
        int x = startX;
        for (Category category : Category.values()) {
            boolean isSelected = category == selectedCategory;
            Theme.drawNeonRect(context, x, 5, 100, 20, isSelected ? Theme.ACCENT : Theme.BACKGROUND);
            Theme.drawNeonText(context, category.getName(), x + 10, 10, true);
            
            // Отрисовка модулей выбранной категории
            if (isSelected) {
                int y = categoryYOffset.get(category);
                for (Module module : ClasterClient.getInstance().getModuleManager().getModulesByCategory(category)) {
                    boolean isEnabled = module.isEnabled();
                    Theme.drawNeonRect(context, x, y, 100, 20, isEnabled ? Theme.ACCENT : Theme.BACKGROUND);
                    Theme.drawNeonText(context, module.getName(), x + 5, y + 6, true);
                    
                    // Отображение описания при наведении
                    if (mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 20) {
                        context.drawTooltip(textRenderer, Text.literal(module.getDescription()), mouseX, mouseY);
                    }
                    
                    y += 25;
                }
            }
            x += 105;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = startX;
        
        // Обработка клика по категориям
        for (Category category : Category.values()) {
            if (mouseX >= x && mouseX <= x + 100 && mouseY >= 5 && mouseY <= 25) {
                selectedCategory = (selectedCategory == category) ? null : category;
                return true;
            }
            
            // Обработка клика по модулям
            if (category == selectedCategory) {
                int y = categoryYOffset.get(category);
                for (Module module : ClasterClient.getInstance().getModuleManager().getModulesByCategory(category)) {
                    if (mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 20) {
                        module.toggle();
                        return true;
                    }
                    y += 25;
                }
            }
            x += 105;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void toggle() {
        this.visible = !this.visible;
        if (visible) {
            MinecraftClient.getInstance().setScreen(this);
        } else {
            MinecraftClient.getInstance().setScreen(null);
        }
    }

    public boolean isVisible() {
        return visible;
    }
} 