package com.shtrih.fiscalprinter.port;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;

import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Hex;
import com.shtrih.util.StaticContext;


public class BluetoothLEPort implements PrinterPort {

    private static final String ACTION_GATT_CONNECTED = "UART.ACTION_GATT_CONNECTED";
    private static final String ACTION_GATT_DISCONNECTED = "UART.ACTION_GATT_DISCONNECTED";
    private static final String ACTION_MTU_CHANGED = "UART.ACTION_MTU_CHANGED";
    private static final String ACTION_GATT_SERVICES_DISCOVERED = "UART.ACTION_GATT_SERVICES_DISCOVERED";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1002; // Permission to scanner

    private static final UUID UART_SERVICE_UUID = UUID.fromString("0000ABF0-0000-1000-8000-00805f9b34fb");
    private static final UUID RX_CHAR_UUID = UUID.fromString("0000ABF1-0000-1000-8000-00805f9b34fb");
    private static final UUID TX_CHAR_UUID = UUID.fromString("0000ABF2-0000-1000-8000-00805f9b34fb");
    private static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final int BLE_MTU = 185; // MTU for Android 5.0+

    private int bluetoothmtu = -1;
    private String portName = "";
    private int timeout = 5000;
    private int openTimeout = 5000;
    private boolean opened = false;
    private Handler mHandler = null;
    private BluetoothDevice device  = null;
    private BluetoothGatt bluetoothGatt = null;
    private BluetoothGattCharacteristic TxChar = null;
    private PipedOutputStream outStream = null;
    private PipedInputStream inStream = null;
    private MainBroadcastReceiver broadcastReceiver = new MainBroadcastReceiver();
    private static CompositeLogger logger = CompositeLogger.getLogger(BluetoothLEPort.class);

    public BluetoothLEPort() {
    }

    private static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    private static final String ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION";

    private void loggerDebug(String text) {
        //logger.debug(text);
    }

    private void loggerError(String text) {
        logger.error(text);
    }
    
