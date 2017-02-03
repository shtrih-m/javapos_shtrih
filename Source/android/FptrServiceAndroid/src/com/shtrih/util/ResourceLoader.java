package com.shtrih.util;

import java.io.InputStream;

public class ResourceLoader {

    private static ResourceLoader instance;

    private static ResourceLoader getInstance() {
        if (instance == null) {
            instance = new ResourceLoader();
        }
        return instance;
    }

    public static InputStream load(String fileName) throws Exception {
        return getInstance().loadResource(fileName);
    }

    private final ClassLoader classLoader;

    private ResourceLoader(){
        this.classLoader = getClassLoaderOfClass(this.getClass());
    }

    private InputStream loadResource(String fileName) {
         return this.classLoader.getResourceAsStream("assets/" + fileName);
    }

    private static ClassLoader getClassLoaderOfClass(final Class<?> clazz) {
        ClassLoader cl = clazz.getClassLoader();
        if (cl == null) {
            return ClassLoader.getSystemClassLoader();
        } else {
            return cl;
        }
    }

//	public static InputStream load(String fileName) throws Exception {
//		Context context = StaticContext.getContext();
//		return context.getResources().getAssets().open(fileName);
//	}
}