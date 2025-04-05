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
package ovh.neziw.defusalkit.defusal;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ovh.neziw.defusalkit.config.ConfigurationService;
import ovh.neziw.defusalkit.item.ItemService;
import ovh.neziw.defusalkit.permission.PluginPermission;

public class StandardDefusalService implements DefusalService {

    private final ConfigurationService configService;
    private final ItemService itemService;
    private final Logger logger;

    public StandardDefusalService(@NotNull final ConfigurationService configService, @NotNull final ItemService itemService, @NotNull final Logger logger) {
        this.configService = configService;
        this.itemService = itemService;
        this.logger = logger;
    }

    @Override
    @NotNull
    public DefusalAttemptResult attemptDefusal(@NotNull final Player player, @NotNull final Entity targetEntity, @NotNull final ItemStack usedItem) {
        if (!player.hasPermission(PluginPermission.DEFUSE.node())) {
            return DefusalAttemptResult.NO_PERMISSION;
        }

        if (!(targetEntity instanceof TNTPrimed || targetEntity instanceof ExplosiveMinecart)) {
            return DefusalAttemptResult.INVALID_ENTITY;
        }

        final Optional<ItemStack> configuredItemOpt = this.configService.defusalItem();
        if (configuredItemOpt.isEmpty()) {
            this.logger.warning("Defusal attempt failed: No defusal item configured properly.");
            return DefusalAttemptResult.FAILURE_OTHER;
        }

        if (!this.itemService.isSimilarIgnoringAmount(usedItem, configuredItemOpt.get())) {
            return DefusalAttemptResult.INVALID_ITEM;
        }

        // ---> Defusal Logic <---
        try {
            final Location location = targetEntity.getLocation();
            final World world = targetEntity.getWorld();

            targetEntity.remove();

            world.dropItemNaturally(location, new ItemStack(Material.TNT, 1));

            world.playSound(location, Sound.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);

            world.spawnParticle(Particle.SMOKE, location.add(0, 0.5, 0), 10, 0.2, 0.2, 0.2, 0.01);

            return DefusalAttemptResult.SUCCESS;
        } catch (final Exception exception) {
            this.logger.log(Level.SEVERE, "Error during TNT defusal for player " + player.getName(), exception);
            return DefusalAttemptResult.FAILURE_OTHER;
        }
    }
}