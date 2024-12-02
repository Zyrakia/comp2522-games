package ca.bcit.comp2522.games.game.crafter.gui;

import ca.bcit.comp2522.games.game.GuiGameController;
import ca.bcit.comp2522.games.game.crafter.CrafterGameController;
import ca.bcit.comp2522.games.game.crafter.gui.event.CraftingSlotClickEvent;
import ca.bcit.comp2522.games.game.crafter.gui.event.InventorySlotClickEvent;
import ca.bcit.comp2522.games.game.crafter.gui.event.InventorySlotHoverEvent;
import ca.bcit.comp2522.games.game.crafter.gui.event.ResultSlotClickEvent;
import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * Represents the main screen of the crafter game.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class CrafterMainScreen extends StackPane {

    private static final ImageView BACKGROUND;

    static {
        BACKGROUND = TextureManager.getInstance().getRenderedTexture("background");
        BACKGROUND.setFitWidth(GuiGameController.WIDTH);
        BACKGROUND.setFitHeight(GuiGameController.HEIGHT);
    }

    private final CrafterGameController gameController;
    private final CrafterHud hud;

    private final HarvesterRenderer harvesterRenderer;
    private final CraftingListGridRenderer craftingGridRenderer;
    private final PaginatedInventoryRenderer inventoryRenderer;

    private Item heldItem;
    private ItemStackRenderer heldItemRenderer;

    /**
     * Creates a new main screen that interacts with the given controller.
     *
     * @param gameController the controller this screen interacts with
     */
    public CrafterMainScreen(final CrafterGameController gameController) {
        CrafterMainScreen.validateGameController(gameController);

        this.gameController = gameController;
        this.hud = new CrafterHud();
        this.harvesterRenderer = new HarvesterRenderer(this.gameController.getHarvester(),
                                                       HarvesterRenderer::defaultTextureProvider);
        this.craftingGridRenderer = new CraftingListGridRenderer(this.gameController.getCraftingList());
        this.inventoryRenderer = new PaginatedInventoryRenderer(this.gameController.getInventory());

        this.craftingGridRenderer.addEventHandler(CraftingSlotClickEvent.EVENT, this::handleCraftSlotClick);
        this.craftingGridRenderer.addEventHandler(ResultSlotClickEvent.EVENT,
                                                  (_) -> this.gameController.attemptCraft());
        this.inventoryRenderer.addEventHandler(InventorySlotClickEvent.EVENT, this::handleInventorySlotClick);
        this.harvesterRenderer.setOnMouseClicked((_) -> this.gameController.performHarvest());

        this.setOnMouseMoved(this::handleGlobalMouseMove);
        this.setOnMouseClicked(this::handleGlobalMouseClick);
        this.addEventHandler(InventorySlotHoverEvent.HOVER, this::handleSlotHoverChange);
        this.addEventHandler(InventorySlotHoverEvent.UNHOVER, this::handleSlotHoverChange);

        this.getChildren().addAll(CrafterMainScreen.BACKGROUND, this.createRoot());
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

    private GridPane createRoot() {
        final GridPane root;
        final HBox topPane;

        root = new GridPane();
        topPane = new HBox();

        final double topPaneHudPerc = 0.67;
        final double topPaneHarvesterPerc = 0.33;

        this.hud.prefWidthProperty().bind(topPane.widthProperty().multiply(topPaneHudPerc));
        this.harvesterRenderer.prefWidthProperty().bind(topPane.widthProperty().multiply(topPaneHarvesterPerc));
        topPane.getChildren().addAll(this.hud, this.harvesterRenderer);

        root.getStyleClass().add("crafter-root");
        root.addRow(root.getRowCount(), topPane);
        root.addRow(root.getRowCount() + 1, this.craftingGridRenderer);
        root.addRow(root.getRowCount() + 1, this.inventoryRenderer);
        GridPane.setVgrow(topPane, Priority.ALWAYS);
    }

    /**
     * Handles a slot hover or unhover event, updating the HUD accordingly.
     *
     * @param event the event to handle
     */
    private void handleSlotHoverChange(final InventorySlotHoverEvent event) {
        if (!event.isHovered()) {
            this.hud.selectItem(this.heldItem);
            return;
        }

        final ItemStack slotStack;
        final Item hoveredItem;

        slotStack = event.getContainedStack();

        if (slotStack == null) {
            hoveredItem = this.heldItem;
        } else {
            hoveredItem = slotStack.getItem();
        }

        this.hud.selectItem(hoveredItem);
    }

    /**
     * Handles a slot in the inventory being clicked.
     *
     * @param event the click event instance
     */
    private void handleInventorySlotClick(final InventorySlotClickEvent event) {
        if (this.heldItem != null) {
            this.setHeldItem(null);
            return;
        }

        final ItemStack containedStack;
        containedStack = event.getContainedStack();

        if (containedStack == null) {
            this.setHeldItem(null);
        } else {
            this.setHeldItem(containedStack.getItem());
        }
    }

    /**
     * Handles a slot in the crafting grid being clicked.
     *
     * @param event the click event instance
     */
    private void handleCraftSlotClick(final CraftingSlotClickEvent event) {
        final int ingredientIndex;
        ingredientIndex = event.getIngredientIndex();

        if (this.heldItem == null) {
            this.gameController.removeCraftingIngredient(ingredientIndex);
        } else {
            this.gameController.addCraftingIngredient(this.heldItem, ingredientIndex);
        }
    }

    /**
     * Handles a mouse movement anywhere on the main screen.
     *
     * @param event the mouse move event
     */
    private void handleGlobalMouseMove(final MouseEvent event) {
        this.updateHeldItemPosition(event.getX(), event.getY());
    }

    /**
     * Updates the position of the current held item renderer.
     *
     * @param localX the target X position within this node
     * @param localY the target Y position within this node
     */
    private void updateHeldItemPosition(final double localX, final double localY) {
        final ItemStackRenderer renderer;
        renderer = this.getHeldItemRenderer();

        if (renderer == null) {
            return;
        }

        // By default, the item is centered (stack pane), so we need to offset it
        renderer.setTranslateX(localX - this.getWidth() / 2);
        renderer.setTranslateY(localY - this.getHeight() / 2);
    }

    /**
     * Handles a mouse click anywhere on the main screen.
     *
     * @param event the mouse click event
     */
    private void handleGlobalMouseClick(final MouseEvent event) {
        // Hotkey for clearing held item
        if (event.getButton() == MouseButton.SECONDARY) {
            this.setHeldItem(null);
        }
    }

    /**
     * Sets the currently held item to the given item.
     *
     * @param item the held item
     */
    private void setHeldItem(final Item item) {
        this.clearHeldItemRenderer();
        this.heldItem = item;
    }

    /**
     * Returns the current floating renderer of the held item stack, if there is a held item stack.
     *
     * @return the renderer for the held item stack
     */
    private ItemStackRenderer getHeldItemRenderer() {
        if (this.heldItem == null) {
            return null;
        }

        if (this.heldItemRenderer != null) {
            return this.heldItemRenderer;
        }

        this.heldItemRenderer = new ItemStackRenderer(new ItemStack(this.heldItem));
        this.getChildren().add(this.heldItemRenderer);
        this.heldItemRenderer.setMouseTransparent(true);
        this.heldItemRenderer.toFront();

        return this.heldItemRenderer;
    }

    /**
     * Removes the current held item stack renderer.
     */
    private void clearHeldItemRenderer() {
        final ItemStackRenderer renderer;
        renderer = this.heldItemRenderer;

        this.heldItemRenderer = null;
        if (renderer == null) {
            return;
        }

        this.getChildren().remove(renderer);
    }

}
