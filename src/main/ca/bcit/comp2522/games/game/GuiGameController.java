package ca.bcit.comp2522.games.game;

import ca.bcit.comp2522.games.util.FileWatcher;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a game that is run within a GUI window.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public abstract class GuiGameController extends GameController {

    /**
     * Represents the fixed width of a GUI game window.
     */
    public static final int WIDTH = 600;

    /**
     * Represents the fixed height of a GUI game window.
     */
    public static final int HEIGHT = 400;

    private static final Path STYLES_DIR = Path.of("src", "resources", "styles");
    private final Stage stage;

    private final FileWatcher stylesheetWatcher;
    private final Set<String> stylesheets;

    /**
     * Creates a new game that will be played within a GUI screen.
     *
     * @param name        the name of the game
     * @param description the description of the game
     */
    public GuiGameController(final String name, final String description) {
        super(name, description);

        this.stage = new Stage();
        this.stylesheetWatcher = new FileWatcher((v) -> this.injectStylesheet(v.toUri().toString()));
        this.stylesheets = new HashSet<>();

        this.stage.setTitle(this.getName());
        this.stage.setResizable(false);
    }

    /**
     * Takes a stylesheet name and prefixes it with the stylesheet directory path.
     *
     * @param stylesheet the stylesheet name
     * @return the path to the stylesheet
     */
    private static Path resolveStylesheetPath(final String stylesheet) {
        return GuiGameController.STYLES_DIR.resolve(stylesheet);
    }

    @Override
    protected final void onStart() {
        this.preRenderSetup();
        this.transitionTo(this.getInitialRoot());
        this.stage.toFront();
        this.stage.showAndWait();
    }

    /**
     * Gets called when a game has started and the stage is about to be shown.
     */
    protected abstract void preRenderSetup();

    /**
     * Prepares the initial root that is rendered when a new game begins.
     *
     * @return the initial root
     */
    protected abstract Parent getInitialRoot();

    /**
     * Returns the stage that this game is being played on. Hiding or closing this stage in any way terminates the game.
     *
     * @return the stage
     */
    protected final Stage getStage() {
        return this.stage;
    }

    /**
     * Transitions to a new scene containing the specified root.
     *
     * @param root the root to transition to
     */
    protected final void transitionTo(final Parent root) {
        final Scene scene;
        scene = new Scene(root, GuiGameController.WIDTH, GuiGameController.HEIGHT);

        scene.getStylesheets().addAll(this.stylesheets);

        this.stage.setScene(scene);
    }

    /**
     * Adds the given stylesheet globally to this stage.
     *
     * @param stylesheet the stylesheet file name
     */
    protected void addStylesheet(final String stylesheet) {
        final Path path;
        final String uri;

        path = GuiGameController.resolveStylesheetPath(stylesheet);
        uri = path.toUri().toString();

        this.stylesheets.add(uri);
        this.stylesheetWatcher.watch(path);
        this.injectStylesheet(uri);
    }

    /**
     * Removes the given stylesheet from this stage.
     *
     * @param stylesheet the stylesheet file name
     */
    protected void removeStylesheet(final String stylesheet) {
        final Path path;
        final String uri;

        path = GuiGameController.resolveStylesheetPath(stylesheet);
        uri = path.toUri().toString();

        this.stylesheets.remove(uri);
        this.stylesheetWatcher.unwatch(path);
        this.pullStylesheet(uri);
    }

    /**
     * Hot reloads the given stylesheet on the current scene.
     *
     * @param stylesheet the stylesheet
     */
    private void injectStylesheet(final String stylesheet) {
        final Scene currentScene;
        currentScene = this.getStage().getScene();

        if (currentScene == null) {
            return;
        }

        final List<String> stylesheets = currentScene.getStylesheets();

        if (!stylesheets.contains(stylesheet)) {
            stylesheets.add(stylesheet);
            return;
        }

        stylesheets.remove(stylesheet);

        // Cache busting
        stylesheets.add("data:,");

        stylesheets.add(stylesheet);

        stylesheets.remove("data:,");
    }

    /**
     * Pulls the given stylesheet from the current scene.
     *
     * @param stylesheet the stylesheet
     */
    private void pullStylesheet(final String stylesheet) {
        final Scene currentScene;
        currentScene = this.getStage().getScene();

        if (currentScene == null) {
            return;
        }

        currentScene.getStylesheets().remove(stylesheet);
    }

}
