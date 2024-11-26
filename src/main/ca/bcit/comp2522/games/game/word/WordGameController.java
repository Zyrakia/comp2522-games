package ca.bcit.comp2522.games.game.word;

import ca.bcit.comp2522.games.Main;
import ca.bcit.comp2522.games.game.GameController;
import ca.bcit.comp2522.games.game.score.RoundResult;
import ca.bcit.comp2522.games.game.word.question.CountryQuestion;
import ca.bcit.comp2522.games.game.word.question.GuessCapitalGivenCountryQuestion;
import ca.bcit.comp2522.games.game.word.question.GuessCountryGivenCapitalQuestion;
import ca.bcit.comp2522.games.game.word.question.GuessCountryGivenFactQuestion;
import ca.bcit.comp2522.games.menu.TerminalMenu;
import ca.bcit.comp2522.games.menu.item.MenuItem;
import ca.bcit.comp2522.games.menu.item.NoMenuItem;
import ca.bcit.comp2522.games.menu.item.YesMenuItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private static final String DATA_FILE_EXTENSION = "txt";
    private static final String[] DATA_FILE_NAMES = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                                                      "n", "o", "p", "q", "r", "s", "t", "u", "v", "y", "z" };

    private static final World WORLD = WordGameController.loadWorld();
    private static final int QUESTIONS_PER_GAME = 10;
    private static final int ATTEMPTS_PER_QUESTION = 2;

    private static final RoundResult FIRST_ATTEMPT_CORRECT_RESULT = new RoundResult(
            "correct answers on the first attempt", 2);
    private static final RoundResult SECOND_ATTEMPT_CORRECT_RESULT = new RoundResult(
            "correct answers on the second attempt", 2);
    private static final RoundResult INCORRECT_RESULT = new RoundResult("incorrect answers on two attempts each", 0);

    private static final List<Function<Country, CountryQuestion>> QUESTION_PROVIDERS = new ArrayList<>();

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
        int totalRounds = 0;

        while (totalRounds < WordGameController.QUESTIONS_PER_GAME) {
            final Country country;
            final CountryQuestion question;
            final RoundResult questionResult;

            country = WordGameController.WORLD.getRandomCountry();
            question = WordGameController.getQuestionOf(country);
            questionResult = this.askQuestion(totalRounds, question);

            this.scoreTracker.reportRound(questionResult);
            totalRounds++;
        }

        // TODO print current state of class wide score tracker
        System.out.println("TEMP: played " + totalRounds + " rounds");
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
        // TODO print total amount of games played
        // TODO save high score
        // TODO report high score status
        // TODO save global score tracker to file
    }

}
