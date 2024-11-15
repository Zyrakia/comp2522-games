package ca.bcit.comp2522.games.word;

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
public class WordGame {

    private static final String DATA_FILE_EXTENSION = "txt";
    private static final String[] DATA_FILE_NAMES = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                                                      "n", "o", "p", "q", "r", "s", "t", "u", "v", "y", "z" };

    private static final World WORLD = WordGame.loadWorld();

    /**
     * Loads all country data files, decodes all countries inside them, and creates a world with those countries.
     *
     * @return the loaded world
     */
    private static World loadWorld() {
        final List<Country> countries;
        countries = new ArrayList<>();

        for (final String fileName : WordGame.DATA_FILE_NAMES) {
            final String fileNameWithExtension;
            final Path filePath;

            fileNameWithExtension = String.format("%s.%s", fileName, WordGame.DATA_FILE_EXTENSION);
            filePath = Path.of("src", "resources", fileNameWithExtension);

            try {
                countries.addAll(WordGame.loadCountriesFromFile(filePath));
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
        countryBlocks = fileContent.split(System.lineSeparator().repeat(2));
        parsedCountries = new ArrayList<>();

        for (String countryBlock : countryBlocks) {
            countryBlock = countryBlock.trim();
            if (countryBlock.isEmpty()) {
                continue;
            }

            final Country country;
            country = Country.decodeFromBlock(countryBlock);

            parsedCountries.add(country);
        }

        return parsedCountries;
    }

}
