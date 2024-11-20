package ca.bcit.comp2522.games.game.word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a geographical world with multiple countries.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class World {

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

    /**
     * Validates the given list of countries to ensure it is within limits.
     *
     * @param countries the list of countries to validate
     */
    private static void validateCountries(final List<Country> countries) {
        if (countries == null || countries.isEmpty()) {
            throw new IllegalArgumentException("A world must have at least one country.");
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
                        "The country name \"" + name + "\" is duplicate in the list of countries.");
            }

            nameToCountry.put(name, country);
        }

        return nameToCountry;
    }

    /**
     * Returns a random country out of the countries within this world.
     *
     * @return a random country
     */
    public Country getRandomCountry() {
        final Random random;
        final List<Country> countries;
        final Country country;

        random = new Random();
        countries = new ArrayList<>(this.countries.values());
        country = countries.get(random.nextInt(countries.size()));

        return country;
    }


}
