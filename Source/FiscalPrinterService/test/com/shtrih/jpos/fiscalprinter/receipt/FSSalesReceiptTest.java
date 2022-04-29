/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.PrinterGraphics;
import com.shtrih.fiscalprinter.PrinterProtocol;
import com.shtrih.fiscalprinter.ProtocolFactory;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.SMFiscalPrinterImpl;
import com.shtrih.fiscalprinter.SMFiscalPrinterNull;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.fiscalprinter.port.PrinterPortFactory;
import com.shtrih.fiscalprinter.receipt.PrinterReceipt;
import com.shtrih.jpos.fiscalprinter.FiscalDay;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterState;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import java.util.List;
import java.util.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Виталий
 */
public class FSSalesReceiptTest {
    
    public FSSalesReceiptTest() {
    }

    private FSSalesReceipt createReceipt() throws Exception
    {
        FptrParameters params = new FptrParameters();
        params.quantityFactor = 1000.0;
        params.amountFactor = 1.0;
        PrinterPort port = PrinterPortFactory.createInstance(params);
        PrinterProtocol device = ProtocolFactory.getProtocol(params, port);
        SMFiscalPrinter printer = new SMFiscalPrinterNull(port, device, params);
        ReceiptPrinter receiptPrinter = new ReceiptPrinterImpl(printer, params);
        FiscalDay fiscalDay = new FiscalDay();
        PrinterReceipt printerReceipt = new PrinterReceipt();
        FiscalPrinterState printerState = new FiscalPrinterState();
        FiscalPrinterImpl impl = new FiscalPrinterImpl();
        ReceiptContext context = new ReceiptContext(
                receiptPrinter, params, fiscalDay,
                printerReceipt, printerState, impl);
        
        return new FSSalesReceipt(context, PrinterConst.SMFP_RECTYPE_SALE);
    }

    /**
     * Test of applyDiscounts method, of class FSSalesReceipt.
     */
    @Test
    public void testApplyDiscounts() throws Exception {
        System.out.println("applyDiscounts");
        
        FSSalesReceipt receipt = createReceipt();
        receipt.beginFiscalReceipt(true);
        receipt.printRecItem("Русский Дар Квас 2л ПЭТ 6Х", 47697, 1.0, 1, 47697, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 1);
        receipt.printRecItem("Драйв Ми Яблоко Карамбола 0.449л БАН 6Х", 3868480, 160.0, 1, 24178, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 64, 1);
        receipt.printRecItem("Драйв Ми Ориджинал 0.449л БАН 12X", 3868480, 80.0, 1, 48356, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 64, 1);
        
        receipt.printRecTotal(7784528, 7784528, 0, "0");
        assertEquals(7784528, receipt.getSubtotal());
        
        assertEquals(true, receipt.isPayed());
        assertEquals(5, receipt.items.size());
    }
    
