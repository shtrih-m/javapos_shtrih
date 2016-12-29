/*
 * FiscalPrinterRequest.java
 *
 * Created on 4 April 2008, 14:32
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

public abstract class FiscalPrinterRequest {
    static int fiscalPrinterRequestId = 0;

    private final int id;

    /**
     * Creates a new instance of FiscalPrinterRequest
     */
    public FiscalPrinterRequest() {
        fiscalPrinterRequestId++;
        id = fiscalPrinterRequestId;
    }

    public int getId() {
        return id;
    }

    public abstract void execute(FiscalPrinterImpl device) throws Exception;
}
