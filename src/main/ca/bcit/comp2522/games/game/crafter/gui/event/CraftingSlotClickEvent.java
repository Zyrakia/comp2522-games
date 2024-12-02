package ca.bcit.comp2522.games.game.crafter.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Represents a click event of a crafting ingredient slot.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class CraftingSlotClickEvent extends Event {

    /**
     * Represents the event type emitted when a crafting slot is clicked.
     */
    public static final EventType<CraftingSlotClickEvent> EVENT = new EventType<>("CRAFTING_SLOT_CLICK");

    private final int ingredientIndex;

    /**
     * Creates a new slot click event.
     *
     * @param ingredientIndex the ingredient index that is represented by the slot that was clicked
     */
    public CraftingSlotClickEvent(final int ingredientIndex) {
        super(CraftingSlotClickEvent.EVENT);
        this.ingredientIndex = ingredientIndex;
    }

    /**
     * Returns the ingredient index that is represented by the slot that emitted this event.
     *
     * @return the ingredient index
     */
    public int getIngredientIndex() {
        return this.ingredientIndex;
    }
}
