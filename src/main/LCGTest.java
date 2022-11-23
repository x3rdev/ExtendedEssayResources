package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LCGTest {

    public static final List<ParameterSet> paramList = new ArrayList<>();
    public static final int TIME_COUNT = 100_000_000;
    public static final int INTERVAL_COUNT = 100_000;

    public static int seed;

    public static void main(String[] args) throws IOException {
        seed = new Random().nextInt();
        if(seed < 0) seed *= -1;
        System.out.println("Seed is " + seed);
//        paramList.add(new ParameterSet("C++", 22695477, 1, (long) (Math.pow(2, 32))));
//        paramList.add(new ParameterSet("glibc", 1103515245, 1, (long) (Math.pow(2, 31))));
//        paramList.add(new ParameterSet("TurboPascal", 134775813, 1, (long) (Math.pow(2, 32))));
//        paramList.add(new ParameterSet("MicrosoftVisualC++", 214013, 2531011, (long) (Math.pow(2, 32))));
//        paramList.add(new ParameterSet("MicrosoftVisualBasic", 1140671485, 12820163, (long) (Math.pow(2, 34))));
//        paramList.add(new ParameterSet("NativeAPI", 2147483629, 2147483587, (long) (Math.pow(2, 31) - 1)));
//        paramList.add(new ParameterSet("C++11", 48271, 0, (long) (Math.pow(2, 31) - 1)));
//        paramList.add(new ParameterSet("Java", 25214903917L, 11, (long) (Math.pow(2, 48))));
//        paramList.add(new ParameterSet("random0", 8121, 28411, (long) (Math.pow(2, 3)*Math.pow(7, 5))));
        paramList.add(new ParameterSet("cc65", 65793, 826366247, (long) (Math.pow(2, 23))));
//        paramList.add(new ParameterSet("RANDU", 65539, 0, (long) (Math.pow(2, 31))));
        for(ParameterSet set : paramList) {
//            spectralTest(set);
            toFile(set);
            intervalTest(set, 10);
        }
    }

    private static long periodTest(ParameterSet set) throws IOException {
        return set.modulus();
    }
    public static long timeTest(ParameterSet set) {
        LinearCongruentialGenerator lcg = new LinearCongruentialGenerator(seed, set.multiplier(), set.increment(), set.modulus());
        long time = System.currentTimeMillis();
        for (int j = 0; j < TIME_COUNT; j++) {
            lcg.next();
        }
        return System.currentTimeMillis() - time;
    }

    private static void spectralTest(ParameterSet set) throws IOException {
        LinearCongruentialGenerator lcg = new LinearCongruentialGenerator(seed, set.multiplier(), set.increment(), set.modulus());
        File file = new File("D:\\Documents\\EE test\\src\\lcg_results\\" + set.name().toLowerCase() + ".txt");
        file.createNewFile();
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append("x, y, z\n");
            long a = lcg.getCurrent();
            long b = lcg.next();
            long c = lcg.next();
            for (int i = 0; i < 100_000; i++) {
                fileWriter.append(a + ", " + b + ", " + c + "\n");
                a = b;
                b = c;
                c = lcg.next();
            }
        }
    }

    private static int[] intervalTest(ParameterSet set, int intervals) {
        LinearCongruentialGenerator lcg = new LinearCongruentialGenerator(seed, set.multiplier(), set.increment(), set.modulus());

        //Because some LCGS are special :)
        //Ugly solution
        int modifier = 0;
        double power = Math.log(set.modulus()) / Math.log(2);
        if(power != (int) power || power < 32) {
            modifier = 1;
        }
        int[] distribution = new int[intervals];
        for (int i = 0; i < INTERVAL_COUNT; i++) {
            float next = lcg.nextFloat(modifier);
            distribution[(int)(next*10)]++;
        }
        return distribution;
    }

    private static double chiSquareTest(double[] distribution, int intervals) {
        double chi = 0;
        //Null hypothesis, probably doesn't need to be float
        float n = INTERVAL_COUNT / (float) intervals;
        for(int i = 0; i < intervals; i++) {
            chi += Math.pow(distribution[i] - n, 2) / n;
        }
        return chi;
    }

    //Spectral test results are seperate
    private static void toFile(ParameterSet set) throws IOException {
        System.out.println(set.name());
        File file = new File("D:\\Documents\\EE test\\src\\results\\" + set.name().toLowerCase() + ".txt");
        file.createNewFile();
        double averageTime = 0;
        double[] averageIntervals = new double[10];
        for (int i = 0; i < 20; i++) {
            seed = new Random().nextInt();
            averageTime += timeTest(set);
            for(int j = 0; j < averageIntervals.length; j++) {
                averageIntervals[j] += intervalTest(set, 10)[j];
            }
        }
        averageTime/=20;
        for(int i = 0; i < averageIntervals.length; i++) {
            averageIntervals[i] /= 20;
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append("Time for " + TIME_COUNT + " numbers is ").append(String.valueOf(averageTime)).append(" milliseconds \n");
            fileWriter.append("Repeats after ").append(String.valueOf(periodTest(set))).append(" numbers of output\n");
            fileWriter.append("Interval of float outputs given 10 intervals: ").append(Arrays.toString(averageIntervals)).append("\n");
            fileWriter.append("Chi square value of float outputs given 10 intervals: ").append(String.valueOf(chiSquareTest(averageIntervals, 10))).append("\n");
        }
    }
}
