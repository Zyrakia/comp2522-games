package ca.bcit.comp2522.games.game.score;

/**
 * Summarizes a score tracker into a readable format.
 *
 * @author Ole Lammers
 * @version 1.0
 */
@FunctionalInterface
public interface GameSessionSummarizer {

    /**
     * Summarizes the given tracker into a readable format.
     *
     * @param tracker the tracker to summarize
     * @return the summary
     */
    String summarize(GameSessionScore tracker);

}
