package ca.bcit.comp2522.games.game.crafter;

import ca.bcit.comp2522.games.game.GuiGameController;
import ca.bcit.comp2522.games.game.crafter.gui.CrafterMainScreen;
import ca.bcit.comp2522.games.game.crafter.inventory.PaginatedInventory;
import javafx.scene.Parent;

/**
 * Represents the controller for the third game, Crafter, which is the third game.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class CrafterGameController extends GuiGameController {

    private static final int INV_PAGE_SIZE = 9;

    private final PaginatedInventory inventory;

    /**
     * Creates a new crafter game controller.
     */
    public CrafterGameController() {
        super("Crafter", "Craft your way to the forsaken materials!");

        this.inventory = new PaginatedInventory(CrafterGameController.INV_PAGE_SIZE);
        this.addStylesheet("crafter.css");
    }

    @Override
    protected void preRenderSetup() {

    }

    @Override
    protected Parent getInitialRoot() {
        return new CrafterMainScreen(this);
    }

    @Override
    protected void onFinish() {
    }

    /**
     * Returns the inventory.
     *
     * @return the inventory
     */
    public PaginatedInventory getInventory() {
        return this.inventory;
    }

}
