package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Logger {

    private final String className;
    private static boolean enabled = false;
    private static BufferedWriter writer = null;

    private Logger(String className) {
        this.className = className;
    }

    public void deleteFile() throws Exception {
        closeFile();
        File file = new File(getFileName());
        if (file.exists()) {
            file.delete();
        }
    }

    public void closeFile() throws Exception {
        if (writer != null) {
            writer.close();
        }
        writer = null;
    }

    public static synchronized Logger getLogger(java.lang.Class c) {
        return getLogger(c.getName());
    }

    public static synchronized Logger getLogger(String className) {
        return new Logger(className);
    }

    public synchronized void fatal(String text, Throwable e) {
        if (text == null) return;
        if (e == null) return;

        error(text + e.getMessage());
    }

    public synchronized void error(String text, Throwable e) {
        if (text == null) return;
        if (e == null) return;

        error(text + e.getMessage());
    }

    public synchronized void error(Throwable e) {
        if (e == null) return;
        error(e.getMessage());
    }

    public synchronized void error(String text) {
        write("ERROR", text);
    }

    public synchronized void debug(String text) {
        if (text == null) return;
        write("DEBUG", text);
    }

    public void setEnabled(boolean value) {
        enabled = value;
    }

    public String getFileName() {
        return SysUtils.getFilesPath() + "shtrihjavapos.log";
    }

    public synchronized void write(String prefix, String text) {
        if (!enabled) return;
        if (text == null) return;
        if (prefix == null) return;

        try {
            if (writer == null) {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(getFileName()), "UTF8"));
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
