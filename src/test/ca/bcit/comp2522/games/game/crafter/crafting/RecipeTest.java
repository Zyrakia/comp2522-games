package ca.bcit.comp2522.games.game.crafter.crafting;

import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import ca.bcit.comp2522.games.game.crafter.item.Items;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {

    @Test
    public void testValidCraftingWithShapedRecipe() {
        final ItemStack resultStack;
        final String format;
        final Recipe recipe;
        final List<Item> validInput;
        final CraftResult result;

        resultStack = new ItemStack(Items.ENCHANTED_GEM, 1);
        format = " # #@# # ";
        recipe = ShapedRecipe.compile(resultStack, format, Items.REDSTONE, Items.DIAMOND);
        validInput = createItemList(null, Items.REDSTONE, null, Items.REDSTONE, Items.DIAMOND, Items.REDSTONE, null,
                                    Items.REDSTONE, null);
        result = recipe.craft(validInput);

        assertTrue(result.isSuccessful());
        assertEquals(resultStack, result.getStack());
    }

    @Test
    public void testInvalidCraftingWithShapedRecipe() {
        final ItemStack resultStack;
        final String format;
        final Recipe recipe;
        final List<Item> invalidInput;
        final CraftResult result;

        resultStack = new ItemStack(Items.ENCHANTED_GEM, 1);
        format = " # #@# # ";
        recipe = ShapedRecipe.compile(resultStack, format, Items.REDSTONE, Items.DIAMOND);
        invalidInput = createItemList(Items.DIRT, null, Items.REDSTONE, // Wrong top row
                                      null, Items.DIAMOND, null, Items.REDSTONE, null, Items.REDSTONE);
        result = recipe.craft(invalidInput);

        assertFalse(result.isSuccessful());
        assertNull(result.getStack());
    }

    @Test
    public void testCraftingWithShapelessRecipe() {
        final ItemStack resultStack;
        final Recipe recipe;
        final List<Item> validInput;
        final CraftResult result;

        resultStack = new ItemStack(Items.GRASS, 3);
        recipe = new ShapelessRecipe(resultStack, List.of(Items.DIRT, Items.DIRT, Items.DIRT));
        validInput = new ArrayList<>();
        validInput.add(Items.DIRT);
        validInput.add(Items.DIRT);
        validInput.add(Items.DIRT);
        result = recipe.craft(validInput);

        assertTrue(result.isSuccessful());
        assertEquals(resultStack, result.getStack());
    }

    @Test
    public void testShapelessRecipeWithInvalidInput() {
        final ItemStack resultStack;
        final Recipe recipe;
        final List<Item> invalidInput;
        final CraftResult result;

        resultStack = new ItemStack(Items.GRASS, 3);
        recipe = new ShapelessRecipe(resultStack, List.of(Items.DIRT, Items.DIRT, Items.DIRT));
        invalidInput = new ArrayList<>();
        invalidInput.add(Items.STONE);
        invalidInput.add(Items.DIRT);
        invalidInput.add(Items.DIRT);
        result = recipe.craft(invalidInput);

        assertFalse(result.isSuccessful());
        assertNull(result.getStack());
    }

    @Test
    public void testShapedRecipeHandlesEmptySpotsCorrectly() {
        final ItemStack resultStack;
        final String format;
        final Recipe recipe;
        final List<Item> validInput;
        final CraftResult result;

        resultStack = new ItemStack(Items.BEACON, 1);
        format = " # @ # ";
        recipe = ShapedRecipe.compile(resultStack, format, Items.DIAMOND, Items.REDSTONE);
        validInput = createItemList(null, Items.DIAMOND, null, Items.REDSTONE, null, Items.DIAMOND, null);
        result = recipe.craft(validInput);

        assertTrue(result.isSuccessful());
        assertEquals(resultStack, result.getStack());
    }

    private List<Item> createItemList(final Item... items) {
        final List<Item> mutableList;
        mutableList = new ArrayList<>(Arrays.asList(items));
        return mutableList;
    }
}
