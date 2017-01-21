package com.shtrih.util;

import android.content.Context;

import com.shtrih.fiscalprinter.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ResourceLoaderTests  {

    @Before
    public void SetupContext(){
        Context context = RuntimeEnvironment.application.getApplicationContext();

        StaticContext.setContext(context);
    }

    @Test
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
