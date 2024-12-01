package ca.bcit.comp2522.games.game.crafter.gui;

import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Renders an item stack with the quantity.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class ItemStackRenderer extends StackPane {

    private final TextureManager textureManager;
    private final ItemStack stack;

    /***
     * Creates a new renderer that renders the given item stack.
     *
     * @param stack the item stack
     */
    public ItemStackRenderer(final ItemStack stack) {
        ItemStackRenderer.validateItemStack(stack);

        this.textureManager = TextureManager.getInstance();
        this.stack = stack;

        this.getStyleClass().add("item-stack-container");
        this.getChildren().add(this.createItemTexture());

        if (stack.getAmount() != 1) {
            final Label amountLabel;
            amountLabel = this.createAmountLabel();

            StackPane.setAlignment(amountLabel, Pos.BOTTOM_RIGHT);
            this.getChildren().add(amountLabel);
        }
    }

    /**
     * Validates the given item stack to ensure it can be rendered.
     *
     * @param stack the item stack
     */
    private static void validateItemStack(final ItemStack stack) {
        if (stack == null) {
            throw new IllegalArgumentException("Cannot render a null item stack.");
        }
    }

    /**
     * Creates the item texture.
     *
     * @return the created texture
     */
    private ImageView createItemTexture() {
        final String itemUID;
        final ImageView texture;

        itemUID = this.stack.getItem().getUID();
        texture = this.textureManager.getRenderedTexture(itemUID);

        texture.getStyleClass().add("item-texture");

        return texture;
    }

    /**
     * Creates the item stack amount label.
     *
     * @return the created label
     */
    private Label createAmountLabel() {
        final Label amountLabel;
        amountLabel = new Label();

        amountLabel.setText(String.valueOf(this.stack.getAmount()));
        amountLabel.getStyleClass().add("item-amount-label");

        return amountLabel;
    }

}
