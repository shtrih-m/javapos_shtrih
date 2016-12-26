/*
 * DIOXMLZReport.java
 *
 * Created on 25 Январь 2011 г., 17:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.command.PrinterParameter;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

/**
 * @author V.Kravtsov
 */
public class DIOReadParameter {

    private FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOXMLZReport
     */
    public DIOReadParameter(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        int parameterIndex = data[0];
        PrinterParameter parameter = service.getPrinter().getModel().getParameters().get(parameterIndex);
        String parameterValue = service.readParameter(parameter.getName());
        ((String[]) object)[0] = parameterValue;
    }
}
