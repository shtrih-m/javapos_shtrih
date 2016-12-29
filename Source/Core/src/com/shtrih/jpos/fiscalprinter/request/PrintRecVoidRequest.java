/*
 * PrintRecVoidRequest.java
 *
 * Created on 4 April 2008, 16:00
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

public final class PrintRecVoidRequest extends FiscalPrinterRequest {
    private final String description;

    /** Creates a new instance of PrintRecVoidRequest */
    public PrintRecVoidRequest(String description) {
        this.description = description;
    }

    public final void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecVoidAsync(description);
    }
}
