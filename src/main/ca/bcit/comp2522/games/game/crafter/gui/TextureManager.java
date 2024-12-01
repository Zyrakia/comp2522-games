package ca.bcit.comp2522.games.game.crafter.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages textures based on their UID (the file name without extension).
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class TextureManager {

    /**
     * Represents the pixel width and height of each texture.
     */
    public static final int DEF_TEXTURE_SIZE = 48;

    private static final Path TEXTURE_DIR = Path.of("src", "resources", "textures");
    private static final String TEXTURE_EXT = "png";
    private static final String FALLBACK_TEXTURE_NAME = "fallback";

    private static TextureManager instance;

    static {
        if (!Files.isDirectory(TextureManager.TEXTURE_DIR)) {
            throw new IllegalStateException("Textures directory does not exist.");
        }
    }

    private final Image fallbackTexture;
    private final Map<String, Image> loadedTextures;

    /**
     * Creates the texture manager, loading the fallback texture.
     */
    private TextureManager() {
        if (TextureManager.instance != null) {
            throw new IllegalStateException("Texture manager has already been initialized.");
        }

        this.fallbackTexture = this.createAssociatedTexture(TextureManager.FALLBACK_TEXTURE_NAME);
        this.loadedTextures = new HashMap<>();

        if (this.fallbackTexture.isError()) {
            throw new IllegalStateException("The fallback texture was not found.");
        }
    }

    /**
     * Returns the global texture manager instance.
     *
     * @return the texture manager
     */
    public static TextureManager getInstance() {
        if (TextureManager.instance == null) {
            TextureManager.instance = new TextureManager();
        }

        return TextureManager.instance;
    }

    /**
     * Returns the file path of the texture that is associated with the given UID.
     *
     * @param uid the uid to get the file name of
     * @return the texture file path
     */
    private Path resolveTexturePath(final String uid) {
        final String fileName;
        fileName = uid.concat(".").concat(TextureManager.TEXTURE_EXT);

        return TextureManager.TEXTURE_DIR.resolve(fileName);
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

    /**
     * Generates a {@link ImageView} of the image texture associated with the given UID, with the default texture
     * size applied.
     *
     * @param uid the uid
     * @return the generated view
     */
    public ImageView getRenderedTexture(final String uid) {
        final ImageView view;
        view = new ImageView(this.getTexture(uid));

        view.setFitHeight(TextureManager.DEF_TEXTURE_SIZE);
        view.setFitWidth(TextureManager.DEF_TEXTURE_SIZE);

        return view;
    }

}
