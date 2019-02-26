
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;
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
public class LoggerTest extends TestCase 
{
   
    /**
     * Creates a new instance of DecimalFormatTest
     */
    public LoggerTest(String testName) {
        super(testName);
    }
    
    public void testLogger() throws Exception
    {
        LoggerTest.class.getResourceAsStream("C:\\projects\\JavaPOS\\Source\\FiscalPrinterTest\\Res\\logging.properties");
        LogManager.getLogManager().readConfiguration();
        
        Logger logger = Logger.getLogger(LoggerTest.class.getName());   
        logger.log(Level.INFO, "info");
        logger.log(Level.WARNING, "warning");
    }
}


