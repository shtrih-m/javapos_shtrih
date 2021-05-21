/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/**
 *
 * Запрос итогов последней фискализации (перерегистрации)
 * * 
Код команды FF09h. Длина сообщения: 6 байт. Пароль системного администратора
 * (4 байта)
 * * 
Ответ для ФФД 1.0 и 1.05: FF09h. Длина сообщения: 50 (55) байт. Код ошибки (1
 * байт) Дата и время (5 байт) DATE_TIME ИНН (12 байт) ASCII Регистрационный
 * номер ККT (20 байт) ASCII Код налогообложения (1 байт): Бит 0 – ОСН Бит 1 –
 * УСН доход Бит 2 – УСН доход минус расход Бит 3 – ЕНВД Бит 4 – ЕСП Бит 5 – ПСН
 * Режим работы (1 байт) Бит 0 – Шифрование Бит 1 – Автономный режим Бит 2 –
 * Автоматический режим Бит 3 – Применение в сфере услуг Бит 4 – Режим БСО Бит 5
 * – Применение в Интернет Номер ФД (4 байта) Фискальный признак (4 байта) Дата
 * и время (5 байт) DATE_TIME2
 *
 * Ответ для ФФД 1.1: FF09h. Длина сообщения: 67 байт1. Код ошибки (1 байт) Дата
 * и время (5 байт) DATE_TIME ИНН (12 байт) ASCII Регистрационный номер ККT (20
 * байт) ASCII Код налогообложения (1 байт): Бит 0 – ОСН Бит 1 – УСН доход Бит 2
 * – УСН доход минус расход Бит 3 – ЕНВД Бит 4 – ЕСП Бит 5 – ПСН Режим работы (1
 * байт): Бит 0 – Шифрование Бит 1 – Автономный режим Бит 2 – Автоматический
 * режим Бит 3 – Применение в сфере услуг Бит 4 – Режим БСО Бит 5 – Применение в
 * Интернет Расширенные признаки работы ККТ (1 байт) ИНН ОФД (12 байт) ASCII Код
 * причины изменения сведений о ККТ (4 байта) Номер ФД (4 байта) Фискальный
 * признак (4 байта)
 */
public class FSReadFiscalization extends PrinterCommand {

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

    // in
    private int sysPassword; // System sdministrator password (4 bytes)
    // out
    private PrinterDate date;
    private PrinterTime time;
    private String taxID;
    private String fdoTaxID;
    private String regID;
    private int taxSystemCode;
    private int mode;
    private int extMode;
    private int reasonCode;
    private long docNumber;
    private long macNumber;
    private PrinterDateTime docDate;

    public FSReadFiscalization() {
    }

    public final int getCode() {
        return 0xFF09;
    }

    public final String getText() {
        return "Fiscal storage: read fiscalization";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
    }

    public void decode(CommandInputStream in) throws Exception 
    {
        if (in.size() >= 64)
        {
            setDate(in.readDateYMD());
            setTime(in.readTimeHM());
            setTaxID(in.readString(12));
            setRegID(in.readString(20));
            setTaxSystemCode(in.readByte());
            setMode(in.readByte());
            setExtMode(in.readByte());
            setFdoTaxID(in.readString(12));
            setReasonCode(in.readInt());
            setDocNumber(in.readLong(4));
            setMacNumber(in.readLong(4));
        } else
        {
            setDate(in.readDateYMD());
            setTime(in.readTimeHM());
            setTaxID(in.readString(12));
            setRegID(in.readString(20));
            setTaxSystemCode(in.readByte());
            setMode(in.readByte());
            setDocNumber(in.readLong(4));
            setMacNumber(in.readLong(4));
            if (in.size() >= 5) {
                setDocDate(in.readFSDateTime());
            }
        }
        
        
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
     * @return the taxID
     */
    public String getTaxID() {
        return taxID;
    }

    /**
     * @param taxID the taxID to set
     */
    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    /**
     * @return the regID
     */
    public String getRegID() {
        return regID;
    }

    /**
     * @param regID the regID to set
     */
    public void setRegID(String regID) {
        this.regID = regID;
    }

    /**
     * @return the taxSystemCode
     */
    public int getTaxSystemCode() {
        return taxSystemCode;
    }

    /**
     * @param taxSystemCode the taxSystemCode to set
     */
    public void setTaxSystemCode(int taxSystemCode) {
        this.taxSystemCode = taxSystemCode;
    }

    /**
     * @return the mode
     */
    public int getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * @return the reasonCode
     */
    public int getReasonCode() {
        return reasonCode;
    }

    /**
     * @param reasonCode the reasonCode to set
     */
    public void setReasonCode(int reasonCode) {
        this.reasonCode = reasonCode;
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

    /**
     * @return the macNumber
     */
    public long getMacNumber() {
        return macNumber;
    }

    /**
     * @param macNumber the macNumber to set
     */
    public void setMacNumber(long macNumber) {
        this.macNumber = macNumber;
    }

    public PrinterDateTime getDocDate() {
        return docDate;
    }

    public void setDocDate(PrinterDateTime value) {
        this.docDate = value;
    }

    /**
     * @return the fdoTaxID
     */
    public String getFdoTaxID() {
        return fdoTaxID;
    }

    /**
     * @param fdoTaxID the fdoTaxID to set
     */
    public void setFdoTaxID(String fdoTaxID) {
        this.fdoTaxID = fdoTaxID;
    }

    /**
     * @return the extMode
     */
    public int getExtMode() {
        return extMode;
    }

    /**
     * @param extMode the extMode to set
     */
    public void setExtMode(int extMode) {
        this.extMode = extMode;
    }
}
