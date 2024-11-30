package ca.bcit.comp2522.games.game.crafter.inventory;

import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import ca.bcit.comp2522.games.util.Observable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents an inventory that can contain item stacks.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class Inventory extends Observable<InventoryEvent> {

    /**
     * Represents the item stacks that this inventory is holding.
     */
    private final Map<Item, ItemStack> itemStacks;

    /**
     * Creates a new empty inventory.
     */
    public Inventory() {
        this.itemStacks = new LinkedHashMap<>();
    }

    /**
     * Validates the given item stack to ensure it can be used with inventory operations.
     *
     * @param stack the item stack
     */
    private static void validateItemStack(final ItemStack stack) {
        if (stack == null) {
            throw new IllegalArgumentException("Item stack must not be null.");
        }
    }

    /**
     * Validates the given item to ensure it can be used with inventory operations.
     *
     * @param item the item
     */
    private static void validateItem(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null.");
        }
    }

    /**
     * Validates the given item remove amount to ensure it is valid.
     *
     * @param amount the amount
     */
    private static void validateRemoveAmount(final int amount) {
        final int minRemovalAmount = 0;

        if (amount <= minRemovalAmount) {
            throw new IllegalArgumentException("Removal amount must be greater than " + minRemovalAmount);
        }
    }

    /**
     * Adds an item stack to this inventory.
     * <p>
     * If the item already exists in this inventory, it increases the current stack size up to the
     * {@link ItemStack#MAX_STACK_SIZE}. If there is overflow, the remainder is returned.
     *
     * @param stack the stack to add
     * @return the overflow of adding the stack, or 0 if the whole stack was added
     */
    public final int addItemStack(final ItemStack stack) {
        Inventory.validateItemStack(stack);

        final Item item;
        final ItemStack currentStack;

        item = stack.getItem();
        currentStack = this.getStackOf(item);

        if (currentStack == null) {
            this.itemStacks.put(item, stack);
            this.emit(InventoryEvent.Type.ADDED, stack);
            return 0;
        }

        final ItemStack newStack;
        final int overflow;

        newStack = currentStack.plus(stack.getAmount());
        overflow = currentStack.getAmount() + stack.getAmount() - newStack.getAmount();

        this.itemStacks.put(item, newStack);
        this.emit(InventoryEvent.Type.CHANGED, newStack);

        return overflow;
    }

    /**
     * Removes a specific amount of an item from the inventory.
     * <p>
     * If the stack size drops to zero (or below), the item is removed from the inventory entirely.
     *
     * @param item   the item to remove
     * @param amount the amount to remove
     * @return the amount successfully removed, 0 if the item does not exist in this inventory
     */
    public final int removeItem(final Item item, final int amount) {
        Inventory.validateItem(item);
        Inventory.validateRemoveAmount(amount);

        final ItemStack currentStack;
        currentStack = this.getStackOf(item);

        if (currentStack == null) {
            return 0;
        }

        if (amount >= currentStack.getAmount()) {
            this.itemStacks.remove(item);
            this.emit(InventoryEvent.Type.REMOVED, currentStack);

            return currentStack.getAmount();
        }

        final ItemStack newStack;
        newStack = currentStack.minus(amount);

        this.itemStacks.put(item, newStack);
        this.emit(InventoryEvent.Type.CHANGED, newStack);

        return amount;
    }

    /**
     * Announces a new event to observers of this inventory.
     *
     * @param type  the type of event
     * @param stack the related stack
     */
    private void emit(final InventoryEvent.Type type, final ItemStack stack) {
        this.announceUpdate(new InventoryEvent(type, stack));
    }

    /**
     * Returns the item stack in this inventory that is holding the given item, if any.
     *
     * @param item the item to find the stack of
     * @return the item stack, or null
     */
    public final ItemStack getStackOf(final Item item) {
        return this.itemStacks.get(item);
    }

}
