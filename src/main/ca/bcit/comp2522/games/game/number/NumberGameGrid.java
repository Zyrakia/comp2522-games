package ca.bcit.comp2522.games.game.number;

import ca.bcit.comp2522.games.util.Point;

import java.util.Iterator;

/**
 * Represents the integer grid used within the number game.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class NumberGameGrid extends IntegerGrid {

    public static final int ROWS = 4;
    public static final int COLUMNS = 5;

    /**
     * Creates a new {@value NumberGameGrid#ROWS}x{@value  NumberGameGrid#COLUMNS} grid for use within the number game.
     */
    public NumberGameGrid() {
        super(NumberGameGrid.ROWS, NumberGameGrid.COLUMNS);
    }

    /**
     * Returns whether a value can be placed into the grid (assuming it's ascending already) while maintaining
     * ascending order.
     *
     * @param valueToPlace the value that should be placed
     * @return whether the value can be placed
     */
    public boolean canPlaceAscending(final int valueToPlace) {
        final Iterator<Point> iter;
        iter = this.griderator();

        while (iter.hasNext()) {
            final Point currentPoint;
            final Integer currentValue;

            currentPoint = iter.next();
            currentValue = this.get(currentPoint);

            if (currentValue != null) {
                // We found a bigger value already, we can short circuit
                // This is only possible because we are assuming it is already ascending
                if (currentValue > valueToPlace) {
                    return false;
                }

                // Move on to find the next empty spot
                continue;
            }

            // We have an empty spot, so if there is a valid right neighbour, we can place here

            final Point nextFilled;
            nextFilled = this.getNextFilled(currentPoint);

            if (nextFilled == null || this.get(nextFilled) > valueToPlace) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines whether the grid contains number only in ascending order.
     * <p>
     * The order is read from left to right and top to bottom, with empty points being ignored.
     *
     * @return whether the current state of the grid is ascending
     */
    public boolean isAscending() {
        Iterator<Point> griderator = this.griderator();

        int last = Integer.MIN_VALUE;
        while (griderator.hasNext()) {
            final Point point;
            final Integer valueAtPoint;

            point = griderator.next();
            valueAtPoint = this.get(point);

            if (valueAtPoint == null) {
                continue;
            }

            if (valueAtPoint < last) {
                return false;
            }

            last = valueAtPoint;
        }

        return true;
    }

}
