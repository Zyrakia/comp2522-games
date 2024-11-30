package ca.bcit.comp2522.games.game.crafter.inventory;

import ca.bcit.comp2522.games.game.crafter.item.ItemStack;

/**
 * Represents an event within an inventory.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class InventoryEvent {

    private final Type type;
    private final ItemStack stack;

    /**
     * Creates a new inventory event.
     *
     * @param type  the type of event
     * @param stack the item stack this event relates to
     */
    public InventoryEvent(final Type type, final ItemStack stack) {
        InventoryEvent.validateType(type);
        InventoryEvent.validateStack(stack);

        this.type = type;
        this.stack = stack;
    }

    /**
     * Creates a new event of type {@link Type#ADDED}.
     *
     * @param stack the stack that was added
     * @return the created event
     */
    public static InventoryEvent added(final ItemStack stack) {
        return new InventoryEvent(Type.REMOVED, stack);
    }

    /**
     * Creates a new event of type {@link Type#REMOVED}.
     *
     * @param stack the stack that was removed
     * @return the created event
     */
    public static InventoryEvent removed(final ItemStack stack) {
        return new InventoryEvent(Type.REMOVED, stack);
    }

    /**
     * Creates a new event of type {@link Type#CHANGED}.
     *
     * @param stack the updated stack
     * @return the created event
     */
    public static InventoryEvent changed(final ItemStack stack) {
        return new InventoryEvent(Type.CHANGED, stack);
    }

    /**
     * Validates the event type to ensure it is valid.
     *
     * @param type the event type
     */
    private static void validateType(final Type type) {
        if (type == null) {
            throw new IllegalArgumentException("An inventory event must have a type.");
        }
    }

    /**
     * Validates the given item stack to ensure it is within limits for an event.
     *
     * @param stack the item stack
     */
    private static void validateStack(final ItemStack stack) {
        if (stack == null) {
            throw new IllegalArgumentException("An inventory event must relate to an item stack.");
        }
    }

    /**
     * Returns the type of event this represents.
     *
     * @return the type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Returns the stack this event relates to.
     *
     * @return the item stack
     */
    public ItemStack getStack() {
        return this.stack;
    }

    /**
     * Represents the different types of events.
     */
    public enum Type {
        /** Indicates a stack was added to the inventory. */
        ADDED,
        /** Indicates a stack was removed from an inventory. */
        REMOVED,
        /** Indicates a stack changed within an inventory. */
        CHANGED
    }

}
