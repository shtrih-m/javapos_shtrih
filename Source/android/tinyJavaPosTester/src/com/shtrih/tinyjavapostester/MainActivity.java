package com.shtrih.tinyjavapostester;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.util.Log;


import com.google.zxing.EncodeHintType;
import com.google.zxing.pdf417.encoder.Compaction;
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.PrinterProtocol;
import com.shtrih.fiscalprinter.PrinterProtocol_1;
import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.fiscalprinter.command.DeviceMetrics;
import com.shtrih.fiscalprinter.command.FSCommunicationStatus;
import com.shtrih.fiscalprinter.command.FSDocumentInfo;
import com.shtrih.fiscalprinter.command.FSStatusInfo;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.fiscalprinter.command.ReadDeviceMetrics;
import com.shtrih.fiscalprinter.command.ReadShortStatus;
import com.shtrih.fiscalprinter.command.ShortPrinterStatus;
import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.fiscalprinter.port.UsbPrinterPort;
import com.shtrih.hoho.android.usbserial.driver.UsbSerialDriver;
import com.shtrih.hoho.android.usbserial.driver.UsbSerialProber;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.util.StaticContext;
import com.shtrih.util.SysUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import jpos.FiscalPrinter;
import jpos.JposConst;
import jpos.JposException;

public class MainActivity extends AppCompatActivity {

    private ShtrihFiscalPrinter printer = null;
    private final Random rand = new Random();
    private final String[] items = {"Кружка", "Ложка", "Миска", "Нож"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFilter();
        findSerialPortDevice();

        StaticContext.setContext(getApplicationContext());
        printer = new ShtrihFiscalPrinter(new FiscalPrinter());

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
                    long startedAt = System.currentTimeMillis();
                    try {
                        connectToDevice(address);
                        long doneAt = System.currentTimeMillis();
                        String message = "Blutooth connected in " + (doneAt - startedAt) + " ms";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        Log.d(TAG, message);
                    } catch (Exception e) {
                        long doneAt = System.currentTimeMillis();
                        e.printStackTrace();
                        String message = e.getMessage() + ". In " + (doneAt - startedAt) + " ms";
                        Log.d(TAG, message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
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

        JposConfig.configure("ShtrihFptr", address, getApplicationContext());
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
        try {
            PrinterBarcode barcode = new PrinterBarcode();
            barcode.setText("460704243915");
            barcode.setLabel("EAN13 test");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_EAN13);
            barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
            barcode.setHeight(100);
            barcode.setBarWidth(2);
            printer.printBarcode(barcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printPDF417Barcode(View v) {
        try {
            PrinterBarcode barcode = new PrinterBarcode();
            barcode.setText("SHTRIH-M, Moscow, 2015");
            barcode.setLabel("PDF417 test");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_PDF417);
            barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
            barcode.setHeight(100);
            barcode.setBarWidth(2);
            printer.printBarcode(barcode);
        } catch (Exception e) {
            e.printStackTrace();
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
        try {
            long startedAt = System.currentTimeMillis();

            printer.resetPrinter();

            String text = "«Мой дядя самых честных правил";
            int lines = 100;

            for (int i = 0; i < lines; i++) {
                printer.printText(text);
            }

            long doneAt = System.currentTimeMillis();

            String message = "Printed in " + (doneAt - startedAt) + " ms";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Log.d(TAG, message);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void printReceipt(View v) {
        try {
            long startedAt = System.currentTimeMillis();

            printSalesReceipt();
            printRefundReceipt();

            long doneAt = System.currentTimeMillis();

            String message = "Printed in " + (doneAt - startedAt) + " ms";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Log.d(TAG, message);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void readFSCommStatus(View v) {
        try {
            FSCommunicationStatus status = printer.fsReadCommStatus();

            String message = "Unsent documents " + status.getUnsentDocumentsCount();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Log.d(TAG, message);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void printSalesReceipt() throws Exception {
        int howMuch = rand.nextInt(10);
        howMuch += 1; // guarantee

        long payment = 0;
        printer.resetPrinter();
        printer.setFiscalReceiptType(jpos.FiscalPrinterConst.FPTR_RT_SALES);
        printer.beginFiscalReceipt(false);
        for (int i = 0; i < howMuch; i++) {
            long price = Math.abs(rand.nextLong() % 1000);
            payment += price;

            String itemName = items[rand.nextInt(items.length)];
            printer.printRecItem(itemName, price, 0, 0, 0, "");
        }

        printer.printRecTotal(payment, payment, "1");

        printer.directIO(0x39, null, "foo@example.com");

        printer.endFiscalReceipt(false);

        FSStatusInfo status = printer.fsReadStatus();
        FSDocumentInfo document = printer.fsFindDocument(status.getDocNumber());
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
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    printer.printZReport();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void openFiscalDay(View v) {
        try {
            printer.openFiscalDay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printJournalCurrentDay(View v) {
        try {
            printer.printJournalCurrentDay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printDuplicateReceipt(View v) {
        try {
            printer.printDuplicateReceipt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectToDeviceDirect(View view) {
        try {
            SysUtils.setFilesPath(this.getFilesDir().getAbsolutePath());
            JposConfig.configure("ShtrihFptr", "192.168.42.150:7778", getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Thread thrd = new Thread() {
            @Override
            public void run() {
                try {
                    if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                        printer.close();
                    }
                    printer.open("ShtrihFptr");
                    printer.claim(3000);
                    printer.setDeviceEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
//
//        try {
        thrd.start();
        try {
            thrd.join();
            Toast.makeText(this, "Успех", Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
        }
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//
//        }
    }

    public void connectToUSBDevice(View view) {

//        UsbPrinterPort.Context = getApplicationContext();
//
//        try {
//            PrinterPort port = new UsbPrinterPort();
//            PrinterProtocol protocol = new PrinterProtocol_1(port);
//
//            port.open(3000);
//            protocol.connect();
//
//            ReadDeviceMetrics cmd = new ReadDeviceMetrics();
//
//            protocol.send(cmd);
//            DeviceMetrics metrics = cmd.getDeviceMetrics();
//            Log.d(TAG, metrics.getDeviceName());
//        } catch (Exception e) {
//            Log.e(TAG, "failed", e);
//            return;
//        } finally {
//            UsbPrinterPort.Context = null;
//        }
//
        try {
            UsbManager usbManager = (UsbManager) getApplicationContext().getSystemService(Context.USB_SERVICE);
            List<UsbSerialDriver> usbs = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);

            if (usbs.size() == 0) {
                Log.e(TAG, "Не найдено ни одного устройства");
                return;
            }

            int deviceId = usbs.get(0).getDevice().getDeviceId();
            SysUtils.setFilesPath(this.getFilesDir().getAbsolutePath());
            JposConfig.configure("ShtrihFptr", String.format(Locale.ENGLISH, "%d", deviceId), getApplicationContext());
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


}

