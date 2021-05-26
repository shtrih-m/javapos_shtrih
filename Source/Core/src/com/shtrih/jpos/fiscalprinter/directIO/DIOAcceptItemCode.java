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
import com.shtrih.fiscalprinter.command.*;
import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOAcceptItemCode extends DIOItem {

    public DIOAcceptItemCode(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        Object[] params = (Object[])object;
        DIOUtils.checkObjectMinLength(params, 2);
        
        int action = (int)params[0];
        FSAcceptMC command = new FSAcceptMC();
        command.setAction(action);
        int rc = getPrinter().fsAcceptMC(command);
        getPrinter().check(rc);
        params[1] = new Integer(command.getErrorCode());
    }

}
