package ru.spbstu.telematics.java;

import java.io.InvalidObjectException;
import java.util.*;

public class MyHashSet<E> {

    private transient MyHashMap<E,Object> map;
    private static final Object PRESENT = new Object();

    public MyHashSet() {
        map = new MyHashMap<>();
    }

    public MyHashSet(int initialCapacity, float loadFactor) {
        map = new MyHashMap<>(initialCapacity, loadFactor);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }

    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }

    public void clear() {
        map.clear();
    }

    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    public int getCapacity() {return map.getCapacity();}



}
