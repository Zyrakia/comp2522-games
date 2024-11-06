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

    private static final char COUNTRY_CAPITAL_SEPARATOR = ':';
    private static final String DATA_FILE_EXTENSION = "txt";
    private static final String[] DATA_FILE_NAMES = { "a", "b", "c", "d", "e",
                                                      "f", "g", "h", "i", "j",
                                                      "k", "l", "m", "n", "o",
                                                      "p", "q", "r", "s", "t",
                                                      "u", "v", "w", "x", "y",
                                                      "z" };

    private static final World WORLD = WordGame.loadWorld();

    private static World loadWorld() {
        final List<Country> countries;
        countries = new ArrayList<>();

        for (final String fileName : WordGame.DATA_FILE_NAMES) {
            final String fileNameWithExtension;
            final Path filePath;

            fileNameWithExtension = String.format("%s.%s", fileName,
                                                  WordGame.DATA_FILE_EXTENSION);
            filePath = Path.of(fileNameWithExtension);

            try {
                countries.addAll(WordGame.loadCountriesFromFile(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new World(countries);
    }

    private static List<Country> loadCountriesFromFile(final Path dataFilePath) throws IOException {
        final List<Country> parsedCountries;
        final String fileContent;
        final String[] fileLines;

        fileContent = Files.readString(dataFilePath);
        fileLines = fileContent.split("\n");
        parsedCountries = new ArrayList<>();

        
        return parsedCountries;
    }

}
