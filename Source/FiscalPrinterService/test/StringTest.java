/*
 * StringTest.java
 *
 * Created on 9 Сентябрь 2010 г., 18:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */
import java.util.Locale;
import junit.framework.TestCase;

public class StringTest extends TestCase {

    public StringTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testReplace()
            throws Exception {
        String text;
        String result;

        text = "abc\r\n\rdef";
        result = text.replaceAll("\r\n", "\r");
        assertEquals("abc\r\rdef", result);

        text = "\r\r";
        result = text.replaceAll("\r", "\n");
        assertEquals("\n\n", result);
    }

    public void testSplit()
            throws Exception {
        String text;
        String[] lines;

        text = "";
        lines = text.split("\r\n|\r|\n");
        assertEquals(1, lines.length);
        assertEquals("", lines[0]);

        text = "\r";
        lines = text.split("\r\n|\r|\n");
        assertEquals(0, lines.length);

        text = "\n";
        lines = text.split("\r\n|\r|\n");
        assertEquals(0, lines.length);

        text = "\r\n\r\n";
        lines = text.split("\r\n|\r|\n");
        assertEquals(0, lines.length);

        text = "\n\r";
        lines = text.split("\r\n|\r|\n");
        assertEquals(0, lines.length);

        text = "Line1\r\nLine2";
        lines = text.split("\r\n|\r|\n");
        assertEquals(2, lines.length);
        assertEquals("Line1", lines[0]);
        assertEquals("Line2", lines[1]);

        text = "Line1\r\n";
        lines = text.split("\r\n|\r|\n");
        assertEquals(1, lines.length);
        assertEquals("Line1", lines[0]);
    }

    public void testSubstring()
            throws Exception {
        String text = "test1test2";
        assertEquals("test1", text.substring(0, 5));
    }
}
