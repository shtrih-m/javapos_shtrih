/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinterNull;
import junit.framework.TestCase;
import com.shtrih.jpos.fiscalprinter.XmlPropReader;
import com.shtrih.jpos.fiscalprinter.XmlPropWriter;

/**
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

    public void testSave() throws Exception {
        System.out.println("save");
        String fileName = "build/XmlPropReaderTest.xml";
        FptrParameters params = new FptrParameters();
        params.getPrinterImages().setMaxSize(1000);
        PrinterImage printerImage = new PrinterImage();
        printerImage.setStartPos(10);
        printerImage.setFileName("File name 1");
        printerImage.setHeight(13);
        printerImage.setIsLoaded(true);
        printerImage.setDigest("image1");
        params.getPrinterImages().add(printerImage);

        printerImage = new PrinterImage();
        printerImage.setStartPos(12);
        printerImage.setFileName("File name 2");
        printerImage.setHeight(12);
        printerImage.setDigest("image2");
        printerImage.setIsLoaded(false);
        params.getPrinterImages().add(printerImage);

        ReceiptImage receiptImage = new ReceiptImage(1, 2);
        params.getReceiptImages().add(receiptImage);

        params.setNumHeaderLines(4);
        params.setHeaderLine(1, "HeaderLine 1", false);
        params.setHeaderLine(2, "HeaderLine 2", true);
        params.setHeaderLine(3, "HeaderLine 3", false);
        params.setHeaderLine(4, "HeaderLine 4", false);

        assertEquals("HeaderLine 1", params.getHeaderLine(1).getText());
        assertEquals(false, params.getHeaderLine(1).isDoubleWidth());
        assertEquals("HeaderLine 2", params.getHeaderLine(2).getText());
        assertEquals(true, params.getHeaderLine(2).isDoubleWidth());
        assertEquals("HeaderLine 3", params.getHeaderLine(3).getText());
        assertEquals(false, params.getHeaderLine(3).isDoubleWidth());
        assertEquals("HeaderLine 4", params.getHeaderLine(4).getText());
        assertEquals(false, params.getHeaderLine(4).isDoubleWidth());


        params.setNumTrailerLines(3);
        params.setTrailerLine(1, "TrailerLine 1", false);
        params.setTrailerLine(2, "TrailerLine 2", true);

        assertEquals("TrailerLine 1", params.getTrailerLine(1).getText());
        assertEquals(false, params.getTrailerLine(1).isDoubleWidth());
        assertEquals("TrailerLine 2", params.getTrailerLine(2).getText());
        assertEquals(true, params.getTrailerLine(2).isDoubleWidth());
        assertEquals("", params.getTrailerLine(3).getText());
        assertEquals(false, params.getTrailerLine(3).isDoubleWidth());

        XmlPropWriter writer = new XmlPropWriter("FiscalPrinter", "Device1");
        writer.write(params);
        writer.save(fileName);

        XmlPropReader reader = new XmlPropReader();
        reader.load("FiscalPrinter", "Device1", fileName);
        reader.read(params);

        assertEquals("HeaderLine 1", params.getHeaderLine(1).getText());
        assertEquals(false, params.getHeaderLine(1).isDoubleWidth());
        assertEquals("HeaderLine 2", params.getHeaderLine(2).getText());
        assertEquals(true, params.getHeaderLine(2).isDoubleWidth());
        assertEquals("HeaderLine 3", params.getHeaderLine(3).getText());
        assertEquals(false, params.getHeaderLine(3).isDoubleWidth());
        assertEquals("HeaderLine 4", params.getHeaderLine(4).getText());
        assertEquals(false, params.getHeaderLine(4).isDoubleWidth());

        assertEquals("TrailerLine 1", params.getTrailerLine(1).getText());
        assertEquals(false, params.getTrailerLine(1).isDoubleWidth());
        assertEquals("TrailerLine 2", params.getTrailerLine(2).getText());
        assertEquals(true, params.getTrailerLine(2).isDoubleWidth());
        assertEquals("", params.getTrailerLine(3).getText());
        assertEquals(false, params.getTrailerLine(3).isDoubleWidth());

        assertEquals(2, params.getPrinterImages().size());
        printerImage = params.getPrinterImages().get(0);
        assertEquals(1, printerImage.getStartPos());
        assertEquals("File name 1", printerImage.getFileName());
        assertEquals(13, printerImage.getHeight());
        assertEquals(true, printerImage.getIsLoaded());

        printerImage = params.getPrinterImages().get(1);
        assertEquals(14, printerImage.getStartPos());
        assertEquals("File name 2", printerImage.getFileName());
        assertEquals(12, printerImage.getHeight());
        assertEquals(false, printerImage.getIsLoaded());

        assertEquals(1, params.getReceiptImages().size());
        receiptImage = params.getReceiptImages().get(0);
        assertEquals(1, receiptImage.getImageIndex());
        assertEquals(2, receiptImage.getPosition());
    }
}
