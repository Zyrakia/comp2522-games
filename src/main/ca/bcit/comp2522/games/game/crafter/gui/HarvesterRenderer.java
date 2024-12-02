package ca.bcit.comp2522.games.game.crafter.gui;

import ca.bcit.comp2522.games.game.crafter.Harvester;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.function.Function;

/**
 * Renders a harvester with a texture for each stage.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class HarvesterRenderer extends StackPane {

    private static final String HARVEST_TEXTURE_PREFIX = "harvest-";

    private final Harvester harvester;
    private final Function<Integer, ImageView> textureProvider;

    /**
     * Creates a new harvester renderer.
     *
     * @param harvester       the harvester to render
     * @param textureProvider the function to retrieve the texture for each stage
     */
    public HarvesterRenderer(final Harvester harvester, final Function<Integer, ImageView> textureProvider) {
        HarvesterRenderer.validateHarvester(harvester);
        HarvesterRenderer.validateTextureProvider(textureProvider);

        this.harvester = harvester;
        this.textureProvider = textureProvider;

        this.getStyleClass().add("harvester-container");

        this.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene == null) {
                this.detachFromHarvester();
            } else {
                this.attachToHarvester();
            }
        });

        if (this.sceneProperty().isNotNull().get()) {
            this.attachToHarvester();
        }
    }

    /**
     * The default texture provider, renders textures in the format:
     * <p>
     * "{@link HarvesterRenderer#HARVEST_TEXTURE_PREFIX}{stage}".
     *
     * @param stage the harvester stage index
     * @return the rendered texture
     */
    public static ImageView defaultTextureProvider(final Integer stage) {
        final String uid;
        final ImageView texture;

        uid = HarvesterRenderer.HARVEST_TEXTURE_PREFIX + stage;
        texture = TextureManager.getInstance().getRenderedTexture(uid);

        texture.getStyleClass().add("harvester-texture");

        return texture;
    }

    /**
     * Validates the given harvester to ensure it can be rendered.
     *
     * @param harvester the harvester
     */
    private static void validateHarvester(final Harvester harvester) {
        if (harvester == null) {
            throw new IllegalArgumentException("Harvester to render cannot be null!");
        }
    }

    /**
     * Validates the given texture provider to ensure it can provide textures.
     *
     * @param textureProvider the texture provider
     */
    private static void validateTextureProvider(final Function<Integer, ImageView> textureProvider) {
        if (textureProvider == null) {
            throw new IllegalArgumentException("Texture provider cannot be null!");
        }
    }

    /**
     * Attaches to the harvester to start listening for stage updates. This will perform a render to ensure the
     * render is up to date with the harvester.
     */
    private void attachToHarvester() {
        this.harvester.observe(this::handleStageUpdate);
        this.render();
    }

    /**
     * Detaches from the harvester and stops listening to stage updates.
     */
    private void detachFromHarvester() {
        this.harvester.unobserve(this::handleStageUpdate);
    }

    /**
     * Renders the current texture.
     */
    private void render() {
        this.getChildren().clear();
        this.getChildren().add(this.getCurrentStageTexture());
    }

    /**
     * Handles a stage update from the harvester.
     *
     * @param stage the new stage of the harvester
     */
    private void handleStageUpdate(final Integer stage) {
        this.render();
    }

    /**
     * Returns the texture associated with the current stage of the harvester.
     *
     * @return the current stage texture
     */
    private ImageView getCurrentStageTexture() {
        return this.textureProvider.apply(this.harvester.getCurrentStage());
    }

}
