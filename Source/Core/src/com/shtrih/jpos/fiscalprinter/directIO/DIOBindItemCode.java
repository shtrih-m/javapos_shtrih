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
import com.shtrih.fiscalprinter.command.FSBindMC;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.MethodParameter;

public class DIOBindItemCode extends DIOItem {

    public DIOBindItemCode(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        Object[] params = (Object[]) object;
        DIOUtils.checkObjectMinLength(params, 7);
        FSBindMC command = new FSBindMC();
        command.data = (byte[]) params[0];
        if (params[1] != null){
            command.volumeAccounting = (Boolean)params[1];
        }
        int rc = getPrinter().fsBindMC(command);
        getPrinter().check(command.getResultCode());
        params[0] = new Integer(command.itemCode);
        params[1] = new Integer(command.codeType);
        params[2] = new Integer(command.localCheckStatus);
        params[3] = new Integer(command.localErrorCode);
        params[4] = new Integer(command.symbolicType);
        params[5] = new Integer(command.serverErrorCode);
        params[6] = new Integer(command.serverCheckStatus);
        params[7] = command.serverTLVData;
    }
}
