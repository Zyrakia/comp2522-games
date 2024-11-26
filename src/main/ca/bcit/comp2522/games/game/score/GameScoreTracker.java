package ca.bcit.comp2522.games.game.score;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * A class to keep track of various game rounds and their results.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class GameScoreTracker {

    private final String gameName;
    private final Map<RoundResult, Integer> roundResults;

    private LocalDateTime startDateTime;

    /**
     * Creates a new game score tracker.
     *
     * @param gameName the name of the game that is tracked
     */
    public GameScoreTracker(final String gameName) {
        GameScoreTracker.validateGameName(gameName);

        this.gameName = gameName;
        this.roundResults = new HashMap<>();
        this.startDateTime = LocalDateTime.now();

        this.reset();
    }

    /**
     * Validates the given game name to ensure it is within limits.
     *
     * @param gameName the game name to validate
     */
    public static void validateGameName(final String gameName) {
        if (gameName == null || gameName.isBlank()) {
            throw new IllegalArgumentException("A game tracker must be associated with a proper game name.");
        }
    }

    /**
     * Generates a report string that summarizes the state of this score tracker.
     *
     * @param totalFormatter   the provider function for the total games played summary string (the first line), will be
     *                         provided with the parameter set: ( total rounds played, total points earned )
     * @param roundFormatter   the provider function for the result of round summary string, will be provided with the
     *                         parameter set: ( round result instance, result occurrence count )
     * @param summaryFormatter the provider function for the game summary string (the last list), will be provided
     *                         with the same parameter set as the total formatter
     * @return the generated lines joined together
     */
    private String generateReport(final BiFunction<Integer, Integer, String> totalFormatter,
                                  final BiFunction<RoundResult, Integer, String> roundFormatter,
                                  final BiFunction<Integer, Integer, String> summaryFormatter) {
        final StringBuilder strResult;
        final int totalRounds;
        final int totalPoints;

        strResult = new StringBuilder();
        totalRounds = this.getTotalRoundsPlayed();
        totalPoints = this.getTotalPoints();

        if (totalFormatter != null) {
            final String totalString;
            totalString = totalFormatter.apply(totalRounds, totalPoints);

            if (totalString != null && !totalString.isBlank()) {
                strResult.append(totalString).append(System.lineSeparator());
            }
        }

        if (roundFormatter != null) {
            for (final Map.Entry<RoundResult, Integer> entry : this.roundResults.entrySet()) {
                final String roundString;
                roundString = roundFormatter.apply(entry.getKey(), entry.getValue());

                if (roundString != null && !roundString.isBlank()) {
                    strResult.append(roundString).append(System.lineSeparator());
                }
            }
        }

        if (summaryFormatter != null) {
            final String summaryString;
            summaryString = summaryFormatter.apply(totalRounds, totalPoints);

            if (summaryString != null && !summaryString.isBlank()) {
                strResult.append(summaryString).append(System.lineSeparator());
            }
        }

        return strResult.toString();
    }

    /**
     * Clears all the rounds played and resets the start date.
     */
    public void reset() {
        this.roundResults.clear();
        this.startDateTime = LocalDateTime.now();
    }

    /**
     * Increments the rounds played of the specified result by one.
     *
     * @param result the result of the round
     */
    public void reportRound(final RoundResult result) {
        int occurrences = this.roundResults.get(result);
        occurrences++;

        this.roundResults.put(result, occurrences);
    }

    /**
     * Returns the amount of times a round has been reported under a specific result.
     * <p>
     * If no occurrence is recorded under the result, 0 is returned.
     *
     * @param result the result to retrieve the occurrence count of
     * @return the amount of rounds stored under that result
     */
    public int getRoundsPlayed(final RoundResult result) {
        if (this.roundResults.containsKey(result)) {
            return this.roundResults.get(result);
        }

        return 0;
    }

    /**
     * Returns the total amount of rounds played throughout all the various results.
     *
     * @return the amount of rounds played
     */
    public int getTotalRoundsPlayed() {
        return this.roundResults.values().stream().reduce(0, Integer::sum);
    }

    /**
     * Returns the amount of points that result from the reported amount of rounds played under a specific result.
     *
     * @param result the result to retrieve the points of
     * @return the occurrence count of the specific result multiplied by how many points each is worth
     */
    public int getPoints(final RoundResult result) {
        final int occurrences = this.getRoundsPlayed(result);
        return result.calculatePoints(occurrences);
    }

    /**
     * Returns the total points after every round result has calculated the points they are worth, and they have been
     * added together.
     *
     * @return the total points
     */
    public int getTotalPoints() {
        return this.roundResults.entrySet()
                .stream()
                .map(entry -> entry.getKey().calculatePoints(entry.getValue()))
                .reduce(0, Integer::sum);
    }

    /**
     * Returns the game name.
     *
     * @return the game name
     */
    public String getGameName() {
        return this.gameName;
    }

    /**
     * Returns the time at which this tracker was instantiated or reset.
     *
     * @return the start date time
     */
    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

}
