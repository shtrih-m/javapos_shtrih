/*
 * UTF8EncodingTest.java
 *
 * Created on November 18, 2009, 5:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import junit.framework.TestCase;

public class UTF8EncodingTest extends TestCase 
{
    
    /** Creates a new instance of UTF8EncodingTest */
    public UTF8EncodingTest(String testName) {
        super(testName);
    }

    public void testWriteFile() 
    throws Exception
    {
        System.out.println("testWriteFile");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader
            (new FileInputStream("UTF8EncodingTest.txt"),"UTF8"));        
        
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
            (new FileOutputStream("UTF8EncodingTest2.txt"),"UTF8"));        
        
        String line = reader.readLine();
        System.out.println(line);
        writer.write(line);
        writer.newLine();
        writer.flush();
    }
}
