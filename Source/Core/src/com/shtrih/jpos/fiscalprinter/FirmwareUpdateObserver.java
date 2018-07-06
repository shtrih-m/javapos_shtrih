package com.shtrih.jpos.fiscalprinter;

public class FirmwareUpdateObserver {

    public void OnUploadingError(Exception exc) {

    }

    public void OnDownloading(int percent, long oldVersion, long newVersion) {

    }

    public void OnUploading(int percent) {

    }

    public void OnWritingTables() {

    }

    public void OnReadingTables() {

    }

    public void OnUpdateSkippedNoSDCard() {

    }

    public void OnFirmwareDownloadingError(Exception e) {

    }

    public void OnNoNewFirmware() {

    }

    public void OnCheckingForUpdate() {

    }
}
