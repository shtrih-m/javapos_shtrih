/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadMaxGraphics {

    private int value1 = 1;
    private int value2 = 65535;
    private int commands = 0;
    private FiscalPrinterImpl service;
    public static CompositeLogger logger = CompositeLogger.getLogger(DIOReadMaxGraphics.class);

    /**
     * Creates a new instance of DIOPrintBarcode
     */
    public DIOReadMaxGraphics(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {

        DIOUtils.checkDataMinLength(data, 1);

        

        value1 = 1;
        value2 = 65535;
        if (!validBound(value2)) {
            detectBounds();
        }
        data[0] = value2;
        logger.debug("Read max graphics. commands: " + String.valueOf(commands));
    }

    private void detectBounds() throws Exception {
        if ((value2 - value1) <= 1) {
            value2 = value2 - 1;
            return;
        }
        int value3 = value1 + ((value2 - value1) / 2);
        if (validBound(value3)) {
            value1 = value3;
        } else {
            value2 = value3;
        }
        detectBounds();
    }

    private boolean validBound(int value) throws Exception {
        SMFiscalPrinter printer = service.getPrinter();
        int result = printer.printGraphics2(value - 1, value);
        commands++;
        if (result == 0) {
            return true;
        }
        if (result == PrinterConst.SMFP_EFPTR_INVALID_PARAMETER) {
            return false;
        }
        printer.check(result);
        return false;
    }
}
