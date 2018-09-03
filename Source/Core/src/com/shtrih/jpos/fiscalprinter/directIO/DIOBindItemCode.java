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
import com.shtrih.fiscalprinter.command.FSBindItemCode;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.MethodParameter;

public class DIOBindItemCode extends DIOItem {

    public DIOBindItemCode(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        String[] params = (String[])object;
        DIOUtils.checkObjectMinLength(params, 6);
        String barcode = params[0];
        FSBindItemCode command = getPrinter().bindItemCode(barcode);
        getPrinter().check(command.getResultCode());
        params[0] = String.valueOf(command.getLocalResultCode());
        params[1] = String.valueOf(command.getProcessingCode());
        params[2] = String.valueOf(command.getSalePermission());
        params[3] = String.valueOf(command.getServerItemStatus());
        params[4] = String.valueOf(command.getServerResultCode());
        params[5] = String.valueOf(command.getServerCheckStatus());
        params[6] = String.valueOf(command.getSymbolicType());
    }

}
