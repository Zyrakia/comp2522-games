package ca.bcit.comp2522.games.game.crafter.gui.event;

import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Represents a hover state event of a rendered inventory slot.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class InventorySlotHoverEvent extends Event {

    /**
     * Fired when an inventory slot is hovered.
     */
    public static final EventType<InventorySlotHoverEvent> HOVER = new EventType<>(Event.ANY, "SLOT_HOVER");

    /**
     * Fired when an inventory slot is unhovered.
     */
    public static final EventType<InventorySlotHoverEvent> UNHOVER = new EventType<>(Event.ANY, "SLOT_UNHOVER");

    private final ItemStack containedStack;

    /**
     * Creates a new slot hover event of the given hover state.
     *
     * @param state          the hover state of this event
     * @param containedStack the stack that is in the slot that emitted this event
     */
    private InventorySlotHoverEvent(final EventType<InventorySlotHoverEvent> state, final ItemStack containedStack) {
        super(state);
        this.containedStack = containedStack;
    }

    /**
     * Creates a new slot hover event indicating that the slot was hovered.
     *
     * @param containedStack the stack that is in the slot that emitted this event
     * @return the created event
     */
    public static InventorySlotHoverEvent hovered(final ItemStack containedStack) {
        return new InventorySlotHoverEvent(InventorySlotHoverEvent.HOVER, containedStack);
    }

    /**
     * Creates a new slot hover event indicating that the slot was unhovered.
     *
     * @param containedStack the stack that is in the slot that emitted this event
     * @return the created event
     */
    public static InventorySlotHoverEvent unhovered(final ItemStack containedStack) {
        return new InventorySlotHoverEvent(InventorySlotHoverEvent.UNHOVER, containedStack);
    }

    /**
     * Determines whether the type of this event is the type emitted by a slot being hovered.
     *
     * @return whether the slot that emitted this event is being hovered
     */
    public boolean isHovered() {
        return this.getEventType() == InventorySlotHoverEvent.HOVER;
    }

    /**
     * Returns the item stack contained in the slot that emitted this event.
     *
     * @return the stack in the slot at the time of the event
     */
    public ItemStack getContainedStack() {
        return this.containedStack;
    }
}
