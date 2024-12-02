package ca.bcit.comp2522.games.game.crafter.gui;

import ca.bcit.comp2522.games.game.crafter.gui.event.InventorySlotClickEvent;
import ca.bcit.comp2522.games.game.crafter.gui.event.InventorySlotHoverEvent;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

/**
 * Renders an item stack as an item slot inside a rendered inventory.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class InventorySlotRenderer extends Pane {

    private static final String SLOT_TEXTURE_UID = "slot";

    private final TextureManager textureManager;
    private final ItemStack stack;

    /**
     * Creates a new renderer that renders the given item stack in an inventory slot.
     *
     * @param stack the item stack, can be null for an empty slot
     */
    public InventorySlotRenderer(final ItemStack stack) {
        this.textureManager = TextureManager.getInstance();
        this.stack = stack;

        this.getChildren().add(this.createSlotTexture());
        if (stack != null) {
            this.getChildren().add(new ItemStackRenderer(stack));
        }

        this.getStyleClass().add("slot");
        this.getStyleClass().add(this.stack == null ? "empty" : "filled");

        this.setOnMouseEntered(_ -> this.fireEvent(InventorySlotHoverEvent.hovered(this.stack)));
        this.setOnMouseExited(_ -> this.fireEvent(InventorySlotHoverEvent.unhovered(this.stack)));
        this.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                this.fireEvent(new InventorySlotClickEvent(this.stack));
            }
        });
    }

    /**
     * Creates the background item slot texture.
     *
     * @return the created texture
     */
    private ImageView createSlotTexture() {
        final ImageView texture;
        texture = this.textureManager.getRenderedTexture(InventorySlotRenderer.SLOT_TEXTURE_UID);
        return texture;
    }

}
