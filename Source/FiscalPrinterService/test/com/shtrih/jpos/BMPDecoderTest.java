/*
 * BitUtilsTest.java
 * JUnit based test
 *
 * Created on 20 March 2008, 17:27
 */

package com.shtrih.jpos;

import com.shtrih.util.Hex;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.File;
import junit.framework.TestCase;
import net.sf.image4j.codec.bmp.BMPDecoder;
/**
 *
 * @author V.Kravtsov
 */
public class BMPDecoderTest extends TestCase {
    
    public BMPDecoderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testRead() 
        throws Exception
    {
        File file = new File("C:\\Logo.bmp");
        BufferedImage image = BMPDecoder.read(file);
        ColorModel colorModel = image.getColorModel();
        System.out.println(colorModel.getClass().getName());
        
        BufferedImage buffered = new BufferedImage(image.getWidth(),
            image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        
        Graphics2D g2 = buffered.createGraphics();
        g2.drawImage(image, null, null);
        image = buffered;
       
        /*
        image = ImageUtils.centerImageX(image, 320);
        
        Raster raster = image.getData();
        SampleModel sampleModel = raster.getSampleModel();
        String s = sampleModel.getClass().getName();
        System.out.println(s);
        
        int[] iArray = new int[image.getWidth()];
        raster.getPixels(0, 10, image.getWidth(), 1, iArray);
        Hex.printHex(iArray);
                */
    }
}
