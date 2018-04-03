package com.shtrih.tinyjavapostester;

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
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.EncodeHintType;
import com.google.zxing.pdf417.encoder.Compaction;
import com.google.zxing.pdf417.encoder.Dimensions;
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.fiscalprinter.SmFiscalPrinterException;
import com.shtrih.fiscalprinter.TLVItem;
import com.shtrih.fiscalprinter.TLVItems;
import com.shtrih.fiscalprinter.TLVParser;
import com.shtrih.fiscalprinter.TLVTag;
import com.shtrih.fiscalprinter.command.DeviceMetrics;
import com.shtrih.fiscalprinter.command.FSCommunicationStatus;
import com.shtrih.fiscalprinter.command.FSDocumentInfo;
import com.shtrih.fiscalprinter.command.FSStatusInfo;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.fiscalprinter.command.ReadTableInfo;
import com.shtrih.fiscalprinter.port.UsbPrinterPort;
import com.shtrih.hoho.android.usbserial.driver.UsbSerialDriver;
import com.shtrih.hoho.android.usbserial.driver.UsbSerialProber;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.util.Hex;
import com.shtrih.util.SysUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import jpos.FiscalPrinterConst;
import jpos.JposConst;
import jpos.JposException;

import static com.shtrih.fiscalprinter.command.PrinterConst.SMFP_EFPTR_INVALID_TABLE;

public class MainActivity extends AppCompatActivity {

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

    private ShtrihFiscalPrinter printer = null;
    private final Random rand = new Random();
    private final String[] items = {"Кружка", "Ложка", "Миска", "Нож"};

    private EditText tbNetworkAddress;
    private EditText nbTextStringCount;
    private EditText nbPositionsCount;
    private EditText nbFiscalizationNumber;
    private EditText nbDocumentNumber;
    private EditText nbTagNumber;
    private EditText nbTextLinesCount;
    private EditText nbTableNumber;
    private EditText nbTableField;
    private EditText nbTableRow;
    private EditText tbTableCellValue;

    private String selectedProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFilter();
        findSerialPortDevice();

