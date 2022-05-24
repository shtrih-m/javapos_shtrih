package com.shtrih.util;

import java.io.File;
import java.io.InputStream;

public class LibraryContext
{
	private static Object context = null;

	public static Object getContext() throws Exception {
		return context;
	}

	public static Object checkContext() throws Exception {
		if (context == null) {
			throw new Exception("Context is not set");
		}
		return context;
	}

	public static void setContext(Object value)
	{
		context = value;
	}
        
        public static InputStream openResource(String fileName) throws Exception 
        {
            throw new Exception("Not implemented");
        }
}
