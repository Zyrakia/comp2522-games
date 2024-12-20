package ca.bcit.comp2522.games.game.word;

import ca.bcit.comp2522.games.Main;
import ca.bcit.comp2522.games.game.GameController;
import ca.bcit.comp2522.games.game.word.question.CountryQuestion;
import ca.bcit.comp2522.games.game.word.question.GuessCapitalGivenCountryQuestion;
import ca.bcit.comp2522.games.game.word.question.GuessCountryGivenCapitalQuestion;
import ca.bcit.comp2522.games.game.word.question.GuessCountryGivenFactQuestion;
import ca.bcit.comp2522.games.game.word.score.Score;
import ca.bcit.comp2522.games.game.word.score.ScoreManager;
import ca.bcit.comp2522.games.menu.TerminalMenu;
import ca.bcit.comp2522.games.menu.item.MenuItem;
import ca.bcit.comp2522.games.menu.item.NoMenuItem;
import ca.bcit.comp2522.games.menu.item.YesMenuItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
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

    private static final Path COUNTRY_DATA_DIR = Path.of("src", "resources", "countries");
    private static final String COUNTRY_DATA_FILE_EXT = "txt";
    private static final String[] COUNTRY_DATA_FILE_NAMES = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
                                                              "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                                                              "y", "z" };

    private static final World WORLD = WordGameController.loadWorld();

    private static final List<Function<Country, CountryQuestion>> QUESTION_PROVIDERS = new ArrayList<>();
    private static final int QUESTIONS_PER_GAME = 10;
    private static final int ATTEMPTS_PER_QUESTION = 2;

    private static final ScoreManager SCORER = new ScoreManager(Path.of("score.txt"));

    static {
        WordGameController.QUESTION_PROVIDERS.add(GuessCountryGivenCapitalQuestion::new);
        WordGameController.QUESTION_PROVIDERS.add(GuessCapitalGivenCountryQuestion::new);
        WordGameController.QUESTION_PROVIDERS.add(GuessCountryGivenFactQuestion::new);
    }

    /**
     * Creates a new word game controller.
     */
    public WordGameController() {
        super("Geo Guesser", "Test your geographical knowledge!");
    }

    /**
     * Loads all country data files, decodes all countries inside them, and creates a world with those countries.
     *
     * @return the loaded world
     */
    private static World loadWorld() {
        final List<Country> countries;
        countries = new ArrayList<>();

        for (final String fileName : WordGameController.COUNTRY_DATA_FILE_NAMES) {
            final String fileNameWithExtension;
            final Path filePath;

            fileNameWithExtension = fileName.concat(".").concat(WordGameController.COUNTRY_DATA_FILE_EXT);
            filePath = WordGameController.COUNTRY_DATA_DIR.resolve(fileNameWithExtension);

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
        final MenuItem yes;
        final MenuItem no;
        final TerminalMenu<MenuItem> menu;

        yes = new YesMenuItem();
        no = new NoMenuItem();
        menu = new TerminalMenu<>("Do you want to play again?", Map.of("Yes", yes, "No", no));

        boolean cont = true;
        while (cont) {
            final Score gameScore;
            final MenuItem choice;

            gameScore = this.playGame();

            WordGameController.SCORER.addToSession(gameScore);
            System.out.println();
            System.out.println(WordGameController.SCORER.getSessionScore().toInterimReport());

            choice = menu.promptChoice();
            cont = choice == yes;
        }
    }

    /**
     * Plays a game that prompts the user for {@value WordGameController#QUESTIONS_PER_GAME} questions, and records
     * the result in the scores list.
     *
     * @return a single game score representing the game that was played
     */
    private Score playGame() {
        int totalRounds = 0;
        int firstAttemptAnswers = 0;
        int secondAttemptAnswers = 0;
        int fails = 0;

        while (totalRounds < WordGameController.QUESTIONS_PER_GAME) {
            final Country country;
            final CountryQuestion question;
            final int attemptsToAnswer;

            country = WordGameController.WORLD.getRandomCountry();
            question = WordGameController.getQuestionOf(country);
            attemptsToAnswer = this.askQuestion(totalRounds, question);

            if (attemptsToAnswer == -1) {
                fails++;
            } else if (attemptsToAnswer == 1) {
                firstAttemptAnswers++;
            } else {
                secondAttemptAnswers++;
            }

            totalRounds++;
        }

        final Score gameScore;
        gameScore = new Score(LocalDateTime.now(), 1, firstAttemptAnswers, secondAttemptAnswers, fails);

        return gameScore;
    }

    /**
     * Prompts the user for the specified question, and returns the result of the question.
     *
     * @param questionIndex the index of the question, used for the prompt
     * @param question      the question to ask
     * @return the amount of attempts the user needed to answer the question, or -1 if no answer was given
     */
    private int askQuestion(final int questionIndex, final CountryQuestion question) {
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

                return attempts;
            } else if (attempts == WordGameController.ATTEMPTS_PER_QUESTION) {
                System.out.println("Incorrect, the answer was \"" + question.getAnswer() + "\"!");
            } else {
                System.out.print("Incorrect answer, try again: ");
            }
        }

        return -1;
    }

    @Override
    protected void onFinish() {
        final Score sessionScore;
        final Optional<Score> highScore;
        final double sessionScorePerGame;
        final double highScorePerGame;
        final LocalDateTime highScoreDateTime;

        sessionScore = WordGameController.SCORER.getSessionScore();
        if (sessionScore == null) {
            return;
        }

        highScore = WordGameController.SCORER.getHighScore();
        sessionScorePerGame = sessionScore.getScorePerGame();
        highScorePerGame = highScore.map(Score::getScorePerGame).orElse(0.0);
        highScoreDateTime = highScore.map(Score::getDateTimePlayed).orElse(LocalDateTime.now());

        System.out.println();
        System.out.println(sessionScore.toFinalizedReport());

        if (highScore.isEmpty()) {
            System.out.printf("CONGRATULATIONS! You set the high score with an average of %s points per game!\n",
                              Score.formatScorePerGame(sessionScorePerGame));
        } else if (sessionScorePerGame > highScorePerGame) {
            System.out.printf(
                    "CONGRATULATIONS! You set the new high score with an average of %s points per game; the previous " +
                            "record was %s points per game on %s.\n", Score.formatScorePerGame(sessionScorePerGame),
                    Score.formatScorePerGame(highScorePerGame), Score.formatDateTimePlayed(highScoreDateTime));
        } else {
            System.out.printf("You did not beat the high score of %s points per game from %s.%n",
                              Score.formatScorePerGame(highScorePerGame),
                              Score.formatDateTimePlayed(highScoreDateTime));
        }

        WordGameController.SCORER.commitSession();
    }

}
