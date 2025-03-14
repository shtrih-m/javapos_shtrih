package com.shtrih.fiscalprinter.port;

import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.shtrih.util.CompositeLogger;
import com.shtrih.util.StaticContext;
import com.shtrih.util.Localizer;
import com.shtrih.util.Time;
import com.shtrih.util.ByteUtils;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author V.Kravtsov
 */
public class BluetoothPort implements PrinterPort2 {
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //private static final UUID RFCOMM_UUID = UUID.fromString("00000003-0000-1000-8000-00805f9b34fb");

    private IPortEvents events;
    private int timeout = 5000;
    private int openTimeout = 5000;
    private String portName = "";
    private boolean portOpened = true;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothDevice device = null;
    private BluetoothSocket socket = null;
    private boolean receiverRegistered = false;
    private static CompositeLogger logger = CompositeLogger.getLogger(BluetoothPort.class);

    public BluetoothPort() {
    }

    public BluetoothSocket getSocket() throws Exception {
        open(openTimeout);
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

    @Override
    public void open(int timeout) throws Exception {
        if (isOpened()) return;

        openPort(timeout);
        portOpened = true;
    }

    public void openPort(int timeout) throws Exception
    {
        Thread.sleep(0); // check thread interrupted
        BluetoothAdapter adapter = getBluetoothAdapter();
        if (BluetoothAdapter.checkBluetoothAddress(portName)) {
            // portName is valid MAC address
            device = adapter.getRemoteDevice(portName);
            connectDevice(device);
        } else {
            // portName is deviceName prefix, for example "SHTRIH-NANO-F"
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            for (BluetoothDevice device : devices) {
                if (device.getName().startsWith(portName)) {
                    try {
                        connectDevice(device);
                        return;
                    } catch (Exception e) {
                        logger.error("Failed to connect device " +
                                device.getName() + ", " + device.getAddress());
                    }
                }
            }
            throw new Exception("Failed to connect any bonded devices");
        }
    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            logger.debug("BroadcastReceiver: " + intent);

            switch (action)
            {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                {
                    int  state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, Short.MIN_VALUE);
                    if ((state == BluetoothAdapter.STATE_TURNING_OFF)||(state == BluetoothAdapter.STATE_OFF)){
                        disconnect();
                    }
                    break;
                }

                case BluetoothDevice.ACTION_FOUND:
                {
                    int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                    logger.debug("RSSI: " + rssi + " dBm");
                    break;
                }

                case BluetoothDevice.ACTION_ACL_CONNECTED: {
                    BluetoothDevice btdevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (btdevice.equals(device)) {
                        logger.debug("BluetoothDevice.ACTION_ACL_CONNECTED");
                        if (events != null) {
                            events.onConnect();
                        }
                    }
                    break;
                }

                case BluetoothDevice.ACTION_ACL_DISCONNECTED: {
                    BluetoothDevice btdevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (btdevice.equals(device)) {
                        logger.debug("BluetoothDevice.ACTION_ACL_DISCONNECTED");
                        disconnect();
                        //startConnectThread();
                    }
                    break;
                }
            }
        }
    };

    public void disconnect() {
        if (events != null) {
            events.onDisconnect();
        }

        if (isOpened()) {
            logger.debug("socket.close");
            try {
                socket.close();
                socket = null;
            } catch (Exception e) {
                logger.error("Failed to close socket ", e);
            }
        }
    }

    public void startConnectThread()
    {
        if (portOpened) {
            logger.debug("startConnectThread");
            Thread connectThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    connectProc();
                }
            });
            connectThread.start();
            logger.debug("startConnectThread: OK");
        }
    }

    public void connectProc() {
        logger.debug("connectProc.start");
        try {
            while (!Thread.currentThread().isInterrupted())
            {
                Thread.sleep(0);
                open(0);
            }
        } catch (InterruptedException e)
        {
            logger.error("connectProc: ", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("connectProc: ", e);
        }
        logger.debug("connectProc.end");
    }

    private void connectDevice(BluetoothDevice device) throws Exception {
        if (device == null) {
            throw new Exception("Failed to get BluetoothDevice by address");
        }

            socket = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
            if (socket == null) {
                throw new Exception("Failed to get bluetooth device socket");
            }

            socket.connect();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            if (events != null) {
                events.onConnect();
            }
            registerReceiver();
    }

    private void registerReceiver()
    {
        logger.debug("registerReceiver");
        try {
            // events
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

            Context context = StaticContext.getContext();
            if (context != null) {
                context.registerReceiver(mBroadcastReceiver, filter);
                receiverRegistered = true;
                logger.debug("Receiver registered");
            }
        } catch (Exception e) {
            logger.error("Failed to register receiver, ", e);
        }
    }

    private BluetoothAdapter getBluetoothAdapter() throws Exception {
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
        return adapter;
    }

    private void waitBluetoothAdapterStateOn(BluetoothAdapter adapter,
                                             int timeout) throws Exception {
        long startTime = System.currentTimeMillis();
        for (; ; ) {
            long currentTime = System.currentTimeMillis();
            if (adapter.getState() == BluetoothAdapter.STATE_TURNING_ON) {
                Time.delay(100);
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
        if (isOpened())
        {
            logger.debug("close()");
            try {
                if (receiverRegistered) {
                    receiverRegistered = false;
                    Context context = StaticContext.getContext();
                    if (context != null) {
                        logger.debug("unregisterReceiver");
                        context.unregisterReceiver(mBroadcastReceiver);
                    }
                }
                socket.close();
                socket = null;
            } catch (Exception e) {
                logger.error("Bluetooth socket close failed", e);
            }
        }
        portOpened = false;
    }

    public synchronized boolean isOpened() {
        return socket != null;
    }

    public synchronized boolean isClosed() {
        return !isOpened();
    }

    @Override
    public synchronized void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private void connect() throws Exception {
        if (isClosed()) {
            open(openTimeout);
        }
    }

    @Override
    public void write(byte[] b) throws Exception {
        connect();
        outputStream.write(b);
    }

    @Override
    public void write(int b) throws Exception {
        byte[] data = new byte[1];
        data[0] = (byte) b;
        write(data);
    }

    @Override
    public byte[] readBytes(int len) throws Exception {
        return read(len);
    }

    @Override
    public int readByte() throws Exception
    {
        return ByteUtils.byteToInt(read(1)[0]);
    }

    public int readByte2() throws Exception {
        int result;
        long startTime = System.currentTimeMillis();
        while (true) {
            if (inputStream.available() == 0) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - startTime) > timeout) {
                    noConnectionError();
                }
            } else {
                result = inputStream.read();
                if (result < 0) {
                    noConnectionError();
                }

                return result;
            }
        }
    }

    public byte[] read(int len) throws Exception {
        checkOpened();
        int offset = 0;
        byte[] buffer = new byte[len];
        long startTime = System.currentTimeMillis();
        while (len > 0) {
            int count = inputStream.read(buffer, offset, len);
            if (count < 0 ) {
                throw new IOException("Socket closed");
            }
            len -= count;
            offset += count;
            if (len == 0) break;

            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) > timeout) {
                noConnectionError();
            }
        }
        return buffer;
    }

    private void noConnectionError() throws Exception {
        throw new IOException(Localizer.getString(Localizer.NoConnection));
    }

    public String[] getPortNames() throws Exception
    {
        Set<String> ports = new HashSet<>();
        Set<BluetoothDevice> devices = getBluetoothAdapter().getBondedDevices();
        for (BluetoothDevice device : devices)
        {
            ports.add(device.getAddress() + " " + device.getName());
        }
        return ports.toArray(new String[0]);
    }

    public void setPortEvents(IPortEvents events) {
        this.events = events;
    }

    // PrinterPort2

    public void checkOpened() throws Exception {
        if (!isOpened()) {
            throw new Exception("Port is not opened");
        }
    }

    public int available() throws Exception {
        checkOpened();
        return inputStream.available();
    }

    public int directIO(int command, int[] data, Object object)
    {
        switch (command){
            case PrinterPort.DIO_READ_IS_RELIABLE:
                data[0] = 1;
                break;

            default: data[0] = 0;
        }
        return 0;
    }

}