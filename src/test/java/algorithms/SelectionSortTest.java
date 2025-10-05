package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SelectionSortTest {

    private void assertSorted(int[] a) {
        for (int i = 1; i < a.length; i++) {
            assertTrue(a[i-1] <= a[i]);
        }
    }

    @Test
    public void testEmpty() {
        PerformanceTracker t = new PerformanceTracker();
        SelectionSort s = new SelectionSort(t);
        int[] a = new int[0];
        s.sort(a);
        assertEquals(0, a.length);
    }

    @Test
    public void testSingle() {
        PerformanceTracker t = new PerformanceTracker();
        SelectionSort s = new SelectionSort(t);
        int[] a = new int[]{5};
        s.sort(a);
        assertEquals(1, a.length);
        assertEquals(5, a[0]);
    }

    @Test
    public void testSorted() {
        PerformanceTracker t = new PerformanceTracker();
        SelectionSort s = new SelectionSort(t);
        int[] a = new int[]{1,2,3,4,5};
        s.sort(a);
        assertSorted(a);
    }

    @Test
    public void testReversed() {
        PerformanceTracker t = new PerformanceTracker();
        SelectionSort s = new SelectionSort(t);
        int[] a = new int[]{5,4,3,2,1};
        s.sort(a);
        assertSorted(a);
    }

    @Test
    public void testDuplicates() {
        PerformanceTracker t = new PerformanceTracker();
        SelectionSort s = new SelectionSort(t);
        int[] a = new int[]{2,2,1,3,2,1};
        s.sort(a);
        assertSorted(a);
    }
}
