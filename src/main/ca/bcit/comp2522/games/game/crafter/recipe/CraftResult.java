package ca.bcit.comp2522.games.game.crafter.recipe;

import ca.bcit.comp2522.games.game.crafter.item.ItemStack;


/**
 * Represents the result of crafting a recipe.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class CraftResult {

    private final ItemStack stack;

    /**
     * Creates a new craft result that holds that wraps the given item.
     *
     * @param stack the result item stack
     */
    public CraftResult(final ItemStack stack) {
        this.stack = stack;
    }

    /**
     * Returns an unsuccessful craft result (one without an underlying item).
     *
     * @return the result
     */
    public static CraftResult unsuccessful() {
        return new CraftResult(null);
    }

    /**
     * Returns whether this craft result is valid (if it holds a valid item stack).
     *
     * @return whether this craft result is successful
     */
    public boolean isSuccessful() {
        return this.stack != null;
    }

    /**
     * Returns the item stack that this craft result holds.
     *
     * @return the stack, null if the result is unsuccessful
     */
    public ItemStack getStack() {
        return this.stack;
    }

}
