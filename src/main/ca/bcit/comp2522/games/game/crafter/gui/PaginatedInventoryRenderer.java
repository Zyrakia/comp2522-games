package ca.bcit.comp2522.games.game.crafter.gui;

import ca.bcit.comp2522.games.game.crafter.inventory.InventoryEvent;
import ca.bcit.comp2522.games.game.crafter.inventory.PaginatedInventory;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * Renders a paginated inventory into an interactable node.
 * <p>
 * This will automatically
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class PaginatedInventoryRenderer extends HBox {

    private final PaginatedInventory inventory;
    private final HBox pageContainer;
    private final Button prevButton;
    private final Button nextButton;

    private int currentPage;

    /**
     * Creates a new renderer for a paginated inventory.
     *
     * @param inventory the inventory to render
     */
    public PaginatedInventoryRenderer(final PaginatedInventory inventory) {
        PaginatedInventoryRenderer.validateInventory(inventory);

        this.inventory = inventory;

        this.pageContainer = this.createPageContainer();
        this.prevButton = this.createPrevButton();
        this.nextButton = this.createNextButton();
        this.currentPage = inventory.getMinPageIndex();

        this.getChildren().addAll(this.prevButton, this.pageContainer, this.nextButton);
        this.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene == null) {
                this.detachFromInventory();
            } else {
                this.attachToInventory();
            }
        });

        if (this.sceneProperty().isNotNull().get()) {
            this.attachToInventory();
        }
    }

    /**
     * Validates the given inventory to ensure it can be rendered.
     *
     * @param inventory the inventory to render
     */
    private static void validateInventory(final PaginatedInventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("A null inventory cannot be rendered.");
        }
    }

    /**
     * Creates the container that will be used to display an inventory item page.
     *
     * @return the created grid
     */
    private HBox createPageContainer() {
        final HBox line;
        line = new HBox();

        line.getStyleClass().add("page-container");

        return line;
    }

    /**
     * Creates the button that is used to navigate to the previous inventory page.
     *
     * @return the created button
     */
    private Button createPrevButton() {
        final ImageView prevTexture;
        final Button btn;

        prevTexture = TextureManager.getInstance().getRenderedTexture("prev-arrow");
        btn = new Button();

        btn.getStyleClass().addAll("nav-button", "prev-button");
        btn.setGraphic(prevTexture);
        btn.setOnAction(_ -> this.prevPage());

        return btn;
    }

    /**
     * Creates the button that is  used to navigate to the next inventory page.
     *
     * @return the created button
     */
    private Button createNextButton() {
        final ImageView nextTexture;
        final Button btn;

        nextTexture = TextureManager.getInstance().getRenderedTexture("next-arrow");
        btn = new Button();

        btn.getStyleClass().addAll("nav-button", "next-button");
        btn.setGraphic(nextTexture);
        btn.setOnAction(_ -> this.nextPage());

        return btn;
    }

    /**
     * Renders the current page.
     */
    private void renderCurrentPage() {
        this.pageContainer.getChildren().clear();

        final List<ItemStack> items;
        items = this.inventory.getPage(this.currentPage);

        for (int i = 0; i < this.inventory.getPageSize(); i++) {
            final ItemStack item;

            if (i >= items.size()) {
                item = null;
            } else {
                item = items.get(i);
            }

            this.pageContainer.getChildren().add(new InventorySlotRenderer(item));
        }
    }

    /**
     * Re-renders the inventory to handle an update within the connected inventory.
     *
     * @param e the inventory event, unused
     */
    private void handleInventoryUpdate(final InventoryEvent e) {
        final boolean isPageNowInvalid;
        final boolean doesRequireRender;

        isPageNowInvalid = this.currentPage > this.inventory.getMaxPageIndex();

        if (isPageNowInvalid) this.clampCurrentPage();
        this.renderCurrentPage();
    }

    /**
     * Goes to the next page and renders it, if possible.
     */
    private void nextPage() {
        this.setPage(this.currentPage + 1);
    }

    /**
     * Goes to the previous page and renders, if possible.
     */
    private void prevPage() {
        this.setPage(this.currentPage - 1);
    }

    /**
     * Goes to the specified page and renders it, if possible.
     * <p>
     * This will do nothing if the given page is out of bounds.
     *
     * @param page the page
     */
    private void setPage(final int page) {
        if (page < this.inventory.getMinPageIndex() || page > this.inventory.getMaxPageIndex()) {
            return;
        }

        this.currentPage = page;
        this.renderCurrentPage();
        this.autoDisableNavigationButtons();
    }

    /**
     * Clamps the current page to the maximum page index of the inventory.
     * <p>
     * This should be called when there is no assurance that the current page index still exists within the inventory.
     */
    private void clampCurrentPage() {
        this.currentPage = Math.min(Math.max(this.currentPage, this.inventory.getMinPageIndex()),
                                    this.inventory.getMaxPageIndex());
        this.autoDisableNavigationButtons();
    }

    /**
     * Attaches to the inventory to begin listening for events, and performs an initial render to ensure the view is
     * up to date.
     */
    private void attachToInventory() {
        this.inventory.observe(this::handleInventoryUpdate);
        this.clampCurrentPage();
        this.renderCurrentPage();
    }

    /**
     * Detaches from the inventory, disabling automatic rendering based on updates from the inventory.
     */
    private void detachFromInventory() {
        this.inventory.unobserve(this::handleInventoryUpdate);
    }

    /**
     * Automatically disables either navigation button depending on the current page and the page limits of the
     * inventory.
     */
    private void autoDisableNavigationButtons() {
        this.prevButton.setDisable(this.currentPage == this.inventory.getMinPageIndex());
        this.nextButton.setDisable(this.currentPage == this.inventory.getMaxPageIndex());
    }

}
