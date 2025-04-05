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

import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitTitleBuilder implements TitleBuilder<BukkitTitle, Player> {

    private Player player;
    private String title;
    private String subtitle;
    private int fadeInTicks;
    private int stayTicks;
    private int fadeOutTicks;

    public static BukkitTitleBuilder builder() {
        return new BukkitTitleBuilder();
    }

    @Override
    public TitleBuilder<BukkitTitle, Player> withPlayer(@NotNull final Player player) {
        this.player = player;
        return this;
    }

    @Override
    public TitleBuilder<BukkitTitle, Player> withTitle(@NotNull final String title) {
        this.title = title;
        return this;
    }

    @Override
    public TitleBuilder<BukkitTitle, Player> withSubtitle(@NotNull final String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    @Override
    public TitleBuilder<BukkitTitle, Player> withFadeInTicks(final int fadeInTicks) {
        this.fadeInTicks = fadeInTicks;
        return this;
    }

    @Override
    public TitleBuilder<BukkitTitle, Player> withStayTicks(final int stayTicks) {
        this.stayTicks = stayTicks;
        return this;
    }

    @Override
    public TitleBuilder<BukkitTitle, Player> withFadeOutTicks(final int fadeOutTicks) {
        this.fadeOutTicks = fadeOutTicks;
        return this;
    }

    @Override
    public void build(@NotNull final Consumer<? super BukkitTitle> titleConsumer) {
        final BukkitTitle bukkitTitle = new BukkitTitle(
                this.player,
                this.title,
                this.subtitle,
                this.fadeInTicks,
                this.stayTicks,
                this.fadeOutTicks
        );
        titleConsumer.accept(bukkitTitle);
    }
}