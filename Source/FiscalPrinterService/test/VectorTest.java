
import java.util.Vector;
import junit.framework.TestCase;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */
public class VectorTest extends TestCase {
    
    /**
     * Creates a new instance of StringTokenizerTest
     */
    public VectorTest(String testName) {
        super(testName);
    }

    public void testToArray() 
    {
        Vector v = new Vector();
        v.add("Line1");
        v.add("Line2");
        v.add("Line3");
        String[] lines = (String[])(v.toArray(new String[0]));
        assertEquals(3, lines.length);
        assertEquals("Line1", lines[0]);
        assertEquals("Line2", lines[1]);
        assertEquals("Line3", lines[2]);
    }
    
    
}
