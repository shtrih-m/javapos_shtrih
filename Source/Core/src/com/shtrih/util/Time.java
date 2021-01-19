package com.shtrih.util;

import java.lang.Thread;
import com.shtrih.util.CompositeLogger;

public class Time
{
    private static CompositeLogger logger = CompositeLogger.getLogger(Time.class);

    public static void delay(int millis) throws InterruptedException
    {
        if (millis > 0) {
            //logger.debug("Delay " + millis + " ms.");
            Thread.sleep(millis);
        }
    }

}
