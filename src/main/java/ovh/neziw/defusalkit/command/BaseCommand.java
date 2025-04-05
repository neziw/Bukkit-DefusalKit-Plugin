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

import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface BaseCommand {

    /**
     * Executes the subcommand logic.
     *
     * @param sender The command sender.
     * @param args   The arguments passed to the subcommand.
     * @return true if the command was handled, false otherwise.
     */
    boolean execute(@NotNull final CommandSender sender, @NotNull final String[] args);

    /**
     * Provides tab completions for the subcommand.
     *
     * @param sender The command sender.
     * @param args   The current arguments typed.
     * @return A list of suggested completions.
     */
    @NotNull
    List<String> tabComplete(@NotNull final CommandSender sender, @NotNull final String[] args);

    /**
     * Gets the permission required for this subcommand.
     *
     * @return The permission node string, or null if no permission is required.
     */
    String permission();

    /**
     * Gets the expected argument count for usage messages.
     * Can be a range or specific number depending on implementation.
     *
     * @return A description of expected arguments.
     */
    String usage();
}