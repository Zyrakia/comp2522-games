package ca.bcit.comp2522.games.game.word.score;

import ca.bcit.comp2522.games.game.score.GameSessionScore;
import ca.bcit.comp2522.games.game.score.GameSessionSummarizer;
import ca.bcit.comp2522.games.game.word.WordGameController;
import ca.bcit.comp2522.games.util.Strings;

import java.text.DecimalFormat;

/**
 * Summarizes the word game for display after each game exit.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class PostSessionWordGameSummarizer implements GameSessionSummarizer {

    private static final DecimalFormat AVG_FMT = new DecimalFormat("#.##");

    @Override
    public String summarize(final GameSessionScore tracker) {
        final StringBuilder result;

        final int totalGames;
        final int totalPoints;
        final double averagePoints;
        final int firstAttemptSuccesses;
        final int firstAttemptPoints;
        final int secondAttemptSuccesses;
        final int secondAttemptPoints;
        final int fails;
        final int failPoints;

        result = new StringBuilder();
        totalGames = tracker.getTotalGamesPlayed();
        totalPoints = tracker.getTotalPoints();
        averagePoints = ((double) totalPoints) / totalGames;
        firstAttemptSuccesses = tracker.getRoundsPlayed(WordGameController.FIRST_ATTEMPT_CORRECT_RESULT);
        firstAttemptPoints = tracker.getPoints(WordGameController.FIRST_ATTEMPT_CORRECT_RESULT);
        secondAttemptSuccesses = tracker.getRoundsPlayed(WordGameController.SECOND_ATTEMPT_CORRECT_RESULT);
        secondAttemptPoints = tracker.getPoints(WordGameController.SECOND_ATTEMPT_CORRECT_RESULT);
        fails = tracker.getRoundsPlayed(WordGameController.INCORRECT_RESULT);
        failPoints = tracker.getPoints(WordGameController.INCORRECT_RESULT);

        result.append(totalGames)
                .append(Strings.pluralize(totalGames, " word game"))
                .append(" played")
                .append(System.lineSeparator());

        result.append(firstAttemptSuccesses)
                .append(Strings.pluralize(firstAttemptSuccesses, " correct answer"))
                .append(" on the first attempt")
                .append("\t\t")
                .append(firstAttemptPoints)
                .append(Strings.pluralize(firstAttemptPoints, " point"))
                .append(System.lineSeparator());

        result.append(secondAttemptSuccesses)
                .append(Strings.pluralize(secondAttemptSuccesses, " correct answer"))
                .append(" on the second attempt")
                .append("\t\t")
                .append(secondAttemptPoints)
                .append(Strings.pluralize(secondAttemptSuccesses, " point"))
                .append(System.lineSeparator());

        result.append(fails)
                .append(Strings.pluralize(secondAttemptSuccesses, " incorrect answer"))
                .append(" on two attempts each")
                .append("\t\t")
                .append(failPoints)
                .append(Strings.pluralize(failPoints, " point"))
                .append(System.lineSeparator());

        result.append(System.lineSeparator())
                .append("Total is ")
                .append(totalPoints)
                .append(Strings.pluralize(totalPoints, " point"))
                .append(" in ")
                .append(totalGames)
                .append(Strings.pluralize(totalGames, " game"))
                .append(", for an average score of ")
                .append(PostSessionWordGameSummarizer.AVG_FMT.format(averagePoints))
                .append(" points per game.")
                .append(System.lineSeparator());

        return result.toString();
    }

}
