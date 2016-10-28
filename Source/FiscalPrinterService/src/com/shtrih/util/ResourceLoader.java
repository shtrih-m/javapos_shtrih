package com.shtrih.util;

import java.io.InputStream;

public class ResourceLoader {

    private static ResourceLoader instance;

    public static ResourceLoader getInstance() {
        if (instance == null) {
            instance = new ResourceLoader();
        }
        return instance;
    }

    public static InputStream load(String fileName) throws Exception {
        return getInstance().getClass().getResourceAsStream("/res/" + fileName);
    }
}
