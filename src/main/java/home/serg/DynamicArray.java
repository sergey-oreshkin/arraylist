package home.serg;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Dynamic array implementation of the List interface.Implements all optional list operations,
 * except {@link #listIterator(int) listIterator(int)},{@link #listIterator() listIterator}
 * and {@link #addAll(int, Collection) addAll(int,Collection)}.
 * Permits all elements, including null.
 *
 * <p>Each {@code DynamicArray} instance has a <i>capacity</i>.  The capacity is
 * the size of the array used to store the elements in the list.  It is always
 * at least as large as the list size.  As elements are added to an DynamicArray,
 * its capacity grows automatically by default capacity multiplier. (75%)
 * If capacity grow over {@link Integer#MAX_VALUE} then will be throw {@link OutOfMemoryError}
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 *
 * <p> Also implements {@link RandomAccess RandomAccess}.
 *
 * @param <E> - the type of elements in this list
 * @see List
 * @see RandomAccess
 */

public class DynamicArray<E> implements List<E>, RandomAccess {
    public static final int INITIAL_CAPACITY = 10;
    public static final float DEFAULT_CAPACITY_MULTIPLIER = 1.75f;

    private Object[] elements;
    private int size;

    /**
     * Sort the list into ascending order. Allow null as list element.
     * List elements must implement {@link Comparable} to be compared.
     *
     * @param list - list to be sorted
     * @param <T>  - the type of elements in this list
     */
    public static <T extends Comparable<T>> void sort(List<T> list) {
        if (list.size() < 2) return;
        quickSort(list, 0, list.size() - 1);
    }

    /**
     * Sorts the specified range of the list into ascending order.
     * Allows null values. Null is always less then any other value.
     * List elements must implement {@link Comparable} to be compared.
     * The range to be sorted extends from the index {@code from}, inclusive,
     * to the index {@code to}, inclusive. If {@code from == to} the range to be sorted is empty.
     * Sorting algorithm is Quick Sort.
     *
     * @param list - list to be sorted
     * @param from - start position in the list to sort
     * @param to   - end position in the list to sort
     * @param <T>  - the type of elements in this list
     */
    private static <T extends Comparable<T>> void quickSort(List<T> list, int from, int to) {
        if (from >= to) return;
        int pivotIndex = from;
        int leftIndex = from;
        int rightIndex = from + 1;
        while (list.get(pivotIndex) == null && rightIndex <= to) {
            swap(list, ++leftIndex, rightIndex++);
            pivotIndex++;
        }
        if (rightIndex >= to) return;
        while (rightIndex <= to) {
            if (list.get(rightIndex) == null) {
                swap(list, ++leftIndex, rightIndex);
            } else if (list.get(pivotIndex).compareTo(list.get(rightIndex)) >= 0) {
                swap(list, ++leftIndex, rightIndex);
            }
            rightIndex++;
        }
        swap(list, pivotIndex, leftIndex);
        quickSort(list, from, leftIndex - 1);
        quickSort(list, leftIndex + 1, to);
    }

    /**
     * Swap two elements in the list
     *
     * @param list   - list which the elements will be swapped
     * @param first  - index of first element
     * @param second - index of second element
     * @param <T>    - the type of elements in this list
     */
    private static <T extends Comparable<T>> void swap(List<T> list, int first, int second) {
        T tmp = list.get(first);
        list.set(first, list.get(second));
        list.set(second, tmp);
    }

    /**
     * Default constructor
     */
    public DynamicArray() {
        size = 0;
        elements = new Object[INITIAL_CAPACITY];
    }

    /**
     * Constructor with initial capacity. If capacity is negative or zero then will be applied default capacity
     *
     * @param capacity - initial capacity
     */
    public DynamicArray(int capacity) {
        size = 0;
        elements = new Object[capacity > 0 ? capacity : INITIAL_CAPACITY];
    }

    /**
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @return true if this list contains no elements
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @param o element whose presence in this list is to be tested
     * @return true if this list contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * @return an iterator over the elements in this list in proper sequence
     * @see Iterator
     */
    @Override
    public Iterator<E> iterator() {
        return new Iter();
    }

    /**
     * @return an array containing all of the elements in this list in proper sequence
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    /**
     * Returns an array containing all of the elements in this list in proper sequence (from first to last element);
     * the runtime type of the returned array is that of the specified array.
     *
     * @param a   the array into which the elements of this list are to
     *            be stored, if it is big enough; otherwise, a new array of the
     *            same runtime type is allocated for this purpose.
     * @param <T> - type of the specified array elements
     * @return an array containing the elements of this list
     * @throws NullPointerException if the specified array is null
     * @throws ArrayStoreException  if the runtime type of the specified array
     *                              is not a supertype of the runtime type of every element in this list
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) Arrays.copyOf(elements, size, a.getClass());
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element whose presence in this collection is to be ensured
     * @return {@code true} (as specified by {@link Collection#add})
     */
    @Override
    public boolean add(E e) {
        if (size + 1 > elements.length) grow();
        elements[size++] = e;
        return true;
    }

    /**
     * Increase capacity.
     *
     * @throws OutOfMemoryError if new capacity more then {@link Integer#MAX_VALUE}
     */
    private void grow() {
        if (size >= Integer.MAX_VALUE - 1) throw new OutOfMemoryError("Index range overflowed");
        int newCapacity = (size * DEFAULT_CAPACITY_MULTIPLIER) > Integer.MAX_VALUE ? Integer.MAX_VALUE - 1 : (int) (size * DEFAULT_CAPACITY_MULTIPLIER);
        Object[] newArray = new Object[newCapacity];
        System.arraycopy(elements, 0, newArray, 0, size);
        elements = newArray;
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     *
     * @param o element to be removed from this list, if present
     * @return {@code true} if this list contained the specified element
     */
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < 0) return false;
        remove(index);
        return true;
    }

    /**
     * @param c collection to be checked for containment in this list
     * @return true if this list contains all of the elements of the specified collection
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object elem : c) {
            if (indexOf(elem) < 0) return false;
        }
        return true;
    }

    /**
     * Appends all of the elements in the specified collection to the end of this list.
     *
     * @param c collection containing elements to be added to this collection
     * @return true if this list changed as a result of the call
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        while (elements.length < size + c.size()) grow();
        System.arraycopy(c.toArray(), 0, elements, size, c.size());
        size += c.size();
        return true;
    }

    /**
     * Method not implemented!
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Method addAll(int,Collection<? extends E>) not implemented!");
    }

    /**
     * Removes from this list all of its elements that are contained in the specified collection.
     *
     * @param c collection containing elements to be removed from this list
     * @return true if this list changed as a result of the call
     * @throws ClassCastException if the class of an element of this list
     *                            is incompatible with the specified collection
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean isChanged = false;
        for (Object elem : c) {
            if (remove(elem)) {
                isChanged = true;
            }
        }
        return isChanged;
    }

    /**
     * Removes from this list all of its elements that are not contained in the specified collection.
     *
     * @param c collection containing elements to be retained in this list
     * @return true if this list changed as a result of the call
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean isRetain = false;
        Object[] retained = new Object[size];
        int retainedIndex = 0;
        for (Object elem : c) {
            if (contains(elem)) {
                retained[retainedIndex++] = elem;
                isRetain = true;
            }
        }
        if (isRetain) {
            size = retainedIndex;
            elements = retained;
        }
        return isRetain;
    }

    /**
     * Removes all of the elements from this list. The list will be empty after this call returns.
     */
    @Override
    public void clear() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public E get(int index) {
        checkIndexOrThrowException(index);
        return (E) elements[index];
    }

    /**
     * Check if index in range of list
     *
     * @param index index to be checked
     * @throws IndexOutOfBoundsException - if index out of list range
     */
    private void checkIndexOrThrowException(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(String.format("Index %d out of bound", index));
    }

    /**
     * Replaces the element at the specified position in this list with the specified element
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public E set(int index, E element) {
        checkIndexOrThrowException(index);
        Object oldValue = elements[index];
        elements[index] = element;
        return (E) oldValue;
    }

    /**
     * Inserts the specified element at the specified position in this list
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public void add(int index, E element) {
        checkIndexOrThrowException(index);
        if (++size > elements.length) grow();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
    }

    /**
     * Removes the element at the specified position in this list
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public E remove(int index) {
        checkIndexOrThrowException(index);
        Object oldValue = elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index);
        size--;
        return (E) oldValue;
    }

    /**
     * @param o element to search for
     * @return the index of the first occurrence of the specified element in this list,
     * or -1 if this list does not contain the element
     */
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(o, elements[i])) return i;
            }
        }
        return -1;
    }

    /**
     * @param o element to search for
     * @return the index of the last occurrence of the specified element in this list,
     * or -1 if this list does not contain the element
     */
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (Objects.equals(o, elements[i])) return i;
            }
        }
        return -1;
    }

    /**
     * Method not implemented!
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Method listIterator() not implemented!");
    }

    /**
     * Method not implemented!
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Method listIterator(int) not implemented!");
    }

    /**
     * Returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive.
     * (If fromIndex and toIndex are equal, the returned list is empty.)
     *
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex   high endpoint (exclusive) of the subList
     * @return a view of the specified range within this list
     * @throws IndexOutOfBoundsException if any of indices are out of range
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        checkIndexOrThrowException(fromIndex);
        checkIndexOrThrowException(toIndex);
        return Arrays.stream((E[]) Arrays.copyOfRange(elements, fromIndex, toIndex + 1)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size - 1; i++) {
            sb.append(elements[i] == null ? "null" : elements[i].toString());
            sb.append(", ");
        }
        sb.append(elements[size - 1] == null ? "null" : elements[size - 1]);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Implementation of {@link Iterator Iterator} for the collection
     *
     * @see Iterator
     */
    private class Iter implements Iterator<E> {

        private int cursor = -1;

        /**
         * @return true if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return !isEmpty() && cursor < size - 1;
        }

        /**
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            cursor++;
            return (E) elements[cursor];
        }
    }
}
