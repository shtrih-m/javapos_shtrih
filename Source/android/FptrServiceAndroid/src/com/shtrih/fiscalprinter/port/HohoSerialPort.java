/*
 * PrinterPort.java
 *
 * Created on August 30 2007, 12:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author V.Kravtsov
 */
package com.shtrih.fiscalprinter.port;

import java.io.IOException;
import java.util.Properties;
import java.util.Vector;
import com.shtrih.util.Localizer;
import com.shtrih.util.CompositeLogger;
import com.shtrih.hoho.android.usbserial.driver.*;
import com.shtrih.util.StaticContext;

import android.hardware.usb.UsbDeviceConnection;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.util.List;


public class HohoSerialPort implements PrinterPort {

    private int timeout = 1000;
    private int baudRate = 9600;
    private String portName = "";
    private UsbSerialPort port = null;
    private static CompositeLogger logger = CompositeLogger.getLogger(HohoSerialPort.class);
    private static final String ACTION_USB_PERMISSION = "com.shtrih.fiscalprinter.port.USB_PERMISSION";

    /**
     * Creates a new instance of PrinterPort
     */
    public HohoSerialPort()
    {
    }

    private Context getContext() throws Exception {
        Context context = StaticContext.getContext();
        if (context == null) throw new Exception("Context not defined");
        return context;
    }

    public void checkOpened() throws Exception {
        if (port == null) {
            throw new Exception("Port is not opened");
        }
    }

    public String getPortName() {
        return portName;
    }

    public UsbSerialPort getPort() throws Exception {
        checkOpened();
        return port;
    }

    public void setPortName(String portName) throws Exception {
        if (!this.portName.equalsIgnoreCase(portName)) {
            close();
            this.portName = portName;
        }
    }

    public void setBaudRate(int baudRate) throws Exception {
        if (this.baudRate != baudRate) {
            this.baudRate = baudRate;
            updateBaudRate();
        }
    }

    public synchronized void open(int timeout) throws Exception {

        if (isClosed())
        {
            logger.debug("open");

            UsbManager usbManager = (UsbManager) getContext().getSystemService(Context.USB_SERVICE);
            List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
            logger.debug("drivers.size: " + drivers.size());

            // Show all devices
            for (int i = 0; i < drivers.size(); i++) {
                UsbDevice device = drivers.get(i).getDevice();
                logger.debug("UsbDevice: " + device.getDeviceName());
            }

            UsbSerialDriver driver = null;
            if (drivers.size() == 0){
                throw new Exception("No driver available");
            }
            if (portName.isEmpty()){
                driver = drivers.get(0);
            } else {
                for (int i = 0; i < drivers.size(); i++) {
                    UsbDevice device = drivers.get(i).getDevice();
                    logger.debug("UsbDevice: " + device.getDeviceName());
                    if (device.getDeviceName().equalsIgnoreCase(portName))
                    {
                        driver = drivers.get(i);
                        break;
                    }
                }
            }
            if (driver == null){
                throw new Exception("Driver not found");
            }
            port = driver.getPorts().get(0);
            UsbDeviceConnection connection = usbManager.openDevice(driver.getDevice());
            port.open(connection);
            port.setParameters(baudRate, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1,
                    UsbSerialPort.PARITY_NONE);
            port.purgeHwBuffers(true, true);
        }
    }

    public synchronized void close() {

        if (isOpened())
        {
            try {
                port.close();
                port = null;
            }
            catch(IOException e){
                logger.error(e);
            }
        }
    }

    public synchronized boolean isOpened() {
        return port != null;
    }

    public synchronized boolean isClosed() {
        return port == null;
    }

    public synchronized void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public synchronized void updateBaudRate() throws Exception {
        if (isOpened())
        {
            port.setParameters(baudRate, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1,
                    UsbSerialPort.PARITY_NONE);
        }
    }

    private void noConnectionError() throws Exception {
        throw new IOException(Localizer.getString(Localizer.NoConnection));
    }

    public void write(byte[] b) throws Exception {
        open(0);
        getPort().write(b, timeout);
    }

    public void write(int b) throws Exception {
        byte[] data = new byte[1];
        data[0] = (byte) b;
        write(data);
    }

    public byte[] readBytes(int len) throws Exception {
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[i] = (byte) doReadByte();
        }
        return data;
    }

    public int readByte() throws Exception {
        int b = doReadByte();
        return b;
    }

    public int doReadByte() throws Exception {
        open(0);
        byte[] buffer = new byte[1];
        getPort().read(buffer, timeout);
        return buffer[0];
    }

    public boolean find(Properties properties) throws Exception {
        return false;
    }

    public void initialize(Properties properties) throws Exception {
    }

    public static String[] getPortNames() throws Exception
    {
        Vector result = new Vector();
        return (String[]) result.toArray(new String[0]);
    }

    public Object getSyncObject() throws Exception {
        return port;
    }

    public boolean isSearchByBaudRateEnabled() {
        return true;
    }
}


/*
*
*
* */