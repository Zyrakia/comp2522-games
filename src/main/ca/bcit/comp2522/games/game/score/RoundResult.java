package ca.bcit.comp2522.games.game.score;

/**
 * Represents a game round result that is worth a specific amount of points.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class RoundResult {

    private final int pointsPerOccurrence;

    /**
     * Creates a new type of result that a round can have.
     *
     * @param pointsPerOccurrence the amount of points each occurrence of this type of outcome is worth
     */
    public RoundResult(final int pointsPerOccurrence) {
        this.pointsPerOccurrence = pointsPerOccurrence;
    }

    /**
     * Returns the amount of points that are achieved by playing a certain amount of rounds of this type of result.
     *
     * @param occurrenceCount the occurrence count of this type of result
     * @return the amount of points that the amount of occurrences are worth
     */
    public final int calculatePoints(final int occurrenceCount) {
        return occurrenceCount * this.getPointsPerOccurrence();
    }

    /**
     * Returns the amount of points this result is worth every time it is reported.
     *
     * @return the points per occurrence
     */
    public final int getPointsPerOccurrence() {
        return this.pointsPerOccurrence;
    }

}
