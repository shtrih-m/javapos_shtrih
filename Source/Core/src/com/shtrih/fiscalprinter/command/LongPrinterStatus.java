/*
 * LongPrinterStatus.java
 *
 * Created on April 2 2008, 19:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.util.CompositeLogger;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author V.Kravtsov
 */
public class LongPrinterStatus implements PrinterConst {

    private int operatorNumber = 0;
    private int flags = 0;
    private int mode = 0;
    private int submode = 0;
    private String firmwareVersion = "";
    private int firmwareBuild = 0;
    private PrinterDate firmwareDate = new PrinterDate();
    private int logicalNumber = 0;
    private int documentNumber = 0;
    private int portNumber = 0;
    private String fmFirmwareVersion = "";
    private int fmFirmwareBuild = 0;
    private PrinterDate fmFirmwareDate = new PrinterDate();
    private PrinterDate date = new PrinterDate();
    private PrinterTime time = new PrinterTime();
    private int fmFlags = 0;
    private int serialNumber = 0;
    private int dayNumber = 0;
    private int freeRecordInFM = 0;
    private int registrationNumber = 0;
    private int freeRegistration = 0;
    private long fiscalID = 0;
    public static CompositeLogger logger = CompositeLogger.getLogger(LongPrinterStatus.class);

    /**
     * Creates a new instance of LongPrinterStatus
     */
    public LongPrinterStatus() {
    }

    public LongPrinterStatus(CommandInputStream in) throws Exception {
        byte[] version = new byte[2];
        operatorNumber = in.readByte();
        version[0] = (byte) in.readByte();
        version[1] = (byte) in.readByte();
        firmwareVersion = new String(version, in.getCharsetName());
        firmwareBuild = in.readShort();
        firmwareDate = in.readDateDMY();
        logicalNumber = in.readByte();
        documentNumber = in.readShort();
        flags = in.readShort();
        mode = in.readByte();
        submode = in.readByte();
        portNumber = in.readByte();

        version[0] = (byte) in.readByte();
        version[1] = (byte) in.readByte();
        String s = new String(version, in.getCharsetName());
        fmFirmwareVersion = s.charAt(0) + "." + s.charAt(1);

        fmFirmwareBuild = in.readShort();
        fmFirmwareDate = in.readDateDMY();
        date = in.readDateDMY();
        time = in.readTimeHMS();
        fmFlags = in.readByte();
        serialNumber = in.readInt();
        dayNumber = in.readShort();
        freeRecordInFM = in.readShort();
        registrationNumber = in.readByte();
        freeRegistration = in.readByte();
        fiscalID = in.readLong(6);
    }

    public int getOperatorNumber() {
        return operatorNumber;
    }

    public int getFlags() {
        return flags;
    }

    public int getMode() {
        return mode;
    }

