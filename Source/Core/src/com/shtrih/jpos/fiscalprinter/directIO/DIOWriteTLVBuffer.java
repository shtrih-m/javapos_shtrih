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
import com.shtrih.fiscalprinter.command.FSCheckMC;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.FSWriteTLVBuffer;


public class DIOWriteTLVBuffer extends DIOItem {

    public DIOWriteTLVBuffer(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        FSWriteTLVBuffer command = new FSWriteTLVBuffer();
        int rc = getPrinter().fsWriteTLVBuffer(command);
        getPrinter().check(rc);
    }
}
