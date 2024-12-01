package ca.bcit.comp2522.games.game.crafter.crafting;

import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.util.Observable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a fixed-size list that can automatically detect when a valid craft result is available.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class CraftingList extends Observable<CraftResult> {

    /**
     * Represents the minimum capacity a crafting list can have.
     */
    public static final int MIN_SIZE = 1;

    private final CraftingManager craftingManager;
    private final List<Item> items;

    /**
     * Creates a new empty crafting list.
     *
     * @param size the total item capacity of the list
     */
    public CraftingList(final int size) {
        this.craftingManager = CraftingManager.getInstance();
        this.items = CraftingList.generateEmptyList(size);
    }

    /**
     * Validates the given size to ensure it is within limits.
     *
     * @param size the size
     */
    private static void validateSize(final int size) {
        if (size < CraftingList.MIN_SIZE) {
            throw new IllegalArgumentException(
                    "Crafting list size must be greater than " + CraftingList.MIN_SIZE + ".");
        }
    }

    /**
     * Creates a fixed-size, but mutable list of null entries that is of the given size.
     *
     * @param size the size of the list
     * @return the null filled list
     */
    private static List<Item> generateEmptyList(final int size) {
        CraftingList.validateSize(size);
        return Arrays.asList(new Item[size]);
    }

    /**
     * Validates that the given index can be used to index this crafting list.
     *
     * @param index the index
     */
    private void validateIndex(final int index) {
        final int minIndex;
        final int maxIndex;

        minIndex = CraftingList.MIN_SIZE - 1;
        maxIndex = this.getCapacity() - 1;

        if (index < minIndex || index > maxIndex) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " must be between " + minIndex + " and " + maxIndex + ".");
        }
    }

    /**
     * Adds an item to the next empty slot in this crafting list.
     *
     * @param item the item to add
     * @return whether the item could be added, false if the list is full
     */
    public boolean addItem(final Item item) {
        final int firstFree = this.items.indexOf(null);
        if (firstFree == -1) {
            return false;
        }

        this.items.set(firstFree, item);
        this.handleItemsUpdate();

        return true;
    }

    /**
     * Sets a specific index to an item in this crafting list.
     *
     * @param item  the item to set
     * @param index the index to set at
     * @return the item that was replaced, if any
     */
    public Item setItem(final Item item, final int index) {
        this.validateIndex(index);

        final Item replacedItem;
        replacedItem = this.items.set(index, item);

        this.handleItemsUpdate();
        return replacedItem;
    }

    /**
     * Removes the item at a specific index.
     *
     * @param index the index to clear
     * @return the item that removed, if any
     */
    public Item removeItem(final int index) {
        this.validateIndex(index);

        final Item removedItem;
        removedItem = this.items.set(index, null);

        this.handleItemsUpdate();
        return removedItem;
    }

    /**
     * Clears all items in this crafting list.
     */
    public void clear() {
        for (int i = 0; i < this.getCapacity(); i++) {
            this.items.set(i, null);
        }

        this.handleItemsUpdate();
    }

    /**
     * Recalculates potential recipes after an item update.
     */
    private void handleItemsUpdate() {
        final CraftResult res;
        res = this.craftingManager.craft(this.items);
        this.announceUpdate(res);
    }

    /**
     * Returns a read-only view of the items currently in this crafting list.
     *
     * @return the current items
     */
    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    /**
     * Returns the amount of items this crafting list can hold.
     *
     * @return the total capacity
     */
    public int getCapacity() {
        return this.items.size();
    }

    /**
     * Returns the amount of free slots in this crafting list.
     *
     * @return the amount of free slots
     */
    public int getFreeCapacity() {
        return (int) this.items.stream().filter(Objects::isNull).count();
    }

    /**
     * Returns whether this crafting list has no empty slots.
     *
     * @return whether all slots are occupied by an item
     */
    public boolean isFull() {
        return this.items.stream().noneMatch(Objects::isNull);
    }

}
