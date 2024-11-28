package ca.bcit.comp2522.games.game.number;

import java.util.Iterator;

/**
 * Represents the integer grid used within the number game.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class NumberGameGrid extends IntegerGrid {

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
        final Iterator<Point> griderator;
        griderator = this.griderator();

        while (griderator.hasNext()) {
            final Point currentPoint;
            final Integer currentValue;

            currentPoint = griderator.next();
            currentValue = this.get(currentPoint);

            System.out.println("Scanning point " + currentPoint + " (" + currentValue + ")");

            // If we have an empty cell, we need to see if we can place it there
            if (currentValue == null) {
                final Iterator<Point> lookaheadGriderator;
                lookaheadGriderator = this.griderator(currentPoint);

                // We need to skip past the current point
                lookaheadGriderator.next();

                while (lookaheadGriderator.hasNext()) {
                    final Point lookaheadPoint;
                    final Integer nextValue;

                    lookaheadPoint = lookaheadGriderator.next();
                    nextValue = this.get(lookaheadPoint);

                    if (nextValue != null && nextValue < valueToPlace) {
                        // There is a next value that is smaller, we cannot place
                        return false;
                    }
                }

                // All the cells after the empty cells are higher or equal, we can place it
                return true;
            } else if (currentValue > valueToPlace) {
                // We now know that we are already past the point at which we can place
                return false;
            }
        }

        // There were no empty cells at all, so we cannot place anything
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
