/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.jpos.DIOUtils;
import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.FDOParameters;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import java.util.List;

public class DIOReadFDOParams extends DIOItem {

    public DIOReadFDOParams(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        List<String> list = (List<String>) object;
        FDOParameters fdoParameters = getPrinter().getFDOParameters();
        list.add(fdoParameters.getHost());
        list.add(String.valueOf(fdoParameters.getPort()));
        list.add(String.valueOf(fdoParameters.getPollPeriodSeconds()));
    }

}
