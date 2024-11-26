package ca.bcit.comp2522.games.game.word.result;

import ca.bcit.comp2522.games.game.score.RoundResult;

/**
 * This result is used when the correct answer is provided on the second attempt.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class SecondAttemptSuccess extends RoundResult {

    /**
     * Creates a result that describes a correct answer on the second attempt.
     */
    public SecondAttemptSuccess() {
        super(1);
    }

}
