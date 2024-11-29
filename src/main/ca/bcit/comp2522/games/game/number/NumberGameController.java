package ca.bcit.comp2522.games.game.number;

import ca.bcit.comp2522.games.game.GuiGameController;
import ca.bcit.comp2522.games.util.Point;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * The controller for the second game, the number placing game.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class NumberGameController extends GuiGameController {

    private static final int RANDOM_MIN = 1;
    private static final int RANDOM_MAX = 1000;

    private final Random rand;
    private final NumberGameStats stats;
    private final Set<Integer> placedNumbers;
    private final NumberGameGrid grid;
    private final Label statusLabel;
    private final RenderedIntegerGrid renderedGrid;

    private Integer targetNumber;

    /**
     * Creates a new number game controller.
     */
    public NumberGameController() {
        super("Grid Gamble", "Arrange numbers in a grid in perfect ascending order!");

        final RenderedIntegerGrid renderedGrid;
        renderedGrid = new RenderedIntegerGrid(this::createGridButton);

        this.rand = new Random();
        this.stats = new NumberGameStats();
        this.placedNumbers = new HashSet<>();
        this.grid = new NumberGameGrid();
        this.statusLabel = this.createStatusLabel();
        this.renderedGrid = renderedGrid;

        this.grid.observe(renderedGrid);
        this.grid.observe(_ -> this.handleGridUpdate());

        this.addStylesheet("number-game.css");
    }

    /**
     * Places the current target number at the given point. If there is no target number, this will do nothing.
     *
     * @param point the point to place at
     */
    public void placeTargetAt(Point point) {
        if (this.targetNumber == null) {
            return;
        }

        if (!this.grid.isEmpty(point)) {
            return;
        }

        this.placedNumbers.add(this.targetNumber);
        this.grid.place(point, this.targetNumber);
    }

    /**
     * Generates a new target number.
     */
    public void setNextTarget() {
        this.targetNumber = this.generateNextTarget();

        if (!this.grid.canPlaceAscending(this.targetNumber)) {
            this.handleLoss("The next number (" + this.targetNumber + ") cannot be placed.");
        } else {
            this.statusLabel.setText("Place " + this.targetNumber + " into an empty slot");
        }
    }

    /**
     * Performs game advancements and status checks when the grid updates.
     */
    private void handleGridUpdate() {
        if (!this.grid.isAscending()) {
            this.handleLoss("The current number (" + this.targetNumber + ") was placed out of order.");
            return;
        }

        if (!this.grid.hasEmpty()) {
            this.handleWin();
            return;
        }

        if (this.targetNumber != null) {
            this.stats.recordPlacement();
        }

        this.setNextTarget();
    }

    /**
     * Handles the loss condition (if the next number cannot be placed).
     *
     * @param reason a detailed description of why the loss occurred
     */
    private void handleLoss(final String reason) {
        this.statusLabel.setText("🪦 You have lost. 🪦");
        this.targetNumber = null;

        this.stats.recordLoss();
        this.showRestartAlert(Alert.AlertType.ERROR, "You lost!", reason);
    }

    /**
     * Handles the win condition (if the grid is filled).
     */
    private void handleWin() {
        this.statusLabel.setText("🎊 Congratulations!! 🎊");
        this.targetNumber = null;

        this.stats.recordWin();
        this.showRestartAlert(Alert.AlertType.INFORMATION, "You have won!",
                              "This was pretty much impossible, so I am not sure how you did it, but congratulations!");
    }

    /**
     * Shows an alert that has two custom buttons, one to restart the game and one to quit the game. Whichever
     * selection is made is handled by this method.
     *
     * @param type        the type of alert to show
     * @param title       the title of the alert
     * @param description the content of the alert
     */
    private void showRestartAlert(final Alert.AlertType type, final String title, final String description) {
        final Alert alert;
        final ButtonType restartButton;
        final ButtonType quitButton;

        alert = new Alert(type);
        restartButton = new ButtonType("Play Again");
        quitButton = new ButtonType("Quit");

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(description);

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(restartButton, quitButton);

        final Optional<ButtonType> res;
        res = alert.showAndWait();

        System.out.println(this.stats);

        if (res.isPresent() && res.get() == restartButton) {
            this.resetGameState();
        } else {
            this.getStage().close();
        }
    }

    @Override
    protected void preRenderSetup() {
        final Alert welcomeAlert;
        final Stage alertStage;
        final ButtonType startButton;

        welcomeAlert = new Alert(Alert.AlertType.INFORMATION);
        alertStage = (Stage) welcomeAlert.getDialogPane().getScene().getWindow();
        startButton = new ButtonType("Start Game");

        welcomeAlert.setTitle("Welcome to " + this.getName() + "!");
        welcomeAlert.setHeaderText(null);
        welcomeAlert.setContentText(
                "Welcome to " + this.getName() + ". You must place numbers between " + NumberGameController.RANDOM_MIN +
                        " and " + NumberGameController.RANDOM_MAX + " into the grid in ascending order to win!");

        welcomeAlert.getButtonTypes().clear();
        welcomeAlert.getButtonTypes().setAll(startButton);

        alertStage.setAlwaysOnTop(true);
        welcomeAlert.showAndWait();

        this.renderedGrid.renderFrom(this.grid);
        this.setNextTarget();
    }

    @Override
    protected Parent getInitialRoot() {
        final VBox root;
        root = new VBox();

        root.getStyleClass().add("vbox");
        root.getChildren().add(this.statusLabel);
        root.getChildren().add(this.renderedGrid);

        return root;
    }

    @Override
    protected void onFinish() {
        this.resetGameState();
        this.stats.reset();
    }

    /**
     * Resets the grid and placed numbers.
     */
    private void resetGameState() {
        this.targetNumber = null;
        this.placedNumbers.clear();
        this.grid.clear();
    }

    /**
     * Generates the next random number that has not already been placed.
     *
     * @return the next random number
     */
    private int generateNextTarget() {
        final int possibleNumbers;
        possibleNumbers = NumberGameController.RANDOM_MAX - NumberGameController.RANDOM_MIN + 1;

        if (this.placedNumbers.size() > possibleNumbers) {
            throw new IllegalStateException(
                    "All possible options have been placed, but a next target is being requested.");
        }

        int num;

        do {
            num = this.rand.nextInt(NumberGameController.RANDOM_MIN, NumberGameController.RANDOM_MAX + 1);
        } while (this.placedNumbers.contains(num));

        return num;
    }

    /**
     * Creates a button that will be displayed in the grid with it's associated value.
     *
     * @param point the point this button is at, used for the click action
     * @param value the value of the button
     * @return the created button
     */
    private Button createGridButton(final Point point, final Integer value) {
        final boolean filled;
        final Button btn;

        filled = value != null;
        btn = new Button();

        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btn.setText(filled ? String.valueOf(value) : "");
        btn.setDisable(filled);
        btn.setOnAction(_ -> this.placeTargetAt(point));

        return btn;
    }

    /**
     * Creates the status label that will display which number should be placed next.
     *
     * @return the created label
     */
    private Label createStatusLabel() {
        final Label label;
        label = new Label();

        label.setText("Get ready...");

        return label;
    }

}
