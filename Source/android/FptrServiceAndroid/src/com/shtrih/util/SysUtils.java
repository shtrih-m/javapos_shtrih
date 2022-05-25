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

    public static String getFilesPath(Context context)
    {
        File downloads = context.getExternalFilesDir(null);

        if (downloads != null) {

            if (downloads.exists())
                return downloads.getAbsolutePath() + File.separator;

            if (downloads.mkdirs())
                return downloads.getAbsolutePath() + File.separator;
        }

        return context.getFilesDir().getAbsolutePath() + File.separator;
    }

    public static void sleep(long millis) throws InterruptedException {
        if(millis == 0)
            return;
        
        Thread.sleep(millis);
    }

}
