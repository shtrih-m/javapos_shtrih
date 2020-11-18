package com.shtrih.fiscalprinter.command;

/**
 * @author P.Zhirkov
 */
public class FSDocument{

    private final int docType;
    private final boolean ticketReceived;
    private final FSDateTime dateTime;
    private final long docNumber;
    private final long docSign;
    private final byte[] data;

    public FSDocument(CommandInputStream in, int docType) throws Exception
    {
        this.docType = docType;
        ticketReceived = in.readByte() != 0;
        dateTime = in.readFSDateTime();
        docNumber = in.readInt();
        docSign = in.readLong(4);
        in.mark();
        data = in.readBytesToEnd();
        in.reset();
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
    public boolean isTicketReceived() {
        return ticketReceived;
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
    
    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
}