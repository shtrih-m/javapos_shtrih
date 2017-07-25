package com.shtrih.fiscalprinter.command;

/**
 * @author P.Zhirkov
 */
public class FSStatusInfo {

    private final FSStatus status;
    private final FSDocType docType;
    private final boolean isDocReceived;
    private final boolean isDayOpened;
    private final int flags;
    private final FSDateTime dateTime;
    private final String fsSerial;
    private final long docNumber;

    public FSStatusInfo(FSStatus status, FSDocType docType, boolean isDocReceived, boolean isDayOpened, int flags, FSDateTime dateTime, String fsSerial, long docNumber){
        this.status = status;
        this.docType = docType;
        this.isDocReceived = isDocReceived;
        this.isDayOpened = isDayOpened;
        this.flags = flags;
        this.dateTime = dateTime;
        this.fsSerial = fsSerial;
        this.docNumber = docNumber;
    }

    public FSStatus getStatus() {
        return status;
    }

    public FSDocType getDocType() {
        return docType;
    }

    public boolean isDocReceived() {
        return isDocReceived;
    }

    public boolean isDayOpened() {
        return isDayOpened;
    }

    public int getFlags() {
        return flags;
    }

    public FSDateTime getDateTime() {
        return dateTime;
    }

    public String getFsSerial() {
        return fsSerial;
    }

    public long getDocNumber() {
        return docNumber;
    }
}
