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
package ovh.neziw.defusalkit.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ovh.neziw.defusalkit.config.ConfigurationService;
import ovh.neziw.defusalkit.item.ItemService;
import ovh.neziw.defusalkit.permission.PluginPermission;
import ovh.neziw.defusalkit.util.MessageUtil;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;
    private final Map<String, BaseCommand> subCommands = new HashMap<>();

    public CommandManager(@NotNull final JavaPlugin plugin, @NotNull final ConfigurationService configService, @NotNull final ItemService itemService) {
        this.plugin = plugin;
        this.registerSubCommand("get", new GetDefusalItemCommand(configService));
        this.registerSubCommand("set", new SetDefusalItemCommand(configService, itemService));
    }

    private void registerSubCommand(final String name, final BaseCommand command) {
        this.subCommands.put(name.toLowerCase(), command);
    }

    public void registerMainCommand(final String commandName) {
        final PluginCommand command = this.plugin.getCommand(commandName);
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
            command.setPermission(PluginPermission.COMMAND_BASE.node());
            this.plugin.getLogger().info("Registered command '" + commandName + "'.");
        } else {
            this.plugin.getLogger().severe("Could not register command '" + commandName + "'! Is it defined in plugin.yml?");
        }
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!sender.hasPermission(PluginPermission.COMMAND_BASE.node())) {
            MessageUtil.sendError(sender, "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            this.sendHelp(sender);
            return true;
        }

        final String subCommandName = args[0].toLowerCase();
        final BaseCommand subCommand = this.subCommands.get(subCommandName);

        if (subCommand == null) {
            MessageUtil.sendError(sender, "Unknown sub-command: " + subCommandName);
            this.sendHelp(sender);
            return true;
        }

        final String requiredPermission = subCommand.permission();
        if (requiredPermission != null && !sender.hasPermission(requiredPermission)) {
            MessageUtil.sendError(sender, "You do not have permission to use the '" + subCommandName + "' sub-command.");
            return true;
        }

        final String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        return subCommand.execute(sender, subArgs);
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        if (!sender.hasPermission(PluginPermission.COMMAND_BASE.node())) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            final String input = args[0].toLowerCase();
            return this.subCommands.entrySet().stream()
                    .filter(entry -> {
                        final String perm = entry.getValue().permission();
                        return (perm == null || sender.hasPermission(perm));
                    })
                    .map(Map.Entry::getKey)
                    .filter(name -> name.startsWith(input))
                    .sorted()
                    .toList();

        } else if (args.length > 1) {
            final String subCommandName = args[0].toLowerCase();
            final BaseCommand subCommand = this.subCommands.get(subCommandName);

            if (subCommand != null) {
                final String requiredPermission = subCommand.permission();
                if (requiredPermission == null || sender.hasPermission(requiredPermission)) {
                    final String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                    return subCommand.tabComplete(sender, subArgs);
                }
            }
        }

        return Collections.emptyList();
    }

    private void sendHelp(final CommandSender sender) {
        MessageUtil.sendMessage(sender, "<gold>--- DefusalKit Help ---</gold>");
        this.subCommands.forEach((name, cmd) -> {
            final String perm = cmd.permission();
            if (perm == null || sender.hasPermission(perm)) {
                MessageUtil.sendMessage(sender, "<yellow>/dk " + cmd.usage() + "</yellow> <gray>- Requires permission: " + (perm != null ? perm : "none") + "</gray>");
            }
        });
    }
}