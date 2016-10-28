/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.SMFiscalPrinter;

public interface PrintItem {

    public void print(SMFiscalPrinter printer) throws Exception;
}
