package ca.bcit.comp2522.games.menu.item;

/**
 * Represents a generic "Yes" item within a terminal.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public class YesMenuItem implements MenuItem {

    @Override
    public String getName() {
        return "Yes";
    }

    @Override
    public String getDescription() {
        return "";
    }

}
