package ru.spbstu.telematics.java;

import java.util.*;

public class MyHashMap<K,V> {

    static final int MAX_CAPACITY = 1 << 20;
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private MyNode<K,V>[] table; // массив, в котором хранятся пары ключ и значение
    private int size; // общее количество пар
    private int threshold; // порог расширения
    private final float loadFactor; // коэффицент загрузки для измерения степени заполнения

    static class MyNode<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        MyNode<K, V> next;

        public MyNode(int hash, K key, V value, MyNode<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
        table = new MyNode[DEFAULT_CAPACITY];
    }

    public MyHashMap(int initCapacity, float initLoadFactor) {
        if (initCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initCapacity);
        if (initCapacity > MAX_CAPACITY) initCapacity = MAX_CAPACITY;
        if (initLoadFactor <= 0)
            throw new IllegalArgumentException("Illegal initial load factor: " + initLoadFactor);

        int cap = 1; // считаем ближайшую наибольшую степень двойки (вместо функции tableSizeFor)
        while (cap < initCapacity)
            cap <<= 1;

        this.loadFactor = initLoadFactor;
        this.threshold = (int) (cap * initLoadFactor);
        table = new MyNode[cap];
    }

    public MyHashMap(int initCapacity) {
        this(initCapacity, DEFAULT_LOAD_FACTOR);
    }

