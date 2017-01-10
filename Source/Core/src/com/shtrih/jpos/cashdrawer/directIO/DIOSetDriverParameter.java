/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.cashdrawer.directIO;

/**
 *
 * @author V.Kravtsov
 */
import jpos.JposConst;
import jpos.JposException;

import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.cashdrawer.CashDrawerImpl;
import com.shtrih.jpos.cashdrawer.ShtrihCashDrawerConst;
import com.shtrih.util.Localizer;

public class DIOSetDriverParameter implements CashDrawerDIOItem {

    public DIOSetDriverParameter() {
    }

    
    public void execute(CashDrawerImpl service, int[] data, Object object)
            throws Exception {
        DIOUtils.checkDataMinLength(data, 1);

        switch (data[0]) {
            case ShtrihCashDrawerConst.SMCASH_PARAM_DRAWER_NUMBER:
                service.getParams().drawerNumber = ((int[]) object)[0];
                break;

            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue));
        }
    }
}
