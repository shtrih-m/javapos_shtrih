package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FileLogger {

    private final String className;
    private static boolean enabled = false;
    private static BufferedWriter writer = null;

    private FileLogger(String className) {
        this.className = className;
    }

    public static synchronized FileLogger getLogger(java.lang.Class c) {
        return getLogger(c.getName());
    }

    public static synchronized FileLogger getLogger(String className) {
        return new FileLogger(className);
    }

    public synchronized void fatal(String text, Throwable e) {
        error(text + e.getMessage());
    }

    public synchronized void error(String text, Throwable e) {
        error(text + e.getMessage());
    }

    public synchronized void error(Throwable e) {
        error(e.getMessage());
    }

    public synchronized void error(String text) {
        write("ERROR", text);
    }

    public synchronized void debug(String text) {
        write("DEBUG", text);
    }

    public void setEnabled(boolean value) {
        enabled = value;
    }

    public String getFileName() {
        return SysUtils.getFilesPath() + "shtrihjavapos.log";
    }

    private synchronized void write(String prefix, String text) {
        if (!enabled) {
            return;
        }
        try {
            if (writer == null) {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(getFileName(), true), "UTF8"));
            }
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
            String line = sdf.format(date) + " " + prefix + " " + className + " - " + text;
            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
