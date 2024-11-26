package ca.bcit.comp2522.games.game.word.result;

import ca.bcit.comp2522.games.game.score.RoundResult;

/**
 * This result is used when the incorrect answer is provided on the final attempt.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class FinalAttemptFail extends RoundResult {

    /**
     * Creates a result that describes the user not getting the correct answer for a question at all.
     */
    public FinalAttemptFail() {
        super(0);
    }

}
