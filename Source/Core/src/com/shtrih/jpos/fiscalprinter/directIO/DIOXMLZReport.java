/*
 * DIOXMLZReport.java
 *
 * Created on 25 Январь 2011 г., 17:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

/**
 * @author V.Kravtsov
 */
public class DIOXMLZReport {

    private FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOXMLZReport
     */
    public DIOXMLZReport(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        String fileName = (String) object;
        service.saveXmlZReport(fileName);
    }
}
