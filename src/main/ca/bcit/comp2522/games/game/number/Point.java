package ca.bcit.comp2522.games.game.number;

/**
 * Represents a point in a 2D plane.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class Point {

    private final int x;
    private final int y;

    /**
     * Creates a new point at the given X and Y.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new point directly on the X axis.
     *
     * @param x the X coordinate
     * @return the (X, 0) point
     */
    public static Point x(final int x) {
        return new Point(x, 0);
    }

    /**
     * Creates a new point directly on the Y axis.
     *
     * @param y the Y coordinate
     * @return the (0, Y) point
     */
    public static Point y(final int y) {
        return new Point(0, y);
    }

    /**
     * Creates a new point in the center.
     *
     * @return the (0, 0) point
     */
    public static Point zero() {
        return new Point(0, 0);
    }

    /**
     * Adds another point to this point and returns the point containing the sum.
     *
     * @param other the point to add
     * @return the sum point
     */
    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtracts another point from this point and returns the point containing the difference.
     *
     * @param other the point to subtract
     * @return the difference point
     */
    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    /**
     * Returns the x coordinate.
     *
     * @return the x
     */
    public int x() {
        return this.x;
    }

    /**
     * Returns the y coordinate.
     *
     * @return the y
     */
    public int y() {
        return this.y;
    }

    /**
     * Returns the magnitude of this point (distance from x: 0, y: 0).
     *
     * @return the magnitude
     */
    public double mag() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    @Override
    public String toString() {
        return String.format("(x: %d, y: %d)", this.x(), this.y());
    }

}
