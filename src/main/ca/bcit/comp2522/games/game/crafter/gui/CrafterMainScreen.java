package ca.bcit.comp2522.games.game.crafter.gui;

import ca.bcit.comp2522.games.game.crafter.CrafterGameController;
import javafx.scene.layout.VBox;

/**
 * Represents the main screen of the crafter game.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class CrafterMainScreen extends VBox {

    private final CrafterGameController gameController;

    /**
     * Creates a new main screen that interacts with the given controller.
     *
     * @param gameController the controller this screen interacts with
     */
    public CrafterMainScreen(final CrafterGameController gameController) {
        CrafterMainScreen.validateGameController(gameController);

        this.gameController = gameController;

        // TODO HUD
        this.getChildren().add(new CraftingListGridRenderer(this.gameController.getCraftingList()));
        this.getChildren().add(new PaginatedInventoryRenderer(this.gameController.getInventory()));
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