        final SharedPreferences pref = this.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);

        tbNetworkAddress = findViewById(R.id.tbNetworkAddress);
        restoreAndSaveChangesTo(tbNetworkAddress, pref, "NetworkAddress", "127.0.0.1:12345");

        nbPositionsCount = findViewById(R.id.nbPositionsCount);
        restoreAndSaveChangesTo(nbPositionsCount, pref, "CheckPositionsCount", "5");

        nbTextStringCount = findViewById(R.id.nbTextStringsCount);
        restoreAndSaveChangesTo(nbTextStringCount, pref, "CheckStringsCount", "5");

        nbFiscalizationNumber = findViewById(R.id.nbFiscalizationNumber);
        restoreAndSaveChangesTo(nbFiscalizationNumber, pref, "FiscalizationNumber", "1");

        nbDocumentNumber = findViewById(R.id.nbDocumentNumber);
        restoreAndSaveChangesTo(nbDocumentNumber, pref, "DocumentNumber", "1");

        nbTagNumber = findViewById(R.id.nbTagNumber);
        restoreAndSaveChangesTo(nbTagNumber, pref, "TagNumber", "1041");

        nbTextLinesCount = findViewById(R.id.nbTextLinesCount);
        restoreAndSaveChangesTo(nbTextLinesCount, pref, "TextLinesCount", "100");

        nbTableNumber = findViewById(R.id.nbTableNumber);
        restoreAndSaveChangesTo(nbTableNumber, pref, "TableNumber", "1");

        nbTableField = findViewById(R.id.nbTableField);
        restoreAndSaveChangesTo(nbTableField, pref, "TableField", "1");

        nbTableRow = findViewById(R.id.nbTableRow);
        restoreAndSaveChangesTo(nbTableRow, pref, "TableRow", "1");

        tbTableCellValue = findViewById(R.id.tbTableCellValue);
        restoreAndSaveChangesTo(tbTableCellValue, pref, "TableCellValue", "");

        Spinner cbProtocol = findViewById(R.id.cbProtocol);

        ArrayList<EnumViewModel> protocols = new ArrayList<>();
        protocols.add(new EnumViewModel("0", "1.0"));
        protocols.add(new EnumViewModel("1", "2.0"));

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

        MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);

        printer = model.getPrinter();
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

    public static final String ACTION_USB_DISCONNECTED = "com.felhr.usbservice.USB_DISCONNECTED";
    public static final String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";
    public static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    public static final String ACTION_USB_PERMISSION_GRANTED = "com.felhr.usbservice.USB_PERMISSION_GRANTED";
    public static final String ACTION_USB_PERMISSION_NOT_GRANTED = "com.felhr.usbservice.USB_PERMISSION_NOT_GRANTED";
    private static final String TAG = "USBList";

    private void findSerialPortDevice() {
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        // This snippet will try to open the first encountered usb device connected, excluding usb root hubs
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                UsbDevice device = entry.getValue();
                int deviceVID = device.getVendorId();
                int devicePID = device.getProductId();

                Log.d(TAG, "opening device VID: " + deviceVID + ", PID " + devicePID);
                PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                usbManager.requestPermission(device, mPendingIntent);
            }
        } else {
            Log.d(TAG, "no usb devices");
        }
    }

    private void setFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DETACHED);
        filter.addAction(ACTION_USB_ATTACHED);
        filter.addAction(ACTION_USB_STATE);
        registerReceiver(usbReceiver, filter);
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            Log.d("onReceive", arg1.getAction());
            if (arg1.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted = arg1.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) // User accepted our USB connection. Try to open the device as a serial port
                {
                    Intent intent = new Intent(ACTION_USB_PERMISSION_GRANTED);
                    arg0.sendBroadcast(intent);
                    Log.d(TAG, "permission granted for USB ");
                } else // User not accepted our USB connection. Send an Intent to the Main Activity
                {
                    Intent intent = new Intent(ACTION_USB_PERMISSION_NOT_GRANTED);
                    arg0.sendBroadcast(intent);
                    Log.d(TAG, "permission not granted for USB");
                }
            }

            if (arg1.getAction().equals(ACTION_USB_STATE)) {
                findSerialPortDevice(); // A USB device has been attached. Try to open it as a Serial port
            }

            if (arg1.getAction().equals(ACTION_USB_ATTACHED)) {
                findSerialPortDevice(); // A USB device has been attached. Try to open it as a Serial port
            }
            if (arg1.getAction().equals(ACTION_USB_DETACHED)) {
                // Usb device was disconnected. send an intent to the Main Activity
                Intent intent = new Intent(ACTION_USB_DISCONNECTED);
                arg0.sendBroadcast(intent);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case DeviceListActivity.REQUEST_CONNECT_BT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {

                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                    long startedAt = System.currentTimeMillis();
//                    try {
//                        connectToDevice(address);
//                        long doneAt = System.currentTimeMillis();
//                        String message = "Blutooth connected in " + (doneAt - startedAt) + " ms";
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                        Log.d(TAG, message);
//                    } catch (Exception e) {
//                        long doneAt = System.currentTimeMillis();
//                        e.printStackTrace();
//                        String message = e.getMessage() + ". In " + (doneAt - startedAt) + " ms";
//                        Log.d(TAG, message);
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                    }

                    new ConnectToBluetoothDeviceTask(this, address).execute();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private class ConnectToBluetoothDeviceTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;
        private final String address;

        private long startedAt;
        private long doneAt;

        private ProgressDialog dialog;

        public ConnectToBluetoothDeviceTask(Activity parent, String address) {
            this.parent = parent;

            this.address = address;
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
                HashMap<String, String> props = new HashMap<>();
                props.put("portName", address);
                props.put("portType", "3");
                props.put("protocolType", "1");
                props.put("portClass", "com.shtrih.fiscalprinter.port.BluetoothPort");

                JposConfig.configure("ShtrihFptr", getApplicationContext(), props);
                if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                    printer.close();
                }
                printer.open("ShtrihFptr");
                printer.claim(3000);
                printer.setDeviceEnabled(true);

                return null;

            } catch (Exception e) {
                e.printStackTrace();
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

    public void connectToDevice(final String address) throws Exception {
        Log.d("", "connectToDevice");
        /*
        UsbManager usbManager = (UsbManager) getApplicationContext().getSystemService(Context.USB_SERVICE);
        for (final UsbDevice usbDevice : usbManager.getDeviceList().values()) {
            Log.d("usbDevice", "vendorId: " + usbDevice.getVendorId());
            Log.d("usbDevice", "productId: " + usbDevice.getProductId());
        }
        */

        HashMap<String, String> props = new HashMap<>();
        props.put("portName", address);
        props.put("portType", "3");
        props.put("protocolType", "1");
        props.put("portClass", "com.shtrih.fiscalprinter.port.BluetoothPort");

        JposConfig.configure("ShtrihFptr", getApplicationContext(), props);
        if (printer.getState() != JposConst.JPOS_S_CLOSED) {
            printer.close();
        }
        printer.open("ShtrihFptr");
        printer.claim(3000);
        printer.setDeviceEnabled(true);

        LongPrinterStatus status = printer.readLongPrinterStatus();
        Log.d(TAG, "" + status.getFiscalID());
        Log.d(TAG, status.getFiscalIDText());
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
        return super.onOptionsItemSelected(item);
    }

    public void printEAN13Barcode(View v) {
        new PrintEAN13BarcodeTask(this).execute();
    }

    private class PrintEAN13BarcodeTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintEAN13BarcodeTask(Activity parent) {
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
                e.printStackTrace();
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

        private final Activity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintPDF417BarcodeTask(Activity parent) {
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
                e.printStackTrace();
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

        private final Activity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintQRBarcodeTask(Activity parent) {
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
                e.printStackTrace();
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

    private void printImage() throws JposException, InterruptedException {
        printer.resetPrinter();
        File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File picsDir = new File(dcimDir, "Camera");
        picsDir.mkdirs(); // make if not exist
        // File newFile = new File(picsDir, "PDF417.bmp");
        File newFile = new File(picsDir, "printer.bmp");
        int imageIndex = printer.loadImage(newFile.getAbsolutePath());
        printer.printImage(imageIndex);
    }

    public void printText(View v) {

        final int lines = Integer.parseInt(nbTextLinesCount.getText().toString());

        new PrintTextTask(this, lines).execute();
    }

    private class PrintTextTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;
        private final int lines;

        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintTextTask(Activity parent, int lines) {
            this.parent = parent;
            this.lines = lines;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Printing receipt", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                printer.resetPrinter();

                String text = "«Мой дядя самых честных правил";

                for (int i = 0; i < lines; i++) {
                    printer.printText(text);
                }

                return null;

            } catch (Exception e) {
                e.printStackTrace();
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

    public void disconnect(View v) {
        new DisconnectTask(this).execute();
    }

    private class DisconnectTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;

        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public DisconnectTask(Activity parent) {
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
                if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                    printer.close();
                }

                return null;

            } catch (Exception e) {
                e.printStackTrace();
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

        new PrintReceiptTask(this, positions, strings).execute();
    }

    private class PrintReceiptTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;
        private final int positions;
        private final int strings;

        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintReceiptTask(Activity parent, int positions, int strings) {
            this.parent = parent;
            this.positions = positions;
            this.strings = strings;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Printing receipt", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            startedAt = System.currentTimeMillis();

            try {
                printSalesReceipt(positions, strings);

                return null;

            } catch (Exception e) {
                e.printStackTrace();
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
        Log.d(TAG, message);
    }

    public void readFSCommStatus(View v) {
        new ReadFSCommStatusTask(this).execute();
    }

    private class ReadFSCommStatusTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;

        private int documentsCount;

        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public ReadFSCommStatusTask(Activity parent) {
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
                e.printStackTrace();
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

    private void printSalesReceipt(final int positions, final int strings) throws Exception {

        long payment = 0;
        printer.resetPrinter();
        printer.setFiscalReceiptType(jpos.FiscalPrinterConst.FPTR_RT_SALES);
        printer.beginFiscalReceipt(false);
        for (int i = 0; i < positions; i++) {
            long price = Math.abs(rand.nextLong() % 1000);
            payment += price;

            String itemName = items[rand.nextInt(items.length)];
            printer.printRecItem(itemName, price, 0, 0, 0, "");

            for (int j = 0; j < strings; j++) {
                printer.printRecMessage("Продажа № " + (i + 1) + ", строка " + (j + 1));
            }
        }

        printer.printRecTotal(payment, payment, "1");

        printer.directIO(0x39, null, "foo@example.com");

        printer.fsWriteTag(1057, 4, 1);

        printer.endFiscalReceipt(false);

//        FSStatusInfo status = printer.fsReadStatus();
//        FSDocumentInfo document = printer.fsFindDocument(status.getDocNumber());
//
//        printer.printBarcode("004ZY3O.0IKN64", "", SmFptrConst.SMFPTR_BARCODE_CODE39, 100, SmFptrConst.SMFPTR_PRINTTYPE_DRIVER, 1, SmFptrConst.SMFPTR_TEXTPOS_NOTPRINTED, 1, 1);
//        printer.feedPaper(3);
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

        private final Activity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintZReportTask(Activity parent) {
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
                e.printStackTrace();
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

        private final Activity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public OpenFiscalDayTask(Activity parent) {
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
                e.printStackTrace();
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

        private final Activity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintJournalCurrentDayTask(Activity parent) {
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
                e.printStackTrace();
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

        private final Activity parent;
        private long startedAt;
        private long doneAt;
        private ProgressDialog dialog;

        public PrintDuplicateReceiptTask(Activity parent) {
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
                e.printStackTrace();
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

        new ConnectToWiFiDeviceTask(this, tbNetworkAddress.getText().toString()).execute();
    }

    private class ConnectToWiFiDeviceTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;
        private final String address;

        private long startedAt;
        private long doneAt;

        private String text;

        private ProgressDialog dialog;

        public ConnectToWiFiDeviceTask(Activity parent, String address) {
            this.parent = parent;

            this.address = address;
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
                SysUtils.setFilesPath(getApplicationContext().getFilesDir().getAbsolutePath());
                JposConfig.configure("ShtrihFptr", address, getApplicationContext(), "2", selectedProtocol);

                if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                    printer.close();
                }
                printer.open("ShtrihFptr");
                printer.claim(3000);
                printer.setDeviceEnabled(true);

                String[] lines = new String[1];
                printer.getData(FiscalPrinterConst.FPTR_GD_PRINTER_ID, null, lines);
                String serialNumber = lines[0];
                DeviceMetrics deviceMetrics = printer.readDeviceMetrics();

                text = deviceMetrics.getDeviceName() + " " + serialNumber;

                return null;

            } catch (Exception e) {
                e.printStackTrace();
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
        }
    }

    public void connectToUSBDevice(View view) {

        try {
            UsbManager usbManager = (UsbManager) getApplicationContext().getSystemService(Context.USB_SERVICE);
            List<UsbSerialDriver> usbs = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);

            if (usbs.size() == 0) {
                Log.e(TAG, "Не найдено ни одного устройства");
                return;
            }

            int deviceId = usbs.get(0).getDevice().getDeviceId();
            SysUtils.setFilesPath(this.getFilesDir().getAbsolutePath());

            HashMap<String, String> props = new HashMap<>();
            props.put("portName", String.format(Locale.ENGLISH, "%d", deviceId));
            props.put("protocolType", "0");
            props.put("portType", "3");
            props.put("portClass", "com.shtrih.fiscalprinter.port.UsbPrinterPort");

            JposConfig.configure("ShtrihFptr", getApplicationContext(), props);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {

            UsbPrinterPort.Context = getApplicationContext();

            try {
                printer.open("ShtrihFptr");
            } finally {
                UsbPrinterPort.Context = null;
            }

            printer.claim(3000);
            printer.setDeviceEnabled(true);


        } catch (Exception e) {
            Log.e(TAG, "failed", e);
        } finally {
//            try {
//                printer.close();
//            } catch (JposException e) {
//                Log.e(TAG, "failed", e);
//            }

        }
    }

    public void readFiscalizationTag(View view) {

        final int fiscalizationNumber = Integer.parseInt(nbFiscalizationNumber.getText().toString());
        final int tagNumber = Integer.parseInt(nbTagNumber.getText().toString());

        new ReadFiscalizationTagTask(this, fiscalizationNumber, tagNumber).execute();
    }

    private class ReadFiscalizationTagTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;
        private final int fiscalizationNumber;
        private final int tagNumber;

        private long startedAt;
        private long doneAt;

        private String text;

        private ProgressDialog dialog;

        public ReadFiscalizationTagTask(Activity parent, int fiscalizationNumber, int tagNumber) {
            this.parent = parent;
            this.fiscalizationNumber = fiscalizationNumber;
            this.tagNumber = tagNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                e.printStackTrace();
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
        }
    }

    public void readFiscalizationTLV(View view) {

        final int fiscalizationNumber = Integer.parseInt(nbFiscalizationNumber.getText().toString());

        new ReadFiscalizationTLVTask(this, fiscalizationNumber).execute();
    }

    private class ReadFiscalizationTLVTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;
        private final int fiscalizationNumber;

        private long startedAt;
        private long doneAt;

        private String text;

        private ProgressDialog dialog;

        public ReadFiscalizationTLVTask(Activity parent, int fiscalizationNumber) {
            this.parent = parent;
            this.fiscalizationNumber = fiscalizationNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                e.printStackTrace();
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
        }
    }

    public void readDocumentTLV(View view) {

        final int documentNumber = Integer.parseInt(nbDocumentNumber.getText().toString());

        new ReadDocumentTLVTask(this, documentNumber).execute();
    }

    private class ReadDocumentTLVTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;
        private final int fiscalizationNumber;

        private long startedAt;
        private long doneAt;

        private String text;

        private ProgressDialog dialog;

        public ReadDocumentTLVTask(Activity parent, int fiscalizationNumber) {
            this.parent = parent;
            this.fiscalizationNumber = fiscalizationNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                e.printStackTrace();
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

        private final Activity parent;
        private final int tableNumber;
        private final int tableColumn;
        private final int tableField;
        private final EditText valueTb;

        private ProgressDialog dialog;

        public ReadTableCellTask(Activity parent, int tableNumber, int tableColumn, int tableField, EditText value) {
            this.parent = parent;
            this.tableNumber = tableNumber;
            this.tableColumn = tableColumn;
            this.tableField = tableField;
            this.valueTb = value;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Reading table cell", "Please wait...", true);
        }

        private String value;

        @Override
        protected String doInBackground(Void... params) {

            try {
                value = printer.readTable(tableNumber, tableColumn, tableField);

                return null;

            } catch (Exception e) {
                e.printStackTrace();
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

        private final Activity parent;
        private final int tableNumber;
        private final int tableColumn;
        private final int tableField;
        private final String value;

        private ProgressDialog dialog;

        public WriteTableCellTask(Activity parent, int tableNumber, int tableColumn, int tableField, String value) {
            this.parent = parent;
            this.tableNumber = tableNumber;
            this.tableColumn = tableColumn;
            this.tableField = tableField;
            this.value = value;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Writing table cell", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                printer.writeTable(tableNumber, tableColumn, tableField, value);

                return null;

            } catch (Exception e) {
                e.printStackTrace();
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
        }
    }

    public void readTables(View view) {
        new ReadTablesTask(this).execute();
    }

    private class ReadTablesTask extends AsyncTask<Void, Void, String> {

        private final Activity parent;

        private ProgressDialog dialog;

        private String text;

        public ReadTablesTask(Activity parent) {
            this.parent = parent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(parent, "Writing table cell", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... params) {

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
                        if (cause != null && cause instanceof SmFiscalPrinterException) {
                            if (((SmFiscalPrinterException) cause).getCode() == SMFP_EFPTR_INVALID_TABLE) {
                                break;
                            } else {
                                e.printStackTrace();
                                return e.getMessage();
                            }
                        } else {
                            e.printStackTrace();
                            return e.getMessage();
                        }
                    }
                }

                text = sb.toString();

                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
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
        }
    }
}

