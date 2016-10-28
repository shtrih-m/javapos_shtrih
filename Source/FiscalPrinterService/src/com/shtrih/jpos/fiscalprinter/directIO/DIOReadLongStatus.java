/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;

/**
 *
 * @author V.Kravtsov
 */
public class DIOReadLongStatus extends DIOItem
{
    
    public DIOReadLongStatus(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        LongPrinterStatus status = service.printer.readLongStatus();
        ((LongPrinterStatus[])object)[0] = status;
    }
}
