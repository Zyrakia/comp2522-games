package ca.bcit.comp2522.games.game.word.score;

import ca.bcit.comp2522.games.game.score.RoundResult;

/**
 * This result is used when the correct answer is provided on the first attempt.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class FirstAttemptSuccessResult extends RoundResult {

    /**
     * Creates a result that describes a correct answer on the first attempt.
     */
    public FirstAttemptSuccessResult() {
        super(2);
    }

}
