package main;

import java.util.Random;

public class ReverseRandom {

    private static final long MULTIPLIER = 0x5DEECE66DL;
    private static final long ADDEND = 0xBL;
    private static final long MASK = (1L << 48) - 1;

    public static void main(String[] args) {
        long constantSeed = new Random().nextLong();
        Random random = new Random(constantSeed);

        // Fix with negative values
        int x1 = random.nextInt();
        int x2 = random.nextInt();
        long v1 = x1 & 0x00000000ffffffffL;
        long v2 = x2 & 0x00000000ffffffffL;

        long seed = 0;

        for (int i = 0; i < 65536; i++) {
            seed = (v1 << 16) + i;
            if ((((seed * MULTIPLIER + ADDEND) & MASK) >>> 16) == v2) {
                break;
            }
        }

        // Random constructor with seed modifies seed
        seed = ((seed ^ MULTIPLIER) & MASK);
        System.out.println("Seed found: " + seed);
        Random r1 = new Random(constantSeed);
        Random r2 = new Random(seed);
        r1.nextInt();
        for (int i = 0; i < 5; i++) {
            System.out.println(r1.nextInt());
            System.out.println(r2.nextInt());
        }
    }
}
