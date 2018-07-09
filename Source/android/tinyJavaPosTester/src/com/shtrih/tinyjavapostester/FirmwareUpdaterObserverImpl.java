package com.shtrih.tinyjavapostester;

import com.shtrih.jpos.fiscalprinter.FirmwareUpdateObserver;

public class FirmwareUpdaterObserverImpl extends FirmwareUpdateObserver {

    private MainViewModel vm;

    public FirmwareUpdaterObserverImpl(MainViewModel vm) {

        this.vm = vm;
    }

    @Override
    public void OnCheckingForUpdate() {
        setText("SCoC: checking for firmware update");
    }

    @Override
    public void OnDownloading(int percent, long oldVersion, long newVersion) {
        setText("SCoC: downloading firmware v" + newVersion + " " + percent + "%");
    }

    @Override
    public void OnUploadingError(Exception exc) {
        setText("SCoC: firmware uploading failed \"" + exc.getMessage() + "\"");
    }

    @Override
    public void OnUploading(int percent) {
        setText("SCoC: uploading firmware " + percent + "%");
    }

    @Override
    public void OnWritingTables() {
        setText("SCoC: restoring tables");
    }

    @Override
    public void OnReadingTables() {
        setText("SCoC: saving tables");
    }

    @Override
    public void OnUpdateSkippedNoSDCard() {
        setText("SCoC: firmware update skipped no SD card");
    }

    @Override
    public void OnFirmwareDownloadingError(Exception exc) {
        setText("SCoC: firmware downloading error \"" + exc.getMessage() + "\"");
    }

    @Override
    public void OnNoNewFirmware() {
        setText("SCoC: no new firmware available");
    }

    @Override
    public void OnUploadingDone(long oldFirmwareVersion, long newFirmwareVersion) {
        setText("SCoC: firmware updated from v" + oldFirmwareVersion + " to " + newFirmwareVersion);
    }

    private void setText(final String msg) {
        vm.ScocUpdaterStatus.set(msg);
    }
}
