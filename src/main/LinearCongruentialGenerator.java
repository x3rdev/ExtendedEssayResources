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

    public boolean nextBoolean() {
        System.out.println(getBits());
        return next() >> (getBits() - 1) != 0;
    }

    public float nextFloat(int modifier) {
        int bits = (getBits() - 1) /2;
        float next = (next() >> bits) / ((float)(1 << (bits + modifier)));
        if(next < 0) next *= -1;
        //random0 doesn't like to behave
        return Math.min(next, 0.999999F);
    }

    public long getCurrent() {
        return current;
    }

    private int getBits() {
        return (int)(Math.log(this.modulus) / Math.log(2) + 1);
    }
}
