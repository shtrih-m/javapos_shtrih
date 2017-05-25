package com.shtrih.util;

import android.content.Context;

import java.io.File;

public class StaticContext 
{
	private static Context context = null;

	public static Context getContext() throws Exception {
		if (context == null) {
			throw new Exception("Context is not set");
		}
		return context;
	}

	public static void setContext(Context value)
	{
		context = value;
		SysUtils.setFilesPath(getFilesPath(value));
	}

	private static String getFilesPath(Context context)
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
}
