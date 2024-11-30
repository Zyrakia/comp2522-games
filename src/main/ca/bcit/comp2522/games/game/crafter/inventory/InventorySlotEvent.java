package ca.bcit.comp2522.games.game.crafter.inventory;

import ca.bcit.comp2522.games.game.crafter.item.ItemStack;

/**
 * Represents an event to a slot within an inventory.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class InventorySlotEvent {

    private final Type type;
    private final int slotIndex;
    private final ItemStack stack;

    /**
     * Creates a new slot event.
     *
     * @param type      the type of event
     * @param slotIndex the slot this event relates to
     * @param stack     the item stack this event relates to
     */
    public InventorySlotEvent(final Type type, final int slotIndex, final ItemStack stack) {
        InventorySlotEvent.validateType(type);
        InventorySlotEvent.validateSlotIndex(slotIndex);
        InventorySlotEvent.validateStack(stack);

        this.type = type;
        this.slotIndex = slotIndex;
        this.stack = stack;
    }

    /**
     * Creates a new slot event of type {@link Type#FILLED}.
     *
     * @param slotIndex the slot index that was filled
     * @param stack     the stack that filled the slot
     * @return the created event
     */
    public static InventorySlotEvent filled(final int slotIndex, final ItemStack stack) {
        return new InventorySlotEvent(Type.FILLED, slotIndex, stack);
    }

    /**
     * Creates a new slot event of type {@link Type#EMPTIED}.
     *
     * @param slotIndex the slot index that was emptied
     * @param stack     the stack that was removed
     * @return the created event
     */
    public static InventorySlotEvent emptied(final int slotIndex, final ItemStack stack) {
        return new InventorySlotEvent(Type.FILLED, slotIndex, stack);
    }

    /**
     * Creates a new slot event of type {@link Type#CHANGED}.
     *
     * @param slotIndex the slot index that was changed
     * @param stack     the updated stack
     * @return the created event
     */
    public static InventorySlotEvent changed(final int slotIndex, final ItemStack stack) {
        return new InventorySlotEvent(Type.FILLED, slotIndex, stack);
    }

    /**
     * Validates the slot event type to ensure it is valid.
     *
     * @param type the event type
     */
    private static void validateType(final Type type) {
        if (type == null) {
            throw new IllegalArgumentException("A slot event must have a type.");
        }
    }

    /**
     * Validates the given slot index to ensure it is within limits for an event.
     *
     * @param slotIndex the slot index
     */
    private static void validateSlotIndex(final int slotIndex) {
        final int minSlot;
        minSlot = 0;

        if (slotIndex < minSlot) {
            throw new IllegalArgumentException(
                    "A slot event cannot occur in slot " + slotIndex + " (min is " + minSlot + ")");
        }
    }

    /**
     * Validates the given item stack to ensure it is within limits for an event.
     *
     * @param stack the item stack
     */
    private static void validateStack(final ItemStack stack) {
        if (stack == null) {
            throw new IllegalArgumentException("A slot event must relate to an item stack.");
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
     * Returns the slot index this event relates to.
     *
     * @return the slot index
     */
    public int getSlotIndex() {
        return this.slotIndex;
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
     * Represents the different types of slot events.
     */
    public enum Type {
        /** Indicates a slot was filled from a previously empty state. */
        FILLED,
        /** Indicates a slot was emptied from a previously filled state. */
        EMPTIED,
        /** Indicates a slot changed from one filled state to another. */
        CHANGED
    }

}
