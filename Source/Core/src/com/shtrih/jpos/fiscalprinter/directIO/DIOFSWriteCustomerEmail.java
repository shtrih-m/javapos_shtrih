/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.TLVList;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOFSWriteCustomerEmail extends DIOItem {

    public DIOFSWriteCustomerEmail(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        boolean print = true;
        if (data != null) {
            print = (data[0] == 1);
        }
        String text = (String) object;
        byte[] tlv = getPrinter().getTLVData(1008, text);
        service.fsWriteTLV(tlv, print);
    }

}
