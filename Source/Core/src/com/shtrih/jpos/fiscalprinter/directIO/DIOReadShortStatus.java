/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.ShortPrinterStatus;

/**
 *
 * @author V.Kravtsov
 */
public class DIOReadShortStatus extends DIOItem
{
    
    public DIOReadShortStatus(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        ShortPrinterStatus status = service.printer.readShortStatus();
        ((Object[])object)[0] = status;
    }
}
