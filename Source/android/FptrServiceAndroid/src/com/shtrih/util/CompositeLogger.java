package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CompositeLogger {
    
    private final Logger log4JLogger;

    private CompositeLogger(String className) {
        log4JLogger = LogManager.getLogger(className);
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
        log4JLogger.fatal(text, e);
    }
    
    public synchronized void error(String text, Throwable e) {
        log4JLogger.error(text, e);
    }
    
    public synchronized void error(Throwable e) {
        log4JLogger.error(e);
    }
    
    public synchronized void error(String text) {
        log4JLogger.error(text);
    }
    
    public synchronized void debug(String text) {
        log4JLogger.debug(text);
    }
}
