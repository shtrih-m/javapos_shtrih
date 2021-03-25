package com.shtrih.tinyjavapostester;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.EncodeHintType;
import com.google.zxing.pdf417.encoder.Compaction;
import com.google.zxing.pdf417.encoder.Dimensions;
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.fiscalprinter.SmFiscalPrinterException;
import com.shtrih.fiscalprinter.TLVItem;
import com.shtrih.fiscalprinter.TLVItems;
import com.shtrih.fiscalprinter.TLVParser;
import com.shtrih.fiscalprinter.TLVTag;
import com.shtrih.fiscalprinter.command.BeginNonFiscalDocument;
import com.shtrih.fiscalprinter.command.CloseNonFiscal;
import com.shtrih.fiscalprinter.command.DeviceMetrics;
import com.shtrih.fiscalprinter.command.FSCommunicationStatus;
import com.shtrih.fiscalprinter.command.FSDocumentInfo;
import com.shtrih.fiscalprinter.command.FSStatusInfo;
import com.shtrih.fiscalprinter.command.GenerateMonoTokenCommand;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.fiscalprinter.command.ReadFieldInfo;
import com.shtrih.fiscalprinter.command.ReadTable;
import com.shtrih.fiscalprinter.command.ReadTableInfo;
import com.shtrih.fiscalprinter.port.UsbPrinterPort;
import com.shtrih.hoho.android.usbserial.driver.UsbSerialDriver;
import com.shtrih.hoho.android.usbserial.driver.UsbSerialProber;
import com.shtrih.jpos.fiscalprinter.FirmwareUpdateObserver;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.tinyjavapostester.databinding.ActivityMainBinding;
import com.shtrih.tinyjavapostester.search.bluetooth.DeviceListActivity;
import com.shtrih.tinyjavapostester.search.tcp.TcpDeviceSearchActivity;
import com.shtrih.util.Hex;
import com.shtrih.util.SysUtils;

import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import jpos.FiscalPrinterConst;
import jpos.JposConst;
import jpos.JposException;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;
import jpos.events.ErrorListener;
import jpos.events.DirectIOListener;
import jpos.events.StatusUpdateListener;

import static com.shtrih.fiscalprinter.command.PrinterConst.SMFP_EFPTR_INVALID_TABLE;

public class MainActivity extends AppCompatActivity
{

    private class EnumViewModel {
        private final String value;
        private final String description;

        public EnumViewModel(String value, String description) {

            this.value = value;
            this.description = description;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    private org.slf4j.Logger log = LoggerFactory.getLogger(MainActivity.class);

    private ShtrihFiscalPrinter printer = null;
    private final Random rand = new Random();
    private final String[] items = {"Кружка", "Ложка", "Миска", "Нож"};

    private EditText tbNetworkAddress;
    private EditText tbMonoToken;
    private EditText tbFFDVersion;
    private EditText nbTextStringCount;
    private EditText nbPositionsCount;
    private EditText nbFiscalizationNumber;
    private EditText nbReceiptCount;
    private EditText nbReceiptInterval;
    private EditText nbDocumentNumber;
    private EditText nbTagNumber;
    private EditText nbTextLinesCount;
    private EditText nbTableNumber;
    private EditText nbTableField;
    private EditText nbTableRow;
    private EditText tbTableCellValue;
    private EditText nbTimeout;

    private AppCompatCheckBox chbFastConnect;
    private AppCompatCheckBox chbScocFirmwareUpdate;

    private String selectedProtocol;

    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        model = ViewModelProviders.of(this).get(MainViewModel.class);

        LogbackConfig.configure(SysUtils.getFilesPath());

        printer = model.getPrinter();

        binding.setVm(model);
        binding.setActivity(this);

        final SharedPreferences pref = this.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);

        tbNetworkAddress = (EditText)findViewById(R.id.tbNetworkAddress);
        restoreAndSaveChangesTo(tbNetworkAddress, pref, "NetworkAddress", "127.0.0.1:12345");

        nbPositionsCount = (EditText)findViewById(R.id.nbPositionsCount);
        restoreAndSaveChangesTo(nbPositionsCount, pref, "CheckPositionsCount", "5");

        nbTextStringCount = (EditText)findViewById(R.id.nbTextStringsCount);
        restoreAndSaveChangesTo(nbTextStringCount, pref, "CheckStringsCount", "5");

        nbReceiptCount = (EditText)findViewById(R.id.nbReceiptCount);
        restoreAndSaveChangesTo(nbReceiptCount, pref, "ReceiptCount", "5");

        nbReceiptInterval = (EditText)findViewById(R.id.nbReceiptInterval);
        restoreAndSaveChangesTo(nbReceiptInterval, pref, "ReceiptInterval", "5");

        nbFiscalizationNumber = (EditText)findViewById(R.id.nbFiscalizationNumber);
        restoreAndSaveChangesTo(nbFiscalizationNumber, pref, "FiscalizationNumber", "1");

        nbDocumentNumber = (EditText)findViewById(R.id.nbDocumentNumber);
        restoreAndSaveChangesTo(nbDocumentNumber, pref, "DocumentNumber", "1");

        nbTagNumber = (EditText)findViewById(R.id.nbTagNumber);
        restoreAndSaveChangesTo(nbTagNumber, pref, "TagNumber", "1041");

        nbTextLinesCount = (EditText)findViewById(R.id.nbTextLinesCount);
        restoreAndSaveChangesTo(nbTextLinesCount, pref, "TextLinesCount", "100");

        nbTableNumber = (EditText)findViewById(R.id.nbTableNumber);
        restoreAndSaveChangesTo(nbTableNumber, pref, "TableNumber", "1");

        nbTableField = (EditText)findViewById(R.id.nbTableField);
        restoreAndSaveChangesTo(nbTableField, pref, "TableField", "1");

        nbTableRow = (EditText)findViewById(R.id.nbTableRow);
        restoreAndSaveChangesTo(nbTableRow, pref, "TableRow", "1");

        tbTableCellValue = (EditText)findViewById(R.id.tbTableCellValue);
        restoreAndSaveChangesTo(tbTableCellValue, pref, "TableCellValue", "");

        tbMonoToken = (EditText)findViewById(R.id.tbMonoToken);
        restoreAndSaveChangesTo(tbMonoToken, pref, "MonoToken", "");

        tbFFDVersion = (EditText)findViewById(R.id.tbFFDVersion);

        nbTimeout = (EditText)findViewById(R.id.nbTimeout);
        restoreAndSaveChangesTo(nbTimeout, pref, "ByteTimeout", "3000");

        chbFastConnect = (AppCompatCheckBox)findViewById(R.id.chbFastConnect);
        restoreAndSaveChangesTo(chbFastConnect, pref, "FastConnect", true);

