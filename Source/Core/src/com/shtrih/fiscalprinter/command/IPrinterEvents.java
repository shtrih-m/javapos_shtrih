/*
 * IPrinterEvents.java
 *
 * Created on 17 Сентябрь 2010 г., 13:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */

public interface IPrinterEvents {

    public void afterCommand(PrinterCommand command) throws Exception;
    public void beforeCommand(PrinterCommand command) throws Exception;
}
