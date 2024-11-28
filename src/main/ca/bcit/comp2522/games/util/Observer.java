package ca.bcit.comp2522.games.util;

/**
 * Represents a class that can observe an observable of a specific type.
 *
 * @param <T> the type of value that is observed
 * @author Ole Lammers
 * @version 1.0
 */
public interface Observer<T> {

    /**
     * Processes an update of the observable with the given data.
     *
     * @param value the announced value of the observable
     */
    void handleUpdate(T value);

}
