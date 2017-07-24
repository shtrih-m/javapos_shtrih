package com.shtrih.fiscalprinter.command;

/**
 * @author P.Zhirkov
 */
public class FSCommunicationStatus {

    private final int unsentDocumentsCount;
    private final int firstUnsentDocumentNumber;
    private final FSDateTime firstUnsentDocumentDateTime;
    private final int communicationState;
    private final int messgeReadingStatus;

    public FSCommunicationStatus(int unsentDocumentsCount, int firstUnsentDocumentNumber, FSDateTime firstUnsentDocumentDateTime, int communicationState, int messgeReadingStatus) {
        this.unsentDocumentsCount = unsentDocumentsCount;
        this.firstUnsentDocumentDateTime = firstUnsentDocumentDateTime;
        this.firstUnsentDocumentNumber = firstUnsentDocumentNumber;
        this.communicationState = communicationState;
        this.messgeReadingStatus = messgeReadingStatus;
    }

    public FSDateTime getFirstUnsentDocumentDateTime() {
        return firstUnsentDocumentDateTime;
    }

    public int getFirstUnsentDocumentNumber() {
        return firstUnsentDocumentNumber;
    }

    public int getUnsentDocumentsCount() {
        return unsentDocumentsCount;
    }

    public int getCommunicationState() {
        return communicationState;
    }

    public int getMessgeReadingStatus() {
        return messgeReadingStatus;
    }
}
