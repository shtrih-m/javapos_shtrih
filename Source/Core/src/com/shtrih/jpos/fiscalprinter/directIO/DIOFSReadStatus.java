/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.TLVParser;
import com.shtrih.fiscalprinter.command.FSDocType;
import com.shtrih.fiscalprinter.command.FSFindDocument;
import com.shtrih.fiscalprinter.command.FSReadDocument;
import com.shtrih.fiscalprinter.command.FSReadStatus;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import java.io.ByteArrayOutputStream;
import com.shtrih.fiscalprinter.TLVItems;
import java.util.Vector;
import com.shtrih.fiscalprinter.command.FSDocumentReceipt;
import com.shtrih.util.StringUtils;

/**
 *
 * @author V.Kravtsov
 */

/*
 * Состояние фазы жизни (1 байт)
 * Текущий документ (1 байт)
 * Данные документа (1 байт)
 * Состояние смены (1 байт)
 * Флаги предупреждения (1 байт)
 * Дата и время (5 байт)
 * Номер ФН (16 байт) ASCII
 * Номер последнего ФД (4 байт)
*/

public class DIOFSReadStatus extends DIOItem {

    public DIOFSReadStatus(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        FSReadStatus status = getPrinter().fsReadStatus();
        String[] lines = (String[])object;
        
        lines[0] = String.valueOf(status.getStatus().getCode());
        lines[1] = String.valueOf(status.getDocType().getValue());
        lines[2] = StringUtils.boolToStr(status.isDocReceived());
        lines[3] = StringUtils.boolToStr(status.isDayOpened());
        lines[4] = String.valueOf(status.getFlags());
        lines[5] = status.getDate().toJposString();
        lines[6] = status.getTime().toJposString();
        lines[7] = status.getFsSerial();
        lines[8] = String.valueOf(status.getDocNumber());
     }
    
}
