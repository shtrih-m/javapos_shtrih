/*
 * DeviceMetrics.java
 *
 * Created on 8 Июль 2010 г., 18:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import static com.shtrih.fiscalprinter.command.PrinterConst.PRINTER_MODEL_SHTRIH_NANO_F;

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

    /**
     * Устройство все прочие Штрих-устройства выпускаемые как стационарные, например Retail-01Ф
     */
    public boolean isDesktop() {
        return !isShtrihNano() &&
                !isShtrihMobile() &&
                !isCashCore() &&
                !isElves();
    }

    /**
     * Устройство на основе Кассовое ядро
     */
    public boolean isCashCore() {
        return getModel() == 16 ||
                getModel() == 20 ||
                getModel() == 21 ||
                getModel() == 45 ||
                getModel() == 46; // КЯ
    }

    /**
     * Устройство Штрих-MOBILE-Ф
     */
    public boolean isShtrihMobile() {
        return getModel() == PrinterConst.PRINTER_MODEL_SHTRIH_MOBILE_F;
    }

    /**
     * Устройство Штрих-NANO-Ф
     */
    public boolean isShtrihNano() {
        return getModel() == PrinterConst.PRINTER_MODEL_SHTRIH_NANO_F;
    }

    /**
     * ШТРИХ-ЭЛВЕС-МФ
     */
    public boolean isElves() {
        return getModel() == PrinterConst.PRINTER_MODEL_SHTRIH_ELVES_MF;
    }
}
