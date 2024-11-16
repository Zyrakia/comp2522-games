package ca.bcit.comp2522.games.game;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a game that is run within a GUI window.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public abstract class GuiGameController extends GameController {

    /**
     * Maps which stages are currently controlled by which game. This is to ensure that each stage can only be used
     * by one game at a time.
     */
    private static final Map<Stage, GuiGameController> STAGE_LOCKS = new HashMap<>();

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private final Stage stage;

    /**
     * Creates a new game that will be played within a GUI screen.
     *
     * @param name        the name of the game
     * @param description the description of the game
     * @param stage       the stage to display the game on
     */
    public GuiGameController(final String name, final String description, final Stage stage) {
        super(name, description);
        this.stage = stage;
    }

    @Override
    protected final void onStart() {
        this.takeStage();

        this.stage.setTitle(this.getName());
        this.stage.setResizable(false);

        this.transitionTo(this.getStartingRoot());
        this.stage.showAndWait();

        this.releaseStage();
    }

    /**
     * Attempts to place a lock onto the stage associated with this instance of this game.
     *
     * @throws IllegalStateException if the stage is already locked
     */
    private void takeStage() {
        if (GuiGameController.STAGE_LOCKS.containsKey(this.stage)) {
            throw new IllegalStateException(
                    "There is already a game running on the Stage \"" + this.stage.getTitle() + "\"");
        }

        GuiGameController.STAGE_LOCKS.put(this.stage, this);
    }

    /**
     * Determines whether the stage associated with this game is locked by this instance.
     *
     * @return whether this instance has control of the stage
     */
    private boolean hasStage() {
        return GuiGameController.STAGE_LOCKS.get(this.stage) == this;
    }

    /**
     * Attempts to release the lock on the stage associated with this instance of this game.
     *
     * @throws IllegalStateException if the stage was not previously locked by this instance
     */
    private void releaseStage() {
        if (!this.hasStage()) {
            throw new IllegalStateException(
                    "Attempting to release a stage not controlled by this game instance (\"" + this.getName() + "\").");
        }


        GuiGameController.STAGE_LOCKS.remove(this.stage);
    }

    /**
     * Returns the starting root for this game. This will be the Node that is displayed when the game is started.
     *
     * @return the starting root node
     */
    protected abstract Parent getStartingRoot();

    /**
     * If the stage is controlled by this game, hides it, indicating that the game has finished.
     */
    protected final void finish() {
        if (this.hasStage()) {
            this.stage.hide();
        }
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
