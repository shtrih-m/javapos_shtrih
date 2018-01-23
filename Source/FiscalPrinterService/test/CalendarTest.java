
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */
public class CalendarTest extends TestCase 
{
   
    /**
     * Creates a new instance of DecimalFormatTest
     */
    public CalendarTest(String testName) {
        super(testName);
    }
    
    public void testUnixTime() throws Exception
    {
        String date = "23012018123445";
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dt = sdf.parse(date.substring(0, 8));
        assertEquals(1516665600, dt.getTime()/1000);
    }
}


