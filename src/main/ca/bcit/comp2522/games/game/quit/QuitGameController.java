package ca.bcit.comp2522.games.game.quit;

import ca.bcit.comp2522.games.game.GameController;

/**
 * A game controller that simply exits the whole application.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class QuitGameController extends GameController {

    /**
     * Creates a new quitting controller.
     */
    public QuitGameController() {
        super("Quit", "Quit the application.");
    }

    @Override
    protected void onStart() {
        System.out.println("Goodbye, world.");
        System.exit(0);
    }

    @Override
    protected void onFinish() {
    }

}
