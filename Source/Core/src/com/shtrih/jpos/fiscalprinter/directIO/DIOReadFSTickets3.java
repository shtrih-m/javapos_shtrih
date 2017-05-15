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

public class DIOReadFSTickets3 extends DIOItem {

    public DIOReadFSTickets3(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        Object[] outParams = (Object[])object;
        int firstFSDocumentNumber = data[0];
        int documentCount = 0;
        if (data.length > 1){
            documentCount = data[1];
        }
        Vector<FSTicket> tickets = service.getPrinter().fsReadTickets(firstFSDocumentNumber, documentCount);
        String[] lines = new String[tickets.size()];
        for (int i = 0; i < tickets.size(); i++) 
        {
            if (i > (lines.length-1)) break;
            FSTicket ticket = tickets.get(i);
            lines[i] = tickets.get(i).getText();
        }
        outParams[0] = lines;
    }

}
