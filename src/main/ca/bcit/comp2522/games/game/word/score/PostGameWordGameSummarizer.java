package ca.bcit.comp2522.games.game.word.score;

import ca.bcit.comp2522.games.game.score.GameSessionScore;
import ca.bcit.comp2522.games.game.score.GameSessionSummarizer;
import ca.bcit.comp2522.games.game.word.WordGameController;
import ca.bcit.comp2522.games.util.Strings;

/**
 * Summarizes the word game for display after each round.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class PostGameWordGameSummarizer implements GameSessionSummarizer {

    @Override
    public String summarize(final GameSessionScore tracker) {
        final StringBuilder result;

        final int totalGames;
        final int firstAttemptSuccesses;
        final int secondAttemptSuccesses;
        final int fails;

        result = new StringBuilder();
        totalGames = tracker.getTotalGamesPlayed();
        firstAttemptSuccesses = tracker.getRoundsPlayed(WordGameController.FIRST_ATTEMPT_CORRECT_RESULT);
        secondAttemptSuccesses = tracker.getRoundsPlayed(WordGameController.SECOND_ATTEMPT_CORRECT_RESULT);
        fails = tracker.getRoundsPlayed(WordGameController.INCORRECT_RESULT);

        result.append(totalGames)
                .append(Strings.pluralize(totalGames, " word game"))
                .append(" played")
                .append(System.lineSeparator());

        result.append(firstAttemptSuccesses)
                .append(Strings.pluralize(firstAttemptSuccesses, " correct answer"))
                .append(" on the first attempt")
                .append(System.lineSeparator());

        result.append(secondAttemptSuccesses)
                .append(Strings.pluralize(secondAttemptSuccesses, " correct answer"))
                .append(" on the second attempt")
                .append(System.lineSeparator());

        result.append(fails)
                .append(Strings.pluralize(secondAttemptSuccesses, " incorrect answer"))
                .append(" on two attempts each")
                .append(System.lineSeparator());

        return result.toString();
    }

}
