package com.shtrih.fiscalprinter.command;

/**
 * @author P.Zhirkov
 */
public class FSDocumentInfo {

    private final int fsDocType;
    private final boolean sentToOFD;
    private final byte[] data;
    private final PrinterDateTime dateTime;
    private final long documentNumber;
    private final long fp;

    public FSDocumentInfo(int fsDocType, boolean sentToOFD, byte[] data, PrinterDateTime dateTime, long documentNumber, long fp) {

        this.fsDocType = fsDocType;
        this.sentToOFD = sentToOFD;
        this.data = data;
        this.dateTime = dateTime;
        this.documentNumber = documentNumber;
        this.fp = fp;
    }

    public int getDocumentType() {
        return fsDocType;
    }

    public boolean getSentToOFD() {
        return sentToOFD;
    }

    public PrinterDateTime getDateTime() {
        return dateTime;
    }

    public long getFP() {
        return fp;
    }

    public long getDocumentNumber() {
        return documentNumber;
    }

    public byte[] getData() {
        return data;
    }
}
