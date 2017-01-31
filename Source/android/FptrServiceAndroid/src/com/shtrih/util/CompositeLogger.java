package com.shtrih.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author V.Kravtsov
 */

public class CompositeLogger {
    
    private final Logger log4JLogger;

    private CompositeLogger(String className) {
        log4JLogger = LoggerFactory.getLogger(className);
    }
    
    public static synchronized CompositeLogger getLogger(java.lang.Class c) {
        return getLogger(c.getName());
    }
    
    public static synchronized CompositeLogger getLogger(String className) {
        return new CompositeLogger(className);
    }
    
    public void setEnabled(boolean value) {

    }
    
    public synchronized void fatal(String text, Throwable e) {
        log4JLogger.error(text, e);
    }
    
    public synchronized void error(String text, Throwable e) {
        log4JLogger.error(text, e);
    }
    
    public synchronized void error(Throwable e) {
        log4JLogger.error("Error", e);
    }
    
    public synchronized void error(String text) {
        log4JLogger.error(text);
    }
    
    public synchronized void debug(String text) {
        log4JLogger.debug(text);
    }
}
