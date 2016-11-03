/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.util.BitUtils;

/**
 *
 * @author V.Kravtsov
 */
/**
 * **************************************************************************
 * «апрос статуса ‘Ќ
 *  од команды FF01h. ƒлина сообщени€: 6 байт.
 * ѕароль системного администратора: 4 байт
 *
 * ќтвет: FF01h	ƒлина сообщени€: 31 байт.
 *  од ошибки (1 байт)
 * —осто€ние фазы жизни (1 байт)
 * Ѕит 0 Ц проведена настройка ‘Ќ
 * Ѕит 1 Ц открыт фискальный режим
 * Ѕит 2 Ц закрыт фискальный режим
 * Ѕит 3 Ц закончена передача фискальных данных в ќ‘ƒ
 * “екущий документ (1 байт)
 * 00h Ц нет открытого документа
 * 01h Ц отчет о фискализации
 * 02h Ц отчет об открытии смены
 * 04h Ц кассовый чек
 * 08h Ц отчет о закрытии смены
 * 10h Ц отчет о закрытии фискального режима
 * ƒанные документа (1 байт)
 * 00 Ц нет данных документа
 * 01 Ц получены данные документа
 * —осто€ние смены (1 байт)
 * 00 Ц смена закрыта
 * 01 Ц смена открыта
 * ‘лаги предупреждени€ (1 байт)
 * ƒата и врем€ (5 байт)
 * Ќомер ‘Ќ (16 байт) ASCII
 * Ќомер последнего ‘ƒ (4 байт)
 ***************************************************************************
 */
public class FSReadStatus extends PrinterCommand {

    // in
    private int sysPassword = 0;    // System sdministrator password (4 bytes)
    // out
    private FSStatus status;        // Status code
    private FSDocType docType;      // Document type
    private boolean isDocReceived;  // Document data received  
    private boolean isDayOpened;    // Fiscal day is opened
    private int flags;
    private PrinterDate date;
    private PrinterTime time;
    private String fsSerial;
    private long docNumber;
    
    public FSReadStatus() {
    }

    public final int getCode() {
        return 0xFF01;
    }

    public final String getText() {
        return "Fiscal storage: read status";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception {
        setStatus(new FSStatus(in.readByte()));
        setDocType(new FSDocType(in.readByte()));
        setIsDocReceived(in.readByte() != 0);
        setIsDayOpened(in.readByte() != 0);
        flags = in.readByte();
        date = in.readDateYMD();
        time = in.readTime2();
        fsSerial = in.readString(16);
        docNumber = in.readLong(4);
    }
    
    /**
     * @return the sysPassword
     */
    public int getSysPassword() {
        return sysPassword;
    }

    /**
     * @param sysPassword the sysPassword to set
     */
    public void setSysPassword(int sysPassword) {
        this.sysPassword = sysPassword;
    }

    /**
     * @return the status
     */
    public FSStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(FSStatus status) {
        this.status = status;
    }

    /**
     * @return the docType
     */
    public FSDocType getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(FSDocType docType) {
        this.docType = docType;
    }

    /**
     * @return the isDocReceived
     */
    public boolean isIsDocReceived() {
        return isDocReceived;
    }

    /**
     * @param isDocReceived the isDocReceived to set
     */
    public void setIsDocReceived(boolean isDocReceived) {
        this.isDocReceived = isDocReceived;
    }

    /**
     * @return the isDayOpened
     */
    public boolean isIsDayOpened() {
        return isDayOpened;
    }

    /**
     * @param isDayOpened the isDayOpened to set
     */
    public void setIsDayOpened(boolean isDayOpened) {
        this.isDayOpened = isDayOpened;
    }

    /**
     * @return the flags
     */
    public int getFlags() {
        return flags;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * @return the date
     */
    public PrinterDate getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(PrinterDate date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public PrinterTime getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(PrinterTime time) {
        this.time = time;
    }

    /**
     * @return the fsSerial
     */
    public String getFsSerial() {
        return fsSerial;
    }

    /**
     * @param fsSerial the fsSerial to set
     */
    public void setFsSerial(String fsSerial) {
        this.fsSerial = fsSerial;
    }

    /**
     * @return the docNumber
     */
    public long getDocNumber() {
        return docNumber;
    }

    /**
     * @param docNumber the docNumber to set
     */
    public void setDocNumber(long docNumber) {
        this.docNumber = docNumber;
    }
    
}
