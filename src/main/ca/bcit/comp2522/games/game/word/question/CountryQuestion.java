package ca.bcit.comp2522.games.game.word.question;

import ca.bcit.comp2522.games.game.word.Country;

/**
 * Represents a question about a certain country.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public abstract class CountryQuestion {

    private final Country country;

    private final String question;
    private final String answer;

    /**
     * Creates a new question based on the given country.
     *
     * @param country the country
     */
    public CountryQuestion(final Country country) {
        this.country = country;
        this.question = this.createQuestionText();
        this.answer = this.createKnownAnswer();
    }

    /**
     * Generates the question text that will tell the user what the question is looking for.
     *
     * @return the question text
     */
    protected abstract String createQuestionText();

    protected abstract String createKnownAnswer();

    /**
     * Returns whether a given answer matches the known valid answer for this question.
     *
     * @param answer the given answer
     * @return whether the given answer is valid for this question
     */
    public boolean isValidAnswer(final String answer) {
        final String givenAnswer;
        final String knownAnswer;

        givenAnswer = answer.trim().toLowerCase();
        knownAnswer = this.answer.trim().toLowerCase();

        return knownAnswer.equals(givenAnswer);
    }

    /**
     * Returns the country associated with this question.
     *
     * @return the country
     */
    public Country getCountry() {
        return this.country;
    }

    /**
     * Returns the known answer for this question.
     *
     * @return the answer
     */
    public String getAnswer() {
        return this.answer;
    }

    /**
     * Returns the question text for this question.
     *
     * @return the question text
     */
    public String getQuestion() {
        return this.question;
    }

}