    public int getSubmode() {
        return submode;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public int getFirmwareBuild() {
        return firmwareBuild;
    }

    public PrinterDate getFirmwareDate() {
        return firmwareDate;
    }

    public int getLogicalNumber() {
        return logicalNumber;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getFMFirmwareVersion() {
        return getFmFirmwareVersion();
    }

    public int getFMFirmwareBuild() {
        return getFmFirmwareBuild();
    }

    public PrinterDate getFMFirmwareDate() {
        return getFmFirmwareDate();
    }

    public PrinterDate getDate() {
        return date;
    }

    public PrinterTime getTime() {
        return time;
    }

    public int getFMFlags() {
        return getFmFlags();
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public int getFMFreeRecords() {
        return getFreeRecordInFM();
    }

    public int getRegistrationNumber() {
        return registrationNumber;
    }

    public int getFreeRegistration() {
        return freeRegistration;
    }

    public long getFiscalID() {
        return fiscalID;
    }

    public String getFiscalIDText() {
        return fiscalID2Text(getFiscalID());
    }

    public String getSerial() {
        return serialNumber2Text(getSerialNumber());
    }

    public String getSerialLong() {
        return String.format("%.016d", getSerialNumber());
    }

    public static String serialNumber2Text(long value) {
        if (value == 0xFFFFFFFF) {
            return "not entered";
        } else {
            return Long.toString(value);
        }
    }

    public PrinterMode getPrinterMode() {
        return new PrinterMode(getMode());
    }

    public PrinterSubmode getPrinterSubmode() {
        return new PrinterSubmode(getSubmode());
    }

    public PrinterFlags getPrinterFlags() {
        return new PrinterFlags(getFlags());
    }

    public FiscalMemoryFlags getFiscalMemoryFlags() {
        return new FiscalMemoryFlags(getFmFlags());
    }

    // "1.0, 1234, 12.02.2006"
    public String getFirmwareRevision() {
        return getFirmwareVersion() + ", " + String.valueOf(getFirmwareBuild())
                + ", " + getFirmwareDate().toString();
    }

    public static String fiscalID2Text(long value) {
        if (value == 0xFFFFFFFFFFFFL) {
            return "not entered";
        } else {
            return Long.toString(value);
        }
    }

    /**
     * @param operatorNumber the operatorNumber to set
     */
    public void setOperatorNumber(int operatorNumber) {
        this.operatorNumber = operatorNumber;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * @param submode the submode to set
     */
    public void setSubmode(int submode) {
        this.submode = submode;
    }

    /**
     * @param firmwareVersion the firmwareVersion to set
     */
    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    /**
     * @param firmwareBuild the firmwareBuild to set
     */
    public void setFirmwareBuild(int firmwareBuild) {
        this.firmwareBuild = firmwareBuild;
    }

    /**
     * @param firmwareDate the firmwareDate to set
     */
    public void setFirmwareDate(PrinterDate firmwareDate) {
        this.firmwareDate = firmwareDate;
    }

    /**
     * @param logicalNumber the logicalNumber to set
     */
    public void setLogicalNumber(int logicalNumber) {
        this.logicalNumber = logicalNumber;
    }

    /**
     * @param documentNumber the documentNumber to set
     */
    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * @param portNumber the portNumber to set
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * @return the fmFirmwareVersion
     */
    public String getFmFirmwareVersion() {
        return fmFirmwareVersion;
    }

    /**
     * @param fmFirmwareVersion the fmFirmwareVersion to set
     */
    public void setFmFirmwareVersion(String fmFirmwareVersion) {
        this.fmFirmwareVersion = fmFirmwareVersion;
    }

    /**
     * @return the fmFirmwareBuild
     */
    public int getFmFirmwareBuild() {
        return fmFirmwareBuild;
    }

    /**
     * @param fmFirmwareBuild the fmFirmwareBuild to set
     */
    public void setFmFirmwareBuild(int fmFirmwareBuild) {
        this.fmFirmwareBuild = fmFirmwareBuild;
    }

    /**
     * @return the fmFirmwareDate
     */
    public PrinterDate getFmFirmwareDate() {
        return fmFirmwareDate;
    }

    /**
     * @param fmFirmwareDate the fmFirmwareDate to set
     */
    public void setFmFirmwareDate(PrinterDate fmFirmwareDate) {
        this.fmFirmwareDate = fmFirmwareDate;
    }

    /**
     * @param date the date to set
     */
    public void setDate(PrinterDate date) {
        this.date = date;
    }

    /**
     * @param time the time to set
     */
    public void setTime(PrinterTime time) {
        this.time = time;
    }

    /**
     * @return the fmFlags
     */
    public int getFmFlags() {
        return fmFlags;
    }

    /**
     * @param fmFlags the fmFlags to set
     */
    public void setFmFlags(int fmFlags) {
        this.fmFlags = fmFlags;
    }

    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @param dayNumber the dayNumber to set
     */
    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    /**
     * @return the freeRecordInFM
     */
    public int getFreeRecordInFM() {
        return freeRecordInFM;
    }

    /**
     * @param freeRecordInFM the freeRecordInFM to set
     */
    public void setFreeRecordInFM(int freeRecordInFM) {
        this.freeRecordInFM = freeRecordInFM;
    }

    /**
     * @param registrationNumber the registrationNumber to set
     */
    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    /**
     * @param freeRegistration the freeRegistration to set
     */
    public void setFreeRegistration(int freeRegistration) {
        this.freeRegistration = freeRegistration;
    }

    /**
     * @param fiscalID the fiscalID to set
     */
    public void setFiscalID(long fiscalID) {
        this.fiscalID = fiscalID;
    }

    public PrinterStatus getPrinterStatus() {
        PrinterStatus printerStatus = new PrinterStatus();
        printerStatus.setMode(mode);
        printerStatus.setSubmode(submode);
        printerStatus.setFlags(flags);
        printerStatus.setOperator(operatorNumber);
        return printerStatus;
    }

    public boolean isFiscalized() {
        return registrationNumber != 0;
    }

    public int getCurrentShiftNumber() {
        int dayNumber =  getDayNumber() + 1;

        if(dayNumber == 10000)
            dayNumber = 1;

        return dayNumber;
    }
    
    public Calendar getDateTime() throws Exception 
    {
        Calendar printerDate = GregorianCalendar.getInstance();
        printerDate.set(date.getYear(), date.getMonth() - 1, date.getDay(),
            time.getHour(), time.getMin(), time.getSec());
        return printerDate;
    }

}
