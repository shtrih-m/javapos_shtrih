/*
 * DIOPrintBarcode.java
 *
 * Created on 11 November 2009, 16:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;

public final class DIOPrintBarcode {

    private final FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOPrintBarcode
     */
    public DIOPrintBarcode(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkObjectMinLength((String[]) object, 1);

        String[] stringParams = (String[]) object;
        String barcodeData = stringParams[0];

        String barcodeLabel = "";
        if (stringParams.length > 1) {
            barcodeLabel = stringParams[1];
        }

        int barcodeType = SmFptrConst.SMFPTR_BARCODE_EAN13;
        int barcodeHeight = 100;
        int barcodePrintType = SmFptrConst.SMFPTR_PRINTTYPE_DRIVER;
        int barcodeBarWidth = 2;
        int barcodeTextPosition = SmFptrConst.SMFPTR_TEXTPOS_BELOW;
        int barcodeTextFont = 1;
        int barcodeAspectRatio = 3;

        if (data != null) {
            // barcode type
            if (data.length > 0) {
                barcodeType = data[0];
            }

            // barcode height, pixels
            if (data.length > 1) {
                barcodeHeight = data[1];
            }

            // print type
            if (data.length > 2) {
                barcodePrintType = data[2];
            }

            // barcode bar width, pixels
            if (data.length > 3) {
                barcodeBarWidth = data[3];
            }

            // text position
            if (data.length > 4) {
                barcodeTextPosition = data[4];
            }

            // text font
            if (data.length > 5) {
                barcodeTextFont = data[5];
            }

            // aspect ratio
            if (data.length > 6) {
                barcodeAspectRatio = data[6];
            }
        }

        PrinterBarcode bc = new PrinterBarcode();
        bc.setText(barcodeData);
        bc.setLabel(barcodeLabel);
        bc.setType(barcodeType);
        bc.setHeight(barcodeHeight);
        bc.setPrintType(barcodePrintType);
        bc.setBarWidth(barcodeBarWidth);
        bc.setTextPosition(barcodeTextPosition);
        bc.setTextFont(barcodeTextFont);
        bc.setAspectRatio(barcodeAspectRatio);
        service.printBarcode(bc);
    }
}
