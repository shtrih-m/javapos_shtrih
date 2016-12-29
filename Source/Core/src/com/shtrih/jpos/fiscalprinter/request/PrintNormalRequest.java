/*
 * PrintNormalRequest.java
 *
 * Created on 4 April 2008, 14:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.request;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class PrintNormalRequest extends FiscalPrinterRequest {
    private final int station;
    private final String data;

    public PrintNormalRequest(int station, String data) {
        this.station = station;
        this.data = data;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printNormalAsync(station, data);
    }
}
