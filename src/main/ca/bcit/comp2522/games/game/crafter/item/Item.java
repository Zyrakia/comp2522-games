package ca.bcit.comp2522.games.game.crafter.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the held type of item stack within crafter.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class Item {

    private static final int MAX_NAME_LEN = 24;
    private static final int MAX_DESC_LEN = 64;

    /**
     * Registered item instances mapped by their UID.
     */
    private static final Map<String, Item> ITEMS = new HashMap<>();

    private final String name;
    private final String description;
    private final String uid;

    /**
     * Creates a new item.
     *
     * @param name        the name
     * @param description the description
     */
    public Item(String name, String description) {
        Item.validateName(name);
        Item.validateDescription(description);

        this.name = name;
        this.description = description;
        this.uid = Item.generateId(this.name);

        Item.register(this);
    }

    /**
     * Validates the given item name to ensure it is within limits.
     *
     * @param name the item name
     */
    private static void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Every item must have a name.");
        }

        if (name.length() > Item.MAX_NAME_LEN) {
            throw new IllegalArgumentException("An item name must be at most " + Item.MAX_NAME_LEN + " characters.");
        }

        final char[] chars;
        chars = name.toCharArray();

        for (final char c : chars) {
            if (!Character.isSpaceChar(c) && Character.isWhitespace(c)) {
                throw new IllegalArgumentException("Item names cannot contain non-space whitespace.");
            }
        }
    }

    /**
     * Validates the given description to ensure it is within limits.
     *
     * @param description the description
     */
    private static void validateDescription(final String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Every item must have a description.");
        }

        if (description.length() > Item.MAX_DESC_LEN) {
            throw new IllegalArgumentException(
                    "An item description must be at most " + Item.MAX_DESC_LEN + " characters.");
        }
    }

    /**
     * Registers an item as a usable item.
     *
     * @param item the item to register
     */
    private static void register(final Item item) {
        final String uid;
        uid = item.getUID();

        if (Item.ITEMS.containsKey(uid)) {
            throw new IllegalArgumentException("Item UID \"" + uid + "\" is already taken.");
        }

        Item.ITEMS.put(uid, item);
    }

    /**
     * Generates the item UID associated with the given name.
     *
     * @param name the name
     * @return the generated UID
     */
    private static String generateId(final String name) {
        return name.trim().toLowerCase().replaceAll(" ", "_");
    }

    /**
     * Returns the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the uid.
     *
     * @return the uid
     */
    public String getUID() {
        return this.uid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Item item)) return false;
        return Objects.equals(this.getUID(), item.getUID());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getUID());
    }

}
