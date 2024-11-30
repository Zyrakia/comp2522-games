package ca.bcit.comp2522.games.game.crafter.item;

import javafx.scene.image.Image;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages textures for items based on their UID.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class ItemTextureManager {

    private static final Path TEXTURE_DIR = Path.of("src", "resources", "textures");
    private static final String TEXTURE_EXT = "png";
    private static final String FALLBACK_TEXTURE_NAME = "fallback";

    private static ItemTextureManager instance;

    static {
        if (!Files.isDirectory(ItemTextureManager.TEXTURE_DIR)) {
            throw new IllegalStateException("Item textures directory does not exist.");
        }
    }

    private final Image fallbackTexture;
    private final Map<String, Image> loadedTextures;

    /**
     * Creates the texture manager, loading the fallback texture.
     */
    private ItemTextureManager() {
        if (ItemTextureManager.instance != null) {
            throw new IllegalStateException("Item texture manager has already been initialized.");
        }

        this.fallbackTexture = this.createAssociatedTexture(ItemTextureManager.FALLBACK_TEXTURE_NAME);
        this.loadedTextures = new HashMap<>();

        if (this.fallbackTexture.isError()) {
            throw new IllegalStateException("The fallback texture for items was not found.");
        }
    }

    /**
     * Returns the global texture manager instance.
     *
     * @return the texture manager
     */
    public static ItemTextureManager getInstance() {
        if (ItemTextureManager.instance == null) {
            ItemTextureManager.instance = new ItemTextureManager();
        }

        return ItemTextureManager.instance;
    }

    /**
     * Returns the file path of the texture that is associated with the given UID.
     *
     * @param uid the uid to get the file name of
     * @return the texture file path
     */
    private Path resolveTexturePath(final String uid) {
        final String fileName;
        fileName = uid.concat(".").concat(ItemTextureManager.TEXTURE_EXT);

        return ItemTextureManager.TEXTURE_DIR.resolve(fileName);
    }

    /**
     * Returns the image instance holding the texture associated with the given UID.
     *
     * @param uid the uid to get the texture for
     * @return the image holding the associated texture, or null if the texture does not exist
     */
    private Image createAssociatedTexture(final String uid) {
        final URI imageUrl;
        imageUrl = this.resolveTexturePath(uid).toUri();

        return new Image(imageUrl.toString());
    }

    /**
     * Returns the image texture associated with the given UID.
     *
     * @param uid the uid
     * @return the associated texture
     */
    public Image getTexture(final String uid) {
        final Image preloadedTexture;
        preloadedTexture = this.loadedTextures.get(uid);

        if (preloadedTexture != null) {
            return preloadedTexture;
        }

        final Image texture;
        texture = this.createAssociatedTexture(uid);

        if (texture.isError()) {
            return this.fallbackTexture;
        }

        this.loadedTextures.put(uid, texture);
        return texture;
    }

}
