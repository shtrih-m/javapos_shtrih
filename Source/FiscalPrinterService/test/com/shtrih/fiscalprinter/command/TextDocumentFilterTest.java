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

    private final TextDocumentFilter filter = new TextDocumentFilter();
    private final FptrParameters params = new FptrParameters();
    private final SMFiscalPrinterNull printer = new SMFiscalPrinterNull(params);
    
    
    @Test
    public void should_serialize_request() throws Exception {
        assertEquals("0.123", filter.quantityToStr(0.123));
        assertEquals("0.123456", filter.quantityToStr(0.123456));
    }

    @Test
    public void test_cashin_command() throws Exception 
    {
        PrintCashIn command = new PrintCashIn();
        command.setReceiptNumber(123);
        command.setAmount(12345);
        printer.getOperationRegisters().put(new Integer(155), new Integer(1234));
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
        printer.getOperationRegisters().put(new Integer(156), new Integer(1234));
        assertEquals(0, filter.getLines().size());
                
        filter.init(printer);
        filter.setEnabled(true);
        filter.afterCommand(command);
        assertEquals(2, filter.getLines().size());
        assertEquals("ВЫПЛАТА                         1234", filter.getLines().get(0));
        assertEquals("                              123.45", filter.getLines().get(1));
    }
    
    @Test
    public void test_print_sale_command() throws Exception 
    {
        PriceItem item = new PriceItem();
        item.setDepartment(1);
        item.setPaymentType(0);
        item.setPrice(123456);
        item.setQuantity(1);
        item.setSubjectType(0);
        item.setTax1(1);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText("Item 1");
        PrintSale command = new PrintSale(30, item);
        printer.getOperationRegisters().put(new Integer(156), new Integer(1234));
        assertEquals(0, filter.getLines().size());
                
        filter.init(printer);
        filter.setEnabled(true);
        filter.afterCommand(command);
        assertEquals(3, filter.getLines().size());
        assertEquals("                               №0001", filter.getLines().get(0));
        assertEquals("Item 1                              ", filter.getLines().get(1));
        assertEquals("01                        =1234.56_А", filter.getLines().get(2));
    }
    
    @Test
    public void test_print_refund_command() throws Exception 
    {
        PriceItem item = new PriceItem();
        item.setDepartment(1);
        item.setPaymentType(0);
        item.setPrice(123456);
        item.setQuantity(1);
        item.setSubjectType(0);
        item.setTax1(1);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText("Item 1");
        PrintRefund command = new PrintRefund(30, item);
        printer.getOperationRegisters().put(new Integer(156), new Integer(1234));
        assertEquals(0, filter.getLines().size());
                
        filter.init(printer);
        filter.setEnabled(true);
        filter.afterCommand(command);
        assertEquals(3, filter.getLines().size());
        assertEquals("                               №0001", filter.getLines().get(0));
        assertEquals("Item 1                              ", filter.getLines().get(1));
        assertEquals("01                        =1234.56_А", filter.getLines().get(2));
    }
    
    @Test
    public void test_print_void_sale_command() throws Exception 
    {
        PriceItem item = new PriceItem();
        item.setDepartment(1);
        item.setPaymentType(0);
        item.setPrice(123456);
        item.setQuantity(1);
        item.setSubjectType(0);
        item.setTax1(1);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText("Item 1");
        PrintVoidSale command = new PrintVoidSale(30, item);
        printer.getOperationRegisters().put(new Integer(156), new Integer(1234));
        assertEquals(0, filter.getLines().size());
                
        filter.init(printer);
        filter.setEnabled(true);
        filter.afterCommand(command);
        assertEquals(3, filter.getLines().size());
        assertEquals("                               №0001", filter.getLines().get(0));
        assertEquals("Item 1                              ", filter.getLines().get(1));
        assertEquals("01                        =1234.56_А", filter.getLines().get(2));
    }
    
    @Test
    public void test_print_void_refund_command() throws Exception 
    {
        PriceItem item = new PriceItem();
        item.setDepartment(1);
        item.setPaymentType(0);
        item.setPrice(123456);
        item.setQuantity(1);
        item.setSubjectType(0);
        item.setTax1(1);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText("Item 1");
        PrintVoidRefund command = new PrintVoidRefund(30, item);
        printer.getOperationRegisters().put(new Integer(156), new Integer(1234));
        assertEquals(0, filter.getLines().size());
                
        filter.init(printer);
        filter.setEnabled(true);
        filter.afterCommand(command);
        assertEquals(3, filter.getLines().size());
        assertEquals("                               №0001", filter.getLines().get(0));
        assertEquals("Item 1                              ", filter.getLines().get(1));
        assertEquals("01                        =1234.56_А", filter.getLines().get(2));
    }
    
    
    @Test
    public void test_print_sale_command2() throws Exception 
    {
        PriceItem item = new PriceItem();
        item.setDepartment(1);
        item.setPaymentType(0);
        item.setPrice(123456);
        item.setQuantity(1.234);
        item.setSubjectType(0);
        item.setTax1(1);
        item.setTax2(0);
        item.setTax3(0);
        item.setTax4(0);
        item.setText("Item 1");
        PrintSale command = new PrintSale(30, item);
        printer.getOperationRegisters().put(new Integer(156), new Integer(1234));
        assertEquals(0, filter.getLines().size());
                
        filter.init(printer);
        filter.setEnabled(true);
        filter.afterCommand(command);
        assertEquals(4, filter.getLines().size());
        assertEquals("                               №0001", filter.getLines().get(0));
        assertEquals("Item 1                              ", filter.getLines().get(1));
        assertEquals("                     1.234 X 1234.56", filter.getLines().get(2));
        assertEquals("01                        =1523.45_А", filter.getLines().get(3));
    }
    
    @Test
    public void test_void_receipt_command() throws Exception 
    {
        VoidFiscalReceipt command = new VoidFiscalReceipt();
        assertEquals(0, filter.getLines().size());
                
        filter.init(printer);
        filter.setEnabled(true);
        filter.afterCommand(command);
        assertEquals(1, filter.getLines().size());
        assertEquals("ЧЕК АННУЛИРОВАН                     ", filter.getLines().get(0));
    }

        
}

