/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import junit.framework.TestCase;
import com.shtrih.jpos.fiscalprinter.XmlPropReader;
import com.shtrih.jpos.fiscalprinter.XmlPropWriter;

/**
 *
 * @author Kravtsov
 */
public class XmlPropReaderTest extends TestCase {

    public XmlPropReaderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSave() {
        try {
            System.out.println("save");
            String fileName = "build/XmlPropReaderTest.xml";
            PrinterImages printerImages = new PrinterImages();
            printerImages.setMaxSize(1000);
            PrinterImage printerImage = new PrinterImage();
            printerImage.setStartPos(10);
            printerImage.setFileName("File name 1");
            printerImage.setHeight(13);
            printerImage.setIsLoaded(true);
            printerImage.setDigest("image1");
            printerImages.add(printerImage);

            printerImage = new PrinterImage();
            printerImage.setStartPos(12);
            printerImage.setFileName("File name 2");
            printerImage.setHeight(12);
            printerImage.setDigest("image2");
            printerImage.setIsLoaded(false);
            printerImages.add(printerImage);

            ReceiptImages receiptImages = new ReceiptImages();
            ReceiptImage receiptImage = new ReceiptImage();
            receiptImage.setImageIndex(1);
            receiptImage.setPosition(2);
            receiptImages.add(receiptImage);

            DriverHeader header = new DriverHeader(null);
            header.setCount(4);
            header.setLine(1, "HeaderLine 1");
            header.setLine(2, "HeaderLine 2");
            header.setLine(3, "HeaderLine 3");
            header.setLine(4, "HeaderLine 4");
            assertEquals("HeaderLine 1", header.get(0).getText());
            assertEquals("HeaderLine 2", header.get(1).getText());
            assertEquals("HeaderLine 3", header.get(2).getText());
            assertEquals("HeaderLine 4", header.get(3).getText());

            DriverTrailer trailer = new DriverTrailer(null);
            trailer.setCount(3);
            trailer.setLine(1, "TrailerLine 1");
            trailer.setLine(2, "TrailerLine 2");
            assertEquals("TrailerLine 1", trailer.get(0).getText());
            assertEquals("TrailerLine 2", trailer.get(1).getText());

            XmlPropWriter writer = new XmlPropWriter("FiscalPrinter", "Device1");
            writer.write(printerImages);
            writer.write(receiptImages);
            writer.writeHeader(header);
            writer.writeTrailer(trailer);
            writer.save(fileName);

            header = new DriverHeader(null);
            trailer = new DriverTrailer(null);

            XmlPropReader reader = new XmlPropReader();
            reader.load("FiscalPrinter", "Device1", fileName);
            reader.read(printerImages);
            reader.read(receiptImages);
            reader.readHeader(header);
            reader.readTrailer(trailer);

            header.setCount(4);

            assertEquals("HeaderLine 1", header.get(0).getText());
            assertEquals("HeaderLine 2", header.get(1).getText());
            assertEquals("HeaderLine 3", header.get(2).getText());
            assertEquals("HeaderLine 4", header.get(3).getText());

            trailer.setCount(3);

            assertEquals("TrailerLine 1", trailer.get(0).getText());
            assertEquals("TrailerLine 2", trailer.get(1).getText());

            assertEquals(2, printerImages.size());
            printerImage = printerImages.get(0);
            assertEquals(1, printerImage.getStartPos());
            assertEquals("File name 1", printerImage.getFileName());
            assertEquals(13, printerImage.getHeight());
            assertEquals(true, printerImage.getIsLoaded());

            printerImage = printerImages.get(1);
            assertEquals(14, printerImage.getStartPos());
            assertEquals("File name 2", printerImage.getFileName());
            assertEquals(12, printerImage.getHeight());
            assertEquals(false, printerImage.getIsLoaded());


            assertEquals(1, receiptImages.size());
            receiptImage = receiptImages.get(0);
            assertEquals(1, receiptImage.getImageIndex());
            assertEquals(2, receiptImage.getPosition());
                    


        } catch (Exception e) {
            fail(e.getMessage());
        }



    }
}
