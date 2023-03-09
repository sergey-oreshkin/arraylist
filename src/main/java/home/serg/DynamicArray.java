package home.serg;

import java.util.*;
import java.util.stream.Collectors;

public class DynamicArray<E> implements List<E>, RandomAccess {
    public static final int INITIAL_CAPACITY = 10;
    public static final int DEFAULT_GROW_SIZE = 10;

    private Object[] elements;
    private int size;

    public static <T extends Comparable<T>> void sort(List<T> list) {
        if (list.size() < 2) return;
        quickSort(list, 0, list.size() - 1);
    }

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

    private static <T extends Comparable<T>> void swap(List<T> list, int first, int second) {
        T tmp = list.get(first);
        list.set(first, list.get(second));
        list.set(second, tmp);
    }

    public DynamicArray() {
        size = 0;
        elements = new Object[INITIAL_CAPACITY];
    }

    /**
     * @return
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iter();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) Arrays.copyOf(elements, size, a.getClass());
    }

    @Override
    public boolean add(E e) {
        if (size + 1 > elements.length) grow();
        elements[size++] = e;
        return true;
    }

    private void grow() {
        if (size + DEFAULT_GROW_SIZE < 0) throw new OutOfMemoryError("Index range overflowed");
        Object[] newArray = new Object[size + DEFAULT_GROW_SIZE];
        System.arraycopy(elements, 0, newArray, 0, size);
        elements = newArray;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < 0) return false;
        remove(index);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object elem : c) {
            if (indexOf(elem) < 0) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        while (elements.length < size + c.size()) grow();
        System.arraycopy(c.toArray(), 0, elements, size, c.size());
        size += c.size();
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Method addAll(int,Collection<? extends E>) not implemented!");
    }

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

    @Override
    public void clear() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public E get(int index) {
        checkIndexOrThrowException(index);
        return (E) elements[index];
    }

    private void checkIndexOrThrowException(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(String.format("Index %d out of bound", index));
    }

    @Override
    public E set(int index, E element) {
        checkIndexOrThrowException(index);
        Object oldValue = elements[index];
        elements[index] = element;
        return (E) oldValue;
    }

    @Override
    public void add(int index, E element) {
        checkIndexOrThrowException(index);
        if (++size > elements.length) grow();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
    }

    @Override
    public E remove(int index) {
        checkIndexOrThrowException(index);
        Object oldValue = elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index);
        size--;
        return (E) oldValue;
    }

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

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Method listIterator() not implemented!");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Method listIterator(int) not implemented!");
    }

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

    private class Iter implements Iterator<E> {

        private int cursor = -1;

        @Override
        public boolean hasNext() {
            return !isEmpty() && cursor < size - 1;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            cursor++;
            return (E) elements[cursor];
        }
    }
}
