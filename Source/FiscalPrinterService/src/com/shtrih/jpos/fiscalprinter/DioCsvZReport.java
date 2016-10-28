/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import com.shtrih.util.SysUtils;

/**
 * @author V.Kravtsov
 */
public class DioCsvZReport {

    private final FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOXMLZReport
     */
    public DioCsvZReport(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        String fileName = (String) object;
        service.saveCsvZReport(SysUtils.getFilesPath() + fileName);
    }
}
