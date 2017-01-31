package com.shtrih.util;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ResourceLoaderAndroidUnitTest {

    @Before
    public void SetupContext() {
    }

    @Test
    public void testResourcesLoading() throws Exception {
        canLoad("commands.xml");
        canLoad("messages_en.txt");
        canLoad("messages_ru.txt");
        canLoad("models.xml");
    }

    private void canLoad(final String resName) throws Exception {
        InputStream commands = ResourceLoader.load(resName);

        boolean isLoaded = commands != null;

        if (commands != null)
            commands.close();

        assertTrue(resName, isLoaded);
    }
}
