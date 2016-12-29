/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.cashdrawer;

/**
 *
 * @author V.Kravtsov
 */
import jpos.BaseControl;
import jpos.CashDrawer;
import jpos.CashDrawerControl110;
import jpos.CashDrawerControl113;
import jpos.JposException;
import jpos.events.DirectIOListener;
import jpos.events.StatusUpdateListener;

import com.shtrih.jpos.cashdrawer.ShtrihCashDrawerConst;

public class ShtrihCashDrawer110 implements BaseControl, CashDrawerControl113 {

    private final String encoding;
    private final CashDrawerControl110 driver;

    /** Creates a new instance of ShtrihCashDrawer */
    public ShtrihCashDrawer110(CashDrawerControl113 driver, String encoding) {
        this.driver = driver;
        this.encoding = encoding;
    }

    public ShtrihCashDrawer110(CashDrawerControl113 driver) {
        this.driver = driver;
        this.encoding = "";
    }

    public ShtrihCashDrawer110(String encoding) {
        this.driver = new CashDrawer();
        this.encoding = encoding;
    }

    
    public String getCheckHealthText() throws JposException {

        return driver.getCheckHealthText();
    }

    
    public boolean getClaimed() throws JposException {

        return driver.getClaimed();
    }

    
    public String getDeviceControlDescription() {

        return driver.getDeviceControlDescription();
    }

    
    public int getDeviceControlVersion() {

        return driver.getDeviceControlVersion();
    }

    
    public boolean getDeviceEnabled() throws JposException {

        return driver.getDeviceEnabled();
    }

    
    public void setDeviceEnabled(boolean deviceEnabled) throws JposException {

        driver.setDeviceEnabled(deviceEnabled);
    }

    
    public String getDeviceServiceDescription() throws JposException {

        return driver.getDeviceServiceDescription();
    }

    
    public int getDeviceServiceVersion() throws JposException {

        return driver.getDeviceServiceVersion();
    }

    
    public boolean getFreezeEvents() throws JposException {

        return driver.getFreezeEvents();
    }

    
    public void setFreezeEvents(boolean freezeEvents) throws JposException {

        driver.setFreezeEvents(freezeEvents);
    }

    
    public String getPhysicalDeviceDescription() throws JposException {

        return driver.getPhysicalDeviceDescription();
    }

    
    public String getPhysicalDeviceName() throws JposException {

        return driver.getPhysicalDeviceName();
    }

    
    public int getState() {

        return driver.getState();
    }

    
    public void claim(int timeout) throws JposException {

        driver.claim(timeout);
    }

    
    public void close() throws JposException {

        driver.close();
    }

    
    public void checkHealth(int level) throws JposException {

        driver.checkHealth(level);
    }

    
    public void directIO(int command, int[] data, Object object)
            throws JposException {

        driver.directIO(command, data, object);
    }

    
    public void open(String logicalDeviceName) throws JposException {

        driver.open(logicalDeviceName);
    }

    
    public void release() throws JposException {

        driver.release();
    }

    
    public boolean getCapStatus() throws JposException {

        return driver.getCapStatus();
    }

    
    public boolean getDrawerOpened() throws JposException {

        return driver.getDrawerOpened();
    }

    
    public void openDrawer() throws JposException {

        driver.openDrawer();
    }

    
    public void waitForDrawerClose(int beepTimeout, int beepFrequency,
            int beepDuration, int beepDelay) throws JposException {

        driver.waitForDrawerClose(beepTimeout, beepFrequency, beepDuration,
                beepDelay);
    }

    
    public void addDirectIOListener(DirectIOListener l) {

        driver.addDirectIOListener(l);
    }

    
    public void removeDirectIOListener(DirectIOListener l) {

        driver.removeDirectIOListener(l);
    }

    
    public void addStatusUpdateListener(StatusUpdateListener l) {

        driver.addStatusUpdateListener(l);
    }

    
    public void removeStatusUpdateListener(StatusUpdateListener l) {

        driver.removeStatusUpdateListener(l);
    }

    
    public int getCapPowerReporting() throws JposException {

        return driver.getCapPowerReporting();
    }

    
    public int getPowerNotify() throws JposException {

        return driver.getPowerNotify();
    }

    
    public void setPowerNotify(int powerNotify) throws JposException {

        driver.setPowerNotify(powerNotify);
    }

    
    public int getPowerState() throws JposException {

        return driver.getPowerState();
    }

    
    public boolean getCapStatusMultiDrawerDetect() throws JposException {

        return driver.getCapStatusMultiDrawerDetect();
    }

    
    public boolean getCapStatisticsReporting() throws JposException {

        return driver.getCapStatisticsReporting();
    }

    
    public boolean getCapUpdateStatistics() throws JposException {

        return driver.getCapUpdateStatistics();
    }

    
    public void resetStatistics(String statisticsBuffer) throws JposException {

        driver.resetStatistics(statisticsBuffer);
    }

    
    public void retrieveStatistics(String[] statisticsBuffer)
            throws JposException {

        driver.retrieveStatistics(statisticsBuffer);
    }

    
    public void updateStatistics(String statisticsBuffer) throws JposException {

        driver.updateStatistics(statisticsBuffer);
    }

    
    public boolean getCapCompareFirmwareVersion() throws JposException {

        return driver.getCapCompareFirmwareVersion();
    }

    
    public boolean getCapUpdateFirmware() throws JposException {

        return driver.getCapUpdateFirmware();
    }

    
    public void compareFirmwareVersion(String firmwareFileName, int[] result)
            throws JposException {

        driver.compareFirmwareVersion(firmwareFileName, result);
    }

    
    public void updateFirmware(String firmwareFileName) throws JposException {

        driver.updateFirmware(firmwareFileName);
    }

    public void setParameter(int paramType, int paramValue)
            throws JposException {
        int data[] = new int[1];
        int object[] = new int[1];
        data[0] = paramType;
        object[0] = paramValue;
        directIO(ShtrihCashDrawerConst.SMCASH_DIO_SET_DRIVER_PARAMETER, data,
                object);
    }

    public int getParameter(int paramType) throws JposException {
        int data[] = new int[1];
        int object[] = new int[1];
        data[0] = paramType;
        directIO(ShtrihCashDrawerConst.SMCASH_DIO_GET_DRIVER_PARAMETER, data,
                object);
        return object[0];
    }

    public void openCashDrawer(int drawerNumber) throws JposException {
        int[] data = new int[1];
        data[0] = drawerNumber;
        directIO(ShtrihCashDrawerConst.SMCASH_DIO_OPEN_DRAWER, data, null);
    }

    public boolean readDrawerState() throws JposException {
        int[] data = new int[1];
        data[0] = 0;
        directIO(ShtrihCashDrawerConst.SMCASH_DIO_READ_DRAWER_STATE, data, null);
        return data[0] != 0;
    }

    public int getDrawerNumber() throws JposException {
        return getParameter(ShtrihCashDrawerConst.SMCASH_PARAM_DRAWER_NUMBER);
    }

    public void setDrawerNumber(int value) throws JposException {
        setParameter(ShtrihCashDrawerConst.SMCASH_PARAM_DRAWER_NUMBER, value);
    }

}
