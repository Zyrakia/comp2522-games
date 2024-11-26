package ca.bcit.comp2522.games.game.score;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Allows for managing of multiple {@link GameSessionScore} instances.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class ScoreManager {

    private final List<GameSessionScore> historicalSessions;
    private GameSessionScore currentSession;

    /**
     * Creates a new score manager.
     */
    public ScoreManager() {
        this.currentSession = null;
        this.historicalSessions = new ArrayList<>();
    }

    /**
     * Initializes a new game score tracker, while also saving the previous game score.
     */
    public void startNewSession() {
        final GameSessionScore score;
        score = new GameSessionScore();

        this.commitCurrentSession();
        this.currentSession = score;
    }

    /**
     * Commits the current score tracker, if any, into the score history. The current game score will be cleared
     * after being committed to history.
     */
    public void commitCurrentSession() {
        if (this.currentSession == null) {
            return;
        }

        this.historicalSessions.add(this.currentSession);
        this.currentSession = null;
    }

    /**
     * Returns the current game score tracker.
     *
     * @return the current score, can be null
     */
    public GameSessionScore getCurrentSession() {
        return this.currentSession;
    }

    /**
     * Returns the historical score with the highest average points per game. This does not include the current score
     * tracker, if any.
     *
     * @return the historical score instance with the highest total points
     */
    public Optional<GameSessionScore> getHighScore() {
        return this.historicalSessions.stream()
                .reduce((identity, score) -> score.getPointsPerGame() > identity.getPointsPerGame() ? score : identity);
    }

}
