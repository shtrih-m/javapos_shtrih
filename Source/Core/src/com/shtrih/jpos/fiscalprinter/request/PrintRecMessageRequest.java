/*
 * PrintRecMessageRequest.java
 *
 * Created on 4 April 2008, 14:42
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

public final class PrintRecMessageRequest extends FiscalPrinterRequest {
    private final String message;

    public PrintRecMessageRequest(String message) {
        this.message = message;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecMessageAsync(message);
    }
}
