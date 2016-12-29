/*
 * SmFiscalPrinterException.java
 *
 * Created on 14 October 2010 пїЅ., 16:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 * @author V.Kravtsov
 */
public class SmFiscalPrinterException extends Exception {

    private final int code;

    /** Creates a new instance of SmFiscalPrinterException */
    public SmFiscalPrinterException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
