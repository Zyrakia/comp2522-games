package ca.bcit.comp2522.games.game.word.score;

import ca.bcit.comp2522.games.util.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a session score from the word game.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class Score {

    private static final String ENCODED_LINE_DATA_SEPARATOR = ": ";

    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DecimalFormat POINTS_PER_GAME_FORMATTER = new DecimalFormat("#.##");

    private static final int FIRST_ATTEMPT_POINTS = 2;
    private static final int SECOND_ATTEMPT_POINTS = 1;

    private final LocalDateTime dateTimePlayed;
    private final int numGamesPlayed;
    private final int numCorrectFirstAttempt;
    private final int numCorrectSecondAttempt;
    private final int numIncorrectTwoAttempts;

    /**
     * Creates a new score record.
     *
     * @param dateTimePlayed          the datetime of when the game session occurred
     * @param numGamesPlayed          the amount of games played in the session
     * @param numCorrectFirstAttempt  the amount of questions correctly answered in the first attempt
     * @param numCorrectSecondAttempt the amount of question correctly answered in the second attempt
     * @param numIncorrectTwoAttempts the amount of questions incorrectly answered after two attempts
     */
    public Score(LocalDateTime dateTimePlayed, final int numGamesPlayed, final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt, final int numIncorrectTwoAttempts) {
        Score.validateScoreParameter(numGamesPlayed);
        Score.validateScoreParameter(numCorrectFirstAttempt);
        Score.validateScoreParameter(numCorrectSecondAttempt);
        Score.validateScoreParameter(numIncorrectTwoAttempts);

        this.dateTimePlayed = dateTimePlayed;
        this.numGamesPlayed = numGamesPlayed;
        this.numCorrectFirstAttempt = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectTwoAttempts = numIncorrectTwoAttempts;
    }

    /**
     * Returns the given score per game value as a formatted string.
     *
     * @param scorePerGame the score per game value
     * @return the formatted string
     */
    public static String formatScorePerGame(final double scorePerGame) {
        return Score.POINTS_PER_GAME_FORMATTER.format(scorePerGame);
    }

    /**
     * Returns the given date time as a formatted string.
     *
     * @param dateTimePlayed the date time played value
     * @return the formatted string
     */
    public static String formatDateTimePlayed(final LocalDateTime dateTimePlayed) {
        final DateTimeFormatter fmt;
        fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm:ss");

        return fmt.format(dateTimePlayed);
    }

    /**
     * Validates the given score parameter to ensure it is a positive integer.
     *
     * @param scoreParameter the score parameter to validate
     */
    private static void validateScoreParameter(final int scoreParameter) {
        if (scoreParameter < 0) {
            throw new IllegalArgumentException(
                    "A score entry parameter cannot be negative, '" + scoreParameter + "' is in violation.");
        }
    }

    /**
     * Appends the given score to the end of the given file, in a readable format.
     *
     * @param score the score to write
     * @param file  the file to append to
     */
    public static void appendScoreToFile(final Score score, final Path file) {
        try {
            final String report;

            if (Files.size(file) == 0) {
                report = score.toPersistentReport();
            } else {
                report = System.lineSeparator().concat(score.toPersistentReport());
            }

            Files.writeString(file, report, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads score entries from the given file, and decodes them into instances.
     *
     * @param file the file to read from
     * @return the decoded scores
     */
    public static List<Score> readScoresFromFile(final Path file) {
        final List<String> lines;
        final List<Score> scores;

        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        scores = new ArrayList<>();

        String currentReport = "";
        for (final String line : lines) {
            if (line.isBlank() && !currentReport.isBlank()) {
                scores.add(Score.fromPersistentReport(currentReport));
                currentReport = "";
            }

            currentReport = currentReport.concat(line).concat(System.lineSeparator());
        }

        if (!currentReport.isBlank()) {
            scores.add(Score.fromPersistentReport(currentReport));
        }

        return scores;
    }

    /**
     * Appends the given score to the end of the given file, in a readable format.
     *
     * @param score the score to write
     * @param file  the file name to append to
     */
    public static void appendScoreToFile(final Score score, final String file) {
        Score.appendScoreToFile(score, Path.of(file));
    }

    /**
     * Reads score entries from the given file, and decodes them into instances.
     *
     * @param file the file name to read from
     * @return the decoded scores
     */
    public static List<Score> readScoresFromFile(final String file) {
        return Score.readScoresFromFile(Path.of(file));
    }

    /**
     * Decodes a score instance based off of it's previously encoded persistent report.
     *
     * @param persistentReport the previously encoded persistent report
     * @return the decoded score
     */
    private static Score fromPersistentReport(final String persistentReport) {
        final int splitDataIndex = 1;
        final int lineContainingDate = 0;
        final int lineContainingGamesPlayed = 1;
        final int lineContainingCorrectFirstAttempts = 2;
        final int lineContainingCorrectSecondAttempts = 3;
        final int lineContainingIncorrectAttempts = 4;

        final List<String> lines;
        lines = Arrays.stream(persistentReport.split(System.lineSeparator()))
                .map((line) -> line.split(Score.ENCODED_LINE_DATA_SEPARATOR))
                .filter((lineParts) -> lineParts.length == splitDataIndex + 1)
                .map((lineParts) -> lineParts[splitDataIndex])
                .toList();

        final LocalDateTime dateTime;
        final int gamesPlayed;
        final int firstAttempts;
        final int secondAttempts;
        final int incorrectAttempts;

        try {
            dateTime = LocalDateTime.parse(lines.get(lineContainingDate), Score.DT_FORMATTER);
            gamesPlayed = Integer.parseInt(lines.get(lineContainingGamesPlayed));
            firstAttempts = Integer.parseInt(lines.get(lineContainingCorrectFirstAttempts));
            secondAttempts = Integer.parseInt(lines.get(lineContainingCorrectSecondAttempts));
            incorrectAttempts = Integer.parseInt(lines.get(lineContainingIncorrectAttempts));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "The encoded date on line " + lineContainingDate + " is not formatted correctly.", e);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(
                    "The persistent report did not include all required lines (%d, %d, %d, %d, %d).".formatted(
                            lineContainingDate, lineContainingGamesPlayed, lineContainingCorrectFirstAttempts,
                            lineContainingCorrectSecondAttempts, lineContainingIncorrectAttempts));
        }

        return new Score(dateTime, gamesPlayed, firstAttempts, secondAttempts, incorrectAttempts);
    }

    /**
     * Returns the total amount of points scored.
     *
     * @return the total score (points)
     */
    public int getScore() {
        int score = 0;

        score += this.numCorrectFirstAttempt * Score.FIRST_ATTEMPT_POINTS;
        score += this.numCorrectSecondAttempt * Score.SECOND_ATTEMPT_POINTS;

        return score;
    }

    /**
     * Returns the average score per game.
     *
     * @return the average score per game
     */
    public double getScorePerGame() {
        if (this.numGamesPlayed == 0) {
            return 0;
        }

        return ((double) this.getScore()) / this.numGamesPlayed;
    }

    /**
     * Adds the parameters from the given score to the parameters of this score, returning a new score that
     * represents the result.
     * <p>
     * The time played will remain the same as the current instance.
     *
     * @param score the score to add with this one
     * @return the sum result score
     */
    public Score add(final Score score) {
        return new Score(this.dateTimePlayed, this.numGamesPlayed + score.numGamesPlayed,
                         this.numCorrectFirstAttempt + score.numCorrectFirstAttempt,
                         this.numCorrectSecondAttempt + score.numCorrectSecondAttempt,
                         this.numIncorrectTwoAttempts + score.numIncorrectTwoAttempts);
    }

    /**
     * Stringifies this score into the format that is expected for end-of-game interim reports.
     *
     * @return the stringified result
     */
    public String toInterimReport() {
        final String dataPrefix;
        final StringBuilder sb;

        dataPrefix = "- ";
        sb = new StringBuilder();

        sb.append(dataPrefix)
                .append(this.numGamesPlayed)
                .append(Strings.pluralize(this.numGamesPlayed, " word game"))
                .append(" played")
                .append(System.lineSeparator());

        sb.append(dataPrefix)
                .append(this.numCorrectFirstAttempt)
                .append(Strings.pluralize(this.numCorrectFirstAttempt, " correct answer"))
                .append(" on the first attempt")
                .append(System.lineSeparator());

        sb.append(dataPrefix)
                .append(this.numCorrectSecondAttempt)
                .append(Strings.pluralize(this.numCorrectSecondAttempt, " correct answer"))
                .append("on the second attempt")
                .append(System.lineSeparator());

        sb.append(dataPrefix)
                .append(this.numIncorrectTwoAttempts)
                .append(Strings.pluralize(this.numIncorrectTwoAttempts, " incorrect answer"))
                .append(" on two attempts each")
                .append(System.lineSeparator());

        return sb.toString();
    }

    /**
     * Stringifies this score into the format that is expected for end-of-session finalized reports.
     *
     * @return the stringified result
     */
    public String toFinalizedReport() {
        final StringBuilder sb;
        final int firstAttemptPoints;
        final int secondAttemptPoints;
        final String formattedScorePerGame;

        sb = new StringBuilder();
        firstAttemptPoints = this.numCorrectFirstAttempt * Score.FIRST_ATTEMPT_POINTS;
        secondAttemptPoints = this.numCorrectSecondAttempt * Score.SECOND_ATTEMPT_POINTS;
        formattedScorePerGame = Score.formatScorePerGame(this.getScorePerGame());

        sb.append(this.numGamesPlayed)
                .append(Strings.pluralize(this.numGamesPlayed, " word game"))
                .append(" played")
                .append(System.lineSeparator());

        sb.append(this.numCorrectFirstAttempt)
                .append(Strings.pluralize(this.numCorrectFirstAttempt, " correct answer"))
                .append(" on the first attempt\t\t")
                .append(firstAttemptPoints)
                .append(" points")
                .append(System.lineSeparator());

        sb.append(this.numCorrectSecondAttempt)
                .append(Strings.pluralize(this.numCorrectSecondAttempt, " correct answer"))
                .append("on the second attempt\t\t")
                .append(secondAttemptPoints)
                .append(" points")
                .append(System.lineSeparator());

        sb.append(this.numIncorrectTwoAttempts)
                .append(Strings.pluralize(this.numIncorrectTwoAttempts, " incorrect answer"))
                .append(" on two attempts each\t0 points")
                .append(System.lineSeparator());

        sb.append(System.lineSeparator())
                .append("Total is ")
                .append(this.getScore())
                .append(" points in ")
                .append(this.numGamesPlayed)
                .append(Strings.pluralize(this.numGamesPlayed, " game"))
                .append(", for an average score of ")
                .append(formattedScorePerGame)
                .append(" points per game.")
                .append(System.lineSeparator());

        return sb.toString();
    }

    /**
     * Stringifies this score into the format that is expected for end-of-session persistent reports that will be
     * decoded later.
     *
     * @return the decode-enabled stringified result
     */
    public String toPersistentReport() {
        final String dataSep;
        final int totalScore;
        final StringBuilder sb;

        dataSep = Score.ENCODED_LINE_DATA_SEPARATOR;
        totalScore = this.getScore();
        sb = new StringBuilder();

        sb.append("Date and Time")
                .append(dataSep)
                .append(this.dateTimePlayed.format(Score.DT_FORMATTER))
                .append(System.lineSeparator());

        sb.append("Games Played").append(dataSep).append(this.numGamesPlayed).append(System.lineSeparator());

        sb.append("Correct First Attempts")
                .append(dataSep)
                .append(this.numCorrectFirstAttempt)
                .append(System.lineSeparator());

        sb.append("Correct Second Attempts")
                .append(dataSep)
                .append(this.numCorrectSecondAttempt)
                .append(System.lineSeparator());

        sb.append("Incorrect Attempts")
                .append(dataSep)
                .append(this.numIncorrectTwoAttempts)
                .append(System.lineSeparator());

        sb.append("Score")
                .append(dataSep)
                .append(totalScore)
                .append(Strings.pluralize(totalScore, " point"))
                .append(System.lineSeparator());

        return sb.toString();
    }

    /**
     * Returns a quick debug view of this score.
     *
     * @return the debug report
     */
    public String toDebugReport() {
        return String.format("Score: { %d, %d, %d, %d }", this.numGamesPlayed, this.numCorrectFirstAttempt,
                             this.numCorrectSecondAttempt, this.numIncorrectTwoAttempts);
    }

    /**
     * Returns the date time played.
     *
     * @return the date time played
     */
    public LocalDateTime getDateTimePlayed() {
        return this.dateTimePlayed;
    }

    /**
     * Returns the persistent report of this score.
     *
     * @return the persistent report
     * @see Score#toPersistentReport()
     */
    @Override
    public String toString() {
        return this.toPersistentReport();
    }

}
