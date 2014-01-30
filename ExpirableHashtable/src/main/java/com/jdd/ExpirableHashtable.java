package com.jdd;

import java.util.Hashtable;

public class ExpirableHashtable<K, V> extends Hashtable<K, V> {

    private final Hashtable<Object, Long> ttl;
    private final Hashtable<Object, Long> lastUpdate;
    private long defaultTtl;

    public ExpirableHashtable() {
        super();
        lastUpdate = new Hashtable<Object, Long>();
        ttl = new Hashtable<Object, Long>();
        setDefaultTtl(500L);
    }

    @Override
    public synchronized V get(Object key) {
        if (super.get(key) != null) {
            if ((System.currentTimeMillis() - lastUpdate.get(key)) >= ttl.get(key)) {
                super.remove(key);
                lastUpdate.remove(key);
                ttl.remove(key);
            }
            else {
                lastUpdate.put(key, System.currentTimeMillis());
            }
        }
        return super.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized V put(K key, V value) {
        return put(key, value, defaultTtl);
    }

    @SuppressWarnings("unchecked")
    public synchronized V put(K key, V value, long ttl) {
        lastUpdate.put(key, System.currentTimeMillis());
        this.ttl.put(key, ttl);
        return super.put(key, value);
    }

    public long getTtl(K key) throws UnknownKeyException, ExpiredKeyException {
        if (super.get(key) != null) {
            if ((System.currentTimeMillis() - lastUpdate.get(key)) < ttl.get(key)) {
                return ttl.get(key);
            }
            else {
                throw new ExpiredKeyException();
            }
        }
        else {
            throw new UnknownKeyException();
        }
    }

    public String showLastUpdates() {
        return lastUpdate.toString();
    }

    public String showTtls() {
        return ttl.toString();
    }

    public long getDefaultTtl() {
        return defaultTtl;
    }

    public void setDefaultTtl(long ttl) {
        this.defaultTtl = ttl;
    }

}
