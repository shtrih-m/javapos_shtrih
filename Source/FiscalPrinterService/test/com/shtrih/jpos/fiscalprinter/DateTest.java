/*
 * DateTest.java
 *
 * Created on 13 январь 2011 г., 18:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import junit.framework.TestCase;


/**
 *
 * @author V.Kravtsov
 */
public class DateTest extends TestCase {
    
    public DateTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void test1() {
        try {
            SimpleDateFormat timeFormat =  new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateFormat =  new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
            
            Calendar time = Calendar.getInstance();
            System.out.println("Now: " + dateFormat.format(time.getTime()));
            
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(timeFormat.parse("18:00"));
            time.set(Calendar.HOUR, startTime.get(Calendar.HOUR));
            time.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
            time.set(Calendar.SECOND, 0);
            time.set(Calendar.MILLISECOND, 0);
            
            System.out.println("Time: " + dateFormat.format(time.getTime()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void test2() {
        try {
            Calendar time = Calendar.getInstance();
            Calendar time1 = Calendar.getInstance();
            Calendar time2 = Calendar.getInstance();
            
            time1.set(Calendar.HOUR, 14);
            time1.set(Calendar.MINUTE, 04);
            
            time.set(Calendar.HOUR, 14);
            time.set(Calendar.MINUTE, 05);
            
            time2.set(Calendar.HOUR, 14);
            time2.set(Calendar.MINUTE, 06);
            
            assertEquals(true, time.after(time1));
            assertEquals(true, time.before(time2));
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

