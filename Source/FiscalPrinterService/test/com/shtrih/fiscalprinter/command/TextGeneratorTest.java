/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.SMFiscalPrinterNull;
import com.shtrih.jpos.fiscalprinter.receipt.FSTLVItem;
import com.shtrih.jpos.fiscalprinter.receipt.FSSalesReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.ReceiptContext;


/**
 *
 * @author Виталий
 */
public class TextGeneratorTest {
    
    public TextGeneratorTest() {
    }

    /**
     * Test of visitSalesReceipt method, of class TextGenerator.
     */
    @Test
    public void testVisitSalesReceipt() {
        System.out.println("visitSalesReceipt");
        /*
        ReceiptContext context = new ReceiptContext();
        FSSalesReceipt receipt = new FSSalesReceipt(context, PrinterConst.SMFP_RECTYPE_SALE);
        SMFiscalPrinter printer = new SMFiscalPrinterNull(null, null, null);
        TextGenerator instance = new TextGenerator(printer);
        instance.visitSalesReceipt(receipt);
        */
    }

}
