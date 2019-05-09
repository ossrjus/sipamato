package ch.difty.scipamato.common.navigator;

import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import ch.difty.scipamato.common.NullArgumentException;

/**
 * List based implementation of the {@link NavigatedItems} interface.
 *
 * @param <T>
 *     type of the items that are managed. Must implement
 *     {@link Serializable}.
 * @author u.joss
 */
class NavigatedList<T extends Serializable> implements NavigatedItems<T> {
    private static final long serialVersionUID = 1L;

    private final List<T> items = new ArrayList<>();

    private final AtomicInteger index = new AtomicInteger();

    /**
     * Instantiate the {@link NavigatedList} with the provided collection of items.
     *
     * @param items
     *     collection of items, must not be null or empty.
     */
    NavigatedList(final Collection<? extends T> items) {
        if (items == null)
            throw new NullArgumentException("items");
        if (items.isEmpty())
            throw new IllegalArgumentException("items must not be empty");
        this.items.addAll(items
            .stream()
            .distinct()
            .filter(Objects::nonNull)
            .collect(toList()));
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public List<T> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public T getItemWithFocus() {
        return items.get(index.get());
    }

    @Override
    public void setFocusToItem(final T item) {
        if (item == null)
            throw new NullArgumentException("item");
        final int idx = items.indexOf(item);
        if (idx == -1)
            throw new IllegalArgumentException(
                "Cannot set focus to item that is not part of the managed list (item " + item + ").");
        this.index.set(idx);
    }

    @Override
    public void next() {
        if (hasNext())
            index.incrementAndGet();
    }

    @Override
    public boolean containsId(final T id) {
        return items.contains(id);
    }

    @Override
    public List<T> without(final T id) {
        return items
            .stream()
            .filter(Objects::nonNull)
            .filter(i -> !i.equals(id))
            .collect(toList());
    }

    @Override
    public void previous() {
        if (hasPrevious())
            index.decrementAndGet();
    }

    @Override
    public boolean hasNext() {
        return index.get() < items.size() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index.get() > 0;
    }

}