        chbScocFirmwareUpdate = (AppCompatCheckBox)findViewById(R.id.chbScocFirmwareUpdate);
        restoreAndSaveChangesTo(chbScocFirmwareUpdate, pref, "ScocFirmwareUpdate", false);

        Spinner cbProtocol = (Spinner)findViewById(R.id.cbProtocol);

        ArrayList<EnumViewModel> protocols = new ArrayList<>();
        protocols.add(new EnumViewModel("0", "Standard"));
        protocols.add(new EnumViewModel("1", "KKT 2.0"));

        ArrayAdapter<EnumViewModel> protocolsAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, protocols);

        final String PREFERENCES_PROTOCOL_KEY = "Protocol";

        cbProtocol.setAdapter(protocolsAdapter);
        cbProtocol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EnumViewModel vm = (EnumViewModel) parent.getItemAtPosition(position);
                selectedProtocol = vm.getValue();
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt(PREFERENCES_PROTOCOL_KEY, position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        int savedProtocolIndex = pref.getInt(PREFERENCES_PROTOCOL_KEY, 0);
        cbProtocol.setSelection(savedProtocolIndex);

        selectedProtocol = ((EnumViewModel) protocolsAdapter.getItem(savedProtocolIndex)).getValue();

        String logPath = "Log path: " + SysUtils.getFilesPath() + LogbackConfig.MainFileName;

        TextView lblLogPath = (TextView)findViewById(R.id.lblLogPathValue);
        lblLogPath.setText(logPath);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerUsbReceiver();
        findSerialPortDevice();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterUsbReceiver();
    }

    private void restoreAndSaveChangesTo(final EditText edit, final SharedPreferences pref, final String key, final String defaultValue) {
        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(key, c.toString());
                editor.apply();
            }

