package ca.bcit.comp2522.games.game.crafter.inventory;

import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.util.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an inventory that holds an unlimited amount of items in slots.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class Inventory extends Observable<InventorySlotEvent> {

    private List<InventorySlot> slots;

    /**
     * Creates a new empty inventory.
     */
    public Inventory() {
        this.slots = new ArrayList<>();
    }

    /**
     * Adds a single quantity of an item to this inventory.
     *
     * @param item the item to add quantity of
     */
    public void addOne(final Item item) {
        final InventorySlot slot;
        slot = this.getSlotHolding(item);

        if (slot == null) {
            final InventorySlot addedSlot;
            addedSlot = new InventorySlot(item);

            this.slots.add(addedSlot);
            this.emitSlotEvent(InventorySlotEvent.added(addedSlot));
            return;
        }

        slot.addOne();
        this.emitSlotEvent(InventorySlotEvent.changed(slot));
    }

    /**
     * Removes a single quantity of an item to this inventory.
     *
     * @param item the item to remove quantity of
     */
    public void removeOne(final Item item) {
        final InventorySlot slot;
        slot = this.getSlotHolding(item);

        if (slot == null) {
            return;
        }

        if (slot.isMinimumQuantity()) {
            this.slots.remove(slot);
            this.emitSlotEvent(InventorySlotEvent.removed(slot));
            return;
        }

        slot.removeOne();
        this.emitSlotEvent(InventorySlotEvent.changed(slot));
    }

    /**
     * Emits the specified event to observers of this inventory.
     *
     * @param event the event to emit
     */
    private void emitSlotEvent(final InventorySlotEvent event) {
        this.announceUpdate(event);
    }

    /**
     * Returns the slot in this inventory that is holding the specified item.
     *
     * @param item the item to find the slot of
     * @return the slot, or null if this inventory does not contain the item
     */
    public InventorySlot getSlotHolding(final Item item) {
        for (final InventorySlot slot : this.slots) {
            if (slot.getItem().equals(item)) {
                return slot;
            }
        }

        return null;
    }

}
