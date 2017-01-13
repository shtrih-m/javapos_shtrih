package com.shtrih.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class SysUtils {
    public static String getFilesPath() {
        Context context = StaticContext.getContext();

        File downloads = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        if (downloads != null) {

            if (downloads.exists())
                return downloads.getAbsolutePath() + File.separator;

            if (downloads.mkdirs())
                return downloads.getAbsolutePath() + File.separator;
        }

        return context.getFilesDir().getAbsolutePath() + File.separator;
    }

    public static void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

}
