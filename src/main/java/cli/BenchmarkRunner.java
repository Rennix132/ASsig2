package cli;

import algorithms.SelectionSort;
import metrics.PerformanceTracker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class BenchmarkRunner {

    private static final double LIMIT_ESTIMATED_OPS = 5e8; // ~500 млн сравнений допустимо

    public static void main(String[] args) {
        int[] sizes = new int[]{100, 1000, 10000, 100000};
        String csvOut = "selectionsort_results.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvOut))) {
            writer.write("algorithm,input_size,distribution,time_ms,comparisons,swaps,accesses,memory_bytes,status");
            writer.newLine();

            for (int n : sizes) {
                double estimatedOps = (double)n * (double)n / 2.0;
                if (estimatedOps > LIMIT_ESTIMATED_OPS) {
                    writer.write(String.format("SelectionSort,%d,ALL,SKIPPED,0,0,0,0,skipped_estimate"));
                    writer.newLine();
                    System.out.println("Skipped size " + n + " due to estimated too many operations: " + estimatedOps);
                    continue;
                }

                runAndRecord(n, "random", writer);
                runAndRecord(n, "sorted", writer);
                runAndRecord(n, "reversed", writer);
                runAndRecord(n, "nearly_sorted", writer);
            }

            System.out.println("Results saved to " + csvOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runAndRecord(int n, String distribution, BufferedWriter writer) throws IOException {
        int[] arr = generateArray(n, distribution);
        PerformanceTracker tracker = new PerformanceTracker();

        // memory before
        long usedBefore = usedMemory();

        SelectionSort algo = new SelectionSort(tracker);
        algo.sort(arr);

        long usedAfter = usedMemory();
        long memUsed = Math.max(0, usedAfter - usedBefore);
        tracker.memoryBytes = memUsed;

        long timeMs = tracker.timeNs / 1_000_000;

        String status = "ok";
        writer.write(String.format("SelectionSort,%d,%s,%d,%d,%d,%d,%d,%s",
                n,
                distribution,
                timeMs,
                tracker.comparisons,
                tracker.swaps,
                tracker.accesses,
                tracker.memoryBytes,
                status));
        writer.newLine();
        writer.flush();

        System.out.println(String.format("n=%d dist=%s time=%dms comps=%d swaps=%d accesses=%d mem=%d",
                n, distribution, timeMs, tracker.comparisons, tracker.swaps, tracker.accesses, tracker.memoryBytes));
    }

    private static int[] generateArray(int n, String distribution) {
        Random rnd = new Random(42); 
        int[] a = new int[n];
        if ("random".equals(distribution)) {
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(n * 10);
        } else if ("sorted".equals(distribution)) {
            for (int i = 0; i < n; i++) a[i] = i;
        } else if ("reversed".equals(distribution)) {
            for (int i = 0; i < n; i++) a[i] = n - i;
        } else if ("nearly_sorted".equals(distribution)) {
            for (int i = 0; i < n; i++) a[i] = i;
            int swaps = Math.max(1, n / 100);
            for (int s = 0; s < swaps; s++) {
                int i = rnd.nextInt(n);
                int j = rnd.nextInt(n);
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
        } else {
            // default random
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(n * 10);
        }
        return a;
    }

    private static long usedMemory() {
        Runtime r = Runtime.getRuntime();
        r.gc();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ignored) {}
        return r.totalMemory() - r.freeMemory();
    }
}
