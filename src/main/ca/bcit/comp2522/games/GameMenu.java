package ca.bcit.comp2522.games;

import ca.bcit.comp2522.games.game.GameController;

import java.util.Map;
import java.util.Scanner;

/**
 * Represents a terminal-input based menu to select which game should be played.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class GameMenu {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int INVALID_INPUT_FLAG = -1;

    private static final int DIVIDER_WIDTH = 80;
    private static final String DIVIDER = "—".repeat(GameMenu.DIVIDER_WIDTH).concat(System.lineSeparator());

    private final Map<Character, GameController> games;
    private final String menuText;
    private boolean isPrompting;

    /**
     * Creates a new menu with the specified games as options.
     *
     * @param games the games that can be played
     */
    public GameMenu(final Map<Character, GameController> games) {
        GameMenu.validateGames(games);

        this.games = games;
        this.isPrompting = false;
        this.menuText = this.buildMenuText();
    }

    /**
     * Validates the provided games to ensure that they can be navigated and displayed in a menu.
     *
     * @param games the games to validate
     */
    private static void validateGames(final Map<Character, GameController> games) {
        if (games == null || games.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a menu from an empty set of games.");
        }

        if (games.keySet().stream().anyMatch(v -> !Character.isAlphabetic(v))) {
            throw new IllegalArgumentException("Every game must be selectable via an alphabetic character.");
        }
    }

    /**
     * Prompts for a choice (if another prompt is not already running) and returns the chosen controller.
     *
     * @return the controller that was chosen, or `null` if another prompt is running
     */
    public GameController promptChoice() {
        if (this.isPrompting) return null;
        this.isPrompting = true;

        System.out.println();

        System.out.print(this.menuText);
        System.out.print("Please enter one of the options: ");

        GameController choice = null;
        do {
            final int choiceCodepoint;
            final char choiceChar;

            choiceCodepoint = this.readChoice();

            if (choiceCodepoint == GameMenu.INVALID_INPUT_FLAG) {
                System.out.print("No recognized selection was received, please try again: ");
                continue;
            }

            choiceChar = (char) choiceCodepoint;
            choice = this.getGameFromChoice(choiceChar);

            if (choice == null) {
                System.out.print("The selection '" + choiceChar + "' is invalid, please try again: ");
            }
        } while (choice == null);

        System.out.println();

        this.isPrompting = false;
        return choice;
    }

    /**
     * Waits for input from the user for a single character, and returns it.
     * If there is no valid input, -1 will be returned.
     *
     * @return the choice codepoint or -1
     */
    private int readChoice() {
        final int expectedLength = 1;

        final String input;
        input = GameMenu.SCANNER.nextLine().trim();

        if (input.isBlank() || input.length() != expectedLength) {
            return GameMenu.INVALID_INPUT_FLAG;
        }

        return input.charAt(expectedLength - 1);
    }

    /**
     * Returns the game associated to the specific choice.
     * <p>
     * This finds the game with case insensitivity.
     *
     * @param choice the choice character
     * @return the associated game, or `null`
     */
    private GameController getGameFromChoice(final char choice) {
        for (final Map.Entry<Character, GameController> entry : this.games.entrySet()) {
            final char activationChar;
            final GameController game;

            activationChar = entry.getKey();
            game = entry.getValue();

            if (Character.toLowerCase(activationChar) == Character.toLowerCase(choice)) {
                return game;
            }
        }

        return null;
    }

    /**
     * Assembles the menu text line by line, including all the names, activation characters and descriptions for each
     * option.
     *
     * @return the final assembled menu text
     */
    private String buildMenuText() {
        final StringBuilder result;

        result = new StringBuilder();

        result.append(GameMenu.DIVIDER);

        for (final Map.Entry<Character, GameController> entry : this.games.entrySet()) {
            final Character activationChar;
            final GameController game;
            final String name;
            final String description;

            activationChar = entry.getKey();
            game = entry.getValue();

            if (game == null) {
                continue;
            }

            name = Character.toUpperCase(activationChar) + " > " + game.getName();
            description = game.getDescription();

            result.append(name).append(System.lineSeparator());
            result.append("\t")
                    .append(description.replace(System.lineSeparator(), "\t".concat(System.lineSeparator())))
                    .append(System.lineSeparator());
        }

        result.append(GameMenu.DIVIDER);
        return result.toString();
    }

}
