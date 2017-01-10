package com.shtrih.util;

import java.io.InputStream;

import android.content.Context;

public class ResourceLoader {

	public static InputStream load(String fileName) throws Exception {
		Context context = StaticContext.getContext();
		return context.getResources().getAssets().open(fileName);
	}
}