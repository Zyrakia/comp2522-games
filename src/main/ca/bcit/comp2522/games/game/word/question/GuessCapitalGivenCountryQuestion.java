package ca.bcit.comp2522.games.game.word.question;

import ca.bcit.comp2522.games.game.word.Country;

/**
 * Represents a question that requires the capital city name, given the country name.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class GuessCapitalGivenCountryQuestion extends CountryQuestion {

    /**
     * Creates a new question.
     *
     * @param country the country to guess the capital city of
     */
    public GuessCapitalGivenCountryQuestion(final Country country) {
        super(country);
    }

    @Override
    protected String createQuestionText() {
        return "What is the capital city of \"" + this.getCountry().getName() + "\"?";
    }

    @Override
    protected String createKnownAnswer() {
        return this.getCountry().getCapitalCityName();
    }

}
