package ca.bcit.comp2522.games.game.crafter.inventory;

import ca.bcit.comp2522.games.game.crafter.item.Item;
import ca.bcit.comp2522.games.game.crafter.item.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * Represents an inventory with pagination capabilities.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class PaginatedInventory extends Inventory {

    /**
     * Represents the minimum page size supported by a paginated inventory.
     */
    public static final int MIN_PAGE_SIZE = 1;

    /**
     * Represents the amount of pages that will always be retrievable by a paginated inventory.
     */
    public static final int MIN_PAGES = 1;

    private final int pageSize;

    /**
     * Creates a new paginated inventory.
     *
     * @param pageSize the amount of items on each page
     */
    public PaginatedInventory(final int pageSize) {
        super();

        PaginatedInventory.validatePageSize(pageSize);
        this.pageSize = pageSize;
    }

    /**
     * Validates the inventory page size to ensure it is within limits.
     *
     * @param pageSize the page size
     */
    private static void validatePageSize(final int pageSize) {
        if (pageSize < PaginatedInventory.MIN_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must be at least " + PaginatedInventory.MIN_PAGE_SIZE + ".");
        }
    }

    /**
     * Validates the given page index to ensure it is within bounds of this paginated inventory.
     * <p>
     * The page index should be between 0 and the page count of this inventory - 1 (because it is an index)
     *
     * @param pageIndex the page index
     */
    private void validatePageIndex(final int pageIndex) {
        final int minIndex;
        final int maxIndex;

        minIndex = 0;
        maxIndex = this.getPageCount() - 1;

        if (pageIndex < minIndex || pageIndex > maxIndex) {
            throw new IllegalArgumentException("Page index must be between " + minIndex + " and " + maxIndex + ".");
        }
    }

    /**
     * Returns the item on a specific page, as a read-only view.
     * <p>
     * If the inventory is empty, an empty list will be returned.
     *
     * @param pageIndex the index of the page (0-based)
     * @return a list of item stacks on the specified page
     */
    public List<ItemStack> getPage(final int pageIndex) {
        this.validatePageIndex(pageIndex);

        final List<ItemStack> allItems;
        final int allItemsCount;
        final int startItemIndex;
        final int firstExcludedItemIndex;

        allItems = this.getAllStacksAsList();
        allItemsCount = allItems.size();
        startItemIndex = pageIndex * this.pageSize;
        firstExcludedItemIndex = Math.min(startItemIndex + this.pageSize, allItemsCount);

        if (startItemIndex >= allItemsCount) {
            return Collections.emptyList();
        }

        return allItems.subList(startItemIndex, firstExcludedItemIndex);
    }

    /**
     * Determines whether a given item falls on the given page index.
     *
     * @param item      the item to check
     * @param pageIndex the page index to check
     * @return whether the item falls on the specified page index
     */
    public boolean isItemOnPage(final Item item, int pageIndex) {
        Inventory.validateItem(item);
        this.validatePageIndex(pageIndex);

        final List<ItemStack> allItems;
        final int allItemsCount;
        final int startIndex;
        final int firstExcludedItemIndex;

        allItems = this.getAllStacksAsList();
        allItemsCount = allItems.size();
        startIndex = pageIndex * this.pageSize;
        firstExcludedItemIndex = Math.min(startIndex + this.pageSize, allItemsCount);

        if (startIndex >= allItemsCount) {
            return false;
        }

        for (int i = startIndex; i < firstExcludedItemIndex; i++) {
            if (allItems.get(i).contains(item)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the amount of pages this paginated inventory is currently holding.
     *
     * @return the current page count, will always be at least 1, even when the inventory is empty
     */
    public int getPageCount() {
        final int totalItems;
        final double pageCount;

        totalItems = this.getAllStacks().size();
        pageCount = Math.ceil((double) totalItems / this.pageSize);

        return Math.max((int) pageCount, 1);
    }

    /**
     * Returns the maximum page that can be retrieved. This will always be one less than the current page count.
     * <p>
     * Retrieving the page of the returned index will never result in an error, unless the inventory has updated
     * since the calculation of the return value.
     *
     * @return the maximum page index
     */
    public int getMaxPageIndex() {
        return this.getPageCount() - 1;
    }

    /**
     * Returns the minimum page that can be retrieved. This will always be one less than
     * {@link PaginatedInventory#MIN_PAGES}.
     * <p>
     * Retrieving the page of the returned index will never result in an error.
     *
     * @return the minimum page index
     */
    public int getMinPageIndex() {
        return PaginatedInventory.MIN_PAGES - 1;
    }

    /**
     * Returns the amount of items on each page.
     *
     * @return the page size
     */
    public int getPageSize() {
        return this.pageSize;
    }

}
