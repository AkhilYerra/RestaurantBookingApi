package com.example.resy.shared;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleCache<K, V> {

    private final ConcurrentMap<K, V> cacheMap = new ConcurrentHashMap<>();

    public V get(K key) {
        return cacheMap.get(key);
    }

    public void put(K key, V value) {
        cacheMap.put(key, value);
    }

    public void evict(K key) {
        cacheMap.remove(key);
    }

    public void evictAll() {
        cacheMap.clear();
    }
}
