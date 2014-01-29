package com.jdd;

import java.util.Random;
import java.util.logging.Logger;

public class Experiment {

    private Logger logger = Logger.getAnonymousLogger();
    private ExpirableHashtable<Long, Long> eh;
    private int size = 1000;  // size of table
    private int ttl = 1; // expire milliseconds
    private int duration = 10000; // how long to abuse the table

    public static void main(String[] args) {
        Experiment e = new Experiment();
        e.setup();
        e.slam();
    }

    private void setup() {
        logger.info("Setup...");
        eh = new ExpirableHashtable<Long, Long>();
        eh.setDefaultTtl((long)ttl);
        for (int i=0; i<size; i++) {
            eh.put((long)i, new Random().nextLong());
        }
    }

    private void slam() {
        logger.info("Slam...");
        int reads = 0;
        int expired = 0;
        Long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < duration) {
            reads++;
            Long key = (long)new Random().nextInt(size - 1);
            Long value = eh.get(key);
            if (value == null) {
                expired++;
                eh.put(key, new Random().nextLong());
            }
        }
        logger.info(reads + " reads " + expired + " expired");
    }
}
