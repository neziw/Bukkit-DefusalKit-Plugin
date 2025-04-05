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
package ovh.neziw.defusalkit.bootstrap;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ovh.neziw.defusalkit.command.CommandManager;
import ovh.neziw.defusalkit.config.BukkitConfigurationService;
import ovh.neziw.defusalkit.config.ConfigurationService;
import ovh.neziw.defusalkit.defusal.DefusalService;
import ovh.neziw.defusalkit.defusal.StandardDefusalService;
import ovh.neziw.defusalkit.item.BukkitItemService;
import ovh.neziw.defusalkit.item.ItemService;
import ovh.neziw.defusalkit.listener.BukkitListenerManager;
import ovh.neziw.defusalkit.listener.ListenerManager;

public class DefusalKitPlugin extends JavaPlugin {

    private ConfigurationService configurationService;
    private ItemService itemService;
    private DefusalService defusalService;
    private CommandManager commandManager;
    private ListenerManager bukkitListenerManager;

    @Override
    public void onEnable() {
        final Logger logger = this.getLogger();
        logger.info("Enabling DefusalKit v" + this.getDescription().getVersion() + "...");

        this.itemService = new BukkitItemService();

        this.configurationService = new BukkitConfigurationService(this, this.itemService);

        this.defusalService = new StandardDefusalService(this.configurationService, this.itemService, logger);

        this.commandManager = new CommandManager(this, this.configurationService, this.itemService);
        this.bukkitListenerManager = new BukkitListenerManager(this, this.defusalService);

        this.commandManager.registerMainCommand("defusalkit");
        this.bukkitListenerManager.registerListeners();

        logger.info("DefusalKit enabled successfully.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabling DefusalKit...");
    }

    @NotNull
    public ConfigurationService configurationservice() {
        if (this.configurationService == null) throw new IllegalStateException("Plugin not enabled yet!");
        return this.configurationService;
    }

    @NotNull
    public ItemService itemService() {
        if (this.itemService == null) throw new IllegalStateException("Plugin not enabled yet!");
        return this.itemService;
    }

    @NotNull
    public DefusalService defusalService() {
        if (this.defusalService == null) throw new IllegalStateException("Plugin not enabled yet!");
        return this.defusalService;
    }
}