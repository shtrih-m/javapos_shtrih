package com.shtrih.tinyjavapostester.search.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanSettings;

import com.shtrih.fiscalprinter.port.BluetoothLEPort;
import com.shtrih.tinyjavapostester.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Set;
import java.util.HashSet;

public class DeviceListActivity extends AppCompatActivity {
    // Debugging
    private static final String TAG = "DeviceListActivity";

    public static final int REQUEST_CONNECT_BT_DEVICE = 481;
    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    // public static String DEFAULT_ADDRESS = "";
    // Member fields
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DeviceListActivity.class);

    private BluetoothAdapter mBtAdapter;
    private BluetoothLeScanner bleScanner;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private HashSet<BluetoothDevice> devices = new HashSet<>();
    private CharSequence originalTitle;
    Button btnScanBT;
    Button btnScanBLE;
    boolean btScanStarted = false;
    boolean bleScanStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        originalTitle = getTitle();

        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);
        // loadSettings();
        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Initialize the button to perform device discovery
        btnScanBT = (Button) findViewById(R.id.button_scan_bt);
        btnScanBLE = (Button) findViewById(R.id.button_scan_ble);

        btnScanBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    accessLocationPermission();
                }
                if (btScanStarted) {
                    stopDiscovery();
                } else {
                    startBTDiscovery();
                }
            }
        });

        btnScanBLE.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT >= 23) {
                    accessLocationPermission();
                }
                if (bleScanStarted)
                {
                    stopDiscovery();
                } else {
                    startBLEDiscovery();
                }
            }
        });


        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBtAdapter == null) {
            log.error("BT adapter missing");
            Toast.makeText(getApplicationContext(), "BT adapter was not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        bleScanner = mBtAdapter.getBluetoothLeScanner();


        // Get a set of currently paired devices Set<BluetoothDevice>
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices(); // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" +
                        device.getAddress());
            }
        } else {
            findViewById(R.id.title_paired_devices).setVisibility(View.GONE);
        }

    }

    final int REQUEST_CODE_LOC = 666;

    private void accessLocationPermission() {
        int accessCoarseLocation = checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFineLocation = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listRequestPermission = new ArrayList<String>();

        if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (accessFineLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listRequestPermission.isEmpty()) {
            String[] strRequestPermission = listRequestPermission.toArray(new String[listRequestPermission.size()]);
            requestPermissions(strRequestPermission, REQUEST_CODE_LOC);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOC:
                if (grantResults.length > 0) {
                    for (int gr : grantResults) {
                        // Check if request is granted or not
                        if (gr != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getApplicationContext(), "Permissions was not granted", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    //startBTDiscovery(); !!!
                }
                break;
            default:
                return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        stopDiscovery();

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
        // saveSettings();
    }

    private void discoveryFinished() {
        setTitle(originalTitle);
        setProgressBarIndeterminateVisibility(false);
        btnScanBT.setText("Start BT scan");
        btnScanBLE.setText("Start BLE scan");
    }

    private void stopDiscovery()
    {
        if (btScanStarted)
        {
            btScanStarted = false;
            if (mBtAdapter.isDiscovering()) {
                mBtAdapter.cancelDiscovery();
            }
        }
        if (bleScanStarted){
            bleScanStarted = false;
            bleScanner.stopScan(scanCallback);
        }
        discoveryFinished();
    }

    private void startBLEDiscovery()
    {
        if (!checkAdapterEnabled()) return;
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        btnScanBLE.setText("Stop BLE scan");

        devices.clear();
        devices.addAll(mBtAdapter.getBondedDevices());
        mNewDevicesArrayAdapter.clear();
        bleScanner.startScan(scanCallback);
        bleScanStarted = true;
    }

    private ScanCallback scanCallback = new ScanCallback()
    {
        // Callback when batch results are delivered.
        @Override
        public void onBatchScanResults(List<ScanResult> results)
        {
            log.debug("onBatchScanResults: " + results.toString());
        }

        //Callback when scan could not be started.
        @Override
        public void onScanFailed(int errorCode)
        {
            log.error("Scan failed: " + errorCode);
            Toast.makeText(getApplicationContext(), "Scan failed: " + errorCode, Toast.LENGTH_LONG).show();
        }

        //Callback when a BLE advertisement has been found.
        @Override
        public void onScanResult(int callbackType, ScanResult result)
        {
            if (!bleScanStarted) return;
            addDevice(result.getDevice());
        }
    };

    private void startBTDiscovery()
    {
        if (!checkAdapterEnabled()) return;
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        btnScanBT.setText("Stop BT scan");

        devices.clear();
        devices.addAll(mBtAdapter.getBondedDevices());
        mNewDevicesArrayAdapter.clear();
        mBtAdapter.startDiscovery();
        btScanStarted = true;
    }

    private boolean checkAdapterEnabled() {
        if (!mBtAdapter.isEnabled()) {
            log.error("BT adapter disabled");
            Toast.makeText(getApplicationContext(), "BT adapter disabled", Toast.LENGTH_LONG).show();
        }
        return mBtAdapter.isEnabled();
    }

    // The on-click listener for all devices in the ListViews
    private final OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect

            stopDiscovery();

            // Get the device MAC address, which is the last 17 chars in the
            // View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (!btScanStarted) return;
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                addDevice(device);
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                discoveryFinished();
            }
        }
    };

    private void addDevice(BluetoothDevice device)
    {
        if (device == null) return;
        log.debug("BT Device: " + device.getName() + ": " + device.getAddress());
        if (!devices.contains(device))
        {
            if (device.getName() != null) {
                devices.add(device);
                mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }
}
