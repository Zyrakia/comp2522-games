package ca.bcit.comp2522.games;

/**
 * Represents an option that can be added to a menu.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public interface MenuItem {

    /**
     * Returns the name of this menu item.
     *
     * @return the name
     */
    String getName();

    /**
     * Returns the description of this menu item.
     *
     * @return the description
     */
    String getDescription();

}
