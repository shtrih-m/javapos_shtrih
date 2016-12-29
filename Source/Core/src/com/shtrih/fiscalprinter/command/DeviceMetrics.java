/*
 * DeviceMetrics.java
 *
 * Created on 8 Июль 2010 г., 18:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */
public class DeviceMetrics {

    private int deviceType;
    private int deviceSubType;
    private int protocolVersion;
    private int protocolSubVersion;
    private int model;
    private int language;
    private String deviceName;

    /**
     * Creates a new instance of DeviceMetrics
     */
    public DeviceMetrics() {
    }

    public int getDeviceType() {
        return deviceType;
    }

    public int getDeviceSubType() {
        return deviceSubType;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public int getProtocolSubVersion() {
        return protocolSubVersion;
    }

    public int getModel() {
        return model;
    }

    public int getLanguage() {
        return language;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public boolean getCapPrintGraphicsLine() {
        return (getProtocolVersion() >= 1) && (getProtocolSubVersion() >= 9);
    }

    public boolean getCapGraphicsEx() {
        return (getProtocolVersion() >= 1) && (getProtocolSubVersion() >= 3);
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceSubType(int deviceSubType) {
        this.deviceSubType = deviceSubType;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setProtocolSubVersion(int protocolSubVersion) {
        this.protocolSubVersion = protocolSubVersion;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

}
