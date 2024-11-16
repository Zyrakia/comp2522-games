package ca.bcit.comp2522.games;

import ca.bcit.comp2522.games.game.GameController;
import ca.bcit.comp2522.games.game.word.WordGameController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the driver class for the COMP2522 term project.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class Main extends Application {

    /**
     * Maps input characters to their associated game controllers.
     */
    private final Map<Character, GameController> games = new HashMap<>();

    /**
     * Entry point for the COMP2522 term project driver class.
     *
     * @param args the command line arguments (unused)
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        this.games.put('w', new WordGameController());
        this.games.get('w').launch();
    }


}
