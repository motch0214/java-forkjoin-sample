package com.eighthours.sample;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class ForkJoinTest {

    private static final Logger log = LoggerFactory.getLogger(ForkJoinTest.class);

    @Test
    public void test() {
        ForkJoinPool pool = new ForkJoinPool(2);

        ForkJoinTask<BigInteger> a = pool.submit(() -> new SyncTask("A").get());
        ForkJoinTask<BigInteger> b = pool.submit(() -> new SyncTask("B").get());
        ForkJoinTask<BigInteger> c = pool.submit(() -> new AsyncTask("C").get());

        log.info("A's result: {}", a.join());
        log.info("B's result: {}", b.join());
        log.info("C's result: {}", c.join());
    }
}
