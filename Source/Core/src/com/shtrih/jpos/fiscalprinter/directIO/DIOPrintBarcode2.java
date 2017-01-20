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

public final class DIOPrintBarcode2 {

    private final FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOPrintBarcode
     */
    public DIOPrintBarcode2(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        Object[] params = (Object[]) object;
        //DIOUtils.checkObjectMinLength((String[]) object, 1);
        String barcodeData = (String) params[0];

        String barcodeLabel = "";
        if (params.length > 1) {
            barcodeLabel = (String) params[1];
        }

        int barcodeType = SmFptrConst.SMFPTR_BARCODE_EAN13;
        int barcodeHeight = 100;
        int barcodePrintType = SmFptrConst.SMFPTR_PRINTTYPE_AUTO;
        int barcodeBarWidth = 2;
        int barcodeTextPosition = SmFptrConst.SMFPTR_TEXTPOS_BELOW;
        int barcodeTextFont = 1;
        int barcodeAspectRatio = 3;

        // barcode type
        if (params.length > 2) {
            barcodeType = ((Integer)params[2]).intValue();
        }

        // barcode height, pixels
        if (params.length > 3) {
            barcodeHeight = ((Integer)params[3]).intValue();
        }

        // print type
        if (params.length > 4) {
            barcodePrintType = ((Integer)params[4]).intValue();;
        }

        // barcode bar width, pixels
        if (params.length > 5) {
            barcodeBarWidth = ((Integer)params[5]).intValue();;
        }

        // text position
        if (params.length > 6) {
            barcodeTextPosition = ((Integer)params[6]).intValue();;
        }

        // text font
        if (params.length > 7) {
            barcodeTextFont = ((Integer)params[7]).intValue();;
        }

        // aspect ratio
        if (params.length > 8) {
            barcodeAspectRatio = ((Integer)params[8]).intValue();;
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
