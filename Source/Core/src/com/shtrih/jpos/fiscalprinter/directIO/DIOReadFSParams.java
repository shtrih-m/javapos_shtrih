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
import com.shtrih.fiscalprinter.command.FSReadFiscalization;
import com.shtrih.fiscalprinter.command.FSReadSerial;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.FSReadExpDate;

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
*/
    
    public void execute(int[] data, Object object) throws Exception {

        SMFiscalPrinter printer = getPrinter();
        FSReadCommStatus commStatus = printer.fsReadCommStatus();
        FSReadFiscalization fiscalization = printer.fsReadFiscalization();
        FSReadSerial serial = printer.fsReadSerial();
        FSReadExpDate expDate = printer.fsReadExpDate();
        
        List<String> list = (List<String>) object;
        list.add(commStatus.getDocumentDate().toString());
        list.add(String.valueOf(commStatus.getQueueSize()));
        list.add(fiscalization.getDate().toString());
        list.add(serial.getSerial());
        list.add(expDate.getDate().toString());
    }

    
}