            @Override
            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable c) {
            }
        });

        String savedAddress = pref.getString(key, defaultValue);
        edit.setText(savedAddress);
    }

    private void restoreAndSaveChangesTo(final CompoundButton edit, final SharedPreferences pref, final String key, final boolean defaultValue) {
        edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(key, compoundButton.isChecked());
                editor.apply();
            }
        });

        boolean savedValue = pref.getBoolean(key, defaultValue);
        edit.setChecked(savedValue);
    }

    public static final String ACTION_USB_DISCONNECTED = "com.felhr.usbservice.USB_DISCONNECTED";
    public static final String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";
    public static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    public static final String ACTION_USB_PERMISSION_GRANTED = "com.felhr.usbservice.USB_PERMISSION_GRANTED";
    public static final String ACTION_USB_PERMISSION_NOT_GRANTED = "com.felhr.usbservice.USB_PERMISSION_NOT_GRANTED";

    private void findSerialPortDevice() {
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        if (usbManager == null) {
            log.debug("UsbManager is not available");
            return;
        }

        // This snippet will try to open the first encountered usb device connected, excluding usb root hubs
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (usbDevices.isEmpty())
            return;

        for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {

            UsbDevice device = entry.getValue();

            int deviceVID = device.getVendorId();
            int devicePID = device.getProductId();

            log.debug("Opening device '" + entry.getKey() + "' VID: " + deviceVID + ", PID " + devicePID);

            if (usbManager.hasPermission(device))
                return;

            PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            usbManager.requestPermission(device, mPendingIntent);
        }
    }

    private void registerUsbReceiver() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_USB_HOST))
            return;

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DETACHED);
        filter.addAction(ACTION_USB_ATTACHED);
        filter.addAction(ACTION_USB_STATE);
        registerReceiver(usbReceiver, filter);
    }

    private void unregisterUsbReceiver() {
        try {
            this.unregisterReceiver(usbReceiver);
        } catch (final Exception exception) {
            // The receiver was not registered.
            // There is nothing to do in that case.
            // Everything is fine.
        }
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {

            String action = arg1.getAction();
            if (action == null)
                return;

            log.debug("Usb broadcast action received: " + arg1.getAction());
            if (action.equals(ACTION_USB_PERMISSION)) {
                boolean granted = arg1.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) // User accepted our USB connection. Try to open the device as a serial port
                {
                    Intent intent = new Intent(ACTION_USB_PERMISSION_GRANTED);
                    arg0.sendBroadcast(intent);
                    log.debug("Permission granted for USB ");
                } else // User not accepted our USB connection. Send an Intent to the Main Activity
                {
                    Intent intent = new Intent(ACTION_USB_PERMISSION_NOT_GRANTED);
                    arg0.sendBroadcast(intent);
                    log.debug("Permission not granted for USB");
                }
            }

            if (action.equals(ACTION_USB_STATE)) {
                findSerialPortDevice(); // A USB device has been attached. Try to open it as a Serial port
            }

            if (action.equals(ACTION_USB_ATTACHED)) {
                findSerialPortDevice(); // A USB device has been attached. Try to open it as a Serial port
            }
            if (action.equals(ACTION_USB_DETACHED)) {
                // Usb device was disconnected. send an intent to the Main Activity
                Intent intent = new Intent(ACTION_USB_DISCONNECTED);
                arg0.sendBroadcast(intent);
            }
        }
    };

    public void verifyBTPermissions() {

        // For Android 6 or higher you have to check the location permission
        // Seen at https://stackoverflow.com/questions/45197191/how-can-i-modify-bluetoothlegatt-to-enable-ble-device-scanning-on-android-6-0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> permissions = new ArrayList<String>();

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED)
            {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
            /*
            if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED)
            {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                {
                    permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                }
            }
            */
            if(permissions.size() != 0)
            {
                requestPermissions(permissions.toArray(new String[0]), 1002);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case DeviceListActivity.REQUEST_CONNECT_BT_DEVICE:
                if (resultCode == Activity.RESULT_OK)
                {
                    verifyBTPermissions();

                    Bundle extras = data.getExtras();

                    if (extras == null)
                        return;

                    String address = extras.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                    if (address == null)
                        return;

                    new ConnectToBluetoothDeviceTask(
                            this,
                            address,
                            createFirmwareUpdateObserver(),
                            nbTimeout.getText().toString(),
                            chbFastConnect.isChecked(),
                            chbScocFirmwareUpdate.isChecked()).execute();
                }
            case TcpDeviceSearchActivity.REQUEST_SEARCH_TCP_DEVICE:
                if (resultCode == Activity.RESULT_OK) {

                    Bundle extras = data.getExtras();

                    if (extras == null)
                        return;

                    String address = extras.getString("Address");

                    if (address == null)
                        return;

                    tbNetworkAddress.setText(address);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private FirmwareUpdateObserver createFirmwareUpdateObserver() {
        return new FirmwareUpdaterObserverImpl(model);
    }

    private class ConnectToBluetoothDeviceTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private final String address;
        private final FirmwareUpdateObserver observer;
        private final String timeout;
        private final boolean fastConnect;
        private final boolean scocFirmwareAutoupdate;

        private long startedAt;
        private long doneAt;

        private ProgressDialog dialog;

        public ConnectToBluetoothDeviceTask(AppCompatActivity parent, String address, FirmwareUpdateObserver observer, String timeout, boolean fastConnect, boolean scocFirmwareAutoupdate) {
            this.parent = parent;

            this.address = address;
            this.observer = observer;
            this.timeout = timeout;
            this.fastConnect = fastConnect;
            this.scocFirmwareAutoupdate = scocFirmwareAutoupdate;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Connecting to device", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                HashMap<String, String> props = new HashMap<>();
                props.put("portName", address);
                props.put("portType", "3");
                props.put("portClass", "com.shtrih.fiscalprinter.port.BluetoothLEPort");
                props.put("protocolType", selectedProtocol);
                props.put("fastConnect", fastConnect ? "1" : "0");
                props.put("capScocUpdateFirmware", scocFirmwareAutoupdate ? "1" : "0");
                props.put("byteTimeout", timeout);
                JposConfig.configure("ShtrihFptr", getApplicationContext(), props);


                // test reconnection
                if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                    printer.close();
                }
                printer.open("ShtrihFptr");
                //printer.addStatusUpdateListener(new FptrEventListener());
                printer.claim(3000);
                printer.setDeviceEnabled(true);
                model.ScocUpdaterStatus.set("");
                printer.setParameter3(SmFptrConst.SMFPTR_DIO_PARAM_FIRMWARE_UPDATE_OBSERVER, observer);
                return null;
            } catch (Exception e) {
                log.error("Bluetooth device " + address + " connection using protocol " + selectedProtocol + " failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public class FptrEventListener implements StatusUpdateListener
    {
        public void statusUpdateOccurred(StatusUpdateEvent event) {
            log.debug("Application statusUpdateOccurred: " + event.getStatus());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_connect_ptk) {
            Intent i = new Intent(this, DeviceListActivity.class);
            startActivityForResult(i, DeviceListActivity.REQUEST_CONNECT_BT_DEVICE);
            return true;
        }

        if(id == R.id.action_auto_connect_bluetooth){
            autoConnectBluetoothDevice();
            return true;
        }

        if (id == R.id.action_share_log) {

            shareLogFile();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void autoConnectBluetoothDevice() {

        new AutoConnectBluetoothDeviceTask(
                this,
                tbNetworkAddress.getText().toString(),
                createFirmwareUpdateObserver(),
                nbTimeout.getText().toString(),
                chbFastConnect.isChecked(),
                chbScocFirmwareUpdate.isChecked()).execute();
    }

    private class AutoConnectBluetoothDeviceTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private final String address;
        private final FirmwareUpdateObserver observer;
        private final String timeout;
        private final boolean fastConnect;
        private final boolean scocFirmwareAutoupdate;

        private long startedAt;
        private long doneAt;

        private String text;

        private ProgressDialog dialog;

        public AutoConnectBluetoothDeviceTask(AppCompatActivity parent, String address, FirmwareUpdateObserver observer, String timeout, boolean fastConnect, boolean scocFirmwareAutoupdate) {
            this.parent = parent;

            this.address = address;
            this.observer = observer;
            this.timeout = timeout;
            this.fastConnect = fastConnect;
            this.scocFirmwareAutoupdate = scocFirmwareAutoupdate;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Connecting to device", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                    printer.close();
                }

                startedAt = System.currentTimeMillis();

                log.debug("Generating jpos.xml...");

                Map<String, String> props = new HashMap<>();
                props.put("portName", "SHTRIH");
                props.put("portType", "3");
                props.put("portClass", "com.shtrih.fiscalprinter.port.BluetoothLEPort");
                props.put("protocolType", selectedProtocol);
                props.put("fastConnect", fastConnect ? "1" : "0");
                props.put("capScocUpdateFirmware", scocFirmwareAutoupdate ? "1" : "0");
                props.put("byteTimeout", timeout);
                props.put("searchByPortEnabled", "1");

                JposConfig.configure("ShtrihFptr", getApplicationContext(), props);

                log.debug("Opening...");

                printer.open("ShtrihFptr");

                log.debug("Claiming...");

                printer.claim(3000);

                log.debug("Setting device enabled...");

                printer.setDeviceEnabled(true);
                model.ScocUpdaterStatus.set("");

                log.debug("Connected!");

                printer.setParameter3(SmFptrConst.SMFPTR_DIO_PARAM_FIRMWARE_UPDATE_OBSERVER, observer);

                doneAt = System.currentTimeMillis();

                String[] lines = new String[1];
                printer.getData(FiscalPrinterConst.FPTR_GD_PRINTER_ID, null, lines);
                String serialNumber = lines[0];
                DeviceMetrics deviceMetrics = printer.readDeviceMetrics();

                text = deviceMetrics.getDeviceName() + " " + serialNumber;

                return null;

            } catch (Exception e) {
                log.error("Connection to Wi-Fi device " + address + " using protocol " + selectedProtocol + " failed", e);
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage(text + "\nSuccess " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    private void shareLogFile() {
        try {
            String logPath = SysUtils.getFilesPath() + LogbackConfig.MainFileName;

            Intent intentShareFile = new Intent(Intent.ACTION_SEND);

            intentShareFile.setType("text/plain");

            boolean logExists = fileExists(logPath);

            if (!logExists) {
                Toast.makeText(this, "Log file was not found", Toast.LENGTH_LONG).show();
                return;
            }

            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + logPath));

            startActivity(Intent.createChooser(intentShareFile, "Share log"));
        } catch (Exception e) {
            log.error("Log sharing failed", e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean fileExists(String path) {
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    public void printEAN13Barcode(View v) {
        new PrintEAN13BarcodeTask(this).execute();
    }

    private class PrintEAN13BarcodeTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintEAN13BarcodeTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Printing EAN13 barcode", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {

                PrinterBarcode barcode = new PrinterBarcode();
                barcode.setText("460704243915");
                barcode.setLabel("EAN13 test");
                barcode.setType(SmFptrConst.SMFPTR_BARCODE_EAN13);
                barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
                barcode.setHeight(100);
                barcode.setBarWidth(2);
                printer.printBarcode(barcode);

                return null;

            } catch (Exception e) {
                log.error("EAN13 printing failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);
        }
    }

    public void printPDF417Barcode(View v) {
        new PrintPDF417BarcodeTask(this).execute();
    }

    private class PrintPDF417BarcodeTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintPDF417BarcodeTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Printing PDF417 barcode", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... p) {

            startedAt = System.currentTimeMillis();

            try {

                PrinterBarcode barcode = new PrinterBarcode();
                barcode.setText("\"4C63A673C86B0976C0B24495848F6EF157792203A0D275\\n\"\n"
                        + "                            + \"1F525456644096478D256A910EFEABB67\"");

                barcode.setType(SmFptrConst.SMFPTR_BARCODE_PDF417);
                barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);

                barcode.setBarWidth(2);
                barcode.setVScale(5);

                Map<EncodeHintType, Object> params = new HashMap<EncodeHintType, Object>();
                // Измерения, тут мы задаем количество колонок и столбцов
                params.put(EncodeHintType.PDF417_DIMENSIONS, new Dimensions(3, 3, 2, 60));
                // Можно задать уровень коррекции ошибок, по умолчанию он 0
                params.put(EncodeHintType.ERROR_CORRECTION, 1);
                barcode.addParameter(params);

                printer.printBarcode(barcode);

                return null;

            } catch (Exception e) {
                log.error("PDF417 printing failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);
        }
    }

    public void printQRBarcode(View v) {
        new PrintQRBarcodeTask(this).execute();
    }

    private class PrintQRBarcodeTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintQRBarcodeTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Printing QR code", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... p) {

            startedAt = System.currentTimeMillis();

            try {

                PrinterBarcode barcode = new PrinterBarcode();
                barcode.setText("\"4C63A673C86B0976C0B24495848F6EF157792203A0D275\\n\"\n"
                        + "                            + \"1F525456644096478D256A910EFEABB67\"");

                barcode.setTextPosition(SmFptrConst.SMFPTR_TEXTPOS_NOTPRINTED);
                barcode.setBarWidth(5);
                barcode.setHeight(100);
                barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);

                barcode.setType(SmFptrConst.SMFPTR_BARCODE_QR_CODE);
                printer.printBarcode(barcode);

                return null;

            } catch (Exception e) {
                log.error("QR-code printing failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);
        }
    }

    private byte[] readFile(String filePath) throws Exception {
        FileInputStream is = new FileInputStream(new File(filePath));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while (is.available() > 0) {
            os.write(is.read());
        }
        is.close();
        return os.toByteArray();
    }

    private void printBarcodeBinary() throws Exception {
        File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File picsDir = new File(dcimDir, "Camera");
        picsDir.mkdirs(); // make if not exist
        File barcodeFile = new File(picsDir, "barcode.bin");
        byte[] bc = readFile(barcodeFile.getAbsolutePath());
        PrinterBarcode barcode = new PrinterBarcode();
        barcode.setText(new String(bc, "ISO-8859-1"));
        barcode.setLabel("PDF417 test");
        barcode.setType(SmFptrConst.SMFPTR_BARCODE_PDF417);
        barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
        barcode.setTextPosition(SmFptrConst.SMFPTR_TEXTPOS_NOTPRINTED);

        Map<EncodeHintType, Object> params = new HashMap<EncodeHintType, Object>(0);
        params.put(EncodeHintType.PDF417_COMPACTION, Compaction.BYTE);
        // params.put(EncodeHintType.PDF417_DIMENSIONS, new Dimensions(5, 5, 2,
        // 60));
        params.put(EncodeHintType.MARGIN, 0);
        params.put(EncodeHintType.ERROR_CORRECTION, 0);
        barcode.addParameter(params);

        printer.printText("_________________________________________");
        printer.printText("PDF417 binary, data length: " + bc.length);
        printer.printText("Module width: " + 2);
        long time = System.currentTimeMillis();
        printer.printBarcode(barcode);
        time = System.currentTimeMillis() - time;
        printer.printText(String.format("Barcode print time: %d ms", time));
        printer.printText("_________________________________________");
    }

    public void printText(View v) {

        final int lines = Integer.parseInt(nbTextLinesCount.getText().toString());

        new PrintTextTask(this, lines).execute();
    }

    private class PrintTextTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private final int lines;

        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintTextTask(AppCompatActivity parent, int lines) {
            this.parent = parent;
            this.lines = lines;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Printing text", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {


            try {
                printer.resetPrinter();

                boolean isCashCore = isCashCore(printer);

                startedAt = System.currentTimeMillis();

                if (isCashCore) {
                    BeginNonFiscalDocument cmd = new BeginNonFiscalDocument();
                    cmd.setPassword(printer.getUsrPassword());
                    printer.executeCommand(cmd);
                }

                String text = "Мой дядя самых честных правил";

                FontNumber font = new FontNumber(1);

                for (int i = 0; i < lines; i++) {
                    printer.printText(text, font);
                }

                if (isCashCore) {
                    CloseNonFiscal cmd = new CloseNonFiscal();
                    cmd.setPassword(printer.getUsrPassword());
                    printer.executeCommand(cmd);
                }

                return null;

            } catch (Exception e) {
                log.error("Text printing failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        private boolean isCashCore(ShtrihFiscalPrinter printer) throws JposException {
            DeviceMetrics metrics = printer.readDeviceMetrics();
            return metrics.getModel() == 45; // КЯ
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);
        }
    }

    public void disconnect(View v) {
        new DisconnectTask(this).execute();
    }

    private class DisconnectTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;

        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public DisconnectTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Disconnecting", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                if (printer.getDeviceEnabled()){
                    printer.setDeviceEnabled(false);
                }
                if (printer.getClaimed()){
                    printer.release();
                }
                if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                    printer.close();
                }

                model.ScocUpdaterStatus.set("");

                return null;

            } catch (Exception e) {
                log.error("Disconnect failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);
        }
    }

    public void printReceipt(View v) {

        final int positions = Integer.parseInt(nbPositionsCount.getText().toString());
        final int strings = Integer.parseInt(nbTextStringCount.getText().toString());

        (new PrintReceiptTask(this, positions, strings, 1, 0)).execute();
    }

    public void printReceipts(View v) {

        final int positions = Integer.parseInt(nbPositionsCount.getText().toString());
        final int strings = Integer.parseInt(nbTextStringCount.getText().toString());
        final int receipts = Integer.parseInt(nbReceiptCount.getText().toString());
        final int interval = Integer.parseInt(nbReceiptInterval.getText().toString());
        (new PrintReceiptTask(this, positions, strings, receipts, interval)).execute();
    }

    private class PrintReceiptTask extends AsyncTask<Void, String, String> {

        private final AppCompatActivity parent;
        private final int positions;
        private final int strings;
        private final int receipts;
        private final int interval;

        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintReceiptTask(AppCompatActivity parent, int positions, int strings,
            int receipts, int interval) {
            this.parent = parent;
            this.positions = positions;
            this.strings = strings;
            this.receipts = receipts;
            this.interval = interval;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(parent, "Printing receipt", "Please wait...", true);
        }

        @Override
        protected void onProgressUpdate(String... title){
            dialog.setTitle(title[0]);
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                startedAt = System.currentTimeMillis();
                for (int i=1;i<=receipts;i++)
                {
                    if (isCancelled()) break;

                    try
                    {
                        String title = String.format("Printing receipt %d...", i);
                        publishProgress(title);
                        printSalesReceipt(positions, strings);
                    }
                    catch(Exception e)
                    {
                        log.error("Receipt printing failed " + i + e.getMessage());
                    }
                    //Thread.sleep(interval * 1000);
                }

                return null;

            } catch (Exception e) {
                log.error("Receipt printing failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        log.debug(message);
    }

    public void readFSCommStatus(View v) {
        new ReadFSCommStatusTask(this).execute();
    }

    private class ReadFSCommStatusTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;

        private int documentsCount;

        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public ReadFSCommStatusTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Reading FS comm status", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {

                FSCommunicationStatus status = printer.fsReadCommStatus();
                documentsCount = status.getUnsentDocumentsCount();

                return null;

            } catch (Exception e) {
                log.error("FS communication status reading failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Unsent documents " + documentsCount);
            else
                showMessage(result);
        }
    }

    public void printFiscalReceipt() throws Exception
    {
            printer.resetPrinter();
            printer.setFiscalReceiptType(SmFptrConst.SMFPTR_RT_SALE);
            printer.beginFiscalReceipt(true);

            char GS = 0x1D;
            String barcode
                    = "010405104227920221TB6qQHbmOTZBf" + GS + "2406402" + GS + "91ffd0" + GS
                    + "92DbZgaQm2x0uA5+8/AzMM9hVq6apGvtM3bJzejjpHan2pvK4O+XbYcVgFRR5I4HmCLQvZ74KgKkIhVADd==";
            printer.setItemCode(barcode, "");
            printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_ITEM_MARK_TYPE, SmFptrConst.MARK_TYPE_TOBACCO);

            printer.printRecItem("Item 1", 10099, 1000, 1, 10099, "");
            printer.fsWriteOperationTag(1197, "KG");

            printer.printRecItem("Item 2", 20000, 1000, 2, 20000, "");
            printer.printRecItem("Item 3", 30000, 1000, 3, 30000, "");
            printer.printRecItem("Item 4", 40000, 1000, 4, 40000, "");
            printer.printRecItem("Item 5", 50000, 1000, 5, 50000, "");
            printer.printRecItem("Item 6", 60000, 1000, 6, 60000, "");
            printer.printRecSubtotal(210099);

            printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_0, 1);
            printer.printRecSubtotalAdjustment(1, "", 99);
            printer.printRecTotal(210000, 210000, "1");
            printer.endFiscalReceipt(false);
    }

    private void printSalesReceipt(int positions, int strings) throws Exception
    {
        if (positions <= 0){
            positions = 1;
        }
        if (strings < 0){
            strings = 0;
        }

        printer.resetPrinter();
        final int fiscalReceiptType = FiscalPrinterConst.FPTR_RT_SALES;
        printer.setFiscalReceiptType(fiscalReceiptType);
        printer.beginFiscalReceipt(true);
        //Развернул сожержимое метода, чтобы было понятно, какие теги передаём
        printer.fsWriteTag(1016, "2225031594  ");
        printer.fsWriteTag(1073, "+78001000000");
        //printer.fsWriteTag(1057, "1");
        printer.fsWriteTag(1005, "НОВОСИБИРСК,КИРОВА,86");
        printer.fsWriteTag(1075, "+73833358088");
        printer.fsWriteTag(1171, "+73833399242");
        printer.fsWriteTag(1044, "Прием денежных средств");
        printer.fsWriteTag(1026, "РНКО \"ПЛАТЕЖНЫЙ ЦЕНТР\"");

        // receipt items
        for (int i=0;i<positions;i++) {
            printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_ITEM_PAYMENT_TYPE, 4);
            printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_ITEM_SUBJECT_TYPE, 4);
            printer.printRecItem("Приём платежа", 123, 0, 0, 0, "Оплата");
        }
        // text lines
        for (int i=0;i<strings;i++)
        {
            printer.printNormal(FiscalPrinterConst.FPTR_S_RECEIPT, "Text line " + i);
        }
        long total = positions * 123;
        printer.printRecTotal(total, total, "0");
        printer.endFiscalReceipt(false);
    }

    private void printRefundReceipt() throws Exception {
        int howMuch = rand.nextInt(10);
        howMuch += 1; // guarantee

        long payment = 0;
        printer.resetPrinter();
        printer.setFiscalReceiptType(jpos.FiscalPrinterConst.FPTR_RT_REFUND);
        printer.beginFiscalReceipt(false);
        for (int i = 0; i < howMuch; i++) {
            long price = Math.abs(rand.nextLong() % 1000);
            payment += price;

            String itemName = items[rand.nextInt(items.length)];
            printer.printRecItemRefund(itemName, price, 0, 0, 0, "");
        }
        printer.printRecTotal(payment, payment, "1");
        printer.endFiscalReceipt(false);

        FSStatusInfo status = printer.fsReadStatus();
        FSDocumentInfo document = printer.fsFindDocument(status.getDocNumber());
    }

    private void printReceipt2() throws JposException, InterruptedException {
        printer.resetPrinter();
        int numHeaderLines = printer.getNumHeaderLines();
        for (int i = 1; i <= numHeaderLines; i++) {
            printer.setHeaderLine(i, "Header line " + i, false);
        }
        int numTrailerLines = printer.getNumTrailerLines();
        for (int i = 1; i <= numTrailerLines; i++) {
            printer.setTrailerLine(i, "Trailer line " + i, false);
        }
        printer.setAdditionalHeader("AdditionalHeader line 1\nAdditionalHeader line 2");
        printer.setFiscalReceiptType(jpos.FiscalPrinterConst.FPTR_RT_SALES);
        printer.beginFiscalReceipt(true);
        printer.printRecItem("За газ\nТел.: 123456\nЛ/сч: 789456", 100, 12, 0, 0, "");
        printer.printRecTotal(70, 70, "0");
        printer.printRecTotal(10, 10, "1");
        printer.printRecTotal(10, 10, "2");
        printer.printRecTotal(10, 10, "3");

        printer.endFiscalReceipt(false);
        printer.printDuplicateReceipt();
    }

    public void printZReport(View v) {
        new PrintZReportTask(this).execute();
    }

    private class PrintZReportTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintZReportTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Printing Z-report", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                printer.resetPrinter();

                printer.printZReport();

                return null;

            } catch (Exception e) {
                log.error("Z-report printing failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);
        }
    }


    public void openFiscalDay(View v) {
        new OpenFiscalDayTask(this).execute();
    }

    private class OpenFiscalDayTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public OpenFiscalDayTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Opening fiscal day", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                printer.resetPrinter();

                printer.openFiscalDay();

                return null;

            } catch (Exception e) {
                log.error("Shift opening failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);
        }
    }

    public void printJournalCurrentDay(View v) {
        new PrintJournalCurrentDayTask(this).execute();
    }

    private class PrintJournalCurrentDayTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintJournalCurrentDayTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Printing journal current day", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                printer.printJournalCurrentDay();

                return null;

            } catch (Exception e) {
                log.error("Current day journal printing failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);
        }
    }

    public void printDuplicateReceipt(View v) {
        new PrintDuplicateReceiptTask(this).execute();
    }

    private class PrintDuplicateReceiptTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintDuplicateReceiptTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Printing duplicate receipt", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                printer.printDuplicateReceipt();

                return null;

            } catch (Exception e) {
                log.error("Duplicate receipt printing failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);
        }
    }

    public void connectToDeviceDirect(View view) {

        new ConnectToWiFiDeviceTask(
                this,
                tbNetworkAddress.getText().toString(),
                createFirmwareUpdateObserver(),
                nbTimeout.getText().toString(),
                chbFastConnect.isChecked(),
                chbScocFirmwareUpdate.isChecked()).execute();
    }

    private class ConnectToWiFiDeviceTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private final String address;
        private final FirmwareUpdateObserver observer;
        private final String timeout;
        private final boolean fastConnect;
        private final boolean scocFirmwareAutoupdate;

        private long startedAt;
        private long doneAt;

        private String text;

        private ProgressDialog dialog;

        public ConnectToWiFiDeviceTask(AppCompatActivity parent, String address, FirmwareUpdateObserver observer, String timeout, boolean fastConnect, boolean scocFirmwareAutoupdate) {
            this.parent = parent;

            this.address = address;
            this.observer = observer;
            this.timeout = timeout;
            this.fastConnect = fastConnect;
            this.scocFirmwareAutoupdate = scocFirmwareAutoupdate;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Connecting to device", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                    printer.close();
                }

                startedAt = System.currentTimeMillis();

                log.debug("Generating jpos.xml...");

                Map<String, String> props = new HashMap<>();
                props.put("portName", address);
                props.put("portType", "2");
                props.put("protocolType", selectedProtocol);
                props.put("fastConnect", fastConnect ? "1" : "0");
                props.put("capScocUpdateFirmware", scocFirmwareAutoupdate ? "1" : "0");
                props.put("byteTimeout", timeout);

                JposConfig.configure("ShtrihFptr", getApplicationContext(), props);

                log.debug("Opening...");

                printer.open("ShtrihFptr");

                log.debug("Claiming...");

                printer.claim(3000);

                log.debug("Setting device enabled...");

                printer.setDeviceEnabled(true);
                model.ScocUpdaterStatus.set("");

                log.debug("Connected!");

                printer.setParameter3(SmFptrConst.SMFPTR_DIO_PARAM_FIRMWARE_UPDATE_OBSERVER, observer);

                doneAt = System.currentTimeMillis();

                String[] lines = new String[1];
                printer.getData(FiscalPrinterConst.FPTR_GD_PRINTER_ID, null, lines);
                String serialNumber = lines[0];
                DeviceMetrics deviceMetrics = printer.readDeviceMetrics();

                text = deviceMetrics.getDeviceName() + " " + serialNumber;

                return null;

            } catch (Exception e) {
                log.error("Connection to Wi-Fi device " + address + " using protocol " + selectedProtocol + " failed", e);
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage(text + "\nSuccess " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void searchDeviceDirect(View view) {

        Intent intent = new Intent(this, TcpDeviceSearchActivity.class);
        startActivityForResult(intent, TcpDeviceSearchActivity.REQUEST_SEARCH_TCP_DEVICE);
    }

    public void connectToUSBDevice(View view) {

        try {
            UsbManager usbManager = (UsbManager) getApplicationContext().getSystemService(Context.USB_SERVICE);
            List<UsbSerialDriver> usbs = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);

            if (usbs.size() == 0) {
                showMessage("No USB device found");
                return;
            }

            log.debug("Found " + usbs.size() + " USB devices");

            int deviceId = usbs.get(0).getDevice().getDeviceId();

            HashMap<String, String> props = new HashMap<>();
            props.put("portName", String.format(Locale.ENGLISH, "%d", deviceId));
            props.put("protocolType", "0");
            props.put("portType", "3");
            props.put("portClass", "com.shtrih.fiscalprinter.port.UsbPrinterPort");
            props.put("fastConnect", chbFastConnect.isChecked() ? "1" : "0");
            props.put("capScocUpdateFirmware", chbScocFirmwareUpdate.isChecked() ? "1" : "0");

            JposConfig.configure("ShtrihFptr", getApplicationContext(), props);
        } catch (Exception e) {
            log.error("USB device connection failed", e);
            showMessage("Configuration error: " + e.getMessage());
            return;
        }

        try {

            if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                printer.close();
            }

            UsbPrinterPort.Context = getApplicationContext();

            try {
                printer.open("ShtrihFptr");
            } finally {
                UsbPrinterPort.Context = null;
            }

            printer.claim(3000);
            printer.setDeviceEnabled(true);
            printer.setParameter3(SmFptrConst.SMFPTR_DIO_PARAM_FIRMWARE_UPDATE_OBSERVER, createFirmwareUpdateObserver());

            String[] lines = new String[1];
            printer.getData(FiscalPrinterConst.FPTR_GD_PRINTER_ID, null, lines);
            String serialNumber = lines[0];
            DeviceMetrics deviceMetrics = printer.readDeviceMetrics();

            showMessage(deviceMetrics.getDeviceName() + " " + serialNumber);

        } catch (Exception e) {
            log.debug("USB device connection failed", e);
            showMessage(e.getMessage());
        }
    }

    public void connectToDevice(View view)
    {
        (new ConnectToDeviceTask(this)).execute();
    }


    private class ConnectToDeviceTask extends AsyncTask<Void, Void, String>
    {
        private long startedAt;
        private long doneAt;
        private final AppCompatActivity parent;
        private ProgressDialog dialog;

        public ConnectToDeviceTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(parent, "Connecting to device", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                connectToDevice();
                return null;
            } catch (Exception e) {
                log.error("Connect to device failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

    }

    private void connectToDevice() throws Exception
    {
        HashMap<String, String> props = new HashMap<>();
        props.put("portName", "SHTRIH-NANO-F");
        props.put("portType", "3");
        props.put("portClass", "com.shtrih.fiscalprinter.port.BluetoothLEPort");
        props.put("protocolType", "1");
        props.put("fastConnect", "1");
        props.put("capScocUpdateFirmware", "0");
        props.put("byteTimeout", "10000");
        JposConfig.configure("ShtrihFptr", getApplicationContext(), props);

        if (printer.getState() != JposConst.JPOS_S_CLOSED) {
            printer.close();
        }
        printer.open("ShtrihFptr");
        printer.claim(3000);
        printer.setDeviceEnabled(true);
    }


    public void readFiscalizationTag(View view) {

        final int fiscalizationNumber = Integer.parseInt(nbFiscalizationNumber.getText().toString());
        final int tagNumber = Integer.parseInt(nbTagNumber.getText().toString());

        new ReadFiscalizationTagTask(this, fiscalizationNumber, tagNumber).execute();
    }

    private class ReadFiscalizationTagTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private final int fiscalizationNumber;
        private final int tagNumber;

        private long startedAt;
        private long doneAt;

        private String text;

        private ProgressDialog dialog;

        public ReadFiscalizationTagTask(AppCompatActivity parent, int fiscalizationNumber, int tagNumber) {
            this.parent = parent;
            this.fiscalizationNumber = fiscalizationNumber;
            this.tagNumber = tagNumber;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Reading fiscalization tag", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                byte[] tlv = printer.readFiscalizationTag(fiscalizationNumber, tagNumber);

                text = renderTLV(tlv);

                return null;

            } catch (Exception e) {
                log.error("Fiscalization tag reading failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage(text);
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void readFiscalizationTLV(View view) {

        final int fiscalizationNumber = Integer.parseInt(nbFiscalizationNumber.getText().toString());

        new ReadFiscalizationTLVTask(this, fiscalizationNumber).execute();
    }

    private class ReadFiscalizationTLVTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private final int fiscalizationNumber;

        private long startedAt;
        private long doneAt;

        private String text;

        private ProgressDialog dialog;

        public ReadFiscalizationTLVTask(AppCompatActivity parent, int fiscalizationNumber) {
            this.parent = parent;
            this.fiscalizationNumber = fiscalizationNumber;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Reading fiscalization TLV", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                byte[] tlv = printer.readFiscalizationTLV(fiscalizationNumber);

                text = renderTLV(tlv);

                return null;

            } catch (Exception e) {
                log.error("Fiscalization TLV reading failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage(text);
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void readDocumentTLV(View view) {

        final int documentNumber = Integer.parseInt(nbDocumentNumber.getText().toString());

        new ReadDocumentTLVTask(this, documentNumber).execute();
    }

    private class ReadDocumentTLVTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private final int fiscalizationNumber;

        private long startedAt;
        private long doneAt;

        private String text;

        private ProgressDialog dialog;

        public ReadDocumentTLVTask(AppCompatActivity parent, int fiscalizationNumber) {
            this.parent = parent;
            this.fiscalizationNumber = fiscalizationNumber;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Reading fiscalization TLV", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                byte[] tlv = printer.fsReadDocumentTLV(fiscalizationNumber).getTLV();

                text = renderTLV(tlv);

                return null;

            } catch (Exception e) {
                log.error("Document TLV reading failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage(text);
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    private String renderTLV(byte[] tlv) throws Exception {
        StringBuilder sb = new StringBuilder();

        renderTLV(sb, "", tlv);

        return sb.toString().trim();
    }

    private void renderTLV(StringBuilder sb, String indent, byte[] tlv) throws Exception {

        TLVParser parser = new TLVParser();
        parser.parse(tlv);

        TLVItems items = parser.getItems();

        for (int i = 0; i < items.size(); i++) {
            TLVItem item = items.get(i);
            if (item.getTag().getType() == TLVTag.TLVType.itSTLV) {
                String tagName = item.getTag().getPrintName();

                sb.append(indent);
                sb.append(item.getTag().getId());
                sb.append(",");
                sb.append(tagName);
                sb.append(":");
                sb.append("\n");
                renderTLV(sb, indent + "  ", item.getData());
            } else {

                String tagName = item.getTag().getPrintName();
                String itemText = item.getText();

                if (item.getTag().getId() == 1077) {// 1077,ФП
                    itemText = toFP(item.getData()) + "(" + Hex.toHex2(item.getData()) + ")";
                }

                sb.append(indent);
                sb.append(item.getTag().getId());
                sb.append(",");
                sb.append(tagName);
                sb.append(":");
                sb.append(itemText);
                sb.append("\n");
            }
        }
    }

    private long toFP(byte[] d) {

        long result = 0;
        for (int i = 2; i < d.length; i++) {
            result <<= 8;
            result |= d[i] & 0xFF;
        }
        return result;
    }

    public void readTableCell(View view) {

        final int tableNumber = Integer.parseInt(nbTableNumber.getText().toString());
        final int tableRow = Integer.parseInt(nbTableRow.getText().toString());
        final int tableField = Integer.parseInt(nbTableField.getText().toString());

        new ReadTableCellTask(this, tableNumber, tableRow, tableField, tbTableCellValue).execute();
    }

    private class ReadTableCellTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private final int tableNumber;
        private final int tableColumn;
        private final int tableField;
        private final EditText valueTb;

        private ProgressDialog dialog;

        public ReadTableCellTask(AppCompatActivity parent, int tableNumber, int tableColumn, int tableField, EditText value) {
            this.parent = parent;
            this.tableNumber = tableNumber;
            this.tableColumn = tableColumn;
            this.tableField = tableField;
            this.valueTb = value;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Reading table cell", "Please wait...", true);
        }

        private String value;

        @Override
        protected String doInBackground(Void... params) {

            try {
                value = printer.readTable(tableNumber, tableColumn, tableField);

                return null;

            } catch (Exception e) {
                log.error("Table " + tableNumber + " cell " + tableColumn + ", " + tableField + " reading failed", e);
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                valueTb.setText(value);
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void writeTableCell(View view) {

        final int tableNumber = Integer.parseInt(nbTableNumber.getText().toString());
        final int tableRow = Integer.parseInt(nbTableRow.getText().toString());
        final int tableField = Integer.parseInt(nbTableField.getText().toString());
        final String value = tbTableCellValue.getText().toString();

        new WriteTableCellTask(this, tableNumber, tableRow, tableField, value).execute();
    }

    private class WriteTableCellTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private final int tableNumber;
        private final int tableColumn;
        private final int tableField;
        private final String value;

        private ProgressDialog dialog;

        public WriteTableCellTask(AppCompatActivity parent, int tableNumber, int tableColumn, int tableField, String value) {
            this.parent = parent;
            this.tableNumber = tableNumber;
            this.tableColumn = tableColumn;
            this.tableField = tableField;
            this.value = value;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Writing table cell", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                printer.writeTable(tableNumber, tableColumn, tableField, value);

                return null;

            } catch (Exception e) {
                log.error("Table " + tableNumber + " cell " + tableColumn + ", " + tableField + " writing failed", e);
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Успех");
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void readTables(View view) {
        new ReadTablesTask(this).execute();
    }

    private class ReadTablesTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;

        private ProgressDialog dialog;

        private String text;

        private long startedAt;
        private long doneAt;

        public ReadTablesTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Reading tables", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {

                StringBuilder sb = new StringBuilder();

                for (int i = 1; i < 255; i++) {
                    ReadTableInfo command = new ReadTableInfo();
                    command.setPassword(printer.getSysPassword());
                    command.setTableNumber(i);

                    try {
                        printer.executeCommand(command);

                        sb.append("" + i + " " + command.getTable().getName() + "\n"); //+ " " + command.getTable().getRowCount() + " " + command.getTable().getFieldCount() + "\n");

                    } catch (JposException e) {
                        Throwable cause = e.getCause();
                        if (cause instanceof SmFiscalPrinterException) {
                            if (((SmFiscalPrinterException) cause).getCode() == SMFP_EFPTR_INVALID_TABLE) {
                                break;
                            } else {
                                log.error("Table " + i + " info reading failed", e);
                                return e.getMessage();
                            }
                        } else {
                            log.error("Table " + i + " info reading failed", e);
                            return e.getMessage();
                        }
                    }
                }

                text = sb.toString();

                return null;

            } catch (Exception e) {
                log.error("Tables list reading failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage(text + "\nSuccess " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }


    public void readTable(View view) {
        final int tableNumber = Integer.parseInt(nbTableNumber.getText().toString());
        new ReadTableTask(this, tableNumber).execute();
    }

    private class ReadTableTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;
        private final int tableNumber;

        private ProgressDialog dialog;

        private String text;

        private long startedAt;
        private long doneAt;

        public ReadTableTask(AppCompatActivity parent, int tableNumber) {
            this.parent = parent;
            this.tableNumber = tableNumber;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Reading table " + tableNumber, "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {

                ReadTableInfo command = new ReadTableInfo();
                command.setPassword(printer.getSysPassword());
                command.setTableNumber(tableNumber);

                printer.executeCommand(command);

                StringBuilder sb = new StringBuilder();

                sb.append("" + tableNumber + ". " + command.getTable().getName() + "\n");

                for (int i = 0; i < command.getTable().getFieldCount(); i++) {

                    ReadFieldInfo fieldInfo = new ReadFieldInfo();
                    fieldInfo.setPassword(printer.getSysPassword());
                    fieldInfo.setTable(tableNumber);
                    fieldInfo.setField(i + 1);

                    printer.executeCommand(fieldInfo);

                    sb.append("  " + i + ". " + fieldInfo.getFieldInfo().getName() + ": ");

                    for (int j = 0; j < command.getTable().getRowCount(); j++) {

                        ReadTable readCell = new ReadTable(printer.getSysPassword(), tableNumber, j + 1, i + 1);
                        printer.executeCommand(readCell);


                        String cell = readCell.fieldValue.toString();

                        sb.append(cell + "; ");
                    }

                    sb.append("\n");
                }

                text = sb.toString();

                return null;

            } catch (Exception e) {
                log.error("Table " + tableNumber + " reading failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage(text + "\nSuccess " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void syncDateTime(View view) {
        new SyncDateTimeTask(this).execute();
    }

    private class SyncDateTimeTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;

        private ProgressDialog dialog;

        private long startedAt;
        private long doneAt;

        public SyncDateTimeTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Setting current date and time", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {

                Calendar c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH) + 1;
                int year = c.get(Calendar.YEAR) - 2000;

                PrinterDate date = new PrinterDate(day, month, year);

                printer.writeDate(date);
                printer.confirmDate(date);

                int seconds = c.get(Calendar.SECOND);
                int minutes = c.get(Calendar.MINUTE);
                int hour = c.get(Calendar.HOUR_OF_DAY);

                PrinterTime time = new PrinterTime(hour, minutes, seconds);

                printer.writeTime(time);

                return null;

            } catch (Exception e) {
                log.error("Date/time sync failed failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("\nSuccess " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void printImage(View view) {
        new PrintImageTask(this).execute();
    }

    private class PrintImageTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;

        private ProgressDialog dialog;

        private long startedAt;
        private long doneAt;

        public PrintImageTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Printing image", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                String path = SysUtils.getFilesPath() + "/printer.bmp";
                JposConfig.copyAsset("printer.bmp", path, getApplicationContext());
                startedAt = System.currentTimeMillis();

                int imageIndex = printer.loadImage(path);
                printer.printImage(imageIndex);
                /*
                ImageRender render = new ImageRender();
                render.render(path);
                byte[][] data = render.getData();
                printer.printRawGraphics(data);
                */

                return null;

            } catch (Exception e) {
                log.error("Image printing failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                showMessage("Success " + (doneAt - startedAt) + " ms");
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void generateMonoToken(View view) {
        new GenerateMonoTokenTask(this).execute();
    }

    private class GenerateMonoTokenTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;

        private ProgressDialog dialog;

        private long startedAt;
        private long doneAt;
        private String token;

        public GenerateMonoTokenTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Generating mono token", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {

                GenerateMonoTokenCommand cmd = new GenerateMonoTokenCommand();

                printer.executeCommand(cmd);

                token = cmd.getToken();

                return null;

            } catch (Exception e) {
                log.error("Mono token generation failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                tbMonoToken.setText(token);
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void readFFDVersion(View view) {
        new ReadFFDVersionTask(this).execute();
    }

    private class ReadFFDVersionTask extends AsyncTask<Void, Void, String> {

        private final AppCompatActivity parent;

        private ProgressDialog dialog;

        private long startedAt;
        private long doneAt;
        private String token;

        public ReadFFDVersionTask(AppCompatActivity parent) {
            this.parent = parent;
        }

        private int oldOrientation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            oldOrientation = getRequestedOrientation();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            dialog = ProgressDialog.show(parent, "Reading FFD version", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                int ffdVersion = printer.readFFDVersion();

                if (ffdVersion == 0)
                    token = "1.0";
                else if (ffdVersion == 1)
                    token = "1.0 NEW";
                else if (ffdVersion == 2)
                    token = "1.05";
                else if (ffdVersion == 3)
                    token = "1.1";
                else
                    token = String.valueOf(ffdVersion);

                return null;

            } catch (Exception e) {
                log.error("Mono token generation failed", e);
                return e.getMessage();
            } finally {
                doneAt = System.currentTimeMillis();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dialog.dismiss();

            if (result == null)
                tbFFDVersion.setText(token);
            else
                showMessage(result);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }
}