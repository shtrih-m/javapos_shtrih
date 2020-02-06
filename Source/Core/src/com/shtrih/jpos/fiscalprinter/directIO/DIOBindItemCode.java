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
import com.shtrih.fiscalprinter.command.FSSetOperationMarking;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.MethodParameter;

public class DIOBindItemCode extends DIOItem {

    public DIOBindItemCode(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        String[] params = (String[])object;
        DIOUtils.checkObjectMinLength(params, 2);
        String barcode = params[0];
        FSSetOperationMarking command = getPrinter().setOperationMarking(barcode);
        getPrinter().check(command.getResultCode());
        params[0] = String.valueOf(command.itemCode);
        params[1] = String.valueOf(command.codeType);
    }

}
