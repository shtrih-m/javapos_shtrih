/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import java.util.List;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.FSReadCommStatus;
import com.shtrih.fiscalprinter.command.FSReadDayParameters;
import com.shtrih.fiscalprinter.command.FSReadFiscalization;
import com.shtrih.fiscalprinter.command.FSReadSerial;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.FSReadExpDate;
import com.shtrih.fiscalprinter.command.FSReadStatus;
import com.shtrih.jpos.fiscalprinter.JposFiscalPrinterDate;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;

public class DIOReadFSParams extends DIOItem {

    public DIOReadFSParams(FiscalPrinterImpl service) {
        super(service);
    }

/*
1) Дата и время первого непереданного ФД
2) Количество непереданных ФД
3) дата и время изменения статуса ФН (в частности, дата и время фискализации)
4) номер фискального накопителя
5) Дата, когда истечет срок действия ФН
    
0 - серийный номер ФН
1 - РНМ
2 - кол-во неотправленных ФД
3 - дата и время самого раннего неотправленного ФД (DDMMYYYYhhmm)
4 - номер документа последней перерегистрации
5 - дата и время последней перерегистрации (DDMMYYYYhhmm)
6 - дата окончания срока действия (DDMMYYYY)
7 - кол-во чеков за смену
8 - номер смены
9 - номер последнего ФД
10 - дата и время последнего ФД (DDMMYYYYhhmm)
11 - фискальный признак последнего ФД

6. 01.02.2018
7. 6
8. 53
9. 169
10. 24.01.201719:36
    
*/
    
    public void execute(int[] data, Object object) throws Exception {

        SMFiscalPrinter printer = getPrinter();
        
        String fsserial = printer.fsReadSerial().getSerial();
        String rnm = printer.ReadRnm();
        
        FSReadStatus fsStatus = printer.fsReadStatus();
        FSReadExpDate expDate = printer.fsReadExpDate();
        FSReadCommStatus commStatus = printer.fsReadCommStatus();
        FSReadFiscalization fiscalization = printer.fsReadFiscalization();
        FSReadDayParameters dayParams = printer.fsReadDayParameters();
        
        JposFiscalPrinterDate docDate = new JposFiscalPrinterDate(
                commStatus.getDocumentDate(), commStatus.getDocumentTime());
        JposFiscalPrinterDate fiscDate = new JposFiscalPrinterDate(
                fiscalization.getDate(), fiscalization.getTime());
        
        List<String> list = (List<String>) object;
        list.add(fsserial);
        list.add(rnm);
        list.add(String.valueOf(commStatus.getQueueSize()));
        list.add(docDate.toString());
        list.add(String.valueOf(fiscalization.getDocNumber()));
        list.add(fiscDate.toString());
        list.add(expDate.getDate().toJposString());
        list.add(String.valueOf(dayParams.getReceiptNumber()));
        list.add(String.valueOf(dayParams.getDayNumber()));
        list.add(String.valueOf(fsStatus.getDocNumber()));
        list.add(fsStatus.getDate().toJposString() + fsStatus.getTime().toJposString());
    }

    
}
