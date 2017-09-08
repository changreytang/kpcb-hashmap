package com.rtang;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by rtang on 9/7/17.
 */
public class KPCBHashMapTest {
    private final KPCBHashMap target;

    public KPCBHashMapTest() {
        target = new KPCBHashMap(100);
    }

    @Test
    public void setGetModifyAndDeleteItem() {
        String key = "key";
        String value = "value";
        Object o = null;

        assertTrue("Unable to set key, value in hash map", target.set(key, value));

        o = target.get(key);
        assertFalse("Unable to retrieve value from hash map", o == null);

        assertTrue("Retrieved and original value do not match", value.equals(o));

        o = target.delete(key);
        assertFalse("Unable to delete value from hash map", o == null);

        o = target.get(key);
        assertTrue("Value should have been deleted from hash map", o == null);
    }

    @Test
    public void checkLoadFactorAccurate() {
        String key = "key";
        String key2 = "key2";
        String value = "value";
        Object o = null;

        assertTrue("Unable to set key, value in hash map", target.set(key, value));
        assertTrue("Load factor inaccurate", target.load() == 1F/100);

        assertTrue("Unable to set key, value in hash map", target.set(key2, value));
        assertTrue("Load factor inaccurate", target.load() == 2F/100);

        o = target.delete(key);
        assertFalse("Unable to delete value from hash map", o == null);

        assertTrue("Load factor inaccurate", target.load() == 1F/100);

        o = target.delete(key2);
        assertFalse("Unable to delete value from hash map", o == null);

        assertTrue("Load factor inaccurate", target.load() == 0F);
    }

    @Test
    public void fillHashMapThenDelete() {
        String value = "value";

        for (int i = 100000; i < 100070; i++) {
            assertTrue("Unable to set key, value in hash map", target.set(String.valueOf(i), value));
        }

        assertTrue("Load factor inaccurate", target.load() == 70F/100);

        for (int i = 100000; i < 100070; i++) {
            assertTrue("Unable to set key, value in hash map", target.delete(String.valueOf(i)) == value);
        }

        assertTrue("Load factor inaccurate", target.load() == 0);

        for (int i = 200000; i < 200100; i += 2) {
            assertTrue("Unable to set key, value in hash map", target.set(String.valueOf(i), value));
        }

        assertTrue("Load factor inaccurate", target.load() == 50F/100);

        for (int i = 200000; i < 200100; i += 2) {
            assertTrue("Unable to set key, value in hash map", target.delete(String.valueOf(i)) == value);
        }

        assertTrue("Load factor inaccurate", target.load() == 0);
    }

    @Test
    public void setItemsThenUpdate() {
        String origValue = "origValue";
        String updatedValue = "updatedValue";

        for (int i = 900000; i < 900050; i++) {
            assertTrue("Unable to set key, value in hash map", target.set(String.valueOf(i), origValue));
        }

        assertTrue("Load factor inaccurate", target.load() == 50F/100);

        for (int i = 900000; i < 900050; i += 2) {
            assertTrue("Unable to set key, value in hash map", target.set(String.valueOf(i), updatedValue));
        }

        assertTrue("Load factor inaccurate", target.load() == 50F/100);

        for (int i = 900000; i < 900050; i++) {
            if (i%2 == 0) {
                assertTrue("Unable to set key, value in hash map", target.get(String.valueOf(i)) == updatedValue);
            } else {
                assertTrue("Unable to set key, value in hash map", target.get(String.valueOf(i)) == origValue);
            }
        }

        for (int i = 900000; i < 900050; i++) {
            target.delete(String.valueOf(i));
        }

        assertTrue("Load factor inaccurate", target.load() == 0);
    }
}
