package com.shtrih.fiscalprinter.command;

import com.shtrih.util.Stopwatch;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author V.Kravtsov
 */
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
            Vector 8200 ms
            ArrayList 1898 ms
            HashMap 44 ms
         */

        Stopwatch sw = Stopwatch.startNew();

        for (int i = 0; i < 1000000; i++) {
            commands.itemByCode(i % 256);
        }

        sw.stop();

        System.out.println(sw.elapsedMilliseconds() + " ms");
    }
}

