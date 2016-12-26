/*
 * PrintFiscalDocumentLineRequest.java
 *
 * Created on 4 April 2008, 14:34
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

class PrintFiscalDocumentLineRequest extends FiscalPrinterRequest {
    private String documentLine;

    public PrintFiscalDocumentLineRequest(String documentLine) {
        this.documentLine = documentLine;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printFiscalDocumentLineAsync(documentLine);
    }
}
