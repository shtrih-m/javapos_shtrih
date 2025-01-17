/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.command.FDOParameters;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

import java.util.List;

public class DIOReboot extends DIOItem {

    public DIOReboot(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        getPrinter().check(getPrinter().reboot());
    }

}
