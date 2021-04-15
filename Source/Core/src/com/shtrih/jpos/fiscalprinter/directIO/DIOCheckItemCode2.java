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

public class DIOCheckItemCode2 extends DIOItem {

    public DIOCheckItemCode2(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        Object[] params = (Object[])object;
        DIOUtils.checkObjectMinLength(params, 10);
        
        FSCheckMC command = new FSCheckMC();
        command.itemStatus = (Integer)params[0];
        command.checkMode = (Integer)params[1];
        command.mcData = (byte[])params[2];
        command.tlv = (byte[])params[3];
        getPrinter().check(getPrinter().fsCheckMC(command));
        params[4] = new Integer(command.localCheckStatus);
        params[5] = new Integer(command.localErrorCode);
        params[6] = new Integer(command.symbolicType);
        params[7] = new Integer(command.serverErrorCode);
        params[8] = new Integer(command.serverCheckStatus);
        params[9] = command.serverTLVData;
    }
}
