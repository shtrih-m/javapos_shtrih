/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import com.shtrih.util.StringUtils;
import junit.framework.TestCase;
import com.shtrih.fiscalprinter.model.PrinterModels;
import com.shtrih.fiscalprinter.model.PrinterModel;
import com.shtrih.fiscalprinter.model.PrinterModelDefault;
import com.shtrih.fiscalprinter.command.PrinterParameter;
import com.shtrih.fiscalprinter.command.PrinterParameters;
import com.shtrih.fiscalprinter.command.PrinterConst;


public class PrinterModelsTest extends TestCase {

    public PrinterModelsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of clear method, of class PrinterModels.
     */
    public void testClear() {
        System.out.println("clear");
        PrinterModels instance = new PrinterModels();
        instance.clear();
        assertEquals(0, instance.size());
    }

    /**
     * Test of size method, of class PrinterModels.
     */
    public void testSize() {
        System.out.println("size");
        PrinterModels instance = new PrinterModels();
        assertEquals(0, instance.size());
        // instance.add(new PrinterModelDefault()); !!!
        assertEquals(1, instance.size());
    }

    /**
     * Test of add method, of class PrinterModels.
     */
    public void testAdd() 
    {
        /*
        System.out.println("add");
        PrinterModel item = new PrinterModelDefault();
        PrinterModels instance = new PrinterModels();
        instance.add(item);
        */
    }

    /**
     * Test of get method, of class PrinterModels.
     */
    public void testGet() {
        /*
        System.out.println("get");
        int index = 0;
        PrinterModels instance = new PrinterModels();
        PrinterModel expResult = new PrinterModelDefault();
        instance.add(expResult);
        PrinterModel result = instance.get(index);
        assertEquals(expResult, result);
        */
    }

    /**
     * Test of setDefaults method, of class PrinterModels.
     */
    public void testSetDefaults() {
        System.out.println("setDefaults");
        PrinterModels instance = new PrinterModels();
        instance.setDefaults();

    }

    /**
     * Test of save method, of class PrinterModels.
     */
    public void testSave() throws Exception {
        /*
        System.out.println("save");
        String fileName = "models.xml";
        PrinterModels instance = new PrinterModels();
        PrinterModels models = new PrinterModels();
        instance.setDefaults();
        models.setDefaults();
        assertTrue(instance.size() > 0);
        int modelCount = instance.size();

        instance.save(fileName);
        instance.clear();
        instance.load(fileName);
        checkModels(instance, models);
                */
    }

