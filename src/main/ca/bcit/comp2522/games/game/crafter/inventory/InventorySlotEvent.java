package ca.bcit.comp2522.games.game.crafter.inventory;

/**
 * Represents an update to an inventory slot.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class InventorySlotEvent {

    private final InventorySlot slot;
    private final InventorySlotEvent.Type type;

    /**
     * Creates a new event relating to the given slot, and of the given type.
     *
     * @param slot the updated slot
     * @param type the type of update
     */
    public InventorySlotEvent(final InventorySlot slot, final InventorySlotEvent.Type type) {
        this.slot = slot;
        this.type = type;
    }

    /**
     * Creates a new event indicating that the specified slot has changed.
     *
     * @param slot the updated slot
     * @return the event
     */
    public static InventorySlotEvent changed(InventorySlot slot) {
        return new InventorySlotEvent(slot, Type.CHANGED);
    }

    /**
     * Creates a new event indicating that the specified slot has been added.
     *
     * @param slot the updated slot
     * @return the event
     */
    public static InventorySlotEvent added(InventorySlot slot) {
        return new InventorySlotEvent(slot, Type.ADDED);
    }

    /**
     * Creates a new event indicating that the specified slot has been removed.
     *
     * @param slot the updated slot
     * @return the event
     */
    public static InventorySlotEvent removed(InventorySlot slot) {
        return new InventorySlotEvent(slot, Type.REMOVED);
    }

    /**
     * Returns the slot this event relates to.
     *
     * @return the slot
     */
    public InventorySlot getSlot() {
        return this.slot;
    }

    /**
     * Returns the type of this event.
     *
     * @return the type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Represents the different types of updates that can occur.
     */
    public enum Type {
        /**
         * Indicates that the relating slot was removed from an inventory.
         */
        REMOVED,
        /**
         * Indicates that the relating slot was added to an inventory.
         */
        ADDED,

        /**
         * Indicates that the relating slot was updated in an inventory.
         */
        CHANGED
    }
}
