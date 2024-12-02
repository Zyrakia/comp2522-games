package ca.bcit.comp2522.games.game.crafter;

import ca.bcit.comp2522.games.game.GuiGameController;
import ca.bcit.comp2522.games.game.crafter.crafting.CraftResult;
import ca.bcit.comp2522.games.game.crafter.crafting.CraftingList;
import ca.bcit.comp2522.games.game.crafter.gui.CrafterMainScreen;
import ca.bcit.comp2522.games.game.crafter.inventory.PaginatedInventory;
import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
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
    private final CraftingList craftingList;

    /**
     * Creates a new crafter game controller.
     */
    public CrafterGameController() {
        super("Crafter", "Craft your way to the forsaken materials!");

        this.inventory = new PaginatedInventory(CrafterGameController.INV_PAGE_SIZE);
        this.craftingList = new CraftingList(CrafterGameController.INV_PAGE_SIZE);

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

    /**
     * Returns the crafting list.
     *
     * @return the crafting list
     */
    public CraftingList getCraftingList() {
        return this.craftingList;
    }

    /**
     * Attempts to craft with the current ingredients of the crafting list.
     */
    public void attemptCraft() {
        final CraftResult result;
        result = this.craftingList.getCurrentResult();

        if (result == null || !result.isSuccessful()) {
            return;
        }

        final ItemStack resultStack;
        resultStack = result.getStack();

        this.craftingList.clear();
        this.inventory.addItemStack(resultStack);
    }

    /**
     * Adds an item from the inventory to the crafting list at the given index.
     * <p>
     * If the item is not in this inventory, nothing happens.
     *
     * @param ingredient the ingredient to add
     * @param index      the index to add the ingredient at
     */
    public void addCraftingIngredient(final Item ingredient, final int index) {
        final int inventoryAmountToRemove;
        final int inventoryAmountRemoved;

        inventoryAmountToRemove = 1;
        inventoryAmountRemoved = this.inventory.removeItem(ingredient, inventoryAmountToRemove);

        if (inventoryAmountRemoved != inventoryAmountToRemove) {
            return;
        }

        final Item replacedItem;
        replacedItem = this.craftingList.setItem(ingredient, index);

        if (replacedItem != null) {
            this.inventory.addItemStack(new ItemStack(replacedItem));
        }
    }

    /**
     * Removes an item from the crafting list, and adds it back into the inventory.
     *
     * @param index the index to remove
     */
    public void removeCraftingIngredient(final int index) {
        final Item removedItem;
        removedItem = this.craftingList.removeItem(index);

        if (removedItem == null) {
            return;
        }

        this.inventory.addItemStack(new ItemStack(removedItem));
    }

}
