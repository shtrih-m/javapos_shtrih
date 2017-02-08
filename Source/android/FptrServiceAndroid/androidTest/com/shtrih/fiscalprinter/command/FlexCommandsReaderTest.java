/*
 * FlexCommandsReaderTest.java
 * JUnit based test
 *
 * Created on 19 Ноябрь 2009 г., 18:29
 */

package com.shtrih.fiscalprinter.command;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.shtrih.util.Stopwatch;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author V.Kravtsov
 */
@RunWith(AndroidJUnit4.class)
public class FlexCommandsReaderTest {

    @Test
    public void testLoadFromXml() throws Exception {

        FlexCommands commands = new FlexCommands();
        FlexCommandsReader instance = new FlexCommandsReader();
        instance.load(commands);
        assertEquals(162, commands.size());
    }

    @Test
    public void testItemByCodePerformance() throws Exception {

        FlexCommands commands = new FlexCommands();
        FlexCommandsReader instance = new FlexCommandsReader();
        instance.load(commands);
        assertEquals(162, commands.size());

        /*
            Vector 72534 ms
            ArrayList 50725 ms
            HashMap 2087 ms
         */

        Stopwatch sw = Stopwatch.startNew();

        for (int i = 0; i < 1000000; i++) {
            commands.itemByCode(i % 256);
        }

        sw.stop();

        Log.d("perf", sw.elapsedMilliseconds() + " ms");
    }
}

