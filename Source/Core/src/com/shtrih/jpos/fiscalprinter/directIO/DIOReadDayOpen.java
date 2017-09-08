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
import com.shtrih.fiscalprinter.command.FSOpenDayReport;

/**
 *
 * @author V.Kravtsov
 */

/*
Необходимо реализовать метод DirectIO для получение значений отчета 
об открытии смены содержащий следующую информацию: 
•         Номер смены
•         Тип документа (отчет об открытии)
•         Дата и время 
•         РН ККТ
•         ЗН ККТ
•         ФН №
•         ФД №
•         ФПД №
•         Наименование ОФД

ОТЧЕТ ОБ ОТКРЫТИИ СМЕНЫ
1041,НОМЕР ФН:99990789312
1037,РЕГ. НОМЕР ККТ:0000000001056401
1018,ИНН ПОЛЬЗ.:505303696069
1040,НОМЕР ФД:134
1012,ДАТА, ВРЕМЯ:30.08.2017 18:49:00
1077,ФП ДОКУМЕНТА:4000513524
1038,НОМЕР СМЕНЫ:8
1021,КАССИР:КАССИР 1
1048,НАИМЕН. ПОЛЬЗ.:ЗАО ТОРГОВЫЙ ОБЪЕКТ N1                
*/

public class DIOReadDayOpen extends DIOItem {

    public DIOReadDayOpen(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        Vector<String> lines = (Vector<String>)object;
        FSOpenDayReport doc = (FSOpenDayReport)getPrinter().fsFindLastDocument(FSDocType.FS_DOCTYPE_DAYOPEN);
        // Номер смены
        lines.add(String.valueOf(doc.getDayNumber()));
        // Тип документа (отчет об открытии)
        lines.add(String.valueOf(doc.getDocType()));
        // Дата и время 
        lines.add(doc.getDateTime().toString());
        // РН ККТ
        lines.add(getPrinter().readRnm());
        // ЗН ККТ
        lines.add(getPrinter().readFullSerial()); 
        // ФН serial
        lines.add(getPrinter().fsReadSerial().getSerial());
        // ФД №
        lines.add(String.valueOf(doc.getDocNumber()));
        // ФПД №
        lines.add(String.valueOf(doc.getDocSign()));
        // Наименование ОФД
        lines.add(getPrinter().readParameter("fdoName"));
    }
    
}