    /**
     * Test of applyDiscounts2 method, of class FSSalesReceipt.
     */
    @Test
    public void testApplyDiscounts2() throws Exception 
    {
        System.out.println("applyDiscounts2");
    
        FSSalesReceipt receipt = createReceipt();
        receipt.beginFiscalReceipt(true);
        receipt.printRecItem("Липтон Чай Хол Зеленый 0.5л ПЭТ 12Х", 819510, 15, 1, 54634, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 6, 1);
        receipt.printRecItem("Драйв Ми Нитро Буст 0.449л БАН 6Х", 898400, 40, 1, 22460, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 32, 1);
        receipt.printRecItem("Драйв Ми Яблоко Карамбола 0.449л БАН 6Х", 898400, 40, 1, 22460, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 32, 1);
        receipt.printRecItem("Любимый Нек Ябл Осв 1.93л СЛ 6ХДП СТДНР", 368705, 5, 2, 73741, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 2);
        receipt.printRecItem("Любимый Нап ЦитрусМикс 1.93л СЛ 6Х ДПСТД", 221223, 3, 2, 73741, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 2);
        receipt.printRecItem("Любимый Нап ВинДуэт 1.93л СЛ 6Х ДПСТД НР", 368705, 5, 2, 73741, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 2);
        receipt.printRecItem("ЛюбимыйНапЯблВишЧереш 1.93л СЛ 6ХДПСТДНР", 368705, 5, 2, 73741, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 2);
        receipt.printRecItem("Любимый Нап ТропМикОбг 1.93лСЛ 6ХДПСТДНР", 368705, 5, 2, 73741, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 2);
        receipt.printRecItem("Любимый Нап АпМангоМанд 1.93 СЛ 6ХДП СТД", 368705, 5.0, 2, 73741, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 2);
        receipt.printRecItem("ЛюбимыйНапЯблГранЧРяб 1.93л СЛ 6ХДПСТДНР", 221223, 3.0, 2, 73741, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 2);
        receipt.printRecItem("Любимый НапЯбЧРябКлубЗем1.93лСЛ6ХДПСТДНР", 368705, 5.0, 2, 73741, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 2);
        receipt.printRecItem("Любимый Нек Ябл Осв 0.95л СЛ 12Х ДПНР", 345590, 5.0, 2, 69118, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 3, 2);
        receipt.printRecItem("Любимый Нек Ябл Перс Нект 0.95лСЛ12ХДПНР", 345590, 5.0, 2, 69118, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 3, 2);
        receipt.printRecItem("Любимый Нек ТоматМорскСоль 0.95л СЛ12ХДП", 345590, 5.0, 2, 69118, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 3, 2);
        receipt.printRecItem("Любимый Нап ЦитрусМикс 0.95л СЛ 12Х ДП", 207354, 3.0, 2, 69118, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 2, 2);
        receipt.printRecItem("Любимый Нап ВинДуэт 0.95л СЛ 12Х ДП НР", 345590, 5.0, 2, 69118, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 3, 2);
        receipt.printRecItem("Любимый Нап Ябл АбрГруша0.95лСЛ12Х ДП НР", 207354, 3.0, 2, 69118, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 2, 2);
        receipt.printRecItem("Любим Нап ЯблБанГрушКиви 0.95лСЛ12ХДПНД", 207354, 3.0, 2, 69118, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 2, 2);
        receipt.printRecItem("Любимый Нап ТропМикс Обг 0.95л СЛ12ХДПНР", 345590, 5.0, 2, 69118, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 3, 2);
        receipt.printRecItem("Любимый Нап АпМангоМанд 0.95л СЛ 12Х ДП", 345590, 5.0, 2, 69118, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 3, 2);
        receipt.printRecItem("Любимый Нап ЯблКлубЧРяб 0.95л СЛ 12ХДПНР", 345590, 5.0, 2, 69118, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 3, 2);
        receipt.printRecItem("Любимый Нек Ябл Осв 0.485л СЛ 24Х ДП НР", 192548, 2.0, 2, 96274, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 2, 2);
        receipt.printRecTotal(8504619, 8504619, 0, "0");
        
        assertEquals(0, receipt.discounts.getTotal());
        assertEquals(8504619, receipt.getSubtotal());
        assertEquals(true, receipt.isPayed());
    }
    
    /**
     * Test of applyDiscounts3 method, of class FSSalesReceipt.
     */
    @Test
    public void testApplyDiscounts3() throws Exception 
    {
        System.out.println("applyDiscounts2");
    
        FSSalesReceipt receipt = createReceipt();
        receipt.beginFiscalReceipt(true);
        receipt.printRecItem("Аква Минер Лимон Без Газ 0.5л ПЭТ 12Х", 45068, 1.0, 1, 45068, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 1);
        receipt.printRecItem("Аква Минер Мята Лайм Без Газ 0.5л ПЭТ12Х", 45068, 1.0, 1, 45068, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 1);
        receipt.printRecItem("Аква Минер Яблоко Газ 0.5л ПЭТ 12Х", 45068, 1.0, 1, 45068, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 1);
        receipt.printRecItem("Аква Минер Акт Малина 0.5л ПЭТ 12Х", 45068, 1.0, 1, 45068, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 1);
        receipt.printRecItem("Аква Минер Акт Цитрус 0.5л ПЭТ 12Х", 45068, 1.0, 1, 45068, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 1);
        receipt.printRecItem("Адреналин Раш 0.449Л БАН 12Х", 334761, 3.0, 1, 111587, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 1);
        receipt.printRecItem("Пепси Кола 0.33л БАН 12Х", 46333, 1.0, 1, 46333, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 1);
        receipt.printRecItem("Адреналин Раш 0.25л БАН 12Х", 141530, 2.0, 1, 70765, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 2, 1);
        receipt.printRecTotal(747955, 747955, 0, "0");
        
        assertEquals(0, receipt.discounts.getTotal());
        assertEquals(747955, receipt.getSubtotal());
        assertEquals(true, receipt.isPayed());
    }

    /**
     * Test of applyDiscounts4 method, of class FSSalesReceipt.
     */
    @Test
    public void testApplyDiscounts4() throws Exception 
    {
        System.out.println("applyDiscounts2");
    
        FSSalesReceipt receipt = createReceipt();
        receipt.beginFiscalReceipt(true);
        receipt.printRecItem("Адреналин Раш 0.449Л БАН 12Х", 334761, 3.0, 1, 111587, "УПК");
        receipt.printRecItemAdjustment(1, "Discount", 1, 1);
        receipt.printRecTotal(334760, 334760, 0, "0");
        
        assertEquals(0, receipt.discounts.getTotal());
        assertEquals(334760, receipt.getSubtotal());
        assertEquals(true, receipt.isPayed());
    }
    
    
}
