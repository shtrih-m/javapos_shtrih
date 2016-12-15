/*
 * BitUtilsTest.java
 * JUnit based test
 *
 * Created on 20 March 2008, 17:27
 */

package com.shtrih.jpos;


/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.util.Sound;
import junit.framework.TestCase;
import org.junit.Ignore;

@Ignore
public class SoundTest extends TestCase {
    
    public SoundTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

   public void testEncode() 
        throws Exception
    {
       Sound.beep(500, 1000);
    }
}
