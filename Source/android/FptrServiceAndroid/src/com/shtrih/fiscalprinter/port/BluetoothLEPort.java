package com.shtrih.fiscalprinter.port;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.Handler;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.BluetoothLeScanner;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Hex;
import com.shtrih.util.StaticContext;
import com.shtrih.util.Time;
import com.shtrih.util.CircularBuffer;

public class BluetoothLEPort implements PrinterPort {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1002; // Permission to scanner
    private static final UUID UART_SERVICE_UUID = UUID.fromString("0000ABF0-0000-1000-8000-00805f9b34fb");
    private static final UUID RX_CHAR_UUID = UUID.fromString("0000ABF1-0000-1000-8000-00805f9b34fb");
    private static final UUID TX_CHAR_UUID = UUID.fromString("0000ABF2-0000-1000-8000-00805f9b34fb");
    private static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final int BLE_MTU = 185; // MTU for Android 5.0+

    private int bluetoothmtu = -1;
    private String portName = "";
    private int timeout = 5000;
    private int writeTimeout = 5000;
    private int openTimeout = 5000;
    private BluetoothAdapter adapter = null;
    private BluetoothDevice device = null;
    private BluetoothGatt bluetoothGatt = null;
    private BluetoothGattCharacteristic TxChar = null;
    private final CircularBuffer rxBuffer = new CircularBuffer(1024);
    private static CompositeLogger logger = CompositeLogger.getLogger(BluetoothLEPort.class);
    private enum ConnectState {Disconnected, ConnectGatt, FailedToConnectGatt, RequestMtu,
        DiscoverServices, DiscoverServicesFailed, UartServiceNotSupported,
        TxCharCharacteristicNotSupported, TxCharCccdDescriptorNotSupported, Connected};
    private ConnectState state = ConnectState.Disconnected;
    private enum ScanState {ScanStopped, ScanStarted, ScanFailed, ScanCompleted};
    private ScanState scanState = ScanState.ScanStopped;
    private int scanError = 0;
    private String scanDeviceName = "";
    private boolean scanSingle = false;
    List<BluetoothDevice> scanDevices = new Vector<BluetoothDevice>();
    HashMap<Object, StatusOperation> operations = new HashMap<Object, StatusOperation>();
    private IPortEvents events = null;
    private boolean portOpened = false;
    private BluetoothLeScanner scanner;

    private static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    private static final String ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION";

    private void loggerDebug(String text) {
        //logger.debug(text);
    }

    private void loggerError(String text) {

        logger.error(text);
    }

    private void setState(ConnectState newState)
    {
        if (newState != state){
            loggerDebug("setState(" + newState + ")");
            state = newState;
        }
    }

