package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.SMFiscalPrinterNull;
import com.shtrih.jpos.fiscalprinter.FptrParameters;


/**
 * @author P.Zhirkov
 */
public class TextDocumentFilterTest {

    @Test
    public void should_serialize_request() throws Exception {
        TextDocumentFilter item = new TextDocumentFilter();
        assertEquals("0.123", item.quantityToStr(0.123));
        assertEquals("0.123456", item.quantityToStr(0.123456));
    }

    @Test
    public void test_cashin_command() throws Exception 
    {
        PrintCashIn command = new PrintCashIn();
        command.setReceiptNumber(123);
        command.setAmount(12345);
        FptrParameters params = new FptrParameters();
        SMFiscalPrinterNull printer = new SMFiscalPrinterNull(params);
        printer.getOperationRegisters().put(new Integer(155), new Integer(1234));
        TextDocumentFilter filter = new TextDocumentFilter();
        assertEquals(0, filter.getLines().size());
                
        filter.init(printer);
        filter.setEnabled(true);
        filter.afterCommand(command);
        assertEquals(2, filter.getLines().size());
        assertEquals("ВНЕСЕНИЕ                        1234", filter.getLines().get(0));
        assertEquals("                              123.45", filter.getLines().get(1));
    }
    
    @Test
    public void test_cashout_command() throws Exception 
    {
        PrintCashOut command = new PrintCashOut();
        command.setReceiptNumber(123);
        command.setAmount(12345);
        FptrParameters params = new FptrParameters();
        SMFiscalPrinterNull printer = new SMFiscalPrinterNull(params);
        printer.getOperationRegisters().put(new Integer(156), new Integer(1234));
        TextDocumentFilter filter = new TextDocumentFilter();
        assertEquals(0, filter.getLines().size());
                
        filter.init(printer);
        filter.setEnabled(true);
        filter.afterCommand(command);
        assertEquals(2, filter.getLines().size());
        assertEquals("ВЫПЛАТА                        1234", filter.getLines().get(0));
        assertEquals("                             123.45", filter.getLines().get(1));
    }
}

