package ca.bcit.comp2522.games.game.number;

import ca.bcit.comp2522.games.util.Observer;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.util.Iterator;
import java.util.function.BiFunction;

/**
 * A pane that renders an integer grid reactively with the given node provider for each point in the grid.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class RenderedIntegerGrid extends GridPane implements Observer<IntegerGrid> {

    private final BiFunction<Point, Integer, Node> nodeProvider;

    /**
     * Creates a new rendered grid.
     *
     * @param nodeProvider the node provider for each of the points in the grid, is provided with the point in the
     *                     grid and the value of the point
     */
    public RenderedIntegerGrid(final BiFunction<Point, Integer, Node> nodeProvider) {
        this.nodeProvider = nodeProvider;
        this.getStyleClass().add("grid");

        VBox.setVgrow(this, Priority.ALWAYS);
    }

    /**
     * Re-renders the pane with all the nodes generated from a grid.
     *
     * @param grid the grid to render from
     */
    public void renderFrom(final IntegerGrid grid) {
        this.applyConstraints(grid);
        this.populate(grid);
    }

    /**
     * Applies the row and columns constraints to evenly space nodes.
     *
     * @param grid the grid that is being constrained for
     */
    private void applyConstraints(final IntegerGrid grid) {
        final double fullPerc = 100.0;

        this.getRowConstraints().clear();
        this.getColumnConstraints().clear();

        final RowConstraints rowConst;
        final ColumnConstraints colConst;

        rowConst = new RowConstraints();
        colConst = new ColumnConstraints();

        rowConst.setVgrow(Priority.ALWAYS);
        rowConst.setPercentHeight(fullPerc / grid.getRows());

        colConst.setHgrow(Priority.ALWAYS);
        colConst.setPercentWidth(fullPerc / grid.getCols());

        for (int i = 0; i < grid.getRows(); i++) {
            this.getRowConstraints().add(rowConst);
        }

        for (int i = 0; i < grid.getCols(); i++) {
            this.getColumnConstraints().add(colConst);
        }
    }

    /**
     * Creates nodes for all the points in a grid and adds them to the list of children.
     *
     * @param grid the grid to populate from
     */
    private void populate(final IntegerGrid grid) {
        this.getChildren().clear();

        final Iterator<Point> griderator;
        griderator = grid.griderator();

        while (griderator.hasNext()) {
            final Point point;
            final Integer value;
            final Node node;

            point = griderator.next();
            value = grid.get(point);
            node = this.nodeProvider.apply(point, value);

            this.add(node, point.x(), point.y());
        }
    }

    @Override
    public void handleUpdate(final IntegerGrid grid) {
        this.renderFrom(grid);
    }

}
