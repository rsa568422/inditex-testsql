package com.inditex.linkedlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {

    private LinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = LinkedList.getInstance();
    }

    @Test
    void getInstance() {
        assertAll(
                () -> assertNotNull(list),
                () -> assertEquals(0, list.size()),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> list.get(1))
        );
    }

    @Test
    void get() {
        list.add(3);
        assertEquals(3, list.get(0));
    }

    @Test
    void add() {
        list.add(3);
        list.add(1);
        list.add(5);
        assertAll(
                () -> assertEquals(3, list.size()),
                () -> assertEquals(3, list.get(0)),
                () -> assertEquals(1, list.get(1)),
                () -> assertEquals(5, list.get(2))
        );
    }

    @Test
    void addWithIndex() {
        list.add(5);
        list.add(3, 0);
        list.add(1, 1);
        assertAll(
                () -> assertEquals(3, list.size()),
                () -> assertEquals(3, list.get(0)),
                () -> assertEquals(1, list.get(1)),
                () -> assertEquals(5, list.get(2))
        );
    }

    @Test
    void size() {
        assertEquals(0, list.size());
        list.add(3);
        assertAll(
                () -> assertEquals(1, list.size()),
                () -> assertEquals(3, list.get(0))
        );
        list.add(1);
        assertAll(
                () -> assertEquals(2, list.size()),
                () -> assertEquals(1, list.get(1))
        );
        list.add(5);
        assertAll(
                () -> assertEquals(3, list.size()),
                () -> assertEquals(5, list.get(2))
        );
    }

    @Test
    void isEmpty() {
        assertAll(
                () -> assertEquals(0, list.size()),
                () -> assertTrue(list.isEmpty())
        );
    }

    @Test
    void contains() {
        assertEquals(0, list.size());
        list.add(3);
        assertAll(
                () -> assertEquals(1, list.size()),
                () -> assertTrue(list.contains(3))
        );
        list.add(1);
        assertAll(
                () -> assertEquals(2, list.size()),
                () -> assertTrue(list.contains(3)),
                () -> assertTrue(list.contains(1))
        );
        list.add(5);
        assertAll(
                () -> assertEquals(3, list.size()),
                () -> assertTrue(list.contains(3)),
                () -> assertTrue(list.contains(1)),
                () -> assertTrue(list.contains(5))
        );
    }

    @Test
    void iterator() {
        list.add(3);
        list.add(1);
        list.add(5);
        Iterator<Integer> iterator = list.iterator();
        assertAll(
                () -> assertNotNull(iterator),
                () -> assertTrue(iterator.hasNext()),
                () -> {
                    List<Integer> elements = new ArrayList<>();
                    iterator.forEachRemaining(elements::add);
                    assertTrue(elements.containsAll(Arrays.asList(1, 3, 5)));
                }
        );
    }

    @Test
    void toArray() {
        list.add(3);
        list.add(1);
        list.add(5);
        assertAll(
                () -> assertEquals(Arrays.asList(3, 1, 5).toArray(), list.toArray()),
                () -> assertEquals(Arrays.asList(3, 1, 5).toArray(new Integer[]{}), list.toArray(new Integer[]{}))
        );
    }

    @Test
    void remove() {
        list.add(3);
        list.add(1);
        list.add(5);
        assertAll(
                () -> assertEquals(3, list.size()),
                () -> assertEquals(3, list.get(0)),
                () -> assertEquals(1, list.get(1)),
                () -> assertEquals(5, list.get(2))
        );
        list.remove(1);
        assertAll(
                () -> assertEquals(2, list.size()),
                () -> assertEquals(3, list.get(0)),
                () -> assertEquals(5, list.get(1))
        );
        list.remove(3);
        assertAll(
                () -> assertEquals(1, list.size()),
                () -> assertEquals(5, list.get(0))
        );
        list.remove(5);
        assertAll(
                () -> assertEquals(0, list.size()),
                () -> assertTrue(list.isEmpty())
        );
    }

    @Test
    void containsAll() {
        list.add(3);
        list.add(1);
        list.add(5);
        List<Integer> expected = Arrays.asList(3, 1, 5);
        assertTrue(list.containsAll(expected));
    }

    @Test
    void addAll() {
        List<Integer> expected = Arrays.asList(3, 1, 5);
        boolean added = list.addAll(expected);
        assertAll(
                () -> assertTrue(added),
                () -> assertEquals(3, list.size()),
                () -> assertEquals(3, list.get(0)),
                () -> assertEquals(1, list.get(1)),
                () -> assertEquals(5, list.get(2))
        );
    }

    @Test
    void removeAll() {
        list.add(3);
        list.add(1);
        list.add(5);
        List<Integer> expected = Arrays.asList(3, 1, 5);
        boolean removed = list.removeAll(expected);
        assertAll(
                () -> assertTrue(removed),
                () -> assertEquals(0, list.size()),
                () -> assertTrue(list.isEmpty())
        );
    }

    @Test
    void retainAll() {
        list.add(3);
        list.add(1);
        list.add(5);
        List<Integer> expected = List.of(1);
        boolean retained = list.retainAll(expected);
        assertAll(
                () -> assertTrue(retained),
                () -> assertEquals(1, list.size()),
                () -> assertEquals(1, list.get(0))
        );
    }

    @Test
    void clear() {
        list.add(3);
        list.add(1);
        list.add(5);
        assertEquals(3, list.size());
        list.clear();
        assertAll(
                () -> assertEquals(0, list.size()),
                () -> assertTrue(list.isEmpty())
        );
    }
}