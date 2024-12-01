package ca.bcit.comp2522.games.game.crafter.crafting;

import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * Represents a crafting recipe.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public abstract class Recipe {

    private final ItemStack resultStack;
    private final List<Item> ingredients;

    /**
     * Creates a new recipe.
     *
     * @param result      the result of the recipe
     * @param ingredients the ingredients required to craft this recipe.
     */
    public Recipe(final ItemStack result, final List<Item> ingredients) {
        Recipe.validateResult(result);

        this.resultStack = result;
        this.ingredients = ingredients;
    }

    /**
     * Validates the given result item stack to ensure it is within limits.
     *
     * @param result the result item stack
     */
    private static void validateResult(final ItemStack result) {
        if (result == null) {
            throw new IllegalArgumentException("A recipe must have a result.");
        }
    }

    /**
     * Attempts to craft this recipe.
     *
     * @param ingredients the ingredients to craft the recipe with
     * @return the result of the craft attempt
     */
    public CraftResult craft(List<Item> ingredients) {
        if (this.canCraftWith(ingredients)) {
            return new CraftResult(this.resultStack);
        }

        return CraftResult.unsuccessful();
    }

    /**
     * Returns whether this recipe can be crafted with the given list of ingredients.
     *
     * @param ingredients the ingredients
     * @return whether the ingredients would result in a successful craft result
     */
    public abstract boolean canCraftWith(List<Item> ingredients);

    /**
     * Returns the ingredients in a read-only list.
     *
     * @return the ingredients
     */
    public List<Item> getIngredients() {
        return Collections.unmodifiableList(this.ingredients);
    }

    /**
     * Returns the result that this recipe would provide if crafted.
     *
     * @return the item stack that would be provided on successful crafts
     */
    public ItemStack getResultStack() {
        return this.resultStack;
    }

}