    static final int hash(Object key) { // расчет хэш-значения
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public V put(K key, V value) { // получение значения hashCode в соответствии со значением ключа
        if (key == null) {
            for (MyNode<K, V> e = table[0]; e != null; e = e.next) {
                if (e.key == null) {
                    V oldValue = e.value;
                    e.value = value;
                    return oldValue;
                }
            }
            MyNode<K, V> e = table[0]; // save to make it next entry
            table[0] = new MyNode<K, V>(0, key, value, e);
            if (size++ >= threshold)
                resize(2 * table.length);
            return null;
        }
        int hash = hash(key.hashCode());
        int index = hash & (table.length - 1);
        for (MyNode<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && Objects.equals(key,e.key)) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        MyNode<K, V> e = table[index]; // save to make it next entry
        table[index] = new MyNode<K, V>(hash, key, value, e);
        if (size++ >= threshold)
            resize(2 * table.length);
        return null;
    }


    private void resize(int newCapacity) {// расширение
        MyNode<K, V>[] oldTable = table;
        int oldCapacity = table.length;
        if (newCapacity >= MAX_CAPACITY) {
            threshold = MAX_CAPACITY;
            return;
        }
        MyNode<K, V>[] newTable = new MyNode[newCapacity];
        int newCapacity1 = newTable.length;
        for (int i = 0; i < table.length; ++i) {
            MyNode<K, V> e = table[i];
            if (e != null) {
                do {
                    MyNode<K, V> next = e.next;
                    int newIndex = e.hash & (newCapacity1 - 1);
                    e.next = newTable[newIndex];
                    newTable[newIndex] = e;
                    e = next;
                } while (e != null);
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
        return;
    }

    final int capacity() {
        return (table != null) ? table.length :
                (threshold > 0) ? threshold :
                        DEFAULT_CAPACITY;
    }

    public int getCapacity() {return this.table.length;}

    final float loadFactor() { return loadFactor; }

    private MyNode<K,V> getEntry(Object key){
        if (size == 0) return null;
        int hash = 0;
        if (key != null) hash = hash(key.hashCode());
        int index = hash & (table.length - 1);
        for (MyNode<K,V> e = table[index]; e!=null; e=e.next){
            if (e.hash == hash && (Objects.equals(key, e.key))){
                return e;
            }
        }
        return null;
    }

    public V get(Object key) {
        MyNode<K,V> e = getEntry(key);
        V value = (e == null) ? null : e.value;
        return value;
    }

    public boolean containsKey(Object key){
        return getEntry(key) != null;
    }


    public int size() { return size; }

    public V remove(Object key) {
        if (size == 0) return null;
        int hash = 0;
        if (key != null) hash = hash(key.hashCode());

        int index = hash & (table.length - 1);
        MyNode<K, V> prev = table[index];
        MyNode<K, V> cur = prev;

        while (cur != null) {
            MyNode<K, V> next = cur.next;
            if (cur.hash == hash && (Objects.equals(key, cur.key))) {
                if (prev == cur)
                    table[index] = next;
                else
                    prev.next = next;
                size--;
                return cur.value;
            }
            prev = cur;
            cur = next;
        }
        return null;
    }

    public void clear(){
        MyNode<K,V>[] tab = table;
        if (tab != null && size > 0){
            for (int i=0; i<tab.length; ++i)
                tab[i]=null;
            size =0;
        }
    }

    //public int getCapacity(){ return this.table.length; }

    public boolean isEmpty() { return size == 0; }

    private abstract class HashIterator<E> implements Iterator<E> {
        MyNode<K, V> next;
        int index;
        MyNode<K, V> current;

        HashIterator() {
            if (size > 0) {
                final MyNode[] t = table;
                do {} while(index < t.length && (next = t[index++]) == null);
            }
        }
        @Override public final boolean hasNext() {
            return next != null;
        }
        final MyNode<K, V> nextEntry() {
            final MyNode<K, V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            if ((next = e.next) == null) {
                final MyNode[] t = table;
                do {} while (index < t.length && (next = t[index++]) == null);
            }
            current = e;
            return e;
        }
        @Override public void remove() {
            if (current == null)
                throw new IllegalStateException();
            final Object k = current.key;
            current = null;
            MyHashMap.this.remove(k);
        }
    }

    final class KeyIterator extends HashIterator<K> {
        @Override public K next() {
            return nextEntry().getKey();
        }
    }

    final class ValueIterator extends HashIterator<V> {
        @Override public V next() {
            return nextEntry().value;
        }
    }

    final class EntryIterator extends HashIterator<MyHashMap.MyNode<K, V>> {
        @Override public MyHashMap.MyNode<K, V> next() {
            return nextEntry();
        }
    }


    Iterator<K> newKeyIterator() {
        return new KeyIterator();
    }
    Iterator<V> newValueIterator() {
        return new ValueIterator();
    }
    Iterator<MyHashMap.MyNode<K, V>> newEntryIterator() {
        return new EntryIterator();
    }


    private Set<MyHashMap.MyNode<K, V>> entrySet = null;
    private Set<K> keySet;
    private Collection<V> values;

    public Set<K> keySet() {
        final Set<K> ks = keySet;
        return ks != null ? ks : (keySet = new KeySet());
    }

    final class KeySet extends AbstractSet<K> {
        @Override public Iterator<K> iterator() {
            return newKeyIterator();
        }
        @Override public int size() {
            return size;
        }
        @Override public boolean contains(final Object o) {
            return containsKey(o);
        }
        @Override public boolean remove(final Object o) {
            return MyHashMap.this.remove(o) != null;
        }
        @Override public void clear() {
            MyHashMap.this.clear();
        }
    }



    public Collection<V> values() {
        final Collection<V> vs = values;
        return vs != null ? vs : (values = new Values());
    }

    private final class Values extends AbstractCollection<V> {
        @Override public Iterator<V> iterator() {
            return newValueIterator();
        }
        @Override public int size() {
            return size;
        }
        @Override public void clear() {
            MyHashMap.this.clear();
        }
    }


    public Set<MyHashMap.MyNode<K, V>> entrySet() {
        return entrySet0();
    }
    private Set<MyHashMap.MyNode<K, V>> entrySet0() {
        final Set<MyHashMap.MyNode<K, V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    final class EntrySet extends AbstractSet<MyHashMap.MyNode<K, V>> {
        @Override public Iterator<MyHashMap.MyNode<K, V>> iterator() {
            return newEntryIterator();
        }
        @Override public boolean contains(final Object o) {
            if (!(o instanceof MyHashMap.MyNode))
                return false;
            final MyHashMap.MyNode<K, V> e = (MyHashMap.MyNode<K, V>) o;
            final MyNode<K, V> candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }
        @Override public int size() {
            return size;
        }
        @Override public void clear() {
            MyHashMap.this.clear();
        }
    }
}
