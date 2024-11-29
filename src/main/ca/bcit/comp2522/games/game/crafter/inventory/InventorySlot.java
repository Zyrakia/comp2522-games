package ca.bcit.comp2522.games.game.crafter.inventory;

import ca.bcit.comp2522.games.game.crafter.item.Item;

/**
 * Represents a slot inside an inventory that contains an item.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class InventorySlot {

    /**
     * Represents the minimum amount of quantity that a slot can hold (1).
     */
    public static final int MIN_QUANTITY = 1;
    private static final int SINGLE_QUANTITY = 1;

    private final Item item;
    private int quantity;

    /**
     * Creates a new inventory slot holding the specified item.
     *
     * @param item            the item in this slot
     * @param initialQuantity the initial quantity of the item
     */
    public InventorySlot(final Item item, final int initialQuantity) {
        InventorySlot.validateQuantity(initialQuantity);

        this.item = item;
        this.quantity = initialQuantity;
    }

    /**
     * Creates a new slot holding the specified item, with a default quantity of {@value InventorySlot#MIN_QUANTITY}.
     *
     * @param item the item in the slot
     */
    public InventorySlot(final Item item) {
        this(item, InventorySlot.MIN_QUANTITY);
    }

    /**
     * Validates the given quantity to ensure it is within limits.
     *
     * @param quantity the quantity to validate
     */
    private static void validateQuantity(final int quantity) {
        if (quantity < InventorySlot.MIN_QUANTITY) {
            throw new IllegalArgumentException("A slot quantity cannot drop below " + InventorySlot.MIN_QUANTITY + ".");
        }
    }

    /**
     * Returns the item in this slot.
     *
     * @return the item
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * Returns the quantity in this slot.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Sets the quantity of items in this slot.
     *
     * @param quantity the new quantity
     */
    void setQuantity(final int quantity) {
        InventorySlot.validateQuantity(quantity);
        this.quantity = quantity;
    }

    /**
     * Adds the given quantity to the current quantity.
     *
     * @param quantity the quantity to add
     */
    void addQuantity(final int quantity) {
        this.setQuantity(this.quantity + quantity);
    }

    /**
     * Adds a single quantity to the current quantity.
     */
    void addOne() {
        this.addQuantity(InventorySlot.SINGLE_QUANTITY);
    }

    /**
     * Removes a single quantity from the current quantity.
     */
    void removeOne() {
        this.removeQuantity(InventorySlot.SINGLE_QUANTITY);
    }

    /**
     * Subtracts the given quantity from the current quantity.
     *
     * @param quantity the quantity to remove
     */
    void removeQuantity(final int quantity) {
        this.setQuantity(this.quantity - quantity);
    }

    /**
     * Returns whether this slot is currently holding the minimum quantity. If it is, removing any will throw an
     * exception.
     *
     * @return whether this slot is at minimum quantity.
     */
    public boolean isMinimumQuantity() {
        return this.quantity == InventorySlot.MIN_QUANTITY;
    }

}
