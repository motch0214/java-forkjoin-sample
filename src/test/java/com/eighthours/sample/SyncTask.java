package com.eighthours.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class SyncTask extends HeavyTask implements Supplier<BigInteger> {

    private static final Logger log = LoggerFactory.getLogger(SyncTask.class);

    private static final Lock LOCK = new ReentrantLock();

    public final String name;

    public SyncTask(String name) {
        this.name = name;
    }

    @Override
    public BigInteger get() {
        log.info("{} starts.", name);
        LOCK.lock();
        log.info("{} restarts.", name);
        try {
            IntStream.range(0, 10).forEach(i -> {
                log.debug("{} is running: {}", name, i);
                doHeavy();
            });

            log.info("{} finished.", name);
            return getResult();
        } finally {
            LOCK.unlock();
        }
    }
}
