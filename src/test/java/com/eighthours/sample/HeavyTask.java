package com.eighthours.sample;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.LongStream;

public abstract class HeavyTask {

    private final AtomicReference<BigInteger> total = new AtomicReference<>(BigInteger.ZERO);

    public void doHeavy() {
        AtomicReference<BigInteger> number = new AtomicReference<>(BigInteger.ZERO);
        LongStream.range(0, 10_000_000).forEach(n ->
                number.getAndUpdate(x -> x.add(BigInteger.ONE)));
        total.getAndUpdate(x -> x.add(number.get()));
    }

    public BigInteger getResult() {
        return total.get();
    }
}
