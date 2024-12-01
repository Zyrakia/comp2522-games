package ca.bcit.comp2522.games.game.crafter.gui;

import ca.bcit.comp2522.games.game.crafter.CrafterGameController;
import javafx.scene.Parent;

/**
 * Represents the main screen of the crafter game.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class CrafterMainScreen extends Parent {

    private final CrafterGameController gameController;
    private final PaginatedInventoryRenderer inventoryRenderer;

    /**
     * Creates a new main screen that interacts with the given controller
     *
     * @param gameController the controller this screen interacts with
     */
    public CrafterMainScreen(final CrafterGameController gameController) {
        CrafterMainScreen.validateGameController(gameController);

        this.gameController = gameController;
        // TODO HUD
        // TODO crafting grid
        this.inventoryRenderer = new PaginatedInventoryRenderer(gameController.getInventory());

        this.getChildren().addAll(this.inventoryRenderer);
    }

    /**
     * Validates the given game controller to ensure it is valid for use within the main screen.
     *
     * @param gameController the game controller
     */
    private static void validateGameController(final CrafterGameController gameController) {
        if (gameController == null) {
            throw new IllegalArgumentException("A game game controller must be provided.");
        }
    }

}
