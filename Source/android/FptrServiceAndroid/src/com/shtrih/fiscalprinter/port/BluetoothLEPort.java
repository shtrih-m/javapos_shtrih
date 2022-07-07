package com.shtrih.fiscalprinter.port;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
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

import com.shtrih.util.Hex;
import com.shtrih.util.Time;
import com.shtrih.util.CircularBuffer;
import com.shtrih.util.StaticContext;
import com.shtrih.util.CompositeLogger;

public class BluetoothLEPort implements PrinterPort2 {

    private static final UUID UART_SERVICE_UUID = UUID.fromString("0000ABF0-0000-1000-8000-00805f9b34fb");
    private static final UUID RX_CHAR_UUID = UUID.fromString("0000ABF1-0000-1000-8000-00805f9b34fb");
    private static final UUID TX_CHAR_UUID = UUID.fromString("0000ABF2-0000-1000-8000-00805f9b34fb");
    private static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final int BLE_MTU = 185; // MTU for Android 5.0+

    private int bluetoothmtu = -1;
    private String portName = "";
    private int timeout = 5000;
    private int writeTimeout = 5000;
    private int openTimeout = 20000;
    private BluetoothAdapter adapter = null;
    private BluetoothDevice device = null;
    private BluetoothGatt bluetoothGatt = null;
    private BluetoothGattCharacteristic TxChar = null;
    private final CircularBuffer rxBuffer = new CircularBuffer(1024);
    private static final CompositeLogger logger = CompositeLogger.getLogger(BluetoothLEPort.class);
    private enum ConnectState {Disconnected, ConnectGatt, FailedToConnectGatt,
        RequestMtu,
        DiscoverServices, DiscoverServicesFailed, UartServiceNotSupported,
        TxCharCharacteristicNotSupported, TxCharCccdDescriptorNotSupported, Connected}
    private ConnectState state = ConnectState.Disconnected;
    private enum ScanState {ScanStopped, ScanStarted, ScanFailed, ScanCompleted}
    private ScanState scanState = ScanState.ScanStopped;
    private int scanError = 0;
    private String scanDeviceName = "";
    private boolean scanSingle = false;
    List<BluetoothDevice> scanDevices = new Vector<>();
    HashMap<Object, StatusOperation> operations = new HashMap<>();
    private IPortEvents events = null;
    private boolean portOpened = false;
    private BluetoothLeScanner scanner;
    private boolean receiverRegistered = false;

    public BluetoothLEPort()
    {
    }

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

    private class BluetoothGattCallbackImpl extends BluetoothGattCallback
    {
        public BluetoothGattCallbackImpl(){
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
        setState(ConnectState.ConnectGatt);
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
        if (events != null) {
            events.onDisconnect();
        }
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

    private final ScanCallback scanOpenedDeviceCallback = new ScanCallback()
    {
        @Override
        public void onScanResult(int callbackType, ScanResult result)
        {
            loggerDebug("scanOpenedDeviceCallback.onScanResult: " + result.toString());
            scanner.stopScan(scanOpenedDeviceCallback);

            device = result.getDevice();
            rxBuffer.clear();
            setState(ConnectState.ConnectGatt);
            Context context = StaticContext.getContext();
            bluetoothGatt = device.connectGatt(context, true, new BluetoothGattCallbackImpl());
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

    public void setOpenTimeout(int openTimeout)
    {
        loggerDebug("setOpenTimeout(" + openTimeout + ")");
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
            case BluetoothAdapter.STATE_ON: {
                break;
            }

            case BluetoothAdapter.STATE_TURNING_ON: {
                waitBluetoothAdapterStateOn(adapter, openTimeout);
                break;
            }

            case BluetoothAdapter.STATE_OFF: {
                throw new Exception("Bluetooth is turned off");
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
        registerReceiver();
        portOpened = true;
        loggerDebug("open: OK");
    }

    private String getStateText(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_ON:
                return "STATE_ON";
            case BluetoothAdapter.STATE_OFF:
                return "STATE_OFF";
            case BluetoothAdapter.STATE_TURNING_ON:
                return "STATE_TURNING_ON";
            case BluetoothAdapter.STATE_TURNING_OFF:
                return "STATE_TURNING_OFF";
            default:
                return "";
        }
    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED))
            {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                String stateText = state + ", " + getStateText(state);
                logger.debug("BluetoothAdapter state changed: " + stateText);

                if ((state == BluetoothAdapter.STATE_TURNING_OFF)||(state == BluetoothAdapter.STATE_OFF)||state == BluetoothAdapter.STATE_TURNING_ON)
                {
                    doClose();
                }
            }
        }
    };

