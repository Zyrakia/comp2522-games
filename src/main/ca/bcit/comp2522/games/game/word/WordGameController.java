package ca.bcit.comp2522.games.game.word;

import ca.bcit.comp2522.games.game.GameController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onStart() {
        System.out.println("Word game!");
    }

    @Override
    protected void onFinish() {
        System.out.println("Word game finished!");
    }

}
