/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.command.CommandParamsTextReader;
import com.shtrih.fiscalprinter.command.CommandParamsTextWriter;
import com.shtrih.fiscalprinter.command.FlexCommand;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.Localizer;

/**
 *
 * @author V.Kravtsov
 */
public class DIOSetDiscountAmount extends DIOItem
{
    public DIOSetDiscountAmount(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        service.setDiscountAmount(((Integer)object).intValue());
    }
    
}
