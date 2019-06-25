package com.eighthours.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
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
        Blocker blocker = new Blocker();
        try {
            ForkJoinPool.managedBlock(blocker);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return blocker.result;
    }

    private class Blocker implements ForkJoinPool.ManagedBlocker {

        BigInteger result = null;

        @Override
        public boolean block() {
            LOCK.lock();
            log.info("{} restarts.", name);
            try {
                IntStream.range(0, 10).forEach(i -> {
                    log.debug("{} is running: {}", name, i);
                    doHeavy();
                });

                log.info("{} finished.", name);
                result = getResult();
                return true;
            } finally {
                LOCK.unlock();
            }
        }

        @Override
        public boolean isReleasable() {
            return result != null;
        }
    }
}
