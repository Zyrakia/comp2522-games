package ca.bcit.comp2522.games.game.crafter.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Represents the event fired when the crafting result slot of a rendered crafting list is clicked.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class ResultSlotClickEvent extends Event {

    /**
     * The event type emitted when a result slot is clicked.
     */
    public static final EventType<ResultSlotClickEvent> EVENT = new EventType<>("RESULT_SLOT_CLICK");

    /**
     * Creates a new result slot click event.
     */
    public ResultSlotClickEvent() {
        super(ResultSlotClickEvent.EVENT);
    }

}
