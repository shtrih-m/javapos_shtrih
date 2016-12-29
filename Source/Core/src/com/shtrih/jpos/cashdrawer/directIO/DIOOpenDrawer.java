/*
 * DIOOpenDrawer.java
 *
 * Created on 4 Март 2010 г., 14:59
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

public class DIOOpenDrawer implements CashDrawerDIOItem {

    /**
     * Creates a new instance of DIOOpenDrawer
     */

    public DIOOpenDrawer() {
    }

    public void execute(CashDrawerImpl service, int[] data, Object object)
            throws Exception {
        DIOUtils.checkDataNotNull(data);
        int drawerNumber = 0;
        if (data.length > 0) {
            drawerNumber = data[0];
        }
        service.getPrinter().openCashDrawer(drawerNumber);
    }

}