    private void checkPermissions() throws Exception
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            checkPermission(ACCESS_FINE_LOCATION);
        }

        /*
        if (Build.VERSION.SDK_INT >= 29)
        {
            checkPermission(ACCESS_BACKGROUND_LOCATION);
        }
         */
    }

    private void checkPermission(String permission) throws Exception {
        if (StaticContext.getContext().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            throw new Exception("No permission " + permission);
        }
    }

    private class BluetoothGattCallbackImpl extends BluetoothGattCallback
    {
        public BluetoothGattCallbackImpl(){
        }

        public void onPhyUpdate (BluetoothGatt gatt,int txPhy, int rxPhy, int status)
        {
            loggerDebug("BluetoothGattCallback.onPhyUpdate");
        }

        public void onPhyRead (BluetoothGatt gatt,int txPhy, int rxPhy, int status)
        {
            loggerDebug("BluetoothGattCallback.onPhyRead");
        }

        public void onConnectionStateChange (BluetoothGatt gatt,int status, int newState)
        {
            loggerDebug("BluetoothGattCallback.onConnectionStateChange(status: " + status + " newState: " + newState);
            if ((status == BluetoothGatt.GATT_SUCCESS)&&(newState == BluetoothProfile.STATE_CONNECTED))
            {
                broadcastUpdate(ACTION_GATT_CONNECTED);
            } else {
                //broadcastUpdate(ACTION_GATT_DISCONNECTED);
            }
        }

        public void onServicesDiscovered (BluetoothGatt gatt,int status)
        {
            loggerDebug("BluetoothGattCallback.onServicesDiscovered(status: " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            }
        }

        public void onCharacteristicRead (BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic,  int status)
        {
            loggerDebug("BluetoothGattCallback.onCharacteristicRead(status: " + status);
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                dataAvailable(characteristic);
            }
        }

        public void onCharacteristicWrite (BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status)
        {
            loggerDebug("BluetoothGattCallback.onCharacteristicWrite(status: " + status);
        }

        public void onCharacteristicChanged (BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic)
        {
            loggerDebug("BluetoothGattCallback.onCharacteristicChanged()");
            dataAvailable(characteristic);
        }

        public void onDescriptorRead (BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                      int status)
        {
            loggerDebug("BluetoothGattCallback.onDescriptorRead(status: " + status + ")");
        }

        public void onDescriptorWrite (BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                       int status)
        {
            loggerDebug("BluetoothGattCallback.onDescriptorWrite(status: " + status + ")");
            opened = true;
        }

        public void onReliableWriteCompleted (BluetoothGatt gatt,int status)
        {
            loggerDebug("BluetoothGattCallback.onReliableWriteCompleted(status: " + status + ")");
        }

        public void onReadRemoteRssi (BluetoothGatt gatt,int rssi, int status)
        {
            loggerDebug("BluetoothGattCallback.onReadRemoteRssi(rssi: " + rssi + ", " + "status: " + status + ")");
        }

        public void onMtuChanged (BluetoothGatt gatt,int mtu, int status)
        {
            loggerDebug("BluetoothGattCallback.onMtuChanged(mtu: " + mtu + ", " + "status: " + status + ")");
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                bluetoothmtu = mtu-3;
                broadcastUpdate(ACTION_MTU_CHANGED);
            }
        }
    }

    private class MainBroadcastReceiver extends BroadcastReceiver
    {
        public MainBroadcastReceiver(){
        }

        @Override
        public void onReceive (Context context, Intent intent)
        {
            loggerDebug(intent.getAction());
            try
            {
                if (intent.getAction().equals(ACTION_GATT_CONNECTED)){
                    gattConnected();
                }
                if (intent.getAction().equals(ACTION_GATT_DISCONNECTED)){
                    gattDisconnected();
                }
                if (intent.getAction().equals(ACTION_MTU_CHANGED)) {
                    discoverServices();
                }
                if (intent.getAction().equals(ACTION_GATT_SERVICES_DISCOVERED)){
                    servicesDiscovered();
                }
            } catch (Exception e) {
                loggerError(e.getMessage());
            }

        }
    }

    private void gattConnected() throws Exception
    {
        loggerDebug("gattConnected");

        if (Build.VERSION.SDK_INT >= 21) // Build.VERSION_CODES.LOLLIPOP
        {
            int mtu = BLE_MTU + 3; // Maximum allowed 517 - 3 bytes of BLE
            if (bluetoothGatt == null) return;
            if (!bluetoothGatt.requestMtu(mtu)){
                loggerError("Failed to configure MTU");
            }
        } else {
            discoverServices();
        }
    }

    private void gattConnected2()
    {
        loggerDebug("gattConnected2");
        loggerDebug("BondState: " + device.getBondState());

        // Attempts to discover services after successful connection.
        if (bluetoothGatt == null) return;
        if (!bluetoothGatt.discoverServices()) {
            loggerError("Failed to discover services");
        }
    }

    private void gattDisconnected() throws Exception
    {
        loggerDebug("gattDisconnected");
        if (isOpened()) {
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
        bluetoothGatt = device.connectGatt (StaticContext.getContext(), false, new BluetoothGattCallbackImpl());
        if (bluetoothGatt == null){
            loggerDebug("connectGatt returns null");
        }
    }

    private void discoverServices() {
        // Attempts to discover services after successful connection.
        if (bluetoothGatt == null) return;
        if (!bluetoothGatt.discoverServices()) {
            loggerError("Failed to discover services");
        }
    }

    private void servicesDiscovered()
    {
        loggerDebug("servicesDiscovered");
        if (bluetoothGatt == null) return;
        BluetoothGattService uartService = bluetoothGatt.getService(UART_SERVICE_UUID);
        if (uartService == null) {
            loggerError("UartService not found!");
            return;
        }
        TxChar = uartService.getCharacteristic(TX_CHAR_UUID);
        if (TxChar == null)
        {
            loggerError("Tx charateristic not found!");
            return;
        }
        bluetoothGatt.setCharacteristicNotification(TxChar, true);
        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
        if(descriptor == null){
            loggerError("CCCD == null!");
            return;
        }
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
    }

    private void broadcastUpdate(String action)
    {
        try {
            StaticContext.getContext().sendBroadcast(new Intent(action));
        }
        catch(Exception e){
            loggerError("broadcastUpdate: " + e.getMessage());
        }
    }

    private void dataAvailable(BluetoothGattCharacteristic characteristic)
    {
        try
        {
            if (characteristic == null) return;
            if (outStream == null) return;

            if (characteristic.getUuid().equals(TX_CHAR_UUID)) {
                loggerDebug("Received: " + Hex.toHex(characteristic.getValue()));
                outStream.write(characteristic.getValue());
            }
        }
        catch(Exception e){
            loggerError("dataAvailable: " + e.getMessage());
        }
    }

    public int getOpenTimeout() {
        return openTimeout;
    }

    public void setOpenTimeout(int openTimeout) {
        this.openTimeout = openTimeout;
    }

    private void checkOpened() throws Exception
    {
        if (!opened){
            throw new Exception("Port is not opened");
        }
    }

    public boolean isOpened(){
        return opened;
    }

    public boolean isClosed(){
        return !opened;
    }

    private BluetoothAdapter getBluetoothAdapter() throws Exception
    {
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
                Thread.sleep(100);
            } else {
                break;
            }
            if ((currentTime - startTime) > timeout) {
                throw new Exception("BluetoothAdapter turning on timeout");
            }
        }
    }

    public void open(int timeout) throws Exception
    {
        loggerDebug("open(" + timeout + ")");

        if (isOpened()) return;

        opened = false;
        checkPermissions();

        outStream = new PipedOutputStream();
        inStream = new PipedInputStream(outStream);

        mHandler = new Handler(StaticContext.getContext().getMainLooper());
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_MTU_CHANGED);
        filter.addAction(ACTION_GATT_CONNECTED);
        filter.addAction(ACTION_GATT_DISCONNECTED);
        filter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        StaticContext.getContext().registerReceiver(broadcastReceiver, filter);

        BluetoothAdapter adapter = getBluetoothAdapter();
        BluetoothDevice device = adapter.getRemoteDevice(portName);
        bluetoothGatt = device.connectGatt(StaticContext.getContext(), false, new BluetoothGattCallbackImpl());
        if (bluetoothGatt == null){
            throw new Exception("ConnectGatt returns null");
        }
        waitOpened(openTimeout);
        loggerDebug("open: OK");
    }

    private void waitOpened(int timeout) throws Exception
    {
        loggerDebug("waitOpened(" + timeout + ")");
        long startTime = System.currentTimeMillis();
        for (; ; ) {
            long currentTime = System.currentTimeMillis();
            if (opened) break;

            Thread.sleep(100);
            if ((currentTime - startTime) > timeout) {
                loggerDebug("waitOpened(): timeout");
                throw new Exception("Port open timeout");
            }
        }
    }

    public void close()
    {
        loggerDebug("close");
        if (!isOpened()) return;

        bluetoothGatt.disconnect();
        bluetoothGatt = null;

        mHandler = null;
        TxChar = null;
        inStream= null;
        outStream= null;
        opened = false;
        loggerDebug("close: OK");
    }

    public int byteToInt(int B) {
        if (B < 0) {
            B = (int) (256 + B);
        }
        return B;
    }

    public int readByte() throws Exception
    {
        return byteToInt(readBytes(1)[0]);
    }

    public byte[] readBytes(int len) throws Exception{
        loggerDebug("readBytes: " + len);
        checkOpened();

        if (len <= 0)
        {
            throw new Exception("Data length <= 0");
        }

        byte[] data = new byte[len];
        // wait for data available with timaout
        long startTime = System.currentTimeMillis();
        for (;;)
        {
            if (inStream.available() >= len) break;

            Thread.sleep(100);
            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) > timeout) {
                throw new Exception("Data read timeout");
            }
        }
        inStream.read(data, 0, len);
        loggerDebug("read(" + Hex.toHex(data) + ")");
        return data;
    }

    public void write(int b) throws Exception
    {
        byte[] bytes = new byte[1];
        bytes[0] = (byte)b;
        write(bytes);
    }

    public void write(byte[] b) throws Exception
    {
        loggerDebug("write(" + Hex.toHex(b) + ")");

        int blockSize = 20;
        if (bluetoothmtu > 0) blockSize = bluetoothmtu;

        int count = (b.length + blockSize -1)/blockSize;
        for (int i=0;i<count;i++) {
            int offset = i * blockSize;
            int dataSize = b.length - i * blockSize;
            if (dataSize > blockSize) {
                dataSize = blockSize;
            }
            byte[] blockData = new byte[dataSize];
            System.arraycopy(b, offset, blockData, 0, dataSize);

            if (!writeData(blockData)) break;
        }
        loggerDebug("write: OK");
    }

    private boolean writeData(byte[] blockData) throws Exception
    {
        loggerDebug("writeData: " + Hex.toHex(blockData));

        checkOpened();

        BluetoothGattService uartService = bluetoothGatt.getService(UART_SERVICE_UUID);
        if (uartService == null) {
            loggerError("UartService not found!");
            return false;
        }
        BluetoothGattCharacteristic RxChar = uartService.getCharacteristic(RX_CHAR_UUID);
        if (RxChar == null) {
            loggerError("Rx charateristic not found!");
            return false;
        }
        RxChar.setValue(blockData);
        boolean status = bluetoothGatt.writeCharacteristic(RxChar);
        if (!status) {
            loggerError("Failed bluetoothGatt.writeCharacteristic");
        }
        return status;
    }

    // no baudrate available for bluwtooth connection
    public void setBaudRate(int baudRate) throws Exception{
    }

    public synchronized void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setPortName(String portName) throws Exception
    {
        this.portName = portName;
    }

    public String getPortName(){
        return portName;
    }

    public Object getSyncObject() throws Exception{
        return this;
    }

    public boolean isSearchByBaudRateEnabled(){
        return false;
    }

    public String[] getPortNames(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices;

        if (bluetoothAdapter == null)
            pairedDevices = new HashSet<BluetoothDevice>();
        else
            pairedDevices = bluetoothAdapter.getBondedDevices();

        Set<String> ports = new HashSet<String>();

        for (BluetoothDevice device : pairedDevices) {
            if (device.getName().startsWith(portName))
                ports.add(device.getAddress());
        }
        return ports.toArray(new String[0]);
    }

}
