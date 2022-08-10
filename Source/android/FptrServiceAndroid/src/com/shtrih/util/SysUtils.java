package com.shtrih.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class SysUtils {

    private static String filesPath;

    public static void setFilesPath(String value) {
        filesPath = value;
    }

    public static String getFilesPath() {

        if (filesPath != null)
            return filesPath;
                             
        filesPath = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator;
        return filesPath;
    }

    public static void sleep(long millis) throws InterruptedException {
        if(millis == 0)
            return;
        
        Thread.sleep(millis);
    }

}
