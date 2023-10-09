/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import java.util.List;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.SMFiscalPrinterNull;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.jpos.fiscalprinter.receipt.FSTLVItem;
import com.shtrih.jpos.fiscalprinter.receipt.FSSalesReceipt;
import com.shtrih.fiscalprinter.table.PrinterField;
import com.shtrih.fiscalprinter.table.PrinterFields;
import com.shtrih.fiscalprinter.table.PrinterTable;
import com.shtrih.fiscalprinter.table.PrinterTables;
import com.shtrih.jpos.fiscalprinter.receipt.CashInReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.CashOutReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.NonfiscalReceipt;


/**
 *
 * @author Виталий
 */
public class TextGeneratorTest {
    
    private SMFiscalPrinterNull printer;
            
    public TextGeneratorTest() {
    }
    
    @Before
    public void setUp() 
    {
        PrinterTable table;
        PrinterField field;
        FieldInfo fieldInfo;

        FptrParameters params = new FptrParameters();
        printer = new SMFiscalPrinterNull(null, null, params);

        printer.getLongStatus().setSerialNumber(12345);
        printer.getLongStatus().setDocumentNumber(1233);
        printer.getLongStatus().setFiscalID(9876);

        //                 
        table = new PrinterTable(PrinterConst.SMFP_TABLE_PAYTYPE, "Наименование типов оплаты", 16, 1);
        fieldInfo = new FieldInfo(PrinterConst.SMFP_TABLE_PAYTYPE, 1, 64, 1, 0, 99999999, "Запрограммированные типы оплаты");
        table.getFields().add(new PrinterField(fieldInfo, 1, "НАЛИЧНЫМИ"));
        table.getFields().add(new PrinterField(fieldInfo, 2, "CREDIT"));
        table.getFields().add(new PrinterField(fieldInfo, 3, "КАРТА"));
        table.getFields().add(new PrinterField(fieldInfo, 4, "СКИДКА"));

        table = new PrinterTable(PrinterConst.SMFP_TABLE_CASHIER, "Наименования операторов", 10, 2);
        fieldInfo = new FieldInfo(2, 1, 4, 0, 0, 99999999, "Пароли кассиров");
        for (int i=1;i<31;i++)
        {
            field = new PrinterField(fieldInfo, i);
            field.setValue(String.valueOf(i));
            table.getFields().add(field);
        }
        fieldInfo = new FieldInfo(2, 2, 64, 1, 0, 99999999, "Должности и фамилии кассиров");
        for (int i=1;i<31;i++)
        {
            field = new PrinterField(fieldInfo, i);
            field.setValue("Кассир " + i);
            table.getFields().add(field);
        }
        printer.tables.add(table);
    }

            
    /**
     * Test of visitSalesReceipt method, of class TextGenerator.
     */
    @Test
    public void testVisitSalesReceipt() {
        System.out.println("visitSalesReceipt");
        try{
            FSSalesReceipt receipt = new FSSalesReceipt(printer, PrinterConst.SMFP_RECTYPE_SALE);
            TextGenerator instance = new TextGenerator(printer);
            
            receipt.beginFiscalReceipt(true);
            receipt.printRecItem("Приём платежа", 12300, 0, 0, 0, "");
            receipt.printRecItem("Размер вознаграждения", 123, 0, 3, 0, "");
            receipt.printRecTotal(12423, 12423, 0, "0");
            receipt.endFiscalReceipt(false);
            
            assertEquals("size != 0", 0, instance.getLines().size());
            instance.visitSalesReceipt(receipt);
            assertEquals(10, instance.getLines().size());
            assertEquals("ККМ 12345             ИНН 9876 №1234", instance.getLines().get(0));
            assertEquals("01.02.2003 01:02            Кассир 1", instance.getLines().get(1));
            assertEquals("Приём платежа", instance.getLines().get(2));
            assertEquals("                      1.000 X 123.00", instance.getLines().get(3));
            assertEquals("01                         =123.00_Г", instance.getLines().get(4));
            assertEquals("Размер вознаграждения", instance.getLines().get(5));
            assertEquals("                        1.000 X 1.23", instance.getLines().get(6));
            assertEquals("01                           =1.23_В", instance.getLines().get(7));
            assertEquals("ИТОГ                         =124.23", instance.getLines().get(8));
            assertEquals("                             =124.23", instance.getLines().get(9));
            
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }
    
    /**
     * Test of visitCashInReceipt method, of class TextGenerator.
     */
    @Test
    public void testVisitCashInReceipt() {
        System.out.println("visitCashInReceipt");
        try{
            CashInReceipt receipt = new CashInReceipt(printer);
            TextGenerator instance = new TextGenerator(printer);
            
            receipt.printRecCash(12300);
            receipt.printRecTotal(12423, 12423, 0, "0");
            receipt.endFiscalReceipt(false);
            
            assertEquals("size != 0", 0, instance.getLines().size());
            instance.visitCashInReceipt(receipt);
            assertEquals(4, instance.getLines().size());
            assertEquals("ККМ 12345             ИНН 9876 №1234", instance.getLines().get(0));
            assertEquals("01.02.2003 01:02            Кассир 1", instance.getLines().get(1));
            assertEquals("ВНЕСЕНИЕ                      123.00", instance.getLines().get(2));
            assertEquals("СДАЧА                          =1.23", instance.getLines().get(3));
            
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }
    
    /**
     * Test of visitCashInReceipt method, of class TextGenerator.
     */
    @Test
    public void testVisitCashOutReceipt() {
        System.out.println("visitCashOutReceipt");
        try{
            CashOutReceipt receipt = new CashOutReceipt(printer);
            TextGenerator instance = new TextGenerator(printer);
            
            receipt.printRecCash(12300);
            receipt.printRecTotal(12423, 12423, 0, "0");
            receipt.endFiscalReceipt(false);
            
            assertEquals("size != 0", 0, instance.getLines().size());
            instance.visitCashOutReceipt(receipt);
            assertEquals(4, instance.getLines().size());
            assertEquals("ККМ 12345             ИНН 9876 №1234", instance.getLines().get(0));
            assertEquals("01.02.2003 01:02            Кассир 1", instance.getLines().get(1));
            assertEquals("ВЫПЛАТА                       123.00", instance.getLines().get(2));
            assertEquals("СДАЧА                          =1.23", instance.getLines().get(3));
            
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }
    
    /**
     * Test of visitNonfiscalReceipt method, of class TextGenerator.
     */
    @Test
    public void testVisitNonfiscalReceipt() {
        System.out.println("testVisitNonfiscalReceipt");
        try{
            NonfiscalReceipt receipt = new NonfiscalReceipt(printer);
            TextGenerator instance = new TextGenerator(printer);
            
            receipt.printNormal(2, "Line 1");
            receipt.printNormal(2, "Line 2");
            receipt.printNormal(2, "Line 3");
            
            assertEquals("size != 0", 0, instance.getLines().size());
            instance.visitNonfiscalReceipt(receipt);
            assertEquals(5, instance.getLines().size());
            
            assertEquals("ККМ 12345             ИНН 9876 №1234", instance.getLines().get(0));
            assertEquals("01.02.2003 01:02            Кассир 1", instance.getLines().get(1));
            assertEquals("Line 1", instance.getLines().get(2));
            assertEquals("Line 2", instance.getLines().get(3));
            assertEquals("Line 3", instance.getLines().get(4));
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }
}
