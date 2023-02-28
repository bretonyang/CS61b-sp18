package lab9tester;

import static org.junit.Assert.*;

import org.junit.Test;
import lab9.BSTMap;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests by Brendan Hu, Spring 2015, revised for 2018 by Josh Hug
 */
public class TestBSTMap {

    @Test
    public void sanityGenericsTest() {
        try {
            BSTMap<String, String> a = new BSTMap<String, String>();
            BSTMap<String, Integer> b = new BSTMap<String, Integer>();
            BSTMap<Integer, String> c = new BSTMap<Integer, String>();
            BSTMap<Boolean, Integer> e = new BSTMap<Boolean, Integer>();
        } catch (Exception e) {
            fail();
        }
    }

    //assumes put/size/containsKey/get work
    @Test
    public void sanityClearTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1 + i);
            //make sure put is working via containsKey and get
            assertTrue(null != b.get("hi" + i));
            assertTrue(b.get("hi" + i).equals(1 + i));
            assertTrue(b.containsKey("hi" + i));
        }
        assertEquals(455, b.size());
        b.clear();
        assertEquals(0, b.size());
        for (int i = 0; i < 455; i++) {
            assertTrue(null == b.get("hi" + i) && !b.containsKey("hi" + i));
        }
    }

    // assumes put works
    @Test
    public void sanityContainsKeyTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertFalse(b.containsKey("waterYouDoingHere"));
        b.put("waterYouDoingHere", 0);
        assertTrue(b.containsKey("waterYouDoingHere"));
    }

    // assumes put works
    @Test
    public void sanityGetTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(null, b.get("starChild"));
        assertEquals(0, b.size());
        b.put("starChild", 5);
        assertTrue(((Integer) b.get("starChild")).equals(5));
        b.put("KISS", 5);
        assertTrue(((Integer) b.get("KISS")).equals(5));
        assertNotEquals(null, b.get("starChild"));
        assertEquals(2, b.size());
    }

    // assumes put works
    @Test
    public void sanitySizeTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(0, b.size());
        b.put("hi", 1);
        assertEquals(1, b.size());
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
        }
        assertEquals(456, b.size());
    }

    //assumes get/containskey work
    @Test
    public void sanityPutTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("hi", 1);
        assertTrue(b.containsKey("hi"));
        assertTrue(b.get("hi") != null);
    }

    // assumes put/containsKey work
    @Test
    public void sanityRemoveTest() {
        BSTMap<String, Integer> map = new BSTMap<>();

        assertNull(map.remove("abc"));

        map.put("a", 1);
        map.put("b", 2);
        map.remove("a");
        assertFalse(map.containsKey("a"));
        assertTrue(map.containsKey("b"));
        assertEquals(1, map.size());
        map.remove("b", 2);
        assertFalse(map.containsKey("b"));
        assertEquals(0, map.size());

        // key/value remove
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.remove("b", 23);
        assertTrue(map.containsKey("b"));
        assertEquals(map.size(), 3);
        map.remove("b", 2);
        assertFalse(map.containsKey("b"));
        assertEquals(2, map.size());
    }

    // assumes put work
    @Test
    public void sanityKeySetTest() {
        BSTMap<String, Integer> map = new BSTMap<>();
        for (int i = 0; i < 20; i++) {
            map.put("key#" + i, i);
        }
        Set<String> keys = map.keySet();
        for (int i = 0; i < 20; i++) {
            assertTrue(keys.contains("key#" + i));
        }
        assertEquals(20, map.size());
    }

    @Test
    public void sanityIteratorTest() {
        BSTMap<String, Integer> map = new BSTMap<>();
        Set<String> expected = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            map.put("key#" + i, i);
            expected.add("key#" + i);
        }
        Set<String> actual = new HashSet<>();
        int counter = 0;
        for (String key : map) {
            actual.add(key);
            counter++;
        }
        assertEquals(expected, actual);
        assertEquals(50, counter);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestBSTMap.class);
    }
}
