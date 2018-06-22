/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.fiscalprinter.receipt.PrinterReceipt;
import com.shtrih.jpos.fiscalprinter.FiscalDay;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import junit.framework.TestCase;
import org.junit.Ignore;

@Ignore
public class SalesReceiptTest extends TestCase {
    
    public SalesReceiptTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    private SalesReceipt getSalesReceipt()
    {
        return null;
        /*
        ReceiptPrinter receiptPrinter = new MockReceiptPrinter();
        FptrParameters printerData = new FptrParameters();
        FiscalDay fiscalDay = new FiscalDay();
        PrinterReceipt receipt = new PrinterReceipt();
                        
        //SalesReceipt result = getSalesReceipt(
        //        printer, printerData, fiscalDay, receipt)
        */
        
    }
            
    /**
     * Test of isOpened method, of class SalesReceipt.
     */
    public void testIsOpened() 
    {
        /*
        System.out.println("isOpened");
        SalesReceipt instance = getSalesReceipt();
        instance.isOpened();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
        */
    }

    /**
     * Test of printRecItem method, of class SalesReceipt.
     */
    public void testPrintRecItem() throws Exception {
        System.out.println("printRecItem");
        String description = "";
        long price = 0L;
        int quantity = 0;
        int vatInfo = 0;
        long unitPrice = 0L;
        String unitName = "";
        SalesReceipt instance = null;
        instance.printRecItem(description, price, quantity, vatInfo, unitPrice, unitName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printSale method, of class SalesReceipt.
     */
    public void testPrintSale() throws Exception {
        System.out.println("printSale");
        long price = 0L;
        long quantity = 0L;
        int department = 0;
        int vatInfo = 0;
        String description = "";
        SalesReceipt instance = null;
        instance.printSale(price, quantity, department, vatInfo, description);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of beginFiscalReceipt method, of class SalesReceipt.
     */
    public void testBeginFiscalReceipt() throws Exception {
        System.out.println("beginFiscalReceipt");
        boolean printHeader = false;
        SalesReceipt instance = null;
        instance.beginFiscalReceipt(printHeader);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of endFiscalReceipt method, of class SalesReceipt.
     */
    public void testEndFiscalReceipt() throws Exception {
        System.out.println("endFiscalReceipt");
        boolean printHeader = false;
        SalesReceipt instance = null;
        instance.endFiscalReceipt(printHeader);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecRefund method, of class SalesReceipt.
     */
    public void testPrintRecRefund() throws Exception {
        System.out.println("printRecRefund");
        String description = "";
        long amount = 0L;
        int vatInfo = 0;
        SalesReceipt instance = null;
        instance.printRecRefund(description, amount, vatInfo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecSubtotal method, of class SalesReceipt.
     */
    public void testPrintRecSubtotal() throws Exception {
        System.out.println("printRecSubtotal");
        long amount = 0L;
        SalesReceipt instance = null;
        instance.printRecSubtotal(amount);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecTotal method, of class SalesReceipt.
     */
    public void testPrintRecTotal() throws Exception {
        System.out.println("printRecTotal");
        long total = 0L;
        long payment = 0L;
        long payType = 0L;
        String description = "";
        SalesReceipt instance = null;
        instance.printRecTotal(total, payment, payType, description);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecItemVoid method, of class SalesReceipt.
     */
    public void testPrintRecItemVoid() throws Exception {
        System.out.println("printRecItemVoid");
        String description = "";
        long price = 0L;
        int quantity = 0;
        int vatInfo = 0;
        long unitPrice = 0L;
        String unitName = "";
        SalesReceipt instance = null;
        instance.printRecItemVoid(description, price, quantity, vatInfo, unitPrice, unitName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printStorno method, of class SalesReceipt.
     */
    public void testPrintStorno() throws Exception {
        System.out.println("printStorno");
        long price = 0L;
        int quantity = 0;
        int department = 0;
        int vatInfo = 0;
        String description = "";
        SalesReceipt instance = null;
        instance.printStorno(price, quantity, department, vatInfo, description);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printDiscount method, of class SalesReceipt.
     */
    public void testPrintDiscount() throws Exception 
    {
        /*
        System.out.println("printDiscount");
        long amount = 0L;
        int tax1 = 0;
        int tax2 = 0;
        int tax3 = 0;
        int tax4 = 0;
        String text = "";
        SalesReceipt instance = null;
        instance.printDiscount(amount, tax1, tax2, tax3, tax4, text);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
        */
    }

    /**
     * Test of printCharge method, of class SalesReceipt.
     */
    public void testPrintCharge() throws Exception 
    {
        /*
        System.out.println("printCharge");
        long amount = 0L;
        int tax1 = 0;
        int tax2 = 0;
        int tax3 = 0;
        int tax4 = 0;
        String text = "";
        SalesReceipt instance = null;
        instance.printCharge(amount, tax1, tax2, tax3, tax4, text);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
        */
    }

    /**
     * Test of printRecItemAdjustment method, of class SalesReceipt.
     */
    public void testPrintRecItemAdjustment() throws Exception {
        System.out.println("printRecItemAdjustment");
        int adjustmentType = 0;
        String description = "";
        long amount = 0L;
        int vatInfo = 0;
        SalesReceipt instance = null;
        instance.printRecItemAdjustment(adjustmentType, description, amount, vatInfo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecSubtotalAdjustment method, of class SalesReceipt.
     */
    public void testPrintRecSubtotalAdjustment() throws Exception {
        System.out.println("printRecSubtotalAdjustment");
        int adjustmentType = 0;
        String description = "";
        long amount = 0L;
        SalesReceipt instance = null;
        instance.printRecSubtotalAdjustment(adjustmentType, description, amount);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecVoidItem method, of class SalesReceipt.
     */
    public void testPrintRecVoidItem() throws Exception {
        System.out.println("printRecVoidItem");
        String description = "";
        long amount = 0L;
        int quantity = 0;
        int adjustmentType = 0;
        long adjustment = 0L;
        int vatInfo = 0;
        SalesReceipt instance = null;
        instance.printRecVoidItem(description, amount, quantity, adjustmentType, adjustment, vatInfo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkAdjustment method, of class SalesReceipt.
     */
    public void testCheckAdjustment() throws Exception {
        System.out.println("checkAdjustment");
        int adjustmentType = 0;
        long amount = 0L;
        SalesReceipt instance = null;
        instance.checkAdjustment(adjustmentType, amount);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecPackageAdjustment method, of class SalesReceipt.
     */
    public void testPrintRecPackageAdjustment() throws Exception {
        System.out.println("printRecPackageAdjustment");
        int adjustmentType = 0;
        String description = "";
        String vatAdjustment = "";
        SalesReceipt instance = null;
        instance.printRecPackageAdjustment(adjustmentType, description, vatAdjustment);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecPackageAdjustVoid method, of class SalesReceipt.
     */
    public void testPrintRecPackageAdjustVoid() throws Exception {
        System.out.println("printRecPackageAdjustVoid");
        int adjustmentType = 0;
        String vatAdjustment = "";
        SalesReceipt instance = null;
        instance.printRecPackageAdjustVoid(adjustmentType, vatAdjustment);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecRefundVoid method, of class SalesReceipt.
     */
    public void testPrintRecRefundVoid() throws Exception {
        System.out.println("printRecRefundVoid");
        String description = "";
        long amount = 0L;
        int vatInfo = 0;
        SalesReceipt instance = null;
        instance.printRecRefundVoid(description, amount, vatInfo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecSubtotalAdjustVoid method, of class SalesReceipt.
     */
    public void testPrintRecSubtotalAdjustVoid() throws Exception {
        System.out.println("printRecSubtotalAdjustVoid");
        int adjustmentType = 0;
        long amount = 0L;
        SalesReceipt instance = null;
        instance.printRecSubtotalAdjustVoid(adjustmentType, amount);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecItemAdjustmentVoid method, of class SalesReceipt.
     */
    public void testPrintRecItemAdjustmentVoid() throws Exception {
        System.out.println("printRecItemAdjustmentVoid");
        int adjustmentType = 0;
        String description = "";
        long amount = 0L;
        int vatInfo = 0;
        SalesReceipt instance = null;
        instance.printRecItemAdjustmentVoid(adjustmentType, description, amount, vatInfo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecItemRefund method, of class SalesReceipt.
     */
    public void testPrintRecItemRefund() throws Exception {
        System.out.println("printRecItemRefund");
        String description = "";
        long amount = 0L;
        int quantity = 0;
        int vatInfo = 0;
        long unitAmount = 0L;
        String unitName = "";
        SalesReceipt instance = null;
        instance.printRecItemRefund(description, amount, quantity, vatInfo, unitAmount, unitName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecItemRefundVoid method, of class SalesReceipt.
     */
    public void testPrintRecItemRefundVoid() throws Exception {
        System.out.println("printRecItemRefundVoid");
        String description = "";
        long amount = 0L;
        int quantity = 0;
        int vatInfo = 0;
        long unitAmount = 0L;
        String unitName = "";
        SalesReceipt instance = null;
        instance.printRecItemRefundVoid(description, amount, quantity, vatInfo, unitAmount, unitName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printRecVoid method, of class SalesReceipt.
     */
    public void testPrintRecVoid() throws Exception {
        System.out.println("printRecVoid");
        String description = "";
        SalesReceipt instance = null;
        instance.printRecVoid(description);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPayed method, of class SalesReceipt.
     */
    public void testIsPayed() {
        System.out.println("isPayed");
        SalesReceipt instance = null;
        boolean expResult = false;
        boolean result = instance.isPayed();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReceipt method, of class SalesReceipt.
     */
    public void testGetReceipt() {
        System.out.println("getReceipt");
        SalesReceipt instance = null;
        PrinterReceipt expResult = null;
        PrinterReceipt result = instance.getReceipt();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
