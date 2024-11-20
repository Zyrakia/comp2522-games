package ca.bcit.comp2522.games;

import ca.bcit.comp2522.games.game.GameController;
import ca.bcit.comp2522.games.game.quit.QuitGameController;
import ca.bcit.comp2522.games.game.word.WordGameController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This is the driver class for the COMP2522 term project.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class Main extends Application {

    /**
     * A global scanner object that is closed when the game is done.
     */
    public static final Scanner SCANNER = new Scanner(System.in);

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
        final Map<Character, GameController> games;
        final GameMenu menu;

        // Linked in order to preserve order within the menu text
        games = new LinkedHashMap<>();

        games.put('w', new WordGameController());
        games.put('q', new QuitGameController());

        menu = new GameMenu(games);

        while (true) {
            final GameController nextGame;
            nextGame = menu.promptChoice();

            if (nextGame == null) {
                break;
            }

            nextGame.launch();
        }

        Main.SCANNER.close();
    }

}
