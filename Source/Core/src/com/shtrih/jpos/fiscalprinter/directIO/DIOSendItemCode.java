/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.jpos.DIOUtils;
import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.ItemCode;


public class DIOSendItemCode extends DIOItem {

    public DIOSendItemCode(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        String[] lines = (String[]) object;
        String barcode = lines[0];
        getPrinter().check(getPrinter().sendItemCode(new ItemCode(barcode.getBytes())));
    }

}
