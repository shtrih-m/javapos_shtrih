/*
 * PrinterException.java
 *
 * Created on March 13 2008, 13:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.util.Localizer;

/**
 * @author V.Kravtsov
 */
public class DeviceException extends Exception {

    private final int errorCode;

    public DeviceException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public static DeviceException readAnswerError() throws Exception {
        return new DeviceException(PrinterConst.SMFPTR_E_READ_ANSWER,
                Localizer.getString(Localizer.NoConnection));

    }

    public static DeviceException writeCommandError() throws Exception {
        return new DeviceException(PrinterConst.SMFPTR_E_WRITE_COMMAND,
                Localizer.getString(Localizer.WriteCommandError));
    }
}
