package com.jdd;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExpirableHashtableTest {

    // the object under test
    private ExpirableHashtable<Long, Long> eh;

    private void addData() {
        eh.put(1L, 1L, 1L);
        eh.put(2L, 2L, 2L);
        eh.put(3L, 3L, 3L);
    }

    @Before
    public void setUp() {
        eh = new ExpirableHashtable<Long, Long>();
    }

    @Test
    public void testGet() throws InterruptedException {
        Long key = 1L;
        Long value = 1L;
        Assert.assertNull(eh.get(key));
        eh.put(key, value);
        Assert.assertEquals(value, eh.get(key));
        Thread.sleep(eh.getDefaultTtl());
        Assert.assertNull(eh.get(key));
    }

    @Test
    public void testGetTtl() {
        Long key = 1L;
        Long value = 1L;
        eh.put(key, value);
        Assert.assertEquals(eh.getDefaultTtl(), eh.getTtl(key));
    }

    @Test
    public void testGetTllUnknownKey() {
        try {
            eh.getTtl(1L);
            Assert.fail("should have thrown UnknownKeyException");
        }
        catch (UnknownKeyException e){

        }
    }

    @Test
    public void testGetTtlExpiredKey() throws InterruptedException {
        Long key = 1L;
        Long value = 1L;
        long ttl = 1L;
        eh.put(key, value, ttl);
        Thread.sleep(ttl);
        try {
            eh.getTtl(key);
            Assert.fail("should have thrown ExpiredKeyException");
        }
        catch (ExpiredKeyException e) {

        }
    }

    @Test
    public void testPutKV() {
        Long key = 1L;
        Long value = 1L;
        Assert.assertNull(eh.get(key));
        eh.put(key, value);
        Long result = eh.get(key);
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result);
    }

    @Test
    public void testPutKVTtl() {
        Long key = 1L;
        Long value = 1L;
        long ttl = 567L;
        Assert.assertNull(eh.get(key));
        eh.put(key, value, ttl);
        Long result = eh.get(key);
        long storedTtl = eh.getTtl(key);
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result);
        Assert.assertEquals(ttl, storedTtl);
    }

    @Test
    public void testShowLastUpdates() {
        addData();
        Assert.assertNotNull(eh.showLastUpdates());
    }

    @Test
    public void testShowTtls() {
        addData();
        Assert.assertNotNull(eh.showTtls());
    }

    @Test
    public void testGetDefaultTtl() {
        Assert.assertEquals(500L, eh.getDefaultTtl());
    }

    @Test
    public void testSetDefaultTtl() {
        long ttl = 123L;
        eh.setDefaultTtl(ttl);
        Assert.assertEquals(ttl, eh.getDefaultTtl());
    }
}
