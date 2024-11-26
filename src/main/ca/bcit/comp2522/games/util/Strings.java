package ca.bcit.comp2522.games.util;

/**
 * String utility class.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class Strings {

    /**
     * Adds an `s` to the end of a string if a reference value is not equal to 1.
     *
     * @param reference the reference value
     * @param content   the content to modify
     * @return the potentially pluralized result
     */
    public static String pluralize(final int reference, final String content) {
        if (reference == 1) return content;
        else return content + "s";
    }

}
