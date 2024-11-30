package ca.bcit.comp2522.games.game.crafter.recipe;

import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a recipe that requires all ingredients to be matched exactly, including `null` values, in order to be
 * successfully crafted.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class ShapedRecipe extends Recipe {

    /**
     * Creates a new shaped recipe.
     *
     * @param result      the result of this recipe
     * @param ingredients the ingredients required
     */
    public ShapedRecipe(final ItemStack result, final List<Item> ingredients) {
        super(result, ingredients);
    }

    /**
     * Validates the given compile format to ensure it is within limits.
     *
     * @param format the compile format
     */
    private static void validateCompileFormat(final String format) {
        if (format == null || format.isEmpty()) {
            throw new IllegalArgumentException("A non-empty format must be provided to be compiled.");
        }
    }

    /**
     * Validates the given compile ingredients to ensure they are within limits.
     *
     * @param ingredients the ingredients
     */
    private static void validateCompileIngredients(final Item[] ingredients) {
        if (ingredients == null) {
            throw new IllegalArgumentException("A shaped recipe must have at least one ingredient.");
        }

        if (Arrays.stream(ingredients).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Empty spots in a shaped recipe format should be represented by whitespace, there should be no " +
                            "null entries in the format items.");
        }
    }

    /**
     * Compiles a format string into a recipe.
     * <p>
     * Unique characters in the format will be mapped to items in order of provision. Once every character was
     * mapped, the recipe format will have characters replaced with the mapped items. Whitespace characters are
     * always considered as `null`. The finalized recipe is then returned.
     * <p>
     *
     * @param result the result of the recipe
     * @param format the format
     * @param items  the items that will be used in the format, there must be one item for each unique character
     *               in the format string (excluding whitespace)
     * @return the compiled recipe
     */
    public static ShapedRecipe compile(final ItemStack result, final String format, final Item... items) {
        ShapedRecipe.validateCompileFormat(format);
        ShapedRecipe.validateCompileIngredients(items);

        final char[] formatChars;
        final Deque<Item> formatIngredients;
        final Map<Character, Item> formatCharToIngredient;
        final List<Item> compiledIngredients;

        formatChars = format.toCharArray();
        formatIngredients = new LinkedList<>();
        formatCharToIngredient = new HashMap<>();
        compiledIngredients = new ArrayList<>();

        Collections.addAll(formatIngredients, items);

        for (final char ch : formatChars) {
            if (Character.isWhitespace(ch)) {
                compiledIngredients.add(null);
                continue;
            }

            final Item ingredient;
            ingredient = formatCharToIngredient.computeIfAbsent(ch, unmatchedCh -> {
                if (formatIngredients.isEmpty()) {
                    throw new IllegalArgumentException(
                            "There are not enough format items to feed all unique format characters. Unmatched " +
                                    "character: " + unmatchedCh);
                }


                return formatIngredients.poll();
            });

            compiledIngredients.add(ingredient);
        }

        return new ShapedRecipe(result, compiledIngredients);
    }

    @Override
    public boolean canCraftWith(final List<Item> givenIngredients) {
        final int requiredIngredientsCount;
        requiredIngredientsCount = this.getIngredients().size();

        if (givenIngredients.size() != requiredIngredientsCount) {
            return false;
        }

        for (int i = 0; i < requiredIngredientsCount; i++) {
            final Item givenIngredient;
            final Item requiredIngredient;

            givenIngredient = this.getIngredients().get(i);
            requiredIngredient = givenIngredients.get(i);

            if (givenIngredient != requiredIngredient) {
                return false;
            }

        }

        return true;
    }

}
