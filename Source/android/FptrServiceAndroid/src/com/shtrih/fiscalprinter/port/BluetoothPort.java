package com.shtrih.fiscalprinter.port;

import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;
import com.shtrih.util.StaticContext;
import com.shtrih.util.Time;

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
    private BluetoothDevice device = null;
    private BluetoothSocket socket = null;
    private boolean receiverRegistered = false;
    private static CompositeLogger logger = CompositeLogger.getLogger(BluetoothPort.class);

    public BluetoothPort() {
    }

    public BluetoothSocket getSocket() throws Exception
    {
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
    public void open(int timeout) throws Exception
    {
        if (isOpened()) return;

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
                if (device.getName().startsWith(portName))
                {
                    try {
                        connectDevice(device);
                        return;
                    }
                    catch(Exception e){
                        logger.error("Failed to connect device " +
                            device.getName() + ", " + device.getAddress());
                    }
                }
            }
            throw new Exception("Failed to connect any bonded devices");
        }
    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                {
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
                        if (events != null) {
                            events.onDisconnect();
                        }

                        if (isOpened()) {
                            try {
                                socket.close();
                                socket = null;
                            } catch (Exception e) {
                                logger.error("BluetoothDevice.ACTION_ACL_DISCONNECTED: ", e);
                            }
                        }
                    }
                    break;
                }
            }
        }
    };

    private void connectDevice(BluetoothDevice device) throws Exception
    {
        if (device == null) {
            throw new Exception("Failed to get BluetoothDevice by address");
        }

        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
            if (socket == null) {
                throw new Exception("Failed to get bluetooth device socket");
            }

            socket.connect();
            registerReceiver();
            return;

        } catch (IOException e) {
            close();
            throw e;
        }
    }

    private void registerReceiver() {
        try {
            // events
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            StaticContext.getContext().registerReceiver(mBroadcastReceiver, filter);
            receiverRegistered = true;

        }catch(Exception e){
            logger.error("Failed to register receiver, " + e.getMessage());
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
    public synchronized void close()
    {
        if (isOpened())
        {
            logger.debug("close()");
            try {
                if (receiverRegistered) {
                    receiverRegistered = false;
                    StaticContext.getContext().unregisterReceiver(mBroadcastReceiver);
                }
                socket.close();
            } catch (Exception e) {
                logger.error("Bluetooth socket close failed", e);
            }
        }
        socket = null;
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
    public InputStream getInputStream() throws Exception{
        return getSocket().getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws Exception{
        return getSocket().getOutputStream();
    }

    @Override
    public void write(byte[] b) throws Exception {
        connect();
        OutputStream os = getSocket().getOutputStream();

        try {
            os.write(b);
            os.flush();
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
            InputStream is = getSocket().getInputStream();
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

    public String[] getPortNames() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices;

        if (bluetoothAdapter == null)
            pairedDevices = new HashSet<>();
        else
            pairedDevices = bluetoothAdapter.getBondedDevices();

        Set<String> ports = new HashSet<>();

        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().startsWith(portName))
                ports.add(device.getAddress());
        }

        return ports.toArray(new String[0]);
    }

    public void setPortEvents(IPortEvents events){
        this.events = events;
    }
}
