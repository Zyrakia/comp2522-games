package ca.bcit.comp2522.games.game.crafter.gui;

import ca.bcit.comp2522.games.game.crafter.crafting.CraftResult;
import ca.bcit.comp2522.games.game.crafter.crafting.CraftingList;
import ca.bcit.comp2522.games.game.crafter.gui.event.CraftingSlotClickEvent;
import ca.bcit.comp2522.games.game.crafter.gui.event.InventorySlotClickEvent;
import ca.bcit.comp2522.games.game.crafter.gui.event.ResultSlotClickEvent;
import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Renders a crafting list as a grid with a result slot.
 * <p>
 * This will render the list as a square grid, as such, it does not work with lists that do not have a capacity that
 * can be translated into a square.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class CraftingListGridRenderer extends HBox {

    final CraftingList craftingList;
    final int dimensionSize;

    private final GridPane ingredientsGrid;
    private final VBox resultContainer;

    /**
     * Creates a new square grid renderer for a crafting list.
     *
     * @param craftingList the crafting list to be rendered
     */
    public CraftingListGridRenderer(final CraftingList craftingList) {
        CraftingListGridRenderer.validateCraftingList(craftingList);

        this.craftingList = craftingList;
        this.dimensionSize = CraftingListGridRenderer.calculateDimensionSize(this.craftingList);

        this.ingredientsGrid = this.createIngredientsGrid();
        this.resultContainer = this.createResultContainer();

        this.getStyleClass().add("crafting-container");
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(this.ingredientsGrid, this.resultContainer);

        this.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene == null) {
                this.detachFromList();
            } else {
                this.attachToList();
            }
        });

        if (this.sceneProperty().isNotNull().get()) {
            this.attachToList();
        }
    }

    /**
     * Validates the given crafting list to ensure it can be rendered.
     *
     * @param craftingList the crafting list
     */
    private static void validateCraftingList(final CraftingList craftingList) {
        if (craftingList == null) {
            throw new IllegalArgumentException("A null crafting list cannot be rendered.");
        }
    }

    /**
     * Validates the given crafting list to ensure it can be rendered as a square grid.
     *
     * @param list the list to render
     * @return the calculated dimension size
     */
    public static int calculateDimensionSize(final CraftingList list) {
        final double dimensionSize;
        dimensionSize = Math.sqrt(list.getCapacity());

        if (Math.floor(dimensionSize) != dimensionSize) {
            throw new IllegalArgumentException("A list with a non-square capacity cannot be rendered.");
        }

        return (int) dimensionSize;
    }

    /**
     * Creates the container for the rendered items in the crafting list.
     *
     * @return the created grid
     */
    private GridPane createIngredientsGrid() {
        final GridPane grid;
        grid = new GridPane();

        grid.getStyleClass().add("ingredients-grid");

        return grid;
    }

    /**
     * Creates the container for the rendered inventory result.
     *
     * @return the created container
     */
    private VBox createResultContainer() {
        final VBox container;
        container = new VBox();

        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("result-slot-container");
        container.addEventHandler(InventorySlotClickEvent.EVENT, (event) -> {
            event.consume();
            this.fireEvent(new ResultSlotClickEvent());
        });

        return container;
    }

    /**
     * Re-renders the current state of the list.
     */
    private void render() {
        final CraftResult res;
        final List<Item> items;
        final List<InventorySlotRenderer> renderedItems;

        res = this.craftingList.getCurrentResult();
        items = this.craftingList.getIngredients();
        renderedItems = items.stream().map((item) -> {
            final ItemStack stack;
            if (item == null) {
                stack = null;
            } else {
                stack = new ItemStack(item);
            }

            return new InventorySlotRenderer(stack);
        }).toList();

        this.resultContainer.getChildren().clear();
        this.ingredientsGrid.getChildren().clear();

        this.resultContainer.getChildren().add(new InventorySlotRenderer(res.getStack()));

        for (int i = 0; i < renderedItems.size(); i++) {
            final int ingredientIndex = i;
            final int row;
            final int col;
            final InventorySlotRenderer renderer;

            row = i / this.dimensionSize;
            col = i % this.dimensionSize;
            renderer = renderedItems.get(i);

            renderer.addEventHandler(InventorySlotClickEvent.EVENT, (event) -> {
                event.consume();
                this.fireEvent(new CraftingSlotClickEvent(ingredientIndex));
            });

            this.ingredientsGrid.add(renderer, col, row);
        }
    }

    /**
     * Handles updates to the automatic craft result from the crafting list.
     *
     * @param res the crafting result
     */
    private void handleResultUpdate(final CraftResult res) {
        this.render();
    }

    /**
     * Attaches to the list to begin listening for events, and performs an initial render to ensure the view is up
     * to date.
     */
    private void attachToList() {
        this.craftingList.observe(this::handleResultUpdate);
        this.render();
    }

    /**
     * Detaches from the list, disabling automatic rendering based on updates from the list.
     */
    private void detachFromList() {
        this.craftingList.unobserve(this::handleResultUpdate);
    }

}
