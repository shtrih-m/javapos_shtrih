/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.FSTicket;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadFSTickets4 extends DIOItem {

    public DIOReadFSTickets4(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        Object[] items = (Object[]) object;
        int firstFSDocumentNumber = data[0];
        int documentCount = 0;
        if (data.length > 1){
            documentCount = data[1];
        }
        items[0] = service.getPrinter().fsReadTickets(firstFSDocumentNumber, documentCount);
    }

}
