package com.shtrih.util;

import android.content.Context;

import java.io.File;
import java.io.InputStream;

public class StaticContext
{
	private static Context context = null;

	public static Context getContext()  {
		return context;
	}

	public static Context checkContext() throws Exception {
		if (context == null) {
			throw new Exception("Context is not set");
		}
		return context;
	}

	public static void setContext(Object value)
	{
		context = (Context)value;
		SysUtils.setFilesPath(getFilesPathContext(context));
	}

	public static String getFilesPathContext(Context context)
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


	public static InputStream openResource(String fileName) throws Exception {
		return checkContext().getAssets().open(fileName);
	}

}
