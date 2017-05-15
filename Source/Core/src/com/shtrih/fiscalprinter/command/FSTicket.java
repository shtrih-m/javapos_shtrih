/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.util.Hex;
import java.util.Vector;

/**
 *
 * @author V.Kravtsov
 */

/*
7.1.8 Формат квитанции, при выдаче из Архива ФН
------------------------------------------------
Поле                        Тип            Длина
------------------------------------------------
Дата и время                DATE_TIME      5
Фискальный признак ОФД      DATA           18
Номер ФД                    Uint32, LE     4
------------------------------------------------
 */
public class FSTicket {
    
    private final int resultCode;
    private final byte[] data;
    private PrinterDate date = new PrinterDate();
    private PrinterTime time = new PrinterTime();
    private byte[] documentMAC = new byte[18];
    private long documentNumber = 0;
    
    public FSTicket(int resultCode, byte[] data) throws Exception {
        this.resultCode = resultCode;
        this.data = data;
        if ((resultCode == 0) && (data.length >= 27)) {
            CommandInputStream stream = new CommandInputStream("");
            stream.setData(data);
            date = stream.readDateYMD();
            time = stream.readTime2();
            documentMAC = stream.readBytes(18);
            documentNumber = stream.readLong(4);
        }
    }
    
    public String getText() {
        Vector<String> lines = new Vector<String>();
        
        lines.add(String.valueOf(resultCode));
        if (resultCode == 0) {
            lines.add(date.toString());
            lines.add(time.toString());
            lines.add(Hex.toHex2(documentMAC));
            lines.add(String.valueOf(documentNumber));
        }
        String text = "";
        String separator = ";";
        for (int i = 0; i < lines.size(); i++) {
            text += (lines.get(i) + separator);
        }
        return text;
    }

    
    /**
     * @return the resultCode
     */
    public int getResultCode() {
        return resultCode;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @return the date
     */
    public PrinterDate getDate() {
        return date;
    }

    /**
     * @return the time
     */
    public PrinterTime getTime() {
        return time;
    }

    /**
     * @return the documentMAC
     */
    public byte[] getDocumentMAC() {
        return documentMAC;
    }

    /**
     * @return the documentNumber
     */
    public long getDocumentNumber() {
        return documentNumber;
    }
    
}
