package algorithms;

import metrics.PerformanceTracker;

public class SelectionSort {
    private PerformanceTracker tracker;

    public SelectionSort(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    public void sort(int[] arr) {
        tracker.reset();
        long start = System.nanoTime();

        int n = arr.length;
        if (n < 2) {
            tracker.timeNs = System.nanoTime() - start;
            return;
        }

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            boolean foundSmaller = false;

            for (int j = i + 1; j < n; j++) {
                tracker.comparisons++;
                tracker.accesses += 2; 
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                    foundSmaller = true;
                }
            }

            if (foundSmaller && minIndex != i) {
                // swap arr[i] и arr[minIndex]
                int tmp = arr[minIndex];
                arr[minIndex] = arr[i];
                arr[i] = tmp;
                tracker.swaps++;
                tracker.accesses += 4; // записи и чтения в swap
            } else {
                break;
            }
        }

        tracker.timeNs = System.nanoTime() - start;
    }
}
