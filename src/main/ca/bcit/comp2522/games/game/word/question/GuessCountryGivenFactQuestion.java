package ca.bcit.comp2522.games.game.word.question;

import ca.bcit.comp2522.games.game.word.Country;

import java.util.Random;

/**
 * Represents a question that requires the country name, given a fact about the county.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class GuessCountryGivenFactQuestion extends CountryQuestion {

    /**
     * Creates a new question.
     *
     * @param country the country to guess the capital city of
     */
    public GuessCountryGivenFactQuestion(final Country country) {
        super(country);
    }

    @Override
    protected String createQuestionText() {
        final String fact;
        fact = this.getRandomFact();

        return "What country is this fact describing?\n\"" + fact + "\"";
    }

    @Override
    protected String createKnownAnswer() {
        return this.getCountry().getName();
    }

    /**
     * Returns a random fact from the country that this question relates to.
     *
     * @return the random fact
     */
    private String getRandomFact() {
        final Random rand;
        final String[] facts;
        final String randomFact;

        rand = new Random();
        facts = this.getCountry().getFacts();
        randomFact = facts[rand.nextInt(facts.length)];

        return randomFact;
    }

}
