package com.shtrih.util;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 * @author P.Zhirkov
 */
import junit.framework.TestCase;

/**
 * @author V.Kravtsov
 */
public class CalendarTest extends TestCase {

    public CalendarTest(String testName) {
        super(testName);
    }

    /**
     * Test calendar methods
     */
    public void testCalendarBefore() {
        System.out.println("testCalendarBefore");

        try {
            Calendar calendar1 = new GregorianCalendar();
            Thread.sleep(2);
            Calendar calendar2 = new GregorianCalendar();
            assertEquals(true, calendar1.before(calendar2));

        } catch (InterruptedException e) {
            fail("InterruptedException");
        }
    }

}
