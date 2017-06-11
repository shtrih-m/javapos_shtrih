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

public class DIOFSWriteTag1162 extends DIOItem {

    public DIOFSWriteTag1162(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        String[] params = (String[]) object;
        DIOUtils.checkObjectMinLength(params, 3);
        int catId = Integer.valueOf(params[0]);
        long groupId = Long.valueOf(params[1]);
        getPrinter().fsWriteTag1162(catId, groupId, params[2]);
    }

}
