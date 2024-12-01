package ca.bcit.comp2522.games.game.crafter;

import ca.bcit.comp2522.games.game.GuiGameController;
import ca.bcit.comp2522.games.game.crafter.gui.CrafterMainScreen;
import ca.bcit.comp2522.games.game.crafter.inventory.PaginatedInventory;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import ca.bcit.comp2522.games.game.crafter.item.Items;
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

        // TODO test:
        this.inventory.addItemStack(new ItemStack(Items.DIRT, 2));
        this.inventory.addItemStack(new ItemStack(Items.STICK, 2));
        this.inventory.addItemStack(new ItemStack(Items.DIAMOND, 2));
        this.inventory.addItemStack(new ItemStack(Items.BEACON, 2));
        this.inventory.addItemStack(new ItemStack(Items.CHARCOAL, 2));
        this.inventory.addItemStack(new ItemStack(Items.ENCHANTED_GEM, 2));
        this.inventory.addItemStack(new ItemStack(Items.OBSIDIAN, 2));
        this.inventory.addItemStack(new ItemStack(Items.COAL, 2));
        this.inventory.addItemStack(new ItemStack(Items.IRON_INGOT, 2));
        this.inventory.addItemStack(new ItemStack(Items.COBBLESTONE, 2));
        this.inventory.addItemStack(new ItemStack(Items.BLAZE_POWDER, 2));
        this.inventory.addItemStack(new ItemStack(Items.SAPLING, 2));
        this.inventory.addItemStack(new ItemStack(Items.LAPIS_LAZULI, 2));

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
