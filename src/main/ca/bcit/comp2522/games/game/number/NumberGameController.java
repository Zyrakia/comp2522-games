package ca.bcit.comp2522.games.game.number;

import ca.bcit.comp2522.games.game.GuiGameController;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * The controller for the second game, the number placing game.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class NumberGameController extends GuiGameController {

    private static final int RANDOM_MIN = 1;
    private static final int RANDOM_MAX = 1000;

    private final Random rand;
    private final Set<Integer> placedNumbers;

    private NumberGameGrid grid;

    /**
     * Creates a new number game controller.
     */
    public NumberGameController() {
        super("Grid Gamble", "Arrange numbers in a grid in perfect ascending order!");

        this.rand = new Random();
        this.placedNumbers = new HashSet<>();
        this.grid = new NumberGameGrid();
    }

    private void displayGrid() {
    }

    @Override
    protected Parent getStartingRoot() {
        return new VBox();
    }

    @Override
    protected void onFinish() {
        this.reset();
    }

    /**
     * Resets the grid and placed numbers.
     */
    private void reset() {
        this.placedNumbers.clear();
        this.grid = new NumberGameGrid();
    }

    /**
     * Generates the next random number that has not already been placed.
     *
     * @return the next random number
     */
    private int generateNextNumber() {
        int num;

        do {
            num = this.rand.nextInt(NumberGameController.RANDOM_MIN, NumberGameController.RANDOM_MAX) + 1;
        } while (this.placedNumbers.contains(num));

        return num;
    }

}
