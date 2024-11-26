package ca.bcit.comp2522.games.game.word;

import ca.bcit.comp2522.games.Main;
import ca.bcit.comp2522.games.game.GameController;
import ca.bcit.comp2522.games.game.score.GameSessionScore;
import ca.bcit.comp2522.games.game.score.RoundResult;
import ca.bcit.comp2522.games.game.word.question.CountryQuestion;
import ca.bcit.comp2522.games.game.word.question.GuessCapitalGivenCountryQuestion;
import ca.bcit.comp2522.games.game.word.question.GuessCountryGivenCapitalQuestion;
import ca.bcit.comp2522.games.game.word.question.GuessCountryGivenFactQuestion;
import ca.bcit.comp2522.games.game.word.score.FinalAttemptFailResult;
import ca.bcit.comp2522.games.game.word.score.FirstAttemptSuccessResult;
import ca.bcit.comp2522.games.game.word.score.PersistentWordGameSummarizer;
import ca.bcit.comp2522.games.game.word.score.PostGameWordGameSummarizer;
import ca.bcit.comp2522.games.game.word.score.PostSessionWordGameSummarizer;
import ca.bcit.comp2522.games.game.word.score.SecondAttemptSuccessResult;
import ca.bcit.comp2522.games.menu.TerminalMenu;
import ca.bcit.comp2522.games.menu.item.MenuItem;
import ca.bcit.comp2522.games.menu.item.NoMenuItem;
import ca.bcit.comp2522.games.menu.item.YesMenuItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

