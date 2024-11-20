package ca.bcit.comp2522.games.game;

import ca.bcit.comp2522.games.menu.item.MenuItem;

/**
 * Represents a game that can be played within this application.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public abstract class GameController implements MenuItem {

    private final String name;
    private final String description;

    /**
     * Creates a new game that can be played within this application.
     *
     * @param name        the name of the game
     * @param description the description of the game
     */
    public GameController(final String name, final String description) {
        GameController.validateGameMetadataPart(name);
        GameController.validateGameMetadataPart(description);

        this.name = name;
        this.description = description;
    }

    /**
     * Validates the given metadata part (e.g. name or description) to ensure it is within limits.
     *
     * @param metadataPart the metadata part to validate
     */
    private static void validateGameMetadataPart(final String metadataPart) {
        if (metadataPart == null || metadataPart.isBlank()) {
            throw new IllegalArgumentException("The input \"" + metadataPart + "\" is not valid metadata for a game.");
        }
    }

    /**
     * Launches this game. This method is expected to block until the game has finished execution.
     * <p>
     * If another instance of this game is currently launched, the new launch will block until after the current game
     * has finished.
     */
    public final synchronized void launch() {
        this.onStart();
        this.onFinish();
    }

    /**
     * Called when a game is launched. This indicates a new game has started, and should begin the game logic.
     */
    protected abstract void onStart();

    /**
     * Called after a game has finished execution, this can be used to perform cleanup, if necessary.
     */
    protected abstract void onFinish();

    /**
     * Returns the name of this game.
     *
     * @return the name
     */
    @Override
    public final String getName() {
        return this.name;
    }

    /**
     * Returns the description of this game.
     *
     * @return the description
     */
    @Override
    public final String getDescription() {
        return this.description;
    }

}
