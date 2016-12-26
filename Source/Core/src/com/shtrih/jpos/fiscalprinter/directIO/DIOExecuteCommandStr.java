/*
 * DIOExecuteCommandStr.java
 *
 * Created on 14 Май 2010 г., 11:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.command.CommandParamsTextReader;
import com.shtrih.fiscalprinter.command.CommandParamsTextWriter;
import com.shtrih.fiscalprinter.command.FlexCommand;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.Localizer;

public class DIOExecuteCommandStr {

    private final FiscalPrinterImpl service;
    static CompositeLogger logger = CompositeLogger.getLogger(DIOExecuteCommandStr.class);

    /** Creates a new instance of DIOReadPaymentName */
    public DIOExecuteCommandStr(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        logger.debug("execute");
        DIOUtils.checkDataMinLength(data, 2);
        DIOUtils.checkObjectNotNull(object);

        String[] lines = (String[]) object;
        int commandCode = data[0];
        int timeout = data[1];
        String inParams = lines[0];
        FlexCommand command = service.getCommands().itemByCode(commandCode);
        if (command == null) {
            throw new Exception(Localizer.getString(Localizer.CommandNotFound));
        }

        command.setTimeout(timeout);
        CommandParamsTextReader.read(inParams, command.getInParams());
        service.printer.execute(command);
        service.printer.check(command.getResultCode());
        String outParams = CommandParamsTextWriter
                .write(command.getOutParams());
        lines[1] = outParams;
    }
}
