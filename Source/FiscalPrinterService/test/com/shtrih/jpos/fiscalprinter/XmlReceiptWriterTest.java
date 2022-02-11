/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import java.io.File;

import org.junit.Ignore;
import org.w3c.dom.Node;
import junit.framework.TestCase;
import java.io.FileInputStream;
import com.shtrih.util.FileUtils;

/**
 *
 * @author V.Kravtsov
 */
@Ignore
public class XmlReceiptWriterTest extends TestCase {

    public XmlReceiptWriterTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of save method, of class XmlReceiptWriter.
     */
    public void testSave() {
        System.out.println("save");
        ReceiptReport report = new ReceiptReport();
        String path = "data/";
        String fileName = path + "XmlReceipt.xml";
        String fileName1 = path + "XmlReceipt1.xml";
        String fileName2 = path + "XmlReceipt2.xml";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }

        report.id = 123;
        report.docID = 234;
        report.recType = 345;
        report.state = 456;
        report.amount = 567;
        report.payments[0] = 789;
        report.payments[1] = 890;
        report.payments[2] = 901;
        report.payments[3] = 12;

        XmlReceiptWriter instance = new XmlReceiptWriter();
        instance.save(report, fileName);

        try {
            String fileData = FileUtils.load(fileName);
            String fileData1 = FileUtils.load(fileName1);
            assertEquals("fileData <> fileData1", fileData, fileData1);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        instance.save(report, fileName);
        try {
            String fileData = FileUtils.load(fileName);
            String fileData1 = FileUtils.load(fileName2);
            assertEquals("fileData <> fileData1", fileData, fileData1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        if (file.exists()) {
            file.delete();
        }
    }
}