    private void checkModels(PrinterModels item1, PrinterModels item2)
            throws Exception 
    {
        /*
        assertEquals("models.size()", item1.size(), item2.size());
        for (int i = 0; i < item1.size(); i++) {
            PrinterModel model1 = item1.get(i);
            PrinterModel model2 = item2.get(i);

            assertEquals("getName", model1.getName(), model2.getName());
            assertEquals("getAmountDecimalPlace", model1.getAmountDecimalPlace(), model2.getAmountDecimalPlace());
            assertEquals("getCapCoverSensor", model1.getCapCoverSensor(), model2.getCapCoverSensor());
            assertEquals("getCapDoubleWidth", model1.getCapDoubleWidth(), model2.getCapDoubleWidth());
            assertEquals("getCapDuplicateReceipt", model1.getCapDuplicateReceipt(), model2.getCapDuplicateReceipt());
            assertEquals("getCapEJPresent", model1.getCapEJPresent(), model2.getCapEJPresent());
            assertEquals("getCapFMPresent", model1.getCapFMPresent(), model2.getCapFMPresent());
            assertEquals("getCapFontMetrics", model1.getCapFontMetrics(), model2.getCapFontMetrics());
            assertEquals("getCapFullCut", model1.getCapFullCut(), model2.getCapFullCut());
            assertEquals("getCapGraphics", model1.getCapGraphics(), model2.getCapGraphics());
            assertEquals("getCapGraphicsEx", model1.getCapGraphicsEx(), model2.getCapGraphicsEx());
            assertEquals("getCapHasVatTable", model1.getCapHasVatTable(), model2.getCapHasVatTable());
            assertEquals("getCapJrnEmptySensor", model1.getCapJrnEmptySensor(), model2.getCapJrnEmptySensor());
            assertEquals("getCapJrnLeverSensor", model1.getCapJrnLeverSensor(), model2.getCapJrnLeverSensor());
            assertEquals("getCapJrnNearEndSensor", model1.getCapJrnNearEndSensor(), model2.getCapJrnNearEndSensor());
            assertEquals("getCapJrnPresent", model1.getCapJrnPresent(), model2.getCapJrnPresent());
            assertEquals("getCapOpenReceipt", model1.getCapOpenReceipt(), model2.getCapOpenReceipt());
            assertEquals("getCapPartialCut", model1.getCapPartialCut(), model2.getCapPartialCut());
            assertEquals("getCapPrintGraphicsLine", model1.getCapPrintGraphicsLine(), model2.getCapPrintGraphicsLine());
            assertEquals("getCapPrintStringFont", model1.getCapPrintStringFont(), model2.getCapPrintStringFont());
            assertEquals("getCapRecEmptySensor", model1.getCapRecEmptySensor(), model2.getCapRecEmptySensor());
            assertEquals("getCapRecLeverSensor", model1.getCapRecLeverSensor(), model2.getCapRecLeverSensor());
            assertEquals("getCapRecNearEndSensor", model1.getCapRecNearEndSensor(), model2.getCapRecNearEndSensor());
            assertEquals("getCapRecPresent", model1.getCapRecPresent(), model2.getCapRecPresent());
            assertEquals("getCapShortStatus", model1.getCapShortStatus(), model2.getCapShortStatus());
            assertEquals("getCapSlpEmptySensor", model1.getCapSlpEmptySensor(), model2.getCapSlpEmptySensor());
            assertEquals("getCapSlpNearEndSensor", model1.getCapSlpNearEndSensor(), model2.getCapSlpNearEndSensor());
            assertEquals("getCapSlpPresent", model1.getCapSlpPresent(), model2.getCapSlpPresent());
            assertEquals("getHeaderTableNumber", model1.getHeaderTableNumber(), model2.getHeaderTableNumber());
            assertEquals("getHeaderTableRow", model1.getHeaderTableRow(), model2.getHeaderTableRow());
            assertEquals("getId", model1.getId(), model2.getId());
            assertEquals("getMaxGraphicsHeight", model1.getMaxGraphicsHeight(), model2.getMaxGraphicsHeight());
            assertEquals("getMaxGraphicsWidth", model1.getMaxGraphicsWidth(), model2.getMaxGraphicsWidth());
            assertEquals("getMinHeaderLines", model1.getMinHeaderLines(), model2.getMinHeaderLines());
            assertEquals("getMinTrailerLines", model1.getMinTrailerLines(), model2.getMinTrailerLines());
            assertEquals("getModelID", model1.getModelID(), model2.getModelID());
            assertEquals("getNumHeaderLines", model1.getNumHeaderLines(), model2.getNumHeaderLines());
            assertEquals("getNumTrailerLines", model1.getNumTrailerLines(), model2.getNumTrailerLines());
            assertEquals("getNumVatRates", model1.getNumVatRates(), model2.getNumVatRates());
            assertEquals("getGraphicLineWidth", model1.getGraphicLineWidth(), model2.getGraphicLineWidth());
            assertEquals("getProtocolSubVersion", model1.getProtocolSubVersion(), model2.getProtocolSubVersion());
            assertEquals("getProtocolVersion", model1.getProtocolVersion(), model2.getProtocolVersion());
            assertEquals("getTrailerTableNumber", model1.getTrailerTableNumber(), model2.getTrailerTableNumber());
            assertEquals("getTrailerTableRow", model1.getTrailerTableRow(), model2.getTrailerTableRow());

            assertEquals("getTextLength",
                    StringUtils.arrayToStr(model1.getTextLength()),
                    StringUtils.arrayToStr(model2.getTextLength()));

            assertEquals("getSupportedBaudRates",
                    StringUtils.arrayToStr(model1.getSupportedBaudRates()),
                    StringUtils.arrayToStr(model2.getSupportedBaudRates()));

            PrinterParameters p1 = model1.getParameters();
            PrinterParameters p2 = model2.getParameters();
            String modelName = model1.getName();
            assertEquals("parameters().size(), " + modelName, p1.size(), p2.size());
            for (int j = 0; j < p1.size(); j++) {
                PrinterParameter pr1 = p1.get(j);
                PrinterParameter pr2 = p2.get(j);

                assertEquals("ID, " + modelName, pr1.getId(), pr2.getId());
                assertEquals("TableNumber, " + modelName, pr1.getTableNumber(), pr2.getTableNumber());
                assertEquals("RowNumber, " + modelName, pr1.getRowNumber(), pr2.getRowNumber());
                assertEquals("FieldNumber, " + modelName, pr1.getFieldNumber(), pr2.getFieldNumber());
            }
        }
        */
    }

    public void testFind() {
        try {
            System.out.println("setDefaults");
            PrinterModels instance = new PrinterModels();
            instance.setDefaults();
            PrinterModel model = instance.find(PrinterConst.PRINTER_MODEL_SHTRIH_FRF, 1, 0);
            PrinterModel model1 = instance.itemByID(PrinterConst.SMFP_MODELID_SHTRIH_FRFv3);

            assertTrue("model not null", model != null);
            assertTrue("model1 not null", model1 != null);
            assertEquals("SMFP_MODELID_SHTRIH_FRFv3", model1.getName(), model.getName());

            model = instance.find(PrinterConst.PRINTER_MODEL_SHTRIH_FRF, 1, 4);
            model1 = instance.itemByID(PrinterConst.SMFP_MODELID_SHTRIH_FRFv4);

            assertTrue("model not null", model != null);
            assertTrue("model1 not null", model1 != null);
            assertEquals("SMFP_MODELID_SHTRIH_FRFv4", model1.getName(), model.getName());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
