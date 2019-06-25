package com.eighthours.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class AsyncTask extends HeavyTask implements Supplier<BigInteger> {

    private static final Logger log = LoggerFactory.getLogger(AsyncTask.class);

    public final String name;

    public AsyncTask(String name) {
        this.name = name;
    }

    @Override
    public BigInteger get() {
        log.info("{} starts.", name);

        IntStream.range(0, 10).forEach(i -> {
            log.debug("{} is running: {}", name, i);
            doHeavy();
        });

        log.info("{} finished.", name);
        return getResult();
    }
}
