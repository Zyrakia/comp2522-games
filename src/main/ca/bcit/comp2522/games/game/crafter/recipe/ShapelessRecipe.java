package ca.bcit.comp2522.games.game.crafter.recipe;

import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a recipe where ingredients must only be present, but not in order, to be successfully crafted.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class ShapelessRecipe extends Recipe {

    private final Map<Item, Long> decomposedIngredients;

    /**
     * Creates a new shapeless recipe.
     *
     * @param result      the result of the recipe
     * @param ingredients the ingredients required to craft this recipe.
     */
    public ShapelessRecipe(final ItemStack result, final List<Item> ingredients) {
        super(result, ingredients);

        ShapelessRecipe.validateIngredients(ingredients);
        this.decomposedIngredients = ShapelessRecipe.decomposeIngredients(ingredients);
    }

    /**
     * Validates shapeless ingredients to ensure they are within limits.
     *
     * @param ingredients the ingredients
     */
    private static void validateIngredients(final List<Item> ingredients) {
        if (ingredients.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("A shapeless recipe cannot contain null entries.");
        }
    }

    /**
     * Returns the given ingredients decomposed into their item mapped to how many instances of the item are inside
     * the ingredients list.
     * <p>
     * This will strip away any null values in the ingredients list, since shapeless recipes do not care about null
     * ingredients.
     *
     * @param ingredients the ingredients to decompose
     * @return the unique ingredients mapped to their occurrence count
     */
    private static Map<Item, Long> decomposeIngredients(final List<Item> ingredients) {
        final Map<Item, Long> decomposition;
        decomposition = new HashMap<>();

        if (ingredients == null) {
            return decomposition;
        }

        for (final Item ingredient : ingredients) {
            if (ingredient == null) {
                continue;
            }

            decomposition.compute(ingredient, (_, count) -> {
                if (count == null) {
                    count = 0L;
                }

                return count + 1;
            });
        }

        return decomposition;
    }

    @Override
    public boolean canCraftWith(final List<Item> givenIngredients) {
        final Map<Item, Long> decomposedGivenIngredients;
        decomposedGivenIngredients = ShapelessRecipe.decomposeIngredients(givenIngredients);
        return this.decomposedIngredients.equals(decomposedGivenIngredients);
    }

}
