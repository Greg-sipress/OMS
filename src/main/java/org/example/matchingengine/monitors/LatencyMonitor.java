package org.example.matchingengine.monitors;
import org.HdrHistogram.Histogram;

public class LatencyMonitor {
    private final Histogram histogram = new Histogram(3600000000000L, 3);

    public void recordLatency(long startNanos) {
        long latency = System.nanoTime() - startNanos;
        histogram.recordValue(latency);
    }
}