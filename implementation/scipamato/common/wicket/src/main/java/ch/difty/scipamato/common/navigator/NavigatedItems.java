package ch.difty.scipamato.common.navigator;

import java.io.Serializable;
import java.util.List;

/**
 * Implementations manage an immutable ordered collection of unique non-null
 * items (nulls or duplicates are ignored). It maintains a reference to the
 * current item, which can be set but is on the first item by default.
 * <p>
 * Two methods {@code advance} and {@code retreat} allow navigating within the
 * list. Advancing beyond the last item leaves the current item on the last
 * item. Similarly, retreating beyond the first item leaves the current item on
 * the first item.
 *
 * @param <T>
 *     the type of the managed items
 * @author u.joss
 */
interface NavigatedItems<T> extends Serializable {

    /**
     * @return the number of items managed
     */
    int size();

    /**
     * @return the managed items
     */
    List<T> getItems();

    /**
     * @return the current item
     */
    T getItemWithFocus();

    /**
     * Sets the current item
     *
     * @param item
     *     the item to set toe focus to
     */
    void setFocusToItem(T item);

    /**
     * @return true if can retreat, false otherwise (we're on the first record or
     *     don't even have some data)
     */
    boolean hasPrevious();

    /**
     * Retreats the current item by one position (unless the current item is the
     * first item)
     */
    void previous();

    /**
     * @return true if can advance, false otherwise (we're on the last record or
     *     don't even have some data)
     */
    boolean hasNext();

    /**
     * Advances the current item by one position (unless the current item is the
     * last item)
     */
    void next();

    /**
     * @param id
     *     the id that may or may not be part of the collections.
     * @return true if it is contained, false otherwise
     */
    boolean containsId(T id);

    /**
     * Returns a copy of the list without the id passed in as parameter
     *
     * @param id
     *     the id that should be excluded from the new list
     * @return the new list not containing the id passed in
     */
    List<T> without(T id);
}
