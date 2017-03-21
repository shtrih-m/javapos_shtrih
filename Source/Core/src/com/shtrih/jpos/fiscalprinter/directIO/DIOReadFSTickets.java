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

public class DIOReadFSTickets extends DIOItem {

    public DIOReadFSTickets(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        Vector<FSTicket> tickets = service.fsReadTickets(data);
        int[] resultCodes = null;
        byte[][] ticketData = null;
        if (tickets.size() > 0) {
            resultCodes = new int[tickets.size()];
            ticketData = new byte[tickets.size()][];
        }
        for (int i = 0; i < tickets.size(); i++) {
            FSTicket ticket = tickets.get(i);
            resultCodes[i] = ticket.getResultCode();
            ticketData[i] = ticket.getData();
        }

        Object[] items = (Object[]) object;
        items[0] = resultCodes;
        items[1] = ticketData;
    }

}
