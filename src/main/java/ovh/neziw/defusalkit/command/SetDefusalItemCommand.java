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

import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ovh.neziw.defusalkit.config.ConfigurationService;
import ovh.neziw.defusalkit.item.ItemService;
import ovh.neziw.defusalkit.permission.PluginPermission;
import ovh.neziw.defusalkit.util.MessageUtil;

public class SetDefusalItemCommand implements BaseCommand {

    private final ConfigurationService configService;
    private final ItemService itemService;

    public SetDefusalItemCommand(@NotNull final ConfigurationService configService, @NotNull final ItemService itemService) {
        this.configService = configService;
        this.itemService = itemService;
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args) {
        if (!(sender instanceof final Player player)) {
            MessageUtil.sendError(sender, "This command can only be executed by a player.");
            return true;
        }

        final ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (this.itemService.isAirOrNull(itemInHand)) {
            MessageUtil.sendError(player, "You must be holding an item in your main hand to set it as the defusal kit.");
            return true;
        }

        try {
            final ItemStack itemToSave = this.itemService.copy(itemInHand);
            if (itemToSave == null) {
                MessageUtil.sendError(player, "Failed to copy the item in your hand.");
                return true;
            }
            itemToSave.setAmount(1);

            this.configService.saveDefusalItem(itemToSave);
            MessageUtil.sendSuccess(player, "Successfully set the defusal kit item to " + itemToSave.getType().name() + "!");
        } catch (final IllegalArgumentException exception) {
            MessageUtil.sendError(player, "Error: " + exception.getMessage());
        } catch (final Exception exception) {
            MessageUtil.sendError(player, "An unexpected error occurred while saving the defusal item.");
            player.getServer().getLogger().log(java.util.logging.Level.SEVERE, "Error saving defusal item set by " + player.getName(), exception);
        }
        return true;
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull final CommandSender sender, @NotNull final String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String permission() {
        return PluginPermission.COMMAND_SET.node();
    }

    @Override
    public String usage() {
        return "set";
    }
}
