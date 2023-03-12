package home.serg;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    @Test
    void sortFourElementsListTest() {
        DynamicArray<Integer> ordinaryList = new DynamicArray<>();
        ordinaryList.addAll(List.of(1, 3, 7, 2));

        DynamicArray.sort(ordinaryList);

        assertEquals(List.of(1, 2, 3, 7), ordinaryList);
    }

    @Test
    void sortSingleElementListTest() {
        DynamicArray<Integer> singleElementList = new DynamicArray<>();
        singleElementList.add(1);

        DynamicArray.sort(singleElementList);

        assertEquals(List.of(1), singleElementList);
    }

    @Test
    void sortEmptyListTest() {
        DynamicArray<Integer> emptyList = new DynamicArray<>();

        DynamicArray.sort(emptyList);

        assertTrue(emptyList.isEmpty());
    }

    @Test
    void sortNullValuesListTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.add(1);
        list.add(null);
        list.add(null);
        list.add(3);

        DynamicArray.sort(list);

        assertEquals("[null, null, 1, 3]", list.toString());
    }

    @Test
    void sizeTest() {
        DynamicArray<Integer> list = new DynamicArray<>();

        assertEquals(0, list.size(), "Size is not zero for new instance");

        list.add(1);

        assertEquals(1, list.size(), "Size is not increase when element added");

        list.add(2);
        list.remove(1);

        assertEquals(1, list.size(), "Size is not decrease when element removed");
    }

    @Test
    void isEmptyTest() {
        DynamicArray<Integer> list = new DynamicArray<>();

        assertTrue(list.isEmpty());

        list.add(1);

        assertFalse(list.isEmpty());
    }

    @Test
    void containsTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.add(5);
        list.add(3);

        assertTrue(list.contains(3));
    }

    @Test
    void toArrayTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.add(5);

        assertEquals(1, list.toArray().length);
    }

    @Test
    void addTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.add(5);

        assertEquals(5, list.get(0));
    }

    @Test
    void removeByValueTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.add(5);
        list.add(3);
        list.remove((Integer) 5);

        assertFalse(list.contains(5));
    }

    @Test
    void containsAllTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(1, 2, 3));

        assertTrue(list.containsAll(List.of(1, 3)));
        assertFalse(list.containsAll(List.of(1, 5)));
    }

    @Test
    void addAllTest() {
        DynamicArray<Integer> list = new DynamicArray<>();

        list.addAll(List.of(1, 2, 3));

        assertEquals(List.of(1, 2, 3), list);
    }

    @Test
    void removeAllTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(1, 2, 3));
        boolean isRemoved = list.removeAll(List.of(1, 3));

        assertTrue(isRemoved);
        assertEquals(List.of(2), list);

        isRemoved = list.removeAll(List.of(1, 3));

        assertFalse(isRemoved);
    }

    @Test
    void retainAllTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(1, 2, 3, 4, 5));
        list.retainAll(List.of(2, 4, 7));

        assertEquals(List.of(2, 4), list);
    }

    @Test
    void clearTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(1, 2, 3));

        list.clear();

        assertTrue(list.isEmpty());
    }

    @Test
    void getTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(5, 2));

        assertEquals(2, list.get(1));
    }

    @Test
    void setTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(1, 2));

        list.set(1, 3);

        assertEquals(List.of(1, 3), list);
    }

    @Test
    void addToIndexTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(1, 2));

        list.add(1, 3);

        assertEquals(List.of(1, 3, 2), list);
    }

    @Test
    void removeByIndexTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(1, 2, 3));

        list.remove(1);

        assertEquals(List.of(1, 3), list);
    }

    @Test
    void indexOfTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(5, 2, 3, 2));

        assertEquals(1, list.indexOf(2));
    }

    @Test
    void lastIndexOfTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(3, 2, 3, 2));

        assertEquals(2, list.lastIndexOf(3));
    }

    @Test
    void subListTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(5, 2, 3, 2));

        assertEquals(List.of(3, 2), list.subList(2, 3));
    }

    @Test
    void toTypedArrayTest() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.addAll(List.of(5, 2, 3, 2));

        Integer[] integers = list.toArray(new Integer[0]);

        assertEquals("[5, 2, 3, 2]", Arrays.toString(integers));
    }

    @Test
    void addToPosInLoopTest() {
        DynamicArray<Object> objects = new DynamicArray<>();

        assertThrows(IndexOutOfBoundsException.class, () -> {
            objects.add(5, new Object());
        });

        for (int i = 0; i < 999; i++) {
            objects.add(0, new Object());
        }

        assertEquals(999, objects.size());
    }
}