package ca.bcit.comp2522.games.game.score;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to keep track of various game rounds and their results.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class GameSessionScore {

    private final Map<RoundResult, Integer> roundResults;
    private final LocalDateTime startDateTime;

    private int gamesPlayed;

    /**
     * Creates a new game score tracker.
     */
    public GameSessionScore() {
        this.roundResults = new HashMap<>();
        this.startDateTime = LocalDateTime.now();
        this.gamesPlayed = 0;
    }

    /**
     * Increments the rounds played of the specified result by one.
     *
     * @param result the result of the round
     */
    public void reportRound(final RoundResult result) {
        final int occurrences;
        occurrences = this.getRoundsPlayed(result);
        this.roundResults.put(result, occurrences + 1);
    }

    /**
     * Increments the games played by one.
     */
    public void reportGameFinished() {
        this.gamesPlayed++;
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
     * Returns the total amount of reported games played.
     *
     * @return the amount of finished games reported under this tracker
     */
    public int getTotalGamesPlayed() {
        return this.gamesPlayed;
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
     * Returns the average amount of points earned per game played in this session.
     *
     * @return the average points per game
     */
    public double getPointsPerGame() {
        return ((double) this.getTotalPoints()) / this.getTotalGamesPlayed();
    }

    /**
     * Returns the time at which this tracker was instantiated or reset.
     *
     * @return the start date time
     */
    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    /**
     * Shortcut for calling {@link GameSessionSummarizer#summarize(GameSessionScore)} with this instance.
     *
     * @param summarizer the summarizer to use
     * @return the summarizer result
     */
    public String summarize(final GameSessionSummarizer summarizer) {
        return summarizer.summarize(this);
    }

}
