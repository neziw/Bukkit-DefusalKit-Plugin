/*
 * This file is part of "Bukkit-DefusalKit-Plugin", licensed under MIT License.
 *
 *  Copyright (c) 2025 neziw
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package ovh.neziw.defusalkit.config;

import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ovh.neziw.defusalkit.item.ItemService;

public class BukkitConfigurationService implements ConfigurationService {

    private final JavaPlugin plugin;
    private final ItemService itemService;
    private FileConfiguration config;
    private ItemStack cachedDefusalItem;

    public BukkitConfigurationService(@NotNull final JavaPlugin plugin, @NotNull final ItemService itemService) {
        this.plugin = plugin;
        this.itemService = itemService;
        this.loadConfiguration();
    }

    @Override
    public void loadConfiguration() {
        this.config = this.plugin.getConfig();

        this.config.addDefault(ConfigKey.DEFUSAL_ITEM.path(), this.defaultDefusalItem());
        this.config.options().copyDefaults(true);
        this.plugin.saveConfig();
        this.cachedDefusalItem = this.config.getItemStack(ConfigKey.DEFUSAL_ITEM.path());

        if (this.itemService.isAirOrNull(this.cachedDefusalItem)) {
            this.plugin.getLogger().warning("Defusal item in config is invalid or missing! Using default (Shears).");
            this.cachedDefusalItem = this.defaultDefusalItem();
            // Save the default back to config if it was invalid
            this.saveDefusalItem(this.cachedDefusalItem);
        } else {
            this.plugin.getLogger().info("Loaded defusal item: " + this.cachedDefusalItem.getType());
        }
    }

    @Override
    @NotNull
    public Optional<ItemStack> defusalItem() {
        return Optional.ofNullable(this.itemService.copy(this.cachedDefusalItem));
    }

    @Override
    public void saveDefusalItem(@NotNull final ItemStack itemStack) {
        if (this.itemService.isAirOrNull(itemStack)) {
            throw new IllegalArgumentException("Cannot save null or air as defusal item.");
        }

        this.config.set(ConfigKey.DEFUSAL_ITEM.path(), itemStack);
        this.plugin.saveConfig();
        this.cachedDefusalItem = this.itemService.copy(itemStack);
        this.plugin.getLogger().info("Saved new defusal item: " + itemStack.getType());
    }

    @Override
    @NotNull
    public ItemStack defaultDefusalItem() {
        return new ItemStack(Material.SHEARS);
    }
}
