package ca.bcit.comp2522.games.util;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents an observable that announces updates of a specific type.
 *
 * @param <T> the type of value that can be observed
 * @author Ole Lammers
 * @version 1.0
 */
public abstract class Observable<T> {

    private final Set<Observer<T>> observers;

    /**
     * Creates a new observable.
     */
    public Observable() {
        this.observers = new LinkedHashSet<>();
    }

    /**
     * Pushes an update to all registered observers.
     *
     * @param value the value to push to observers
     */
    protected void announceUpdate(final T value) {
        for (final Observer<T> observer : this.observers) {
            observer.handleUpdate(value);
        }
    }

    /**
     * Registers a new observer to this observable.
     *
     * @param observer the observer to register
     */
    public void observe(final Observer<T> observer) {
        this.observers.add(observer);
    }

    /**
     * Unregisters an observer from this observable.
     *
     * @param observer the observer to unregister
     */
    public void unobserve(final Observer<T> observer) {
        this.observers.remove(observer);
    }

}
