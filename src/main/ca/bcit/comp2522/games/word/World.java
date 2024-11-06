package ca.bcit.comp2522.games.word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a geographical world with multiple countries.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class World {

    /**
     * Maps country names to associated {@link Country} instances.
     */
    private final Map<String, Country> countries;

    /**
     * Creates a new world.
     *
     * @param countries the countries within the world
     */
    public World(final List<Country> countries) {
        World.validateCountries(countries);
        this.countries = World.mapCountriesByName(countries);
    }

    private static void validateCountries(final List<Country> countries) {
        if (countries == null || countries.isEmpty()) {
            throw new IllegalArgumentException(
                    "A world must have at least one country.");
        }
    }

    /**
     * Takes a list of countries and maps each country with the name of the
     * country.
     *
     * @param countries the countries to map
     * @return a map of country names to the associated {@link Country}
     */
    private static Map<String, Country> mapCountriesByName(final List<Country> countries) {
        final Map<String, Country> nameToCountry;
        nameToCountry = new HashMap<>();

        for (final Country country : countries) {
            final String name;
            name = country.getName();

            if (nameToCountry.containsKey(name)) {
                throw new IllegalArgumentException(
                        "The country name \"" + name +
                                "\" is duplicate in the list of countries.");
            }

            nameToCountry.put(name, country);
        }

        return nameToCountry;
    }

    /**
     * Obtains the list of countries within this world.
     *
     * @return the countries
     */
    public final Map<String, Country> getCountries() {

    }

}