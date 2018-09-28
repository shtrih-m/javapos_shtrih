package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.command.FSDocType;
import com.shtrih.fiscalprinter.command.FSDocument;
import com.shtrih.fiscalprinter.command.FSDocumentReceipt;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.StringUtils;

import java.util.Vector;

/**
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

    public void execute(int[] data, Object object) throws Exception {
        Vector<String> lines = (Vector<String>) object;

        FSDocument searchResult = getPrinter().fsFindLastDocument(FSDocType.FS_DOCTYPE_RECEIPT);
        if (searchResult == null)
            return;

        FSDocumentReceipt doc = (FSDocumentReceipt) searchResult;
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
