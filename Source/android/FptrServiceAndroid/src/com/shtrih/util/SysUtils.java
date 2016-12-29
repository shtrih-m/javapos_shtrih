package com.shtrih.util;

import java.io.File;

import android.content.Context;

public class SysUtils {
	public static String getFilesPath() throws Exception {
		Context context = StaticContext.getContext();
		return context.getFilesDir().getAbsolutePath() + File.separator;
	}

	public static void sleep(long millis) throws InterruptedException
	{
		Thread.sleep(millis);
	}

}
