package ca.bcit.comp2522.games.game.word.question;

import ca.bcit.comp2522.games.game.word.Country;

/**
 * Represents a question that requires the country name, given the capital city name.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class GuessCountryGivenCapitalQuestion extends CountryQuestion {

    /**
     * Creates a new question.
     *
     * @param country the country to guess the capital city of
     */
    public GuessCountryGivenCapitalQuestion(final Country country) {
        super(country);
    }

    @Override
    protected String createQuestionText() {
        return "What is the country with the capital city name of \"" + this.getCountry().getCapitalCityName() + "\"?";
    }

    @Override
    protected String createKnownAnswer() {
        return this.getCountry().getName();
    }

}
