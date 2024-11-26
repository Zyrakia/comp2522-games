package ca.bcit.comp2522.games.game.score;

/**
 * Represents a game round result that is worth a specific amount of points.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class RoundResult {

    private final String name;
    private final int pointsPerOccurrence;

    /**
     * Creates a new type of result that a round can have.
     *
     * @param name                the category name
     * @param pointsPerOccurrence the amount of points each occurrence of this type of outcome is worth
     */
    public RoundResult(final String name, final int pointsPerOccurrence) {
        RoundResult.validateName(name);

        this.name = name;
        this.pointsPerOccurrence = pointsPerOccurrence;
    }

    /**
     * Validates a category name to ensure it is within limits.
     *
     * @param name the name to validate
     */
    private static void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Each round result must have a descriptive name.");
        }
    }

    /**
     * Returns the amount of points that are achieved by playing a certain amount of rounds of this type of result.
     *
     * @param occurrenceCount the occurrence count of this type of result
     * @return the amount of points that the amount of occurrences are worth
     */
    public int calculatePoints(final int occurrenceCount) {
        return occurrenceCount * this.pointsPerOccurrence;
    }

    /**
     * Returns the name of this result.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

}
