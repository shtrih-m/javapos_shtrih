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
Необходимо реализовать метод DirectIO для получение последнего 
записанного ФД в ФН в следующем формате:
•         Тип документа
•         Флаг получения квитанции ОФД
•         Дата и время
•         ФД №
•         ФПД №
•         Тип операции
•         Сумма
*/

public class DIOReadReceipt extends DIOItem {

    public DIOReadReceipt(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        Vector<String> lines = (Vector<String>)object;
        FSDocumentReceipt doc = (FSDocumentReceipt)getPrinter().fsFindLastDocument(FSDocType.FS_DOCTYPE_RECEIPT);
        // Тип документа
        lines.add(String.valueOf(doc.getDocType()));
        // Квитанция получена
        lines.add(StringUtils.boolToStr(doc.isTicketReceived()));
        // Дата и время 
        lines.add(doc.getDateTime().toString());
        // ФД №
        lines.add(String.valueOf(doc.getDocNumber()));
        // ФПД №
        lines.add(String.valueOf(doc.getDocSign()));
        // Тип операции
        lines.add(String.valueOf(doc.getType()));
        // Сумма
        lines.add(String.valueOf(doc.getAmount()));
    }
    
}
