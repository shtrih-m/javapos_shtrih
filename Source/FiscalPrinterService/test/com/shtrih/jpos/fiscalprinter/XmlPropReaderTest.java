/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinterNull;
import junit.framework.TestCase;

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

        SMFiscalPrinterNull printer = new SMFiscalPrinterNull(null, null, new FptrParameters());

        DriverHeader header = new DriverHeader(printer);
        header.setNumHeaderLines(4);
        header.setHeaderLine(1, "HeaderLine 1", false);
        header.setHeaderLine(2, "HeaderLine 2", true);
        header.setHeaderLine(3, "HeaderLine 3", false);
        header.setHeaderLine(4, "HeaderLine 4", false);

        assertEquals("HeaderLine 1", header.getHeaderLine(1).getText());
        assertEquals(false, header.getHeaderLine(1).isDoubleWidth());
        assertEquals("HeaderLine 2", header.getHeaderLine(2).getText());
        assertEquals(true, header.getHeaderLine(2).isDoubleWidth());
        assertEquals("HeaderLine 3", header.getHeaderLine(3).getText());
        assertEquals(false, header.getHeaderLine(3).isDoubleWidth());
        assertEquals("HeaderLine 4", header.getHeaderLine(4).getText());
        assertEquals(false, header.getHeaderLine(4).isDoubleWidth());


        header.setNumTrailerLines(3);
        header.setTrailerLine(1, "TrailerLine 1", false);
        header.setTrailerLine(2, "TrailerLine 2", true);

        assertEquals("TrailerLine 1", header.getTrailerLine(1).getText());
        assertEquals(false, header.getTrailerLine(1).isDoubleWidth());
        assertEquals("TrailerLine 2", header.getTrailerLine(2).getText());
        assertEquals(true, header.getTrailerLine(2).isDoubleWidth());
        assertEquals("", header.getTrailerLine(3).getText());
        assertEquals(false, header.getTrailerLine(3).isDoubleWidth());

        XmlPropWriter writer = new XmlPropWriter("FiscalPrinter", "Device1");
        writer.write(printerImages);
        writer.write(receiptImages);
        writer.writePrinterHeader(header);
        writer.writeNonFiscalDocNumber(666);
        writer.save(fileName);

        header = new DriverHeader(printer);

        XmlPropReader reader = new XmlPropReader();
        reader.load("FiscalPrinter", "Device1", fileName);
        reader.read(printerImages);
        reader.read(receiptImages);

        //header.initDevice();

        header.setNumHeaderLines(4);
        header.setNumTrailerLines(3);
        reader.readPrinterHeader(header);



        assertEquals("HeaderLine 1", header.getHeaderLine(1).getText());
        assertEquals(false, header.getHeaderLine(1).isDoubleWidth());
        assertEquals("HeaderLine 2", header.getHeaderLine(2).getText());
        assertEquals(true, header.getHeaderLine(2).isDoubleWidth());
        assertEquals("HeaderLine 3", header.getHeaderLine(3).getText());
        assertEquals(false, header.getHeaderLine(3).isDoubleWidth());
        assertEquals("HeaderLine 4", header.getHeaderLine(4).getText());
        assertEquals(false, header.getHeaderLine(4).isDoubleWidth());

        assertEquals("TrailerLine 1", header.getTrailerLine(1).getText());
        assertEquals(false, header.getTrailerLine(1).isDoubleWidth());
        assertEquals("TrailerLine 2", header.getTrailerLine(2).getText());
        assertEquals(true, header.getTrailerLine(2).isDoubleWidth());
        assertEquals("", header.getTrailerLine(3).getText());
        assertEquals(false, header.getTrailerLine(3).isDoubleWidth());

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

        assertEquals(666, reader.readNonFiscalDocNumber());
    }

    public void test_when_non_fiscal_was_not_found_should_return_default_value() throws Exception {
        String fileName = "build/XmlPropReaderTest.xml";

        XmlPropWriter writer = new XmlPropWriter("FiscalPrinter", "Device1");
        writer.save(fileName);

        XmlPropReader reader = new XmlPropReader();
        reader.load("FiscalPrinter", "Device1", fileName);

        assertEquals(1, reader.readNonFiscalDocNumber());
    }
}
