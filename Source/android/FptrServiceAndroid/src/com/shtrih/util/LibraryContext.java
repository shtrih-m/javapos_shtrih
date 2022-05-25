package com.shtrih.util;

import android.content.Context;

import java.io.File;
import java.io.InputStream;

public class LibraryContext
{
	private static Context context = null;

	public static Context getContext() throws Exception {
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
		SysUtils.setFilesPath(SysUtils.getFilesPath(context));
	}

	public static InputStream openResource(String fileName) throws Exception {
		return checkContext().getAssets().open(fileName);
	}

}
