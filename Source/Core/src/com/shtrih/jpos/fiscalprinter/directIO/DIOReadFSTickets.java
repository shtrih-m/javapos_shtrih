/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.command.FSTicket;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

import java.util.Vector;

public class DIOReadFSTickets extends DIOItem {

    public DIOReadFSTickets(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        Object[] outParams = (Object[])object;
        Vector<FSTicket> tickets = service.getPrinter().fsReadTickets(data);
        String[] lines = new String[tickets.size()];
        for (int i = 0; i < tickets.size(); i++) {
            if (i > (lines.length-1)) break;
            FSTicket ticket = tickets.get(i);
            lines[i] = tickets.get(i).getText();
        }
        outParams[0] = lines;
    }

}
