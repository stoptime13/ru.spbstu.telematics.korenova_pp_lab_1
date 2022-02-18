package ru.spbstu.telematics.java;

import org.junit.*;
import static  org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;


public class MyHashSetTest
{
    private HashSet<String> controlSet = new HashSet<>();
    private MyHashSet<String> testSet = new MyHashSet<>();

    @Test
    public void testInit(){
        assertTrue(controlSet.isEmpty());
        assertTrue(controlSet.size() == 0);
    }

    @Test
    public void testAddElement(){
        controlSet.add("Ivan");
        controlSet.add("Kirk");
        testSet.add("Ivan");
        testSet.add("Kirk");

        assertEquals(controlSet.contains("Ivan"), testSet.contains("Ivan"));
        assertEquals(controlSet.contains("Kirk"),testSet.contains("Kirk"));
        assertEquals(controlSet.size(), testSet.size());
    }

    @Test
    public void testEqualEl(){
        controlSet.add("Button");
        controlSet.add("Kirk");
        testSet.add("Button");
        testSet.add("Kirk");

        assertEquals(controlSet.contains("Button"), testSet.contains("Button"));
        assertEquals(controlSet.contains("Kirk"),testSet.contains("Kirk"));
    }

    @Test
    public void testContains(){
        controlSet.add("Ivan");

        assertTrue(controlSet.contains("Ivan"));
        assertFalse(controlSet.contains("Petr"));
    }

    @Test
    public void testRemove(){
        controlSet.add("Ivan");
        controlSet.add("Kirk");
        controlSet.add("Kettle");
        testSet.add("Ivan");
        testSet.add("Kirk");
        testSet.add("Kettle");

        controlSet.remove("Kirk");
        testSet.remove("Kirk");

        assertEquals(controlSet.contains("Kirk"), testSet.contains("Kirk"));
    }

    @Test
    public void testClean(){
        controlSet.add("Ivan");
        controlSet.add("Kirk");
        controlSet.add("Kettle");
        testSet.add("Ivan");
        testSet.add("Kirk");
        testSet.add("Kettle");

        assertEquals(controlSet.size(), testSet.size());

        controlSet.clear();
        testSet.clear();

        assertEquals(controlSet.isEmpty(), testSet.isEmpty());
    }

    @Test
    public void testIterator(){
        controlSet.add("Ivan");
        controlSet.add("Kirk");
        controlSet.add("Kettle");
        testSet.add("Ivan");
        testSet.add("Kirk");
        testSet.add("Kettle");

        ArrayList<String> controlValues = new ArrayList<>();
        ArrayList<String> testValues = new ArrayList<>();
        Iterator controlIt = controlSet.iterator();
        Iterator testIt = testSet.iterator();

        while (controlIt.hasNext()) {
            controlValues.add((String) controlIt.next());
        }

        while (testIt.hasNext()) {
            testValues.add((String) testIt.next());
        }

        assertEquals(controlValues, testValues);
    }

    @Test
    public void testThreshold(){
        MyHashSet<String> testSet2 = new MyHashSet<>(2, 0.5f);
        assertEquals(testSet2.getCapacity(), 2);
        testSet2.add("sepulka");
        testSet2.add("bubushka");
        assertEquals(testSet2.getCapacity(), 4);
    }

    @Test
    public void testNullKey(){
        controlSet.add(null);
        testSet.add(null);
        assertEquals(controlSet.contains(null), testSet.contains(null));
    }
}
