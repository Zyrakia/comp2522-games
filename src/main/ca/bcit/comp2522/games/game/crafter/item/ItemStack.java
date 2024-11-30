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
     * @param item      the item in this stack
     * @param stackSize the amount of items in this stack
     */
    public ItemStack(final Item item, final int stackSize) {
        ItemStack.validateStackSize(stackSize);

        this.item = item;
        this.stackSize = stackSize;
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
    public int getAmount() {
        return this.stackSize;
    }

    /**
     * Creates a new stack with the stack size increased by the given amount.
     * <p>
     * This method will clamp the new stack size to ensure it is within the {@link ItemStack#MIN_STACK_SIZE} and
     * {@link ItemStack#MAX_STACK_SIZE}.
     * <p>
     * While negative input is not rejected, it is advised to use the {@link ItemStack#minus(int)} method instead.
     *
     * @param amount the amount to increase the stack size by
     * @return the new stack with the increased stack size
     */
    public ItemStack plus(final int amount) {
        int newAmount;

        newAmount = this.getAmount() + amount;
        newAmount = Math.clamp(newAmount, ItemStack.MIN_STACK_SIZE, ItemStack.MAX_STACK_SIZE);

        return new ItemStack(this.getItem(), newAmount);
    }

    /**
     * Creates a new stack with the stack size decreased by the given amount.
     * <p>
     * This method will clamp the new stack size to ensure it is within the {@link ItemStack#MIN_STACK_SIZE} and
     * {@link ItemStack#MAX_STACK_SIZE}.
     * <p>
     * While negative input is not rejected, it is advised to use the {@link ItemStack#plus(int)} method instead.
     *
     * @param amount the amount to decrease the stack size by
     * @return the new stack with the decreased stack size
     */
    public ItemStack minus(final int amount) {
        int newAmount;

        newAmount = this.getAmount() - amount;
        newAmount = Math.clamp(newAmount, ItemStack.MIN_STACK_SIZE, ItemStack.MAX_STACK_SIZE);

        return new ItemStack(this.getItem(), newAmount);
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
