/*
 * PrintFixedOutputRequest.java
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

public class PrintFixedOutputRequest extends FiscalPrinterRequest {
    private int documentType;
    private int lineNumber;
    private String data;

    public PrintFixedOutputRequest(int documentType, int lineNumber, String data) {
        this.documentType = documentType;
        this.lineNumber = lineNumber;
        this.data = data;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printFixedOutputAsync(documentType, lineNumber, data);
    }
}
