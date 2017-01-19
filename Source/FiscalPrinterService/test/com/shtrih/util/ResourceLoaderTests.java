package com.shtrih.util;

import junit.framework.TestCase;

import java.io.InputStream;

/**
 * Created by P.Zhirkov on 19.01.2017.
 */
public class ResourceLoaderTests extends TestCase {
    public void testResourcesLoading() throws Exception {
         canLoad("commands.xml");
         canLoad("messages_en.txt");
         canLoad("messages_ru.txt");
         canLoad("models.xml");
    }

    private void canLoad(final String resName) throws Exception
    {
        InputStream commands = ResourceLoader.load(resName);

        boolean isLoaded = commands != null;

        if(commands != null)
            commands.close();

        assertTrue(resName, isLoaded);
    }
}
