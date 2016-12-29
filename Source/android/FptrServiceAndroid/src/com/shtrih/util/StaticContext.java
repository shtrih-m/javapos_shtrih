package com.shtrih.util;

import android.content.Context;

public class StaticContext {
	private static Context context = null;

	public static Context getContext() throws Exception {
		if (context == null) {
			throw new Exception("Context is not set");
		}
		return context;
	}

	// NOTE: Application must call setContext to provide application instance to
	// library
	public static void setContext(Context value) {
		context = value;
	}
}
