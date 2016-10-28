
import jpos.JposException;
import jpos.FiscalPrinterConst;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author V.Kravtsov
 */
public class AdidasReceiptTest {

    private ShtrihFiscalPrinter printer;

    public AdidasReceiptTest() {
    }

    public void open()
            throws Exception {
        printer = new ShtrihFiscalPrinter("");
        printer.open("StringFptr");
        printer.setPowerNotify(printer.JPOS_PN_ENABLED);
        printer.claim(1000);
        printer.setDeviceEnabled(true);
        printer.setFreezeEvents(true);
        printer.setCheckTotal(false);
        printer.setAdditionalHeader("Additional header");
        printer.setAdditionalTrailer("Additional trailer");
    }

    public void loadImages()
            throws Exception {
        printer.clearLogo();
        printer.clearImages();
        printer.loadImage("AdidasLogo.bmp");    // 0
        printer.loadImage("AdidasQRCode.bmp");  // 1
        printer.addLogo(0, SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
        printer.addLogo(1, SmFptrConst.SMFPTR_LOGO_AFTER_ADDTRAILER);
    }

    public void printReceipt()
            throws Exception {
        printer.setFreezeEvents(true);
        printer.setCheckTotal(false);
        printer.setAdditionalHeader("");
        printer.setAdditionalTrailer("");
        // Header
        printer.setNumHeaderLines(3);
        printer.setHeaderLine(1, "             Eurosoftware Store", false);
        printer.setHeaderLine(2, "        321 00, Pilsen. Radcicka 40", false);
        printer.setHeaderLine(3, "             Тел. *123 456 789 ЧЕК", false);

        printer.setNumTrailerLines(0);

        printer.setFiscalReceiptType(FiscalPrinterConst.FPTR_RT_SALES);
        printer.beginFiscalReceipt(true);
        printer.printRecItem("1 004411/0G Carabiner Hook Orig Multi Co", 200, 0, 0, 0, "");
        printer.printRecSubtotal(200);
        printer.printRecTotal(200, 1000, "0");

        printer.printRecMessage("   Благодарим за покупку! Посетите наш сайт");
        printer.printRecMessage("   adidas.ru. Напоминаем, что обмен товара ");
        printer.printRecMessage("надлежащего качества осуществляется в течение 30");
        printer.printRecMessage("дней. Гарантийный срок на все товары составляет");
        printer.printRecMessage("           30 дней. Ждем Вас снова!");

        printer.printBarcode(
                "0015555290114101000010",
                "0015555290114101000010",
                SmFptrConst.SMFPTR_BARCODE_CODE128,
                100,
                SmFptrConst.SMFPTR_PRINTTYPE_DRIVER,
                2,
                SmFptrConst.SMFPTR_TEXTPOS_BELOW,
                1, 2);

        printer.printRecMessage("Касса=101                    Kaccир: Admin Admin");
        printer.printRecMessage("№ чека: 479");

        printer.endFiscalReceipt(true);

    }

    public static void main(String args[]) {
        try 
        {
            AdidasReceiptTest test = new AdidasReceiptTest();
            test.open();
            test.loadImages();
            test.printReceipt();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
