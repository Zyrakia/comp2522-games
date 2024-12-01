package ca.bcit.comp2522.games.game.crafter.gui;

import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Renders an item stack as an item slot inside a rendered inventory.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class InventorySlotRenderer extends StackPane {

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
