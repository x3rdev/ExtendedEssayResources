package main;

public class LinearCongruentialGenerator {
    private final long multiplier;
    private final long addend;
    private final long modulus;

    private long current;

    public LinearCongruentialGenerator(long initialValue, long multiplier, long addend, long modulus) {
        this.current = initialValue;
        this.modulus = modulus;
        this.multiplier = multiplier;
        this.addend = addend;
    }

    public long next() {
        current = ((multiplier * current) + addend) % modulus;
        return getCurrent();
    }

    public long getCurrent() {
        return current;
    }
}
