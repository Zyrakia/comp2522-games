package ca.bcit.comp2522.games.menu;

import ca.bcit.comp2522.games.Main;
import ca.bcit.comp2522.games.menu.item.MenuItem;

import java.util.Map;

/**
 * Represents a terminal-input based menu to give multiple options to the user.
 *
 * @param <T> the type of menu item this terminal prompts for
 * @author Ole Lammers
 * @version 1.0
 */
public final class TerminalMenu<T extends MenuItem> {

    private static final int DIVIDER_WIDTH = 80;
    private static final String DIVIDER = "—".repeat(TerminalMenu.DIVIDER_WIDTH).concat(System.lineSeparator());

    private final Map<String, T> items;
    private final String title;
    private final String menuText;
    private boolean isPrompting;

    /**
     * Creates a new menu with the specified items as options.
     *
     * @param title the title of the menu
     * @param items the items that can be selected
     */
    public TerminalMenu(final String title, final Map<String, T> items) {
        TerminalMenu.validateTitle(title);
        TerminalMenu.validateItems(items);

        this.title = title;
        this.items = items;
        this.isPrompting = false;
        this.menuText = this.buildMenuText();
    }

    private static void validateTitle(final String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Cannot create a menu without a title.");
        }
    }

    /**
     * Validates the provided items to ensure that they can be navigated and displayed in a menu.
     *
     * @param items the items to validate
     * @param <T>   the type of menu item
     */
    private static <T extends MenuItem> void validateItems(final Map<String, T> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a menu from an empty set of items.");
        }
    }

    /**
     * Prompts for a choice (if another prompt is not already running) and returns the chosen controller.
     *
     * @return the controller that was chosen, or `null` if another prompt is running
     */
    public T promptChoice() {
        if (this.isPrompting) return null;
        this.isPrompting = true;

        System.out.println();

        System.out.print(this.menuText);
        System.out.print("Please enter one of the options: ");

        T choice = null;
        do {
            final String choiceInput;
            choiceInput = this.readChoice();

            if (choiceInput.isBlank()) {
                System.out.print("No recognized selection was received, please try again: ");
                continue;
            }

            choice = this.getItemFromChoice(choiceInput);

            if (choice == null) {
                System.out.print("The selection '" + choiceInput + "' is invalid, please try again: ");
            }
        } while (choice == null);

        System.out.println();

        this.isPrompting = false;
        return choice;
    }

    /**
     * Waits for input from the user for a line.
     *
     * @return the choice input, trimmed to remove trailing whitespace
     */
    private String readChoice() {
        return Main.SCANNER.nextLine().trim();
    }

    /**
     * Returns the item associated to the specific choice.
     * <p>
     * This finds the item with case insensitivity.
     *
     * @param choice the choice character
     * @return the associated item, or `null`
     */
    private T getItemFromChoice(final String choice) {
        for (final Map.Entry<String, T> entry : this.items.entrySet()) {
            final String activationString;
            final T item;

            activationString = entry.getKey();
            item = entry.getValue();

            if (activationString.equalsIgnoreCase(choice)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Assembles the menu text line by line, including all the names, activation characters and descriptions for each
     * option.
     *
     * @return the final assembled menu text
     */
    private String buildMenuText() {
        final StringBuilder result;

        result = new StringBuilder();

        result.append(this.title).append(System.lineSeparator());
        result.append(TerminalMenu.DIVIDER);

        for (final Map.Entry<String, T> entry : this.items.entrySet()) {
            final String activationString;
            final T item;
            final String name;
            final String description;

            activationString = entry.getKey();
            item = entry.getValue();

            if (item == null) {
                continue;
            }

            name = String.format("'%s' > %s", activationString, item.getName());
            description = item.getDescription();

            result.append(name).append(System.lineSeparator());

            if (!description.isBlank()) {
                result.append("\t")
                        .append(description.replace(System.lineSeparator(), "\t".concat(System.lineSeparator())))
                        .append(System.lineSeparator());
            }
        }

        result.append(TerminalMenu.DIVIDER);
        return result.toString();
    }

}
