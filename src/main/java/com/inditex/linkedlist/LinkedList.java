package com.inditex.linkedlist;

import java.util.Collection;
import java.util.Iterator;

public class LinkedList<T> implements Collection<T> {

    private Node<T> first;

    private Node<T> last;

    private int size = 0;

    private LinkedList() {}

    public static <T> LinkedList<T> getInstance() {
        return null;
    }

    public T get(int index) {
        return null;
    }

    public boolean add(T element, int index) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    private static class Node<T> {
        private T element;

        private Node<T> prev;

        private Node<T> next;
    }
}
