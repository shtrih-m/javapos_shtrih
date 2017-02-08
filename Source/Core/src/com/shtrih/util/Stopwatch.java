package com.shtrih.util;

public class Stopwatch {

    public static Stopwatch startNew() {
        Stopwatch sw = new Stopwatch();
        sw.start();
        return sw;
    }

    private long start;
    private long stop;

    public void start() {
        start = System.nanoTime();
    }

    public void stop() {
        stop = System.nanoTime();
    }

    public long elapsedMilliseconds() {
        return (long) ((stop - start) / 1000.0 / 1000.0);
    }
}
