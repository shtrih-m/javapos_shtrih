package com.shtrih.util;

public class SysUtils {

    private static String filePath = "";

    public static String getFilesPath() {
        return filePath;
    }

    public static void setFilePath(String value) {
        filePath = value;
    }

    public static double round2(double value) {
        return ((double)Math.round(value * 100)) / 100.0;
    }
}
