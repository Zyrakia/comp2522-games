package ca.bcit.comp2522.games.game.crafter.item;

/**
 * Represents a stack of a certain item.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class ItemStack {

    /**
     * Represents the minimum item stack size.
     */
    public static final int MIN_STACK_SIZE = 1;

    /**
     * Represents the maximum item stack size.
     */
    public static final int MAX_STACK_SIZE = 999;

    private static final int SINGLE_STACK_AMOUNT = 1;

    private final Item item;
    private final int stackSize;

    /**
     * Creates a new inventory stack holding the specified item.
     *
     * @param item             the item in this stack
     * @param initialStackSize the initial size of the stack
     */
    public ItemStack(final Item item, final int initialStackSize) {
        ItemStack.validateStackSize(initialStackSize);

        this.item = item;
        this.stackSize = initialStackSize;
    }

    /**
     * Creates a new stack holding a single item.
     *
     * @param item the item in the stack
     */
    public ItemStack(final Item item) {
        this(item, ItemStack.SINGLE_STACK_AMOUNT);
    }

    /**
     * Validates the given stack size to ensure it is within limits.
     *
     * @param stackSize the stack size to validate
     */
    private static void validateStackSize(final int stackSize) {
        if (stackSize < ItemStack.MIN_STACK_SIZE || stackSize > ItemStack.MAX_STACK_SIZE) {
            throw new IllegalArgumentException(
                    "A stack size must be between " + ItemStack.MIN_STACK_SIZE + " and " + ItemStack.MAX_STACK_SIZE +
                            ".");
        }
    }

    /**
     * Returns the item in this stack.
     *
     * @return the item
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * Returns the amount of items in this stack.
     *
     * @return the stack size
     */
    public int getStackSize() {
        return this.stackSize;
    }

    /**
     * Returns whether this stack is currently holding the minimum quantity. If it is, removing any will throw an
     * exception.
     *
     * @return whether this stack is at minimum quantity
     */
    public boolean isMinimumQuantity() {
        return this.stackSize == ItemStack.MIN_STACK_SIZE;
    }

    /**
     * Returns whether this stack is current holding the maximum quantity. If it is, adding any will throw an exception.
     *
     * @return whether this stack is at maximum quantity
     */
    public boolean isMaximumQuantity() {
        return this.stackSize == ItemStack.MAX_STACK_SIZE;
    }

}
