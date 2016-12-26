/*
 * DIOReadDrawerState.java
 *
 * Created on 4 Март 2010 г., 15:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.cashdrawer.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.cashdrawer.CashDrawerImpl;

public class DIOReadDrawerState implements CashDrawerDIOItem {

    /**
     * Creates a new instance of DIOReadDrawerState
     */
    public DIOReadDrawerState() {
    }

    public void execute(CashDrawerImpl service, int[] data, Object object)
            throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        data[0] = 0;
        if (service.getPrinter().readPrinterStatus().getPrinterFlags()
                .isDrawerOpened()) {
            data[0] = 1;
        }
    }
}
