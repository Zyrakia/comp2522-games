package ca.bcit.comp2522.games.game.crafter.crafting;

import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import ca.bcit.comp2522.games.game.crafter.item.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all default recipes for the crafter game.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class CraftingManager {

    private static CraftingManager instance;

    private final List<Recipe> recipes;

    /**
     * Creates the crafting manager.
     */
    private CraftingManager() {
        if (CraftingManager.instance != null) {
            throw new IllegalStateException("CraftingManager has already been initialized");
        }

        this.recipes = new ArrayList<>();

        this.recipes.add(new ShapelessRecipe(new ItemStack(Items.GRASS), List.of(Items.DIRT, Items.DIRT, Items.DIRT)));
    }

    /**
     * Returns the global crafting manager.
     *
     * @return the crafting manager
     */
    public static CraftingManager getInstance() {
        if (CraftingManager.instance == null) {
            CraftingManager.instance = new CraftingManager();
        }

        return CraftingManager.instance;
    }

    /**
     * Attempts to craft with the given list of ingredients on all registered recipes.
     *
     * @param ingredients the ingredients to craft with
     * @return the craft result
     */
    public CraftResult craft(final List<Item> ingredients) {
        for (final Recipe recipe : this.recipes) {
            final CraftResult res;
            res = recipe.craft(ingredients);

            if (res.isSuccessful()) {
                return res;
            }
        }

        return CraftResult.unsuccessful();
    }

}
