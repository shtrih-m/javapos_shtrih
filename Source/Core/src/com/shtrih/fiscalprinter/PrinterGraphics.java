package com.shtrih.fiscalprinter;

import com.shtrih.jpos.fiscalprinter.PrintItem;

/**
 * @author P.Zhirkov
 */
public class PrinterGraphics implements PrintItem {

    private final byte[][] data;

    public PrinterGraphics(byte[][] data) {
        this.data = data;
    }

    public void print(SMFiscalPrinter printer) throws Exception {
        int startPos = printer.loadRawGraphics(data);
        int endPos = startPos + data.length;

        printer.printGraphics(startPos, endPos);
    }
}
