package ca.bcit.comp2522.games.game.word.score;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages multiple score instances.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class ScoreManager {

    private final List<Score> scores;
    private final Path scoresFile;

    private Score sessionScore;

    /**
     * Creates a new score manager.
     *
     * @param scoresFile the path to the data file to save and load scores
     */
    public ScoreManager(final Path scoresFile) {
        this.scores = new ArrayList<>();
        this.scoresFile = scoresFile;
        this.load();
    }

    /**
     * Commits the given score to the current session score.
     * <p>
     * If there is no active session, it will be set to the given score, otherwise, the given score will be added to
     * the session.
     *
     * @param score the score to commit
     */
    public void addToSession(final Score score) {
        if (this.sessionScore == null) {
            this.sessionScore = score;
        } else {
            this.sessionScore = this.sessionScore.add(score);
        }
    }

    /**
     * Returns the current session score, or null if there is none.
     *
     * @return the current session score
     */
    public Score getSessionScore() {
        return this.sessionScore;
    }

    /**
     * Clears the current session score.
     */
    public void resetSession() {
        this.sessionScore = null;
    }

    /**
     * Commits the current session score, if any, to the tracked scores, and resets the session.
     */
    public void commitSession() {
        if (this.sessionScore == null) {
            return;
        }

        this.commitScore(this.sessionScore);
        this.resetSession();
    }

    /**
     * Clears the current saved scores, and loads the new list from the scores data file.
     */
    private void load() {
        this.scores.clear();
        this.scores.addAll(Score.readScoresFromFile(this.scoresFile));
    }

    /**
     * Adds the given score to the list of tracked scores.
     *
     * @param score the score to add
     */
    public void commitScore(final Score score) {
        this.scores.add(score);
        Score.appendScoreToFile(score, this.scoresFile);
    }

    /**
     * Returns the score instance that holds the highest score per game in all the tracked scores. This excludes the
     * current session score.
     *
     * @return the high score instance
     */
    public Optional<Score> getHighScore() {
        return this.scores.stream()
                .reduce((highest, score) -> score.getScorePerGame() > highest.getScorePerGame() ? score : highest);
    }

}
