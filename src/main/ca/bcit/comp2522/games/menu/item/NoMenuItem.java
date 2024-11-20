package ca.bcit.comp2522.games.menu.item;

/**
 * Represents a generic "No" item within a terminal.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class NoMenuItem implements MenuItem {

    @Override
    public String getName() {
        return "No";
    }

    @Override
    public String getDescription() {
        return "";
    }

}
