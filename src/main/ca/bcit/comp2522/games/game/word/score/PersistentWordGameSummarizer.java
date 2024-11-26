package ca.bcit.comp2522.games.game.word.score;

import ca.bcit.comp2522.games.game.score.GameSessionScore;
import ca.bcit.comp2522.games.game.score.GameSessionSummarizer;
import ca.bcit.comp2522.games.game.word.WordGameController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Summarizes the word game for persistent reference storage in a file, which means including the date and time of
 * playing.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class PersistentWordGameSummarizer implements GameSessionSummarizer {

    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String summarize(final GameSessionScore tracker) {
        final StringBuilder result;

        final LocalDateTime startDateTime;
        final int totalGames;
        final int totalScore;
        final int firstAttemptSuccesses;
        final int secondAttemptSuccesses;
        final int fails;

        result = new StringBuilder();

        startDateTime = tracker.getStartDateTime();
        totalGames = tracker.getTotalGamesPlayed();
        totalScore = tracker.getTotalPoints();
        firstAttemptSuccesses = tracker.getRoundsPlayed(WordGameController.FIRST_ATTEMPT_CORRECT_RESULT);
        secondAttemptSuccesses = tracker.getRoundsPlayed(WordGameController.SECOND_ATTEMPT_CORRECT_RESULT);
        fails = tracker.getRoundsPlayed(WordGameController.INCORRECT_RESULT);

        result.append("Date and Time: ").append(startDateTime.format(DT_FORMAT)).append(System.lineSeparator());
        result.append("Games Played: ").append(totalGames).append(System.lineSeparator());
        result.append("Correct First Attempts: ").append(firstAttemptSuccesses).append(System.lineSeparator());
        result.append("Correct Second Attempts: ").append(secondAttemptSuccesses).append(System.lineSeparator());
        result.append("Incorrect Attempts: ").append(fails).append(System.lineSeparator());
        result.append("Total Score: ").append(totalScore).append(System.lineSeparator());

        return result.toString();
    }

}
