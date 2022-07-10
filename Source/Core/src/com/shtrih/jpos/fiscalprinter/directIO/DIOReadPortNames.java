/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadPortNames extends DIOItem {

    public DIOReadPortNames(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception
    {
        Object[] params = (Object[])object;
        if (params.length >= 3)
        {
            service.getParams().portNamePrefix = (String)params[0];
            service.getParams().portNameTimeout = (Integer)params[1];
            service.getParams().portNameSingle = (Boolean) params[2];
        }
        String[] portNames = service.getPort().getPortNames();
        params[0] = portNames;
    }
}