    private void checkPermissions() throws Exception
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            checkPermission(ACCESS_FINE_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= 29)
        {
            checkPermission(ACCESS_BACKGROUND_LOCATION);
        }
    }

    private void checkPermission(String permission) throws Exception
    {
        /* !!!
        if (ContextCompat.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            throw new Exception("No permission " + permission);
        }
         */
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
            // onConnectionStateChange(status: 8 newState: 0
            loggerDebug("BluetoothGattCallback.onConnectionStateChange(status: " + status + " newState: " + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gattConnected();
            }
            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                gattDisconnected();
            }
        }

        public void onServicesDiscovered (BluetoothGatt gatt,int status)
        {
            loggerDebug("BluetoothGattCallback.onServicesDiscovered(status: " + status);
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                servicesDiscovered();
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
            completeOperation(characteristic, status);
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
            setState(ConnectState.Connected);
            if (events != null) {
                events.onConnect();
            }
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
                discoverServices();
            }
        }
    }

    private void gattConnected()
    {
        loggerDebug("gattConnected");
        if (state != ConnectState.ConnectGatt)
        {
            loggerError("state != ConnectState.ConnectGatt");
        }

        if (Build.VERSION.SDK_INT >= 21) // Build.VERSION_CODES.LOLLIPOP
        {
            int mtu = BLE_MTU + 3; // Maximum allowed 517 - 3 bytes of BLE
            if (bluetoothGatt == null) return;
            setState(ConnectState.RequestMtu);
            if (!bluetoothGatt.requestMtu(mtu)){
                loggerError("Failed to configure MTU");
            }
        } else {
            discoverServices();
        }
    }

    private void gattDisconnected()
    {
        loggerDebug("gattDisconnected");

        doClose();
        setState(ConnectState.FailedToConnectGatt);
        // start wait for device
        if (portOpened)
        {
            loggerDebug("Start scan");

            scanState = ScanState.ScanStarted;
            List<ScanFilter> filters = new ArrayList<>();
            filters.add(new ScanFilter.Builder().setDeviceAddress(device.getAddress()).build());
            ScanSettings settings = new ScanSettings.Builder().setScanMode(
                    ScanSettings.CALLBACK_TYPE_FIRST_MATCH).build();

            scanner = adapter.getBluetoothLeScanner();
            scanner.startScan(filters, settings, scanOpenedDeviceCallback);
            loggerDebug("Scan started!");
        }
    }

    private ScanCallback scanOpenedDeviceCallback = new ScanCallback()
    {
        @Override
        public void onScanResult(int callbackType, ScanResult result)
        {
            loggerDebug("scanOpenedDeviceCallback.onScanResult: " + result.toString());

            scanner.stopScan(scanOpenedDeviceCallback);
            setState(ConnectState.ConnectGatt);
            device = result.getDevice();
            bluetoothGatt = device.connectGatt(null, false, new BluetoothGattCallbackImpl());
            if (bluetoothGatt == null) {
                loggerError("ConnectGatt returns null");
            }
        }
    };

    private void discoverServices() {
        // Attempts to discover services after successful connection.
        if (bluetoothGatt == null) return;
        setState(ConnectState.DiscoverServices);
        if (!bluetoothGatt.discoverServices())
        {
            loggerError("Failed to discover services");
            setState(ConnectState.DiscoverServicesFailed);
        }
    }

    private void servicesDiscovered()
    {
        loggerDebug("servicesDiscovered");

        if (state != ConnectState.DiscoverServices)
        {
            loggerError("state != ConnectState.DiscoverServices");
        }

        if (bluetoothGatt == null) return;
        BluetoothGattService uartService = bluetoothGatt.getService(UART_SERVICE_UUID);
        if (uartService == null) {
            loggerError("UartService not found!");
            setState(ConnectState.UartServiceNotSupported);
            return;
        }
        TxChar = uartService.getCharacteristic(TX_CHAR_UUID);
        if (TxChar == null)
        {
            loggerError("Tx charateristic not found!");
            setState(ConnectState.TxCharCharacteristicNotSupported);
            return;
        }
        bluetoothGatt.setCharacteristicNotification(TxChar, true);
        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
        if(descriptor == null){
            loggerError("CCCD == null!");
            setState(ConnectState.TxCharCccdDescriptorNotSupported);
            return;
        }
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
    }

    private void dataAvailable(BluetoothGattCharacteristic characteristic)
    {
        try
        {
            if (characteristic == null) return;

            if (characteristic.getUuid().equals(TX_CHAR_UUID)) {
                //loggerDebug("Received: " + Hex.toHex(characteristic.getValue()));
                rxBuffer.write(characteristic.getValue());
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

    public boolean isOpened(){
        return state == ConnectState.Connected;
    }

    public boolean isClosed(){
        return !isOpened();
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
                Time.delay(10);
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

        doOpen(timeout);
        portOpened = true;
        loggerDebug("open: OK");
    }

    public void openPort() throws Exception{
        doOpen(0);
    }

    public void doOpen(int timeout) throws Exception
    {
        if (isOpened()) return;
        if (state == ConnectState.Disconnected)
        {
            checkPermissions();

            if (scanner != null){
                scanner.stopScan(scanOpenedDeviceCallback);
            }

            adapter = getBluetoothAdapter();
            if (BluetoothAdapter.checkBluetoothAddress(portName)) {
                // portName is valid MAC address
                device = adapter.getRemoteDevice(portName);
                connectDevice(device);
                
            } else {
                // portName is deviceName prefix, for example "SHTRIH-NANO-F"
                device = scanSingle(portName, 10000);
                connectDevice(device);
            }
        }
        waitOpened(openTimeout);
    }

    private void connectDevice(BluetoothDevice device) throws Exception
    {
        setState(ConnectState.ConnectGatt);
        bluetoothGatt = device.connectGatt(StaticContext.getContext(), false, new BluetoothGattCallbackImpl());
        if (bluetoothGatt == null) {
            throw new Exception("ConnectGatt returns null");
        }
    }

    private void waitOpened(int timeout) throws Exception
    {
        loggerDebug("waitOpened(" + timeout + ")");
        long startTime = System.currentTimeMillis();
        for (; ; ) {
            long currentTime = System.currentTimeMillis();
            if (isOpened()) break;

            switch (state){
                case FailedToConnectGatt:
                    throw new Exception("Failed to connect Gatt");

                case DiscoverServicesFailed:
                    throw new Exception("DiscoverServices function failed");

                case UartServiceNotSupported:
                    throw new Exception("Uart service not supported");

                case TxCharCharacteristicNotSupported:
                    throw new Exception("TxChar characteristic not supported");

                case TxCharCccdDescriptorNotSupported:
                    throw new Exception("TxChar CCCD descriptor not supported");
            }

            Time.delay(10);
            if ((currentTime - startTime) > timeout) {
                loggerDebug("waitOpened(): timeout");
                throw new Exception("Port open timeout");
            }
        }
    }

    public void close()
    {
        loggerDebug("close");
        doClose();

        portOpened = false;
        loggerDebug("close: OK");
    }

    public void doClose()
    {
        if (!isOpened()) return;

        if (scanner != null){
            scanner.stopScan(scanOpenedDeviceCallback);
        }
        bluetoothGatt.close();
        bluetoothGatt = null;

        TxChar = null;
        setState(ConnectState.Disconnected);
        if (events != null) {
            events.onDisconnect();
        }
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
        //loggerDebug("readBytes: " + len);
        openPort();

        if (len <= 0)
        {
            throw new Exception("Data length <= 0");
        }

        // wait for data available with timaout
        long startTime = System.currentTimeMillis();
        for (;;)
        {
            checkPortState();
            if (rxBuffer.available() >= len) break;

            Time.delay(1);
            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) > timeout) {
                throw new IOException("Data read timeout");
            }
        }
        byte[] data = rxBuffer.read(len);
        //loggerDebug("read(" + Hex.toHex(data) + ")");
        return data;
    }

    public void checkPortState() throws IOException {
        if (state != ConnectState.Connected) {
            throw new IOException("Device disconnected");
        }
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

        rxBuffer.clear();

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

        openPort();

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
        StatusOperation operation = new StatusOperation(RxChar, "Write");
        operations.put(RxChar, operation);
        boolean status = bluetoothGatt.writeCharacteristic(RxChar);
        if (!status) {
            loggerError("Failed bluetoothGatt.writeCharacteristic");
        }
        operation.wait(writeTimeout);
        operation.checkStatus();
        operations.remove(operation);
        return status;
    }

    private class StatusOperation
    {
        private int status;
        private final Object obj;
        private final String name;
        private boolean completed = false;

        public StatusOperation(Object obj, String name){
            this.obj = obj;
            this.name = name;
        }

        public void complete(int status){
            this.status = status;
            completed = true;
        }

        public void wait(int timeout) throws Exception
        {
            long startTime = System.currentTimeMillis();
            while (!completed)
            {
                checkPortState();
                long currentTime = System.currentTimeMillis();
                if ((currentTime - startTime) > timeout) {
                    throw new IOException(name + "failed with timeout");
                }
                Time.delay(1);
            }
        }

        public void checkStatus() throws Exception
        {
            if (status != BluetoothGatt.GATT_SUCCESS){
                throw new IOException(name + "failed with status " + status);
            }
        }

    }

    public void completeOperation(Object object, int status)
    {
        StatusOperation operation = operations.get(object);
        if (operation != null){
            operation.complete(status);
        }
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

    public List<BluetoothDevice> scan(String deviceName, int timeout, boolean singleDevice) throws Exception
    {
        checkPermissions();
        scanSingle = singleDevice;
        scanDevices.clear();
        scanDeviceName = deviceName;
        scanState = ScanState.ScanStarted;

        getBluetoothAdapter().getBluetoothLeScanner().startScan(scanCallback);
        long startTime = System.currentTimeMillis();
        for (; ; ) {
            long currentTime = System.currentTimeMillis();

            if (scanState == ScanState.ScanFailed) {
                getBluetoothAdapter().getBluetoothLeScanner().stopScan(scanCallback);
                throw new Exception("Scan failed to start, error code " + scanError);
            }
            if (scanState == ScanState.ScanCompleted) break;

            Time.delay(10);
            if ((currentTime - startTime) > timeout) break;
        }
        getBluetoothAdapter().getBluetoothLeScanner().stopScan(scanCallback);
        return scanDevices;
    }

    public List<BluetoothDevice> scan(String deviceName, int timeout) throws Exception
    {
        return scan(deviceName, timeout, false);
    }

    public BluetoothDevice scanSingle(String deviceName, int timeout) throws Exception
    {
        List<BluetoothDevice> devices = scan(deviceName, timeout, true);
        if (devices.isEmpty()) throw new Exception("Device not found");
        return devices.get(0);
    }

    private boolean isValidDevice(BluetoothDevice device) {
        if (device == null) return false;
        String name = device.getName();
        if (name == null) return false;
        if (name.isEmpty()) return false;
        if (scanDeviceName.isEmpty()) return true;
        if (name.startsWith(scanDeviceName)) return true;
        return false;
    }

    private ScanCallback scanCallback = new ScanCallback()
    {
        // Callback when batch results are delivered.
        @Override
        public void onBatchScanResults(List<ScanResult> results)
        {
            loggerDebug("onBatchScanResults: " + results.toString());
        }

        //Callback when scan could not be started.
        @Override
        public void onScanFailed(int errorCode)
        {
            loggerDebug("onScanFailed: " + errorCode);

            scanError = errorCode;
            scanState = ScanState.ScanFailed;
        }

        //Callback when a BLE advertisement has been found.
        @Override
        public void onScanResult(int callbackType, ScanResult result)
        {
            loggerDebug("onScanResult: " + result.toString());
            if (isValidDevice(result.getDevice()))
            {
                scanDevices.add(result.getDevice());
                if (scanSingle) {
                    scanState = ScanState.ScanCompleted;
                }
            }
        }

    };

    public void setPortEvents(IPortEvents events){
        this.events = events;
    }
}
