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
import com.shtrih.fiscalprinter.request.CheckCodeRequest;

public class DIOCheckItemCode extends DIOItem {

    public DIOCheckItemCode(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        String[] lines = (String[])object;
        CheckCodeRequest request = new CheckCodeRequest();
        request.setData(lines[0].getBytes());
        request.setQuantity(1);
        request.setIsSale(true);
        request.setUnit(10); // gramms
        request.setNumerator(0);
        request.setDenominator(0);
        
        if (lines.length > 5)
        {
            request.setIsSale(!(lines[1].equalsIgnoreCase("0")));
            request.setQuantity(Long.parseLong(lines[2]) / 1000000.0);
            request.setUnit(Integer.parseInt(lines[3]));
            request.setNumerator(Long.parseLong(lines[4]));
            request.setDenominator(Long.parseLong(lines[5]));
        }
        getPrinter().checkItemCode(request);
    }

}
