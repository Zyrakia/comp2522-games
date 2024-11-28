package ca.bcit.comp2522.games.game.number;

import ca.bcit.comp2522.games.util.Observable;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a variable grid of integers.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class IntegerGrid extends Observable<IntegerGrid> {

    private final int rows;
    private final int cols;
    private final Integer[][] grid;

    /**
     * Creates a new grid with the specified amount of rows and columns.
     *
     * @param rows the amount of rows (y)
     * @param cols the amount of columns (x)
     */
    public IntegerGrid(final int rows, final int cols) {
        IntegerGrid.validateGridDimension(rows);
        IntegerGrid.validateGridDimension(cols);

        this.rows = rows;
        this.cols = cols;
        this.grid = IntegerGrid.initGrid(rows, cols);
    }

    /**
     * Validates the given grid dimension (either rows or cols) to ensure it is within limits.
     *
     * @param gridDimension the grid dimension
     */
    private static void validateGridDimension(final int gridDimension) {
        if (gridDimension <= 0) {
            throw new IllegalArgumentException("Grid rows and columns must both be >= 0!");
        }
    }

    /**
     * Initializes a grid full of empty unplaced spots. The rows and columns MUST be above 0.
     *
     * @param rows the rows the grid has
     * @param cols the cols the grid has
     * @return the initialized grid
     */
    private static Integer[][] initGrid(final int rows, final int cols) {
        return new Integer[rows][cols];
    }

    /**
     * Validates the given grid point to ensure it is within range.
     *
     * @param point the coordinate point
     */
    private void assertPoint(final Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Point must not be null!");
        }

        if (!this.isWithinRange(point)) {
            throw new IllegalArgumentException("Out of bounds grid point: " + point);
        }
    }

    /**
     * Returns whether the given point is within range in this grid.
     *
     * @param point the point to check
     * @return whether the point is in range
     */
    public boolean isWithinRange(final Point point) {
        return point.x() >= 0 && point.x() < this.cols && point.y() >= 0 && point.y() < this.rows;
    }

    /**
     * Returns whether there is a value placed at the given point.
     *
     * @param point the point to check
     * @return whether the point is empty
     */
    public boolean isEmpty(final Point point) {
        return this.get(point) == null;
    }

    /**
     * Returns the value placed at the given point.
     * <p>
     * If the point is empty, `null` will be returned.
     *
     * @param point the point
     * @return the value at the point
     */
    public Integer get(final Point point) {
        this.assertPoint(point);
        return this.grid[point.y()][point.x()];
    }

    /**
     * Returns the point that holds the given value.
     *
     * @param value the value to find
     * @return the point, or `null` if no point holds the value
     */
    public Point find(final int value) {
        final Iterator<Point> griderator;
        griderator = this.griderator();

        while (griderator.hasNext()) {
            final Point point;
            point = griderator.next();

            if (this.get(point).equals(value)) {
                return point;
            }
        }

        return null;
    }

    /**
     * Returns whether this grid has an empty point.
     *
     * @return whether this grid has at least one empty point
     */
    public boolean hasEmpty() {
        final Iterator<Point> griderator;
        griderator = this.griderator();

        while (griderator.hasNext()) {
            final Point point;
            point = griderator.next();

            if (this.isEmpty(point)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a new iterator that moves over all the points on the grid from left to right and top to bottom. If the
     * last point has been retrieved, there will be no more points in the iterator.
     *
     * @param startingPoint the point to start at within the grid
     * @return the point iterator
     */
    public Iterator<Point> griderator(final Point startingPoint) {
        this.assertPoint(startingPoint);

        return new Iterator<>() {
            private Point nextPoint = startingPoint;

            @Override
            public boolean hasNext() {
                return this.nextPoint != null;
            }

            @Override
            public Point next() {
                if (this.nextPoint == null) {
                    throw new NoSuchElementException("The end of the grid has already been reached.");
                }

                final Point point;
                point = this.nextPoint;

                if (point.x() >= IntegerGrid.this.getCols() - 1) {
                    this.nextPoint = Point.y(point.y() + 1);
                } else {
                    this.nextPoint = point.add(Point.x(1));
                }

                if (!IntegerGrid.this.isWithinRange(this.nextPoint)) {
                    this.nextPoint = null;
                }

                return point;
            }
        };
    }

    /**
     * Returns a new iterator that moves over all the points on the grid from left to right and top to bottom.
     *
     * @return the point iterator
     */
    public Iterator<Point> griderator() {
        return this.griderator(Point.zero());
    }

    /**
     * Returns the nearest point after a given point that has a value.
     *
     * @param point the point to search after
     * @return the point of the next empty neighbour, or null if there is no such neighbour
     */
    public Point getNextFilled(final Point point) {
        this.assertPoint(point);

        final Iterator<Point> griderator;
        griderator = this.griderator(point);
        griderator.next();

        while (griderator.hasNext()) {
            final Point nextPoint;
            nextPoint = griderator.next();

            if (!this.isEmpty(nextPoint)) {
                return nextPoint;
            }
        }

        return null;
    }

    /**
     * Places the given value at the given point.
     *
     * @param point the point to place at
     * @param value the value to place
     */
    public void place(final Point point, final int value) {
        this.assertPoint(point);
        this.grid[point.y()][point.x()] = value;
        this.announceUpdate(this);
    }

    /**
     * Resets all points on the grid.
     */
    public void clear() {
        final Iterator<Point> griderator;
        griderator = this.griderator();

        while (griderator.hasNext()) {
            final Point point = griderator.next();
            this.grid[point.y()][point.x()] = null;
        }

        this.announceUpdate(this);
    }

    /**
     * Returns the number of rows in this grid.
     *
     * @return the rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Returns the number of columns in this grid.
     *
     * @return the cols
     */
    public int getCols() {
        return this.cols;
    }

}
