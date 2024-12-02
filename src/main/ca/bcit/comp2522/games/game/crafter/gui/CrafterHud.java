package ca.bcit.comp2522.games.game.crafter.gui;

import ca.bcit.comp2522.games.game.crafter.item.Item;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The heads-up display for Crafter, displays information like the selected item and the game progress.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class CrafterHud extends HBox {

    private final Label itemNameLabel;
    private final Label itemDescText;

    /**
     * Creates a new heads-up display for the crafter game.
     */
    public CrafterHud() {

        final VBox metaContainer;
        metaContainer = this.createItemMetaContainer();

        this.itemNameLabel = this.createItemNameLabel();
        this.itemDescText = this.createItemDescLabel();

        metaContainer.getChildren().addAll(this.itemNameLabel, this.itemDescText);
        this.getChildren().add(metaContainer);
    }

    /**
     * Creates the metadata container to hold the item name and description.
     *
     * @return the created container
     */
    private VBox createItemMetaContainer() {
        final VBox container;
        container = new VBox();

        container.getStyleClass().add("item-meta-container");

        return container;
    }

    /**
     * Creates the label that will hold the name of the selected item.
     *
     * @return the created label
     */
    private Label createItemNameLabel() {
        final Label label;
        label = new Label();

        label.getStyleClass().add("item-name");

        return label;
    }

    /**
     * Creates the text that will hold the description of the selected item.
     *
     * @return the created text
     */
    private Label createItemDescLabel() {
        final Label text;
        text = new Label();

        text.getStyleClass().add("item-desc");

        return text;
    }

    /**
     * Renders information of the given item on the HUD.
     *
     * @param item the item, can be null
     */
    public void selectItem(final Item item) {
        if (item == null) {
            this.itemNameLabel.setText("");
            this.itemDescText.setText("");
        } else {
            this.itemNameLabel.setText(item.getName());
            this.itemDescText.setText(item.getDescription());
        }
    }

    /**
     * Clears the selected item information.
     */
    public void clearSelectedItem() {
        this.selectItem(null);
    }

}
