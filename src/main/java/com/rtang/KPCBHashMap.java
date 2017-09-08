package com.rtang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rtang on 9/7/17.
 */
public class KPCBHashMap {
    private Bucket[] map;
    private int size;
    private int numEntries;

    private static final int REMOVE_INDICATOR = -100;
    private static final int EMPTY_INDICATOR = -10;
    private static final Logger LOG = LoggerFactory.getLogger(KPCBHashMap.class);

    public KPCBHashMap(int size) {
        this.size = size;
        this.numEntries = 0;
        this.map = new Bucket[size];
        for (int i = 0; i < size; i++) {
            map[i] = new Bucket();
        }
    }

    public boolean set(String key, Object value) {
        int it = 0;
        if (load() > 0.7) {
            LOG.warn("Load above 0.7, remove items or this operation won't be guaranteed to terminate");
        }
        if (numEntries == size) {
            LOG.warn("Hash Map is full, please remove items");
            return false;
        }
        while (map[Math.abs(hash1(key) + it*hash2(key))%size].getValue() != null) {
            if (map[Math.abs(hash1(key) + it*hash2(key))%size].checkKey(key)) {
                map[Math.abs(hash1(key) + it*hash2(key))%size].updateValue(value);
                //LOG.info("Updating item on key {}", key);
                return true;
            }
            ++it;
        }
        map[Math.abs(hash1(key) + it*hash2(key))%size].updateKey(key);
        map[Math.abs(hash1(key) + it*hash2(key))%size].updateValue(value);
        ++numEntries;
        //LOG.info("Successfully inserted item on key {}", key);
        return true;
    }

    public Object get(String key) {
        int it  = 0;
        while (map[Math.abs(hash1(key) + it*hash2(key))%size].getValue() != null || map[Math.abs(hash1(key) + it*hash2(key))%size].getKey() == REMOVE_INDICATOR) {
            if (map[Math.abs(hash1(key) + it*hash2(key))%size].getKey() == REMOVE_INDICATOR) {
                ++it;
                continue;
            }
            if (map[Math.abs(hash1(key) + it*hash2(key))%size].checkKey(key)) {
                //LOG.info("Successfully inserted item on key {}", key);
                return map[Math.abs(hash1(key) + it*hash2(key))%size].getValue();
            }
            ++it;
        }
        //LOG.info("Key {} doesn't exist, couldn't get item", key);
        return null;
    }

    public Object delete(String key) {
        int it  = 0;
        while (map[Math.abs(hash1(key) + it*hash2(key))%size].getValue() != null || map[Math.abs(hash1(key) + it*hash2(key))%size].getKey() == REMOVE_INDICATOR) {
            if (map[Math.abs(hash1(key) + it*hash2(key))%size].getKey() == REMOVE_INDICATOR) {
                ++it;
                continue;
            }
            if (map[Math.abs(hash1(key) + it*hash2(key))%size].checkKey(key)) {
                Object value = map[(hash1(key) + it*hash2(key))%size].getValue();
                map[Math.abs(hash1(key) + it*hash2(key))%size].updateValue(null);
                map[Math.abs(hash1(key) + it*hash2(key))%size].removeKey();
                --numEntries;
                //LOG.info("Successfully deleted item on key {}", key);
                return value;
            }
            ++it;
        }
        //LOG.info("Key {} doesn't exist, couldn't delete item");
        return null;
    }

    public float load() {
        return numEntries / (float) size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Bucket b : map) {
            if (b.getValue() != null) {
                sb.append(b.toString());
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    private int hash1(String key) {
        int hash = key.hashCode();
        return (hash % 492113) % size;
    }

    private int hash2(String key) {
        int hash = key.hashCode();
        return (hash % 392113) % size;
    }

    class Bucket {
        private String rawKey;
        private int key;
        private Object value;

        public Bucket() {
            this.rawKey = "";
            this.key = EMPTY_INDICATOR;
            this.value = null;
        }

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public boolean checkKey(String key) {
            return key.hashCode() == this.key;
        }

        public void removeKey() {
            this.rawKey = "";
            this.key = REMOVE_INDICATOR;
        }

        public void updateKey(String key) {
            this.rawKey = key;
            this.key = key.hashCode();
        }

        public void updateValue(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("key: %s, value: %s", rawKey, value);
        }
    }
}
