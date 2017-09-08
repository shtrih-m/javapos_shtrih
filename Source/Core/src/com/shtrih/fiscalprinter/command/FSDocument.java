package com.shtrih.fiscalprinter.command;

/**
 * @author P.Zhirkov
 */
public class FSDocument{

    private final int docType;
    private final boolean sentToOFD;
    private final FSDateTime dateTime;
    private final long docNumber;
    private final long docSign;

    public FSDocument(CommandInputStream in, int docType) throws Exception
    {
        this.docType = docType;
        sentToOFD = in.readByte() != 0;
        dateTime = in.readFSDate();
        docNumber = in.readInt();
        docSign = in.readLong(4);
    }

    /**
     * @return the docType
     */
    public int getDocType() {
        return docType;
    }

    /**
     * @return the sentToOFD
     */
    public boolean isSentToOFD() {
        return sentToOFD;
    }

    /**
     * @return the dateTime
     */
    public FSDateTime getDateTime() {
        return dateTime;
    }

    /**
     * @return the docNumber
     */
    public long getDocNumber() {
        return docNumber;
    }

    /**
     * @return the docSign
     */
    public long getDocSign() {
        return docSign;
    }
    
    
    
}