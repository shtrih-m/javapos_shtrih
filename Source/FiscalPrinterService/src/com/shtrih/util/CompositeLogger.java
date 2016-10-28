package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.logging.SimpleFormatter;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.Format;
import org.apache.log4j.Logger;

public class CompositeLogger {
    
    private final Logger log4JLogger;
    private final FileLogger fileLogger;
    
    private CompositeLogger(String className) {
        fileLogger = FileLogger.getLogger(className);
        log4JLogger = Logger.getLogger(className);
    }
    
    public static synchronized CompositeLogger getLogger(java.lang.Class c) {
        return getLogger(c.getName());
    }
    
    public static synchronized CompositeLogger getLogger(String className) {
        return new CompositeLogger(className);
    }
    
    public void setEnabled(boolean value) {
        fileLogger.setEnabled(value);
    }
    
    public synchronized void fatal(String text, Throwable e) {
        log4JLogger.fatal(text, e);
        fileLogger.fatal(text, e);
    }
    
    public synchronized void error(String text, Throwable e) {
        log4JLogger.error(text, e);
        fileLogger.error(text, e);
    }
    
    public synchronized void error(Throwable e) {
        log4JLogger.error(e);
        fileLogger.error(e);
    }
    
    public synchronized void error(String text) {
        log4JLogger.error(text);
        fileLogger.error(text);
    }
    
    public synchronized void debug(String text) {
        log4JLogger.debug(text);
        fileLogger.debug(text);
    }
    
}
