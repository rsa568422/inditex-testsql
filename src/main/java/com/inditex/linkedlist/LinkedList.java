package com.inditex.linkedlist;

import java.util.*;

public class LinkedList<T> implements Collection<T> {

    private Node<T> first;

    private Node<T> last;

    private int size = 0;

    private LinkedList() {}

    public static <T> LinkedList<T> getInstance() {
        return new LinkedList<>();
    }

    public T get(int index) {
        return getNode(index).element;
    }

    public boolean add(T element, int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        Node<T> node = new Node<>(element);
        if (size == 0) {
            first = node;
            last = node;
        } else if (index == 0) {
            node.next = first;
            first.prev = node;
            first = node;
        } else if (index == size) {
            node.prev = last;
            last.next = node;
            last = node;
        } else {
            Node<T> actual = getNode(index);
            node.next = actual;
            node.prev = actual.prev;
            node.prev.next = node;
            actual.prev = node;
        }
        size++;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object object) {
        T t = (T) object;
        Node<T> node = first;
        while (Objects.nonNull(node)) {
            if (node.element.equals(t)) return true;
            node = node.next;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator<>(first);
    }

    @Override
    public Object[] toArray() {
        if (size == 0) return new Object[0];
        Node<T> actual = first;
        Object[] objects = new Object[size];
        for (int i = 0; i < size; i++) {
            objects[i] = actual.element;
            actual = actual.next;
        }
        return objects;
    }

    @Override
    public <E> E[] toArray(E[] baseArray) {
        if (size == 0) return Collections.emptyList().toArray(baseArray);
        Node<T> actual = first;
        List<E> elements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            elements.add(i, (E) actual.element);
            actual = actual.next;
        }
        return elements.toArray(baseArray);
    }

    @Override
    public boolean add(T t) {
        return add(t, size);
    }

    @Override
    public boolean remove(Object object) {
        if (size == 0) {
            return false;
        } else if (size == 1 && first.element.equals(object)) {
            clear();
            return true;
        } else if (size > 1 && first.element.equals(object)){
            first.next.prev = null;
            first = first.next;
            size--;
            return true;
        } else if (size > 1 && last.element.equals(object)) {
            last.prev.next = null;
            last = last.prev;
            size--;
            return true;
        } else if (size > 1) {
            Node<T> actual = first.next;
            while (Objects.nonNull(actual)) {
                if (actual.element.equals(object)) {
                    actual.prev.next = actual.next;
                    actual.next.prev = actual.prev;
                    size--;
                    return true;
                }
                actual = actual.next;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return collection.stream().map(this::contains).reduce(true, Boolean::logicalAnd);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return collection.stream().map(this::add).reduce(true, Boolean::logicalAnd);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return collection.stream().map(this::remove).reduce(true, Boolean::logicalAnd);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean changed = false;
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (!collection.contains(element)) {
                remove(element);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        Node<T> actual = first;
        first = null;
        last = null;
        while (Objects.nonNull(actual)) {
            Node<T> next = actual.next;
            actual.prev = null;
            actual.next = null;
            actual = next;
        }
        size = 0;
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        Node<T> actual = first;
        for (int i = 0; i < size; i++) {
            if (i == index) break;
            actual = actual.next;
        }
        return actual;
    }

    private static class Node<T> {
        private final T element;

        private Node<T> prev = null;

        private Node<T> next = null;

        private Node(T element) {
            this.element = element;
        }
    }

    private static class LinkedListIterator<T> implements Iterator<T> {

        private Node<T> actual;

        private LinkedListIterator(Node<T> first) {
            this.actual = first;
        }

        @Override
        public boolean hasNext() {
            return Objects.nonNull(actual);
        }

        @Override
        public T next() {
            if (Objects.isNull(actual))
                throw new NoSuchElementException();
            T element = actual.element;
            actual = actual.next;
            return element;
        }
    }
}
