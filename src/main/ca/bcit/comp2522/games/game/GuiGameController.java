package ca.bcit.comp2522.games.game;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents a game that is run within a GUI window.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public abstract class GuiGameController extends GameController {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private final Stage stage;

    /**
     * Creates a new game that will be played within a GUI screen.
     *
     * @param name        the name of the game
     * @param description the description of the game
     */
    public GuiGameController(final String name, final String description) {
        super(name, description);
        this.stage = new Stage();
    }

    @Override
    protected final void onStart() {
        this.stage.setTitle(this.getName());
        this.stage.setResizable(false);
        this.stage.setAlwaysOnTop(true);

        this.transitionTo(this.getStartingRoot());

        this.stage.showAndWait();
    }

    /**
     * Returns the starting root for this game. This will be the Node that is displayed when the game is started.
     *
     * @return the starting root node
     */
    protected abstract Parent getStartingRoot();

    /**
     * Hides the stage associated with this game, indicating that the game has finished.
     */
    protected final void finish() {
        this.stage.hide();
    }

    /**
     * Transitions to a new scene containing the specified root.
     *
     * @param root the root to transition to
     */
    protected final void transitionTo(final Parent root) {
        final Scene scene;
        scene = new Scene(root, GuiGameController.WIDTH, GuiGameController.HEIGHT);

        // TODO figure out stylesheets

        this.stage.setScene(scene);
    }

}
