package metrics;

public class PerformanceTracker {
    public long comparisons = 0;
    public long swaps = 0;
    public long accesses = 0;
    public long memoryBytes = 0; 
    public long timeNs = 0;

    public void reset() {
        comparisons = 0;
        swaps = 0;
        accesses = 0;
        memoryBytes = 0;
        timeNs = 0;
    }
}
