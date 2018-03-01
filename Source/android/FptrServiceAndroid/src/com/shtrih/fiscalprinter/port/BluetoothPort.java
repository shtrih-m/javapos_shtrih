package com.shtrih.fiscalprinter.port;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author V.Kravtsov
 */
public class BluetoothPort implements PrinterPort {
    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    private int timeout = 5000;
    private int openTimeout = 5000;
    private String portName = "";
    private BluetoothSocket socket = null;
    private static CompositeLogger logger = CompositeLogger.getLogger(BluetoothPort.class);

    public BluetoothPort() {
    }

    private void checkOpened() throws Exception {
        if (socket == null) {
            throw new Exception("Port is not opened");
        }
    }

    public BluetoothSocket getPort() throws Exception {
        checkOpened();
        return socket;
    }

    @Override
    public void setPortName(String portName) throws Exception {
        if (!this.portName.equalsIgnoreCase(portName)) {
            this.portName = portName;
        }
    }

    @Override
    public String getPortName() {
        return portName;
    }

    @Override
    public Object getSyncObject() throws Exception {
        //checkOpened();
        return this;
    }

    @Override
    public boolean isSearchByBaudRateEnabled() {
        return false;
    }

    @Override
    public void setBaudRate(int baudRate) throws Exception {
    }

    public int getOpenTimeout() {
        return openTimeout;
    }

    public void setOpenTimeout(int openTimeout) {
        this.openTimeout = openTimeout;
    }

    private InputStream inputStream;
    private OutputStream outputStream;

    @Override
    public void open(int timeout) throws Exception {
        if (isClosed()) {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                throw new Exception("Bluetooth not supported");
            }

            if (!adapter.isEnabled()) {
                throw new Exception("Bluetooth is not enabled");
            }

            if (adapter.isDiscovering()) {
                adapter.cancelDiscovery();
            }
            switch (adapter.getState()) {
                case BluetoothAdapter.STATE_TURNING_ON: {
                    waitBluetoothAdapterStateOn(adapter, openTimeout);
                    break;

                }
                case BluetoothAdapter.STATE_TURNING_OFF: {
                    throw new Exception("Bluetooth is turning off");
                }
            }
            BluetoothDevice device = adapter.getRemoteDevice(portName);
            if (device == null) {
                throw new Exception("Failed to get BluetoothDevice by address");
            }
            try {

                socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                if (socket == null) {
                    throw new Exception("Failed to get bluetooth device socket");
                }

                socket.connect();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                return;
            } catch (IOException e) {
                close();
            }
            try {
                // IOException - create new socket
                socket = (BluetoothSocket) device.getClass()
                        .getMethod("createRfcommSocket", new Class[]{int.class})
                        .invoke(device, 1);
                if (socket == null) {
                    throw new Exception("Failed to get bluetooth device socket");
                }
                socket.connect();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (Exception e) {
                close();
                throw e;
            }
        }
    }

    private void waitBluetoothAdapterStateOn(BluetoothAdapter adapter,
                                             int timeout) throws Exception {
        long startTime = System.currentTimeMillis();
        for (; ; ) {
            long currentTime = System.currentTimeMillis();
            if (adapter.getState() == BluetoothAdapter.STATE_TURNING_ON) {
                Thread.sleep(100);
            } else {
                break;
            }
            if ((currentTime - startTime) > timeout) {
                throw new Exception("BluetoothAdapter turning on timeout");
            }
        }
    }

    @Override
    public synchronized void close() {
        if (isOpened()) {
            try {
                socket.close();
            } catch (Exception e) {
                logger.error("Bluetooth socket close failed", e);
            }
        }

        inputStream = null;
        outputStream = null;
        socket = null;
    }

    public synchronized boolean isOpened() {
        return socket != null;
    }

    public synchronized boolean isClosed() {
        return socket == null;
    }

    @Override
    public synchronized void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private void connect() throws Exception {
        if (socket == null) {
            open(openTimeout);
        }
    }

    @Override
    public void write(byte[] b) throws Exception {
        connect();
        try {
            outputStream.write(b);
            outputStream.flush();
        } catch (IOException e) {
            close();
            throw e;
        }
    }

    @Override
    public void write(int b) throws Exception {
        byte[] data = new byte[1];
        data[0] = (byte) b;
        write(data);
    }

    @Override
    public byte[] readBytes(int len) throws Exception {
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[i] = (byte) readByte();
        }
        return data;
    }

    @Override
    public int readByte() throws Exception {
        try {
            int result;
            InputStream is = inputStream;
            long startTime = System.currentTimeMillis();
            while (true) {
                if (is.available() == 0) {
                    long currentTime = System.currentTimeMillis();
                    if ((currentTime - startTime) > timeout) {
                        noConnectionError();
                    }
                } else {
                    result = is.read();

                    if (result < 0) {
                        noConnectionError();
                    }

                    return result;
                }
            }
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    private void noConnectionError() throws Exception {
        throw new IOException(Localizer.getString(Localizer.NoConnection));
    }
}