/**
 * The controller for the first game, the word game. This game has the user
 * guess country names based off of certain information.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class WordGameController extends GameController {

    /** The result achieved by guessing the question answer on the first attempt. */
    public static final RoundResult FIRST_ATTEMPT_CORRECT_RESULT = new FirstAttemptSuccessResult();

    /** The result achieved by guessing the question answer on the second attempt. */
    public static final RoundResult SECOND_ATTEMPT_CORRECT_RESULT = new SecondAttemptSuccessResult();

    /** The result achieved when not being able to guess the question by the last attempt. */
    public static final RoundResult INCORRECT_RESULT = new FinalAttemptFailResult();

    private static final Path PERSISTENT_SCORES_FILE = Path.of("score.txt");
    private static final String DATA_FILE_EXTENSION = "txt";
    private static final String[] DATA_FILE_NAMES = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                                                      "n", "o", "p", "q", "r", "s", "t", "u", "v", "y", "z" };

    private static final World WORLD = WordGameController.loadWorld();

    private static final List<Function<Country, CountryQuestion>> QUESTION_PROVIDERS = new ArrayList<>();
    private static final int QUESTIONS_PER_GAME = 10;
    private static final int ATTEMPTS_PER_QUESTION = 2;

    static {
        WordGameController.QUESTION_PROVIDERS.add(GuessCountryGivenCapitalQuestion::new);
        WordGameController.QUESTION_PROVIDERS.add(GuessCapitalGivenCountryQuestion::new);
        WordGameController.QUESTION_PROVIDERS.add(GuessCountryGivenFactQuestion::new);
    }

    /**
     * Creates a new word game controller.
     */
    public WordGameController() {
        super("Country Guesser", "Guess countries based off of facts and their capital city!");
    }

    /**
     * Loads all country data files, decodes all countries inside them, and creates a world with those countries.
     *
     * @return the loaded world
     */
    private static World loadWorld() {
        final List<Country> countries;
        countries = new ArrayList<>();

        for (final String fileName : WordGameController.DATA_FILE_NAMES) {
            final String fileNameWithExtension;
            final Path filePath;

            fileNameWithExtension = String.format("%s.%s", fileName, WordGameController.DATA_FILE_EXTENSION);
            filePath = Path.of("src", "resources", fileNameWithExtension);

            try {
                countries.addAll(WordGameController.loadCountriesFromFile(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new World(countries);
    }

    /**
     * Decodes all the encoded countries inside the specified data file.
     * <p>
     * Each encoded country will be a block, and each encoded country block will be separated from the other by a
     * completely empty line.
     * <p>
     * If there are any extraneous empty lines, they will be ignored, each block will be completely trimmed as well
     * before decoding.
     * <p>
     * Decoding of each country block is done by the {@link Country#decodeFromBlock(String)} method.
     *
     * @param dataFilePath the path of the countries data file
     * @return the list of decoded countries within the data file
     * @throws IOException if the data file cannot be opened
     */
    private static List<Country> loadCountriesFromFile(final Path dataFilePath) throws IOException {
        final List<Country> parsedCountries;
        final String fileContent;
        final String[] countryBlocks;

        fileContent = Files.readString(dataFilePath);
        countryBlocks = fileContent.split("\n".repeat(2));
        parsedCountries = new ArrayList<>();

        for (final String rawCountryBlock : countryBlocks) {
            final String normalizedCountryBlock;
            normalizedCountryBlock = rawCountryBlock.trim();

            if (normalizedCountryBlock.isEmpty()) {
                continue;
            }

            parsedCountries.add(Country.decodeFromBlock(normalizedCountryBlock));
        }

        return parsedCountries;
    }

    /**
     * Returns a random question type from the available question providers that is based on the given country.
     *
     * @param country the country to base the question off of
     * @return the question based off of the country
     */
    private static CountryQuestion getQuestionOf(final Country country) {
        if (WordGameController.QUESTION_PROVIDERS.isEmpty()) {
            throw new IllegalStateException(
                    "At least one question provider must be present in order for a question to be created for a " +
                            "country.");
        }

        final Random rand;
        final int providerCount;
        final int chosenIndex;

        rand = new Random();
        providerCount = WordGameController.QUESTION_PROVIDERS.size();
        chosenIndex = rand.nextInt(providerCount);

        return WordGameController.QUESTION_PROVIDERS.get(chosenIndex).apply(country);
    }

    @Override
    protected void onStart() {
        final TerminalMenu<MenuItem> menu;
        menu = new TerminalMenu<>("Do you want to play again?",
                                  Map.of("Yes", new YesMenuItem(), "No", new NoMenuItem()));

        boolean cont = true;
        while (cont) {
            final MenuItem choice;

            this.playGame();

            choice = menu.promptChoice();
            cont = choice instanceof YesMenuItem;
        }
    }

    /**
     * Plays a game that prompts the user for {@value WordGameController#QUESTIONS_PER_GAME} questions, and record
     * the result in a file.
     */
    private void playGame() {
        final GameSessionScore scoreTracker;
        int totalRounds = 0;

        scoreTracker = this.getCurrentScoreTracker();

        while (totalRounds < WordGameController.QUESTIONS_PER_GAME) {
            final Country country;
            final CountryQuestion question;
            final RoundResult questionResult;

            country = WordGameController.WORLD.getRandomCountry();
            question = WordGameController.getQuestionOf(country);
            questionResult = this.askQuestion(totalRounds, question);

            scoreTracker.reportRound(questionResult);
            totalRounds++;
        }

        this.getCurrentScoreTracker().reportGameFinished();

        final String summary;
        summary = scoreTracker.summarize(new PostGameWordGameSummarizer());

        System.out.println(System.lineSeparator() + summary);
    }

    /**
     * Prompts the user for the specified question, and returns whether they entered a correct answer or not.
     *
     * @param questionIndex the index of the question, used for the prompt
     * @param question      the question to ask
     * @return the validity of the answer given by the user
     */
    private RoundResult askQuestion(final int questionIndex, final CountryQuestion question) {
        int attempts = 0;

        System.out.println(System.lineSeparator() + "Question " + (questionIndex + 1) + ". " + question.getQuestion());
        System.out.print("Enter your answer: ");

        while (attempts < WordGameController.ATTEMPTS_PER_QUESTION) {
            final String givenAnswer;
            final boolean answerCorrect;

            givenAnswer = Main.SCANNER.nextLine();
            answerCorrect = question.isValidAnswer(givenAnswer);

            attempts++;

            if (answerCorrect) {
                System.out.println("Correct answer, great job!");

                if (attempts >= WordGameController.ATTEMPTS_PER_QUESTION) {
                    return WordGameController.SECOND_ATTEMPT_CORRECT_RESULT;
                } else {
                    return WordGameController.FIRST_ATTEMPT_CORRECT_RESULT;
                }
            } else if (attempts == WordGameController.ATTEMPTS_PER_QUESTION) {
                System.out.println("Incorrect, the answer was \"" + question.getAnswer() + "\"!");
            } else {
                System.out.print("Incorrect answer, try again: ");
            }
        }

        return WordGameController.INCORRECT_RESULT;
    }

    @Override
    protected void onFinish() {
        final DecimalFormat pointsPerGameFormat;
        pointsPerGameFormat = new DecimalFormat("#.##");

        final GameSessionScore scoreTracker;
        final Optional<GameSessionScore> highScoreTracker;
        final double pointsPerGame;
        final double highScorePointsPerGame;
        final String pointsPerGameStr;
        final String highScorePointsPerGameStr;
        final String summary;

        scoreTracker = this.getCurrentScoreTracker();
        highScoreTracker = this.getHighScore();
        pointsPerGame = scoreTracker.getPointsPerGame();
        highScorePointsPerGame = highScoreTracker.map(GameSessionScore::getPointsPerGame).orElse(0.0);
        pointsPerGameStr = pointsPerGameFormat.format(pointsPerGame);
        highScorePointsPerGameStr = pointsPerGameFormat.format(highScorePointsPerGame);
        summary = scoreTracker.summarize(new PostSessionWordGameSummarizer());

        System.out.println(System.lineSeparator() + summary);
        this.writeCurrentScore();

        if (highScoreTracker.isEmpty()) {
            System.out.println(
                    "You have set a new high score with an average of " + pointsPerGameStr + " points per game!");
            return;
        }

        final LocalDateTime highScoreDateTime;
        final String highScoreDateStr;
        final String highScoreTimeStr;

        highScoreDateTime = highScoreTracker.get().getStartDateTime();
        highScoreDateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(highScoreDateTime);
        highScoreTimeStr = DateTimeFormatter.ofPattern("hh:mm:ss").format(highScoreDateTime);

        if (pointsPerGame > highScorePointsPerGame) {
            System.out.println("You have a set new high score with an average of " + pointsPerGameStr +
                                       " points per game; the previous record was " + highScorePointsPerGameStr +
                                       " points per game on " + highScoreDateStr + " at " + highScoreTimeStr + ".");
        } else {
            System.out.println(
                    "You did not beat the high score of " + highScorePointsPerGameStr + " points per game from " +
                            highScoreDateStr + " at  " + highScoreTimeStr + ".");
        }
    }

    /**
     * Writes the current score tracker to the persistent scores file.
     */
    private void writeCurrentScore() {
        final GameSessionScore scoreTracker;
        final String persistentSummary;

        scoreTracker = this.getCurrentScoreTracker();
        persistentSummary = scoreTracker.summarize(new PersistentWordGameSummarizer());

        try {
            Files.writeString(WordGameController.PERSISTENT_SCORES_FILE, persistentSummary + System.lineSeparator(),
                              StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
