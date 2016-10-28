/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shtrih.ej;

import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class EJReportParserTest extends TestCase {
    
    public EJReportParserTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of parseEJActivation method, of class EJReportParser.
     */
   
/*    
------------------------------------------
 40 ÒËÏ‚ÓÎÓ‚
 ------------------------------------------
 ÿ“–»’-Ã-‘–- 
   Ã 000012345678 »ÕÕ 000000012345
 › À« 0000000018
 »“Œ√ ¿ “»¬»«¿÷»»
 10/02/14 12:18 «¿ –€“»≈ —Ã≈Õ€ 0000
 –≈√»—“–¿÷»ŒÕÕ€… ÕŒÃ≈–       000000123456
 00000001 #037110
 ------------------------------------------
 16 ÒËÏ‚ÓÎÓ‚
 ------------------------------------------
 ÿ“–»’-LIGHT-‘–- 
   Ã 000000001012
 »ÕÕ 000000012345
 › À« 0113154054
 »“Œ√ ¿ “»¬»«¿÷»»
 10/02/14 13:57
 «¿ –.—Ã≈Õ€  0000
 –≈√ 000000123456
 00000001 #013047
 ------------------------------------------    
 */

    private String[] ActivationText40 = {
 "ÿ“–»’-Ã-‘–- ",
 "  Ã 000012345678 »ÕÕ 000000012345", 
 "› À« 0000000018", 
 "»“Œ√ ¿ “»¬»«¿÷»»", 
 "10/02/14 12:18 «¿ –€“»≈ —Ã≈Õ€ 0000", 
 "–≈√»—“–¿÷»ŒÕÕ€… ÕŒÃ≈–       000000123456", 
 "00000001 #037110"};
 
    private String[] ActivationText16 = {
 "ÿ“–»’-LIGHT-‘–- ",
 "  Ã 000000001012",
 "»ÕÕ 000000012345",
 "› À« 0113154054",
 "»“Œ√ ¿ “»¬»«¿÷»»",
 "10/02/14 13:57",
 "«¿ –.—Ã≈Õ€  0000",
 "–≈√ 000000123456",
 "00000001 #013047"};
    
    
    public void testParseEJActivation() throws Exception {
        System.out.println("parseEJActivation");
        EJActivation result;
        
        result = EJReportParser.parseEJActivation(ActivationText40);
        assertEquals("0000000018", result.getEJSerial());
        assertEquals("10/02/14", result.getDate());
        assertEquals("12:18", result.getTime());
        
        result = EJReportParser.parseEJActivation(ActivationText16);
        assertEquals("0113154054", result.getEJSerial());
        assertEquals("10/02/14", result.getDate());
        assertEquals("13:57", result.getTime());
        
    }
    
}
