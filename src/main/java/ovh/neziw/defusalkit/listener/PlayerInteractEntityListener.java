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
package ovh.neziw.defusalkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ovh.neziw.defusalkit.defusal.DefusalAttemptResult;
import ovh.neziw.defusalkit.defusal.DefusalService;
import ovh.neziw.defusalkit.title.BukkitTitle;
import ovh.neziw.defusalkit.title.BukkitTitleBuilder;
import ovh.neziw.defusalkit.util.MessageUtil;

public class PlayerInteractEntityListener implements Listener {

    private final DefusalService defusalService;

    public PlayerInteractEntityListener(@NotNull final DefusalService defusalService) {
        this.defusalService = defusalService;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        final Player player = event.getPlayer();
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType().isAir()) {
            return;
        }

        final DefusalAttemptResult result = this.defusalService.attemptDefusal(
                player,
                event.getRightClicked(),
                itemInHand
        );

        switch (result) {
            case SUCCESS -> {
                event.setCancelled(true);
                MessageUtil.sendSuccess(player, "TNT successfully defused!");
            }
            case NO_PERMISSION -> BukkitTitleBuilder.builder()
                    .withPlayer(player)
                    .withTitle("<red>No permission</red>")
                    .withSubtitle("<white>You don't have permission to use defuse kits</white>")
                    .withFadeInTicks(10)
                    .withStayTicks(80)
                    .withFadeOutTicks(10)
                    .build(BukkitTitle::send);
            case FAILURE_OTHER -> MessageUtil.sendError(player, "An internal error occurred while attempting defusal.");
        }
    }
}