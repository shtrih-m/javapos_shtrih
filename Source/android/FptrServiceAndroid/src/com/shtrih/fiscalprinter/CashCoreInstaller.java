package com.shtrih.fiscalprinter;

import android.content.Context;

import com.shtrih.util.ApkInstaller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CashCoreInstaller {
    private Context context;
    
    public CashCoreInstaller(Object appContext) {
        if(appContext instanceof Context)
            context = (Context)appContext;
        else                       
            throw new IllegalArgumentException("appContext is not an instance of Android Context");
    }

    public void install(byte[] firmware) throws IOException {

        final File destination = new File(context.getExternalCacheDir(), "cashcore.apk");

        OutputStream writer = new FileOutputStream(destination);
        try {
            writer.write(firmware);
            writer.flush();
        } finally {
            writer.close();
        }

        new ApkInstaller(context).installApk(destination);
    }
}
