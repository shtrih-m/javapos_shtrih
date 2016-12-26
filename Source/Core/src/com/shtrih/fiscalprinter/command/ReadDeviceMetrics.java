/*
 * ReadDeviceMetrics.java
 *
 * Created on 16 January 2009, 16:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/**
 * **************************************************************************
 * Get Device Type Command: FCH. Length: 1 byte. Answer: FCH. Length: (8+X)
 * bytes. · Result Code (1 byte) · Device type (1 byte) 0…255 · Device subtype
 * (1 byte) 0…255 · Protocol version supported by device (1 byte) 0…255 ·
 * Subprotocol version supported by device (1 byte) 0…255 · Device model (1
 * byte) 0…255 · Language (1 byte) 0…255, «0» – Russian, «1» – English · Device
 * name (X bytes) string of WIN1251 code page characters; string length in bytes
 * depends on device model
 */
public final class ReadDeviceMetrics extends PrinterCommand {

    // out
    private final DeviceMetrics deviceMetrics = new DeviceMetrics();

    /**
     * Creates a new instance of ReadDeviceMetrics
     */
    public ReadDeviceMetrics() {
    }

    public final int getCode() {
        return 0xFC;
    }

    public final String getText() {
        return "Get device metrics";
    }

    public final void encode(CommandOutputStream out) throws Exception {
    }

    public final void decode(CommandInputStream in) throws Exception {
        deviceMetrics.setDeviceType(in.readByte());
        deviceMetrics.setDeviceSubType(in.readByte());
        deviceMetrics.setProtocolVersion(in.readByte());
        deviceMetrics.setProtocolSubVersion(in.readByte());
        deviceMetrics.setModel(in.readByte());
        deviceMetrics.setLanguage(in.readByte());
        deviceMetrics.setDeviceName(in.readString().trim());
    }

    public DeviceMetrics getDeviceMetrics() {
        return deviceMetrics;
    }

    public boolean getIsRepeatable() {
        return true;
    }
}
