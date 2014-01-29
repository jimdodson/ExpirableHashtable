package com.jdd;

import com.jdd.ExpirableHashtable;
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
    public void setUp() throws Exception {
        eh = new ExpirableHashtable<Long, Long>();
    }

    @Test
    public void testGet() throws Exception {
        Long key = 1L;
        Long value = 1L;
        Assert.assertNull(eh.get(key));
        eh.put(key, value);
        Assert.assertEquals(value, eh.get(key));
        Thread.sleep(eh.getDefaultTtl());
        Assert.assertNull(eh.get(key));
    }

    @Test
    public void testGetTtl() throws RuntimeException {
        Long key = 1L;
        Long value = 1L;
        eh.put(key, value);
        Assert.assertEquals(eh.getDefaultTtl(), eh.getTtl(key));
    }

    @Test
    public void testPutKV() throws Exception {
        Long key = 1L;
        Long value = 1L;
        Assert.assertNull(eh.get(key));
        eh.put(key, value);
        Long result = eh.get(key);
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result);
    }

    @Test
    public void testPutKVTtl() throws Exception {
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
    public void testShowLastUpdates() throws Exception {
        addData();
        System.out.println(eh.showLastUpdates());
    }

    @Test
    public void testShowTtls() throws Exception {
        addData();
        System.out.println(eh.showTtls());
    }

    @Test
    public void testGetDefaultTtl() throws Exception {
        Assert.assertEquals(500L, eh.getDefaultTtl());
    }

    @Test
    public void testSetDefaultTtl() throws Exception {
        long ttl = 123L;
        Long defaultTtl = eh.getDefaultTtl();
        eh.setDefaultTtl(ttl);
        Assert.assertEquals(ttl, eh.getDefaultTtl());
    }
}
