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

        this.recipes = CraftingManager.getDefaultRecipes();
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
     * Returns a list containing all the default recipes.
     *
     * @return the default recipe list
     */
    private static List<Recipe> getDefaultRecipes() {
        final List<Recipe> recipes;
        recipes = new ArrayList<>();

        // Dirt and cobblestone will be the "base ingredients" that you will get by "mining"

        recipes.add(new ShapelessRecipe(new ItemStack(Items.GRASS, 2), Items.DIRT, Items.DIRT, Items.DIRT));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.SAPLING, 1), "####@####", Items.GRASS, Items.DIRT));
        recipes.add(new ShapelessRecipe(new ItemStack(Items.WOOD, 1), Items.SAPLING, Items.DIRT));
        recipes.add(new ShapelessRecipe(new ItemStack(Items.STONE, 1), Items.COBBLESTONE, Items.COBBLESTONE,
                                        Items.COBBLESTONE, Items.COBBLESTONE));
        recipes.add(new ShapelessRecipe(new ItemStack(Items.PLANK, 4), Items.WOOD));

        recipes.add(ShapedRecipe.compile(new ItemStack(Items.STICK, 2), "#  #     ", Items.PLANK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.STICK, 2), " #  #    ", Items.PLANK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.STICK, 2), "  #  #   ", Items.PLANK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.STICK, 2), "   #  #  ", Items.PLANK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.STICK, 2), "    #  # ", Items.PLANK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.STICK, 2), "     #  #", Items.PLANK));

        recipes.add(ShapedRecipe.compile(new ItemStack(Items.STONE_PICKAXE, 1), "### @    ", Items.STONE, Items.STICK));
        recipes.add(new ShapelessRecipe(new ItemStack(Items.COAL), Items.STONE, Items.STONE_PICKAXE));

        recipes.add(ShapedRecipe.compile(new ItemStack(Items.TORCH, 2), "#  @     ", Items.COAL, Items.STICK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.TORCH, 2), " #  @    ", Items.COAL, Items.STICK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.TORCH, 2), "  #  @   ", Items.COAL, Items.STICK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.TORCH, 2), "   #  @  ", Items.COAL, Items.STICK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.TORCH, 2), "    #  @ ", Items.COAL, Items.STICK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.TORCH, 2), "     #  @", Items.COAL, Items.STICK));

        recipes.add(new ShapelessRecipe(new ItemStack(Items.CHARCOAL), Items.TORCH, Items.WOOD));

        recipes.add(
                new ShapelessRecipe(new ItemStack(Items.IRON_ORE), Items.STONE, Items.STONE, Items.STONE, Items.TORCH,
                                    Items.STONE_PICKAXE));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.IRON_INGOT), "#@##$##%#", Items.COBBLESTONE, Items.COAL,
                                         Items.IRON_ORE, Items.TORCH));
        recipes.add(
                ShapedRecipe.compile(new ItemStack(Items.IRON_INGOT), "#@##$##%#", Items.COBBLESTONE, Items.CHARCOAL,
                                     Items.IRON_ORE, Items.TORCH));
        recipes.add(
                ShapedRecipe.compile(new ItemStack(Items.IRON_PICKAXE, 1), "### @    ", Items.IRON_INGOT, Items.STICK));

        recipes.add(
                new ShapelessRecipe(new ItemStack(Items.GOLD_ORE), Items.STONE, Items.STONE, Items.STONE, Items.TORCH,
                                    Items.IRON_PICKAXE));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.GOLD_INGOT), "#@##$##%#", Items.COBBLESTONE, Items.COAL,
                                         Items.GOLD_ORE, Items.TORCH));
        recipes.add(
                ShapedRecipe.compile(new ItemStack(Items.GOLD_INGOT), "#@##$##%#", Items.COBBLESTONE, Items.CHARCOAL,
                                     Items.GOLD_ORE, Items.TORCH));

        recipes.add(ShapedRecipe.compile(new ItemStack(Items.REDSTONE, 2), "#  @     ", Items.GOLD_ORE, Items.TORCH));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.REDSTONE, 2), " #  @    ", Items.GOLD_ORE, Items.TORCH));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.REDSTONE, 2), "  #  @   ", Items.GOLD_ORE, Items.TORCH));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.REDSTONE, 2), "   #  @  ", Items.GOLD_ORE, Items.TORCH));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.REDSTONE, 2), "    #  @ ", Items.GOLD_ORE, Items.TORCH));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.REDSTONE, 2), "     #  @", Items.GOLD_ORE, Items.TORCH));

        recipes.add(ShapedRecipe.compile(new ItemStack(Items.GOLD_SWORD, 1), "#  #  @  ", Items.GOLD_ORE, Items.STICK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.GOLD_SWORD, 1), " #  #  @ ", Items.GOLD_ORE, Items.STICK));
        recipes.add(ShapedRecipe.compile(new ItemStack(Items.GOLD_SWORD, 1), "  #  #  @", Items.GOLD_ORE, Items.STICK));

        recipes.add(new ShapelessRecipe(new ItemStack(Items.BLAZE_POWDER, 2), Items.GOLD_SWORD));
        recipes.add(new ShapelessRecipe(new ItemStack(Items.OBSIDIAN), Items.BLAZE_POWDER, Items.CHARCOAL));
        recipes.add(new ShapelessRecipe(new ItemStack(Items.OBSIDIAN), Items.BLAZE_POWDER, Items.COAL));
        recipes.add(new ShapelessRecipe(new ItemStack(Items.NETHER_BRICK), Items.OBSIDIAN, Items.BLAZE_POWDER));
        recipes.add(new ShapelessRecipe(new ItemStack(Items.DIAMOND), Items.OBSIDIAN, Items.OBSIDIAN, Items.OBSIDIAN,
                                        Items.COAL));

        recipes.add(ShapedRecipe.compile(new ItemStack(Items.ENCHANTED_GEM, 2), " # #@# # ", Items.REDSTONE,
                                         Items.DIAMOND));

        recipes.add(ShapedRecipe.compile(new ItemStack(Items.BEACON), "###@*@$$$", Items.DIAMOND, Items.REDSTONE,
                                         Items.ENCHANTED_GEM, Items.OBSIDIAN));

        return recipes;
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
