/*
 * StringTokenizerTest.java
 *
 * Created on 20 Ноябрь 2009 г., 18:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */

import java.util.StringTokenizer;
import junit.framework.TestCase;


public class StringTokenizerTest extends TestCase {
    
    /**
     * Creates a new instance of StringTokenizerTest
     */
    public StringTokenizerTest(String testName) {
        super(testName);
    }
    
    public void testSplit() 
    throws Exception
    {
        System.out.println("testSplit");
        
        String line = "a\nb\rc\n\r\n\r";
        StringTokenizer tokenizer = new StringTokenizer(line, "\n\r");
        assertEquals(3, tokenizer.countTokens());
        assertEquals("a", tokenizer.nextToken());
        assertEquals("b", tokenizer.nextToken());
        assertEquals("c", tokenizer.nextToken());
        
    }
    
}
