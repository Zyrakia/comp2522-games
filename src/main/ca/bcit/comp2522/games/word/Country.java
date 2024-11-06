package ca.bcit.comp2522.games.word;

import java.util.List;

/**
 * Represents a country and various details of that country.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class Country {

    /**
     * The minimum amount of facts a country must have defined to be valid.
     */
    public static final int MIN_FACTS = 1;

    private final String name;
    private final String capitalCityName;
    private final String[] facts;

    /**
     * Creates a new country.
     *
     * @param name            the name
     * @param capitalCityName the name of the capital city
     * @param facts           various facts relating to the country
     */
    public Country(final String name, final String capitalCityName,
                   final String[] facts) {
        Country.validateName(name);
        Country.validateName(capitalCityName);
        Country.validateFacts(facts);

        this.name = name;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    /**
     * Validates the given name to ensure it is valid. This could either be a
     * country or capital name.
     *
     * @param name the name to validate
     */
    private static void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A name must be specified.");
        }
    }

    /**
     * Validates the given array of facts to ensure it is valid.
     *
     * @param facts the facts array to validate
     */
    private static void validateFacts(final String[] facts) {
        if (facts == null || facts.length < Country.MIN_FACTS) {
            throw new IllegalArgumentException(
                    "A country must have at least " + Country.MIN_FACTS +
                            " facts specified.");
        }
    }

    /**
     * Returns the name of this country.
     *
     * @return the country name
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Returns the name of the capital city of this country.
     *
     * @return the capital city name
     */
    public final String getCapitalCityName() {
        return this.capitalCityName;
    }

    /**
     * Returns a copy of the facts relating to this country.
     *
     * @return a readonly of the facts
     */
    public final List<String> getFacts() {
        return List.of(this.facts);
    }

}
