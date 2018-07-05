package com.shtrih.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

public class ApkInstaller {
    private final Context context;

    public ApkInstaller(Context context) {
        this.context = context;
    }

    public void installApk(File apkFile) {
        Uri uri = Uri.fromFile(apkFile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.shtrih.jpos.files.provider", apkFile);
        }

        final Intent install = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        install.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(install);
    }
}
