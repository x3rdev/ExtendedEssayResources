package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class LCGTest {

    public static final List<ParameterSet> paramList = new ArrayList<>();
    public static final int RANDOM_COUNT = 100_000_000;

    public static int seed;

    public static void main(String[] args) throws IOException {
        seed = new Random().nextInt();
        System.out.println("Seed is " + seed);
        paramList.add(new ParameterSet("C++", 22695477, 1, (long) (Math.pow(2, 32))));
//        paramList.add(new ParameterSet("glibc", 1103515245, 1, (long) (Math.pow(2, 31))));
//        paramList.add(new ParameterSet("TurboPascal", 134775813, 1, (long) (Math.pow(2, 32))));
//        paramList.add(new ParameterSet("MicrosoftVisualC++", 214013, 2531011, (long) (Math.pow(2, 32))));
//        paramList.add(new ParameterSet("MicrosoftVisualBasic", 1140671485, 12820163, (long) (Math.pow(2, 34))));
//        paramList.add(new ParameterSet("NativeAPI", 2147483629, 2147483587, (long) (Math.pow(2, 31) - 1)));
//        paramList.add(new ParameterSet("C++11", 48271, 0, (long) (Math.pow(2, 31) - 1)));
//        paramList.add(new ParameterSet("MMIX", 6364136223846793005L, 1442695040888963407L, (long) (Math.pow(2, 64))));
//        paramList.add(new ParameterSet("Newlib", 6364136223846793005L, 1, (long) (Math.pow(2, 64))));
//        paramList.add(new ParameterSet("Java", 25214903917L, 11, (long) (Math.pow(2, 48))));
//        paramList.add(new ParameterSet("random0", 8121, 28411, (long) (Math.pow(2, 3)*Math.pow(7, 5))));
//        paramList.add(new ParameterSet("cc65", 65793, 826366247, (long) (Math.pow(2, 23))));
//        paramList.add(new ParameterSet("RANDU", 65539, 0, (long) (Math.pow(2, 31))));
        for(ParameterSet param : paramList) {
            printTimeResult(param);
            printPeriodResult(param);
            spectralTest(param);
        }
    }

    private static void printTimeResult(ParameterSet set) {
        System.out.println("Average time is: " + timeTest(set) + " milliseconds to generate " + RANDOM_COUNT + " numbers");
    }

    private static void printPeriodResult(ParameterSet set) throws IOException {
        System.out.println("It takes " + set.modulus() + " iterations for these parameters to repeat given seed " + seed);
    }
    public static long timeTest(ParameterSet set) {
        LinearCongruentialGenerator lcg = new LinearCongruentialGenerator(seed, set.multiplier(), set.increment(), set.modulus());
        long time = System.currentTimeMillis();
        for (int j = 0; j < RANDOM_COUNT; j++) {
            lcg.next();
        }
        return System.currentTimeMillis() - time;
    }

    private static void spectralTest(ParameterSet set) throws IOException {
        LinearCongruentialGenerator lcg = new LinearCongruentialGenerator(seed, set.multiplier(), set.increment(), set.modulus());
        File file = new File("D:\\Documents\\EE test\\src\\results\\" + set.name().toLowerCase() + ".txt");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.append("x, y, z\n");
        long a = lcg.getCurrent();
        long b = lcg.next();
        long c = lcg.next();
        for(int i = 0; i < 100_000; i++) {
            fileWriter.append(a + ", " + b + ", " + c + "\n");
            a = b;
            b = c;
            c = lcg.next();
        }
        fileWriter.close();
    }

}
