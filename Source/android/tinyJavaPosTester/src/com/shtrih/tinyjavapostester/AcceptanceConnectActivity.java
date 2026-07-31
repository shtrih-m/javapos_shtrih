package com.shtrih.tinyjavapostester;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.util.AndroidCompat;
import com.shtrih.util.StaticContext;
import com.shtrih.util.Time;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import jpos.FiscalPrinter;
import jpos.JposConst;

/**
 * Headless acceptance test: open/claim/enable fiscal printer over BLE+PPP
 * using assets/jpos.xml (SuperTroyka params + fixed MAC).
 *
 * Result is written to app files and logcat tag {@link #TAG}:
 * {@code ACCEPTANCE_TEST result=PASS|FAIL ...}
 */
public class AcceptanceConnectActivity extends Activity {

    public static final String TAG = "AcceptanceConnect";
    public static final String RESULT_MARKER = "ACCEPTANCE_TEST";
    public static final String PRINTER_MAC = "30:AE:A4:95:F3:7E";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Headless: no UI. Result goes to logcat + acceptance_result.txt
        StaticContext.setContext(getApplicationContext());
        // Disable file/SLF4J logging — assets/logback.xml otherwise hits EPERM on emulator storage.
        try {
            LogbackConfig.disable();
        } catch (Exception e) {
            Log.w(TAG, "LogbackConfig.disable failed", e);
        }
        try {
            com.shtrih.util.CompositeLogger.getLogger(AcceptanceConnectActivity.class).setEnabled(false);
        } catch (Exception e) {
            Log.w(TAG, "CompositeLogger.setEnabled(false) failed", e);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                runConnectTest();
            }
        }, "acceptance-connect").start();
    }

    private void runConnectTest() {
        ShtrihFiscalPrinter printer = new ShtrihFiscalPrinter(new FiscalPrinter());
        String result = "FAIL";
        String detail = "";
        long startedAt = System.currentTimeMillis();
        try {
            waitBluetoothEnabled(60000);
            AndroidCompat.ensureBluetoothConnectAndScan(getApplicationContext());
            ensureLocationForBleScan(getApplicationContext());
            // Soft LE probe only: driver connects by MAC via getRemoteDevice+GATT.
            // On API<=30 empty scan results are common right after BT enable / without
            // a stable location provider; do not fail the acceptance test here.
            try {
                scanForPrinter(PRINTER_MAC, 20000);
            } catch (Exception scanError) {
                Log.w(TAG, "Pre-connect LE scan miss (continuing to GATT connect): "
                        + scanError.getMessage());
            }

            Map<String, String> props = new HashMap<String, String>();
            props.put("portName", PRINTER_MAC);
            JposConfig.configure("ShtrihFptr", getApplicationContext(), props);

            if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                printer.close();
            }
            printer.open("ShtrihFptr");
            printer.setLibraryContext(getApplicationContext());
            printer.claim(3000);
            printer.setDeviceEnabled(true);

            String deviceName = printer.getPhysicalDeviceName();
            int serviceVersion = printer.getDeviceServiceVersion();
            detail = "device='" + deviceName + "' serviceVersion=" + serviceVersion;

            printer.setDeviceEnabled(false);
            printer.release();
            printer.close();
            result = "PASS";
        } catch (Exception e) {
            detail = e.getClass().getSimpleName() + ": " + e.getMessage();
            Log.e(TAG, "connect failed", e);
            try {
                if (printer.getState() != JposConst.JPOS_S_CLOSED) {
                    try {
                        printer.setDeviceEnabled(false);
                    } catch (Exception ignored) {
                    }
                    try {
                        printer.release();
                    } catch (Exception ignored) {
                    }
                    printer.close();
                }
            } catch (Exception closeError) {
                Log.e(TAG, "cleanup failed", closeError);
            }
        }

        long elapsedMs = System.currentTimeMillis() - startedAt;
        final String line = RESULT_MARKER
                + " result=" + result
                + " timeMs=" + elapsedMs
                + " detail=" + detail;
        Log.i(TAG, line);
        writeResultFile(line);
        // Give logcat / adb a moment to observe the marker, then exit.
        try {
            Time.delay(2000);
        } catch (Exception ignored) {
        }
        finish();
    }

    private void waitBluetoothEnabled(int timeoutMs) throws Exception {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            throw new Exception("BluetoothAdapter is null");
        }
        if (!adapter.isEnabled()) {
            Log.i(TAG, "Bluetooth off, requesting enable... state=" + adapter.getState());
            try {
                adapter.enable();
            } catch (SecurityException e) {
                throw new Exception("Cannot enable Bluetooth (missing BLUETOOTH_CONNECT?): "
                        + e.getMessage(), e);
            }
        }
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (!adapter.isEnabled()) {
            if (System.currentTimeMillis() > deadline) {
                throw new Exception("Bluetooth is not enabled (adapter state="
                        + adapter.getState() + ")");
            }
            Time.delay(500);
        }
        Log.i(TAG, "Bluetooth enabled");
    }

    /**
     * On API 23..30 BLE scan results require runtime location permission and
     * location services enabled. API 31+ uses BLUETOOTH_SCAN instead.
     */
    private void ensureLocationForBleScan(Context context) throws Exception {
        if (Build.VERSION.SDK_INT < 23 || Build.VERSION.SDK_INT >= 31) {
            return;
        }
        if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            throw new Exception("Missing runtime location permission required for BLE scan on API "
                    + Build.VERSION.SDK_INT);
        }
        int mode = Settings.Secure.getInt(context.getContentResolver(), "location_mode", 0);
        Log.i(TAG, "location_mode=" + mode);
        if (mode == 0) {
            throw new Exception("Location services are off (required for BLE scan on API "
                    + Build.VERSION.SDK_INT + ")");
        }
    }

    private void scanForPrinter(final String mac, int timeoutMs) throws Exception {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if (scanner == null) {
            throw new Exception("BluetoothLeScanner is null");
        }
        final String want = mac.toUpperCase(Locale.US);
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean found = new AtomicBoolean(false);
        final AtomicInteger callbacks = new AtomicInteger(0);
        ScanCallback callback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                callbacks.incrementAndGet();
                BluetoothDevice device = result.getDevice();
                if (device == null || device.getAddress() == null) {
                    return;
                }
                String addr = device.getAddress().toUpperCase(Locale.US);
                if (want.equals(addr)) {
                    Log.i(TAG, "scan found printer rssi=" + result.getRssi());
                    found.set(true);
                    latch.countDown();
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.e(TAG, "scan failed errorCode=" + errorCode);
                latch.countDown();
            }
        };
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        Log.i(TAG, "Starting LE scan for " + mac);
        scanner.startScan(null, settings, callback);
        try {
            latch.await(timeoutMs, TimeUnit.MILLISECONDS);
        } finally {
            try {
                scanner.stopScan(callback);
            } catch (Exception e) {
                Log.e(TAG, "stopScan failed", e);
            }
        }
        Log.i(TAG, "LE scan done found=" + found.get() + " callbacks=" + callbacks.get());
        if (!found.get()) {
            throw new Exception("Printer not found in BLE scan: " + mac
                    + " (callbacks=" + callbacks.get() + ")");
        }
    }

    private void writeResultFile(String line) {
        try {
            FileOutputStream fos = openFileOutput("acceptance_result.txt", MODE_PRIVATE);
            try {
                fos.write(line.getBytes(Charset.forName("UTF-8")));
                fos.write('\n');
            } finally {
                fos.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "writeResultFile private failed", e);
        }
        try {
            File dir = getExternalFilesDir(null);
            if (dir != null) {
                File out = new File(dir, "acceptance_result.txt");
                FileOutputStream fos = new FileOutputStream(out);
                try {
                    fos.write(line.getBytes(Charset.forName("UTF-8")));
                    fos.write('\n');
                } finally {
                    fos.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "writeResultFile external failed", e);
        }
    }
}