    private void registerReceiver() {
        try {
            // events
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            Context context = StaticContext.getContext();
            if (context != null) {
                context.registerReceiver(mBroadcastReceiver, filter);
                receiverRegistered = true;
            }
        } catch (Exception e) {
            logger.error("Failed to register receiver, " + e.getMessage());
        }
    }

    private void unregisterReceiver() {
        if (receiverRegistered) {
            receiverRegistered = false;
            Context context = StaticContext.getContext();
            if (context != null) {
                context.unregisterReceiver(mBroadcastReceiver);
            }
        }
    }

    public void checkPortOpened() throws Exception {
        if (!isOpened()) {
            throw new IOException("Device disconnected");
        }
    }

    public synchronized void doOpen(int timeout) throws Exception
    {
        if (isOpened()) return;

        registerReceiver();
        if (state == ConnectState.Disconnected)
        {
            if (scanner != null){
                scanner.stopScan(scanOpenedDeviceCallback);
            }

            adapter = getBluetoothAdapter();


            if (BluetoothAdapter.checkBluetoothAddress(portName)) {
                // portName is valid MAC address
                device = adapter.getRemoteDevice(portName);
            } else {
                // portName is deviceName prefix, for example "SHTRIH-NANO-F"
                device = scanSingle(portName, 10000);
            }
            if (device.getBondState() == BluetoothDevice.BOND_BONDED)
            {
                throw new Exception(String.format("Device %s must be unpaired.",
                    device.getAddress()));
            }
            connectDevice(device);
        }
        waitOpened(timeout);
    }

    private void connectDevice(BluetoothDevice device) throws Exception
    {
        rxBuffer.clear();
        setState(ConnectState.ConnectGatt);
        Context context = StaticContext.checkContext();
        bluetoothGatt = device.connectGatt(context, true, new BluetoothGattCallbackImpl());
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

            Time.delay(100);
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
        unregisterReceiver();

        portOpened = false;
        loggerDebug("close: OK");
    }

    public synchronized void doClose()
    {
        if (!isOpened()) return;
        logger.debug("doClose");

        if (scanner != null){
            scanner.stopScan(scanOpenedDeviceCallback);
        }
        bluetoothGatt.disconnect();
        bluetoothGatt.close();
        bluetoothGatt = null;

        TxChar = null;
        setState(ConnectState.Disconnected);
        if (events != null) {
            events.onDisconnect();
        }
        rxBuffer.clear();
        logger.debug("doClose: OK");
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
        checkPortOpened();

        //loggerDebug("readBytes: " + len);
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
            if ((currentTime - startTime) > timeout)
            {
                throw new IOException("Read timed out");
            }
        }
        return rxBuffer.read(len);
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
        //loggerDebug("write(" + Hex.toHex(b) + ")");

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
            writeData(blockData);
        }
        loggerDebug("write: OK");
    }

    private void writeData(byte[] blockData) throws Exception
    {
        loggerDebug("writeData: " + Hex.toHex(blockData));
        checkPortOpened();
        BluetoothGattService uartService = bluetoothGatt.getService(UART_SERVICE_UUID);
        if (uartService == null) {
            throw new IOException("UartService not found!");
        }
        BluetoothGattCharacteristic RxChar = uartService.getCharacteristic(RX_CHAR_UUID);
        if (RxChar == null) {
            throw new IOException("Rx charateristic not found!");
        }
        RxChar.setValue(blockData);
        StatusOperation operation = new StatusOperation(RxChar, "Write");
        operations.put(RxChar, operation);
        boolean status = bluetoothGatt.writeCharacteristic(RxChar);
        if (!status) {
            throw new IOException("Failed bluetoothGatt.writeCharacteristic");
        }
        operation.wait(writeTimeout);
        operation.checkStatus();
        operations.remove(operation);
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

    public void setTimeout(int timeout)
    {
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

    public String[] getPortNames() throws Exception
    {
        Set<String> ports = new HashSet<>();
        List<BluetoothDevice> devices = scan("", 3000);
        for (BluetoothDevice device : devices) {
            ports.add(device.getAddress() + " " + device.getName());
        }
        return ports.toArray(new String[0]);
    }

    public List<BluetoothDevice> scan(String deviceName, int timeout, boolean singleDevice) throws Exception
    {
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

    public void checkOpened() throws Exception{
        if (!isOpened()) {
            throw new Exception("Port is not opened");
        }
    }

    public void openPort() throws Exception{
        doOpen(openTimeout);
    }

    public void setPortEvents(IPortEvents events){
        this.events = events;
    }

    // PrinterPort2

    public int available() throws Exception{
        openPort();
        return rxBuffer.available();
    }

    public byte[] read(int len) throws Exception{
        openPort();
        return rxBuffer.read(len);
    }

    public String readParameter(int parameterID){
        switch (parameterID){
            case PrinterPort.PARAMID_IS_RELIABLE: return "1";
            default: return null;
        }
    }
}
