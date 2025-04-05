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
package ovh.neziw.defusalkit.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ItemService {

    /**
     * Checks if two ItemStacks are similar, ignoring their amount.
     *
     * @param item1 The first ItemStack (can be null).
     * @param item2 The second ItemStack (can be null).
     * @return true if they are similar (same type, meta, etc., ignoring amount), false otherwise.
     */
    boolean isSimilarIgnoringAmount(@Nullable final ItemStack item1, @Nullable final ItemStack item2);

    /**
     * Creates a defensive copy of an ItemStack.
     * Returns null if the input is null.
     *
     * @param itemStack The ItemStack to copy.
     * @return A new ItemStack instance or null.
     */
    @Nullable
    ItemStack copy(@Nullable final ItemStack itemStack);

    /**
     * Checks if an ItemStack is considered 'air' or null/empty.
     *
     * @param itemStack The ItemStack to check.
     * @return true if the item is effectively air or null.
     */
    boolean isAirOrNull(@Nullable final ItemStack itemStack);
}
