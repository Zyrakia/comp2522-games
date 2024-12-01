package ca.bcit.comp2522.games.game.crafter;

import ca.bcit.comp2522.games.game.GuiGameController;
import ca.bcit.comp2522.games.game.crafter.crafting.CraftingList;
import ca.bcit.comp2522.games.game.crafter.gui.CrafterMainScreen;
import ca.bcit.comp2522.games.game.crafter.inventory.PaginatedInventory;
import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import ca.bcit.comp2522.games.game.crafter.item.Items;
import javafx.scene.Parent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

        this.populateWithDemoItems();
        this.craftingList.setItem(Items.REDSTONE, 1);
        this.craftingList.setItem(Items.REDSTONE, 3);
        this.craftingList.setItem(Items.DIAMOND, 4);
        this.craftingList.setItem(Items.REDSTONE, 5);
        this.craftingList.setItem(Items.REDSTONE, 7);
        
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

    private void populateWithDemoItems() {
        final Random random;
        random = new Random();

        List<Item> allItems = Arrays.asList(Items.DIRT, Items.GRASS, Items.SAPLING, Items.WOOD, Items.STONE,
                                            Items.COBBLESTONE, Items.PLANK, Items.STICK, Items.COAL,
                                            Items.STONE_PICKAXE, Items.TORCH, Items.IRON_ORE, Items.IRON_INGOT,
                                            Items.CHARCOAL, Items.IRON_PICKAXE, Items.GOLD_ORE, Items.GOLD_INGOT,
                                            Items.REDSTONE, Items.GOLD_SWORD, Items.DIAMOND, Items.OBSIDIAN,
                                            Items.ENCHANTED_GEM, Items.BEACON, Items.BLAZE_POWDER, Items.NETHER_BRICK,
                                            Items.LAPIS_LAZULI);

        for (Item item : allItems) {
            int amount = random.nextInt(ItemStack.MAX_STACK_SIZE) + 1;
            this.inventory.addItemStack(new ItemStack(item, amount));
        }
    }

}
