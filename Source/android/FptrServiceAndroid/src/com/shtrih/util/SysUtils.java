package com.shtrih.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class SysUtils {

    private static String filesPath = null;

    public static void setFilesPath(String value) {
        filesPath = value;
    }

    public static String getFilesPath() {

        if (filesPath != null) {
            return filesPath;
        }

        filesPath = "";
        File file = Environment.getExternalStorageDirectory();
        if (file != null) {
            filesPath = file.getAbsoluteFile() + File.separator;
        }
        return filesPath;
    }

    public static String getFilesPathContext(Context context)
    {
        if (context == null){
            return "";
        }

        File downloads = context.getExternalFilesDir(null);
        if (downloads != null) {

            if (downloads.exists())
                return downloads.getAbsolutePath() + File.separator;

            if (downloads.mkdirs())
                return downloads.getAbsolutePath() + File.separator;
        }
        File file = context.getFilesDir();
        if (file != null) {
            return file.getAbsolutePath() + File.separator;
        }
        return "";
    }

    public static void sleep(long millis) throws InterruptedException {
        if(millis == 0)
            return;
        
        Thread.sleep(millis);
    }

}
