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
package ovh.neziw.defusalkit.title;

import net.kyori.adventure.util.Ticks;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ovh.neziw.defusalkit.util.MessageUtil;

public class BukkitTitle extends Title<Player> {

    private final String title;
    private final String subtitle;
    private final int fadeInTicks;
    private final int stayTicks;
    private final int fadeOutTicks;

    public BukkitTitle(@NotNull final Player player,
                       @NotNull final String title,
                       @NotNull final String subtitle,
                       final int fadeInTicks,
                       final int stayTicks,
                       final int fadeOutTicks) {
        super(player);
        this.title = title;
        this.subtitle = subtitle;
        this.fadeInTicks = fadeInTicks;
        this.stayTicks = stayTicks;
        this.fadeOutTicks = fadeOutTicks;
    }

    @Override
    public void send() {
        final net.kyori.adventure.title.Title adventureTitle = net.kyori.adventure.title.Title.title(
                MessageUtil.format(this.title),
                MessageUtil.format(this.subtitle),
                net.kyori.adventure.title.Title.Times.times(
                        Ticks.duration(this.fadeInTicks),
                        Ticks.duration(this.stayTicks),
                        Ticks.duration(this.fadeOutTicks)
                )
        );
        this.player().showTitle(adventureTitle);
    }
}