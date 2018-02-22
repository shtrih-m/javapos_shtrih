package com.shtrih.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.util.Log;

/**
 * @author V.Kravtsov
 */
public class CompositeLogger {

    private final Logger log4JLogger;
    private final String className;

    private static boolean isEnabled;

    private CompositeLogger(String className) {
        log4JLogger = LoggerFactory.getLogger(className);
        this.className = className;
    }

    public static synchronized CompositeLogger getLogger(java.lang.Class c) {
        return getLogger(c.getName());
    }

    public static synchronized CompositeLogger getLogger(String className) {
        return new CompositeLogger(className);
    }

    public void setEnabled(boolean value) {
        isEnabled = value;
    }

    public synchronized void fatal(String text, Throwable e) {
        if (isEnabled)
            Log.e(className, text, e);

        log4JLogger.error(text, e);
    }

    public synchronized void error(String text, Throwable e) {
        if (isEnabled)
            Log.e(className, text, e);

        log4JLogger.error(text, e);
    }

    public synchronized void error(Throwable e) {
        // TODO: убрать этот метод, нужно всегда передавать сообщение

        if (isEnabled)
            Log.e(className, "", e);

        log4JLogger.error("Error", e);
    }

    public synchronized void error(String text) {
        if (isEnabled)
            Log.e(className, text);

        log4JLogger.error(text);
    }

    public synchronized void debug(String text) {
        if (isEnabled)
            Log.d(className, text);

        log4JLogger.debug(text);
    }
}
