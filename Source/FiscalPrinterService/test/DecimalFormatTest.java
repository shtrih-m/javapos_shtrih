/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */

import java.util.*;
import junit.framework.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DecimalFormatTest  extends TestCase {
   
    /**
     * Creates a new instance of DecimalFormatTest
     */
    public DecimalFormatTest(String testName) {
        super(testName);
    }
    
    public void testFormat() 
    throws Exception
    {
        double value;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        assertEquals('#', symbols.getDigit());
        assertEquals('0', symbols.getZeroDigit());
        
        symbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("0.00", symbols);
        
        value = 0.12;
        assertEquals("0.12", formatter.format(value));
        
        value = 9.234;
        assertEquals("9.23", formatter.format(value));
        
        value = 0;
        assertEquals("0.00", formatter.format(value));

        formatter = new DecimalFormat("0.000000", symbols);
        value = 0.000001;
        assertEquals("0.000001", formatter.format(value));
        
        formatter = new DecimalFormat("0.000", symbols);
        value = 0.001;
        assertEquals("0.001", formatter.format(value));
    }
}
