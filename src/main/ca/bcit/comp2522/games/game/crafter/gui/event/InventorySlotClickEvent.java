package ca.bcit.comp2522.games.game.crafter.gui.event;

import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Represents a click event of an inventory slot.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class InventorySlotClickEvent extends Event {

    /**
     * Represents the event type emitted when an inventory slot is clicked.
     */
    public static final EventType<InventorySlotClickEvent> EVENT = new EventType<>("INVENTORY_SLOT_CLICK");

    private final ItemStack containedStack;

    /**
     * Creates a new inventory slot click event.
     *
     * @param containedStack the stack inside the inventory slot that was clicked, can be null
     */
    public InventorySlotClickEvent(final ItemStack containedStack) {
        super(InventorySlotClickEvent.EVENT);
        this.containedStack = containedStack;
    }

    /**
     * Returns the stack contained in the inventory slot that emitted this event.
     *
     * @return the stack
     */
    public ItemStack getContainedStack() {
        return this.containedStack;
    }

}
