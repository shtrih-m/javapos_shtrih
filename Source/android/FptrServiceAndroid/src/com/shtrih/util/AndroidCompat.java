package com.shtrih.util;

import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Compatibility helpers for Android 12+ Bluetooth permissions,
 * Android 13+ receiver registration flags, and USB permission dialogs.
 */
public final class AndroidCompat {

    public static final String PERMISSION_BLUETOOTH_CONNECT = "android.permission.BLUETOOTH_CONNECT";
    public static final String PERMISSION_BLUETOOTH_SCAN = "android.permission.BLUETOOTH_SCAN";
    public static final String ACTION_USB_PERMISSION = "com.shtrih.fiscalprinter.USB_PERMISSION";

    /** Context.RECEIVER_NOT_EXPORTED (API 33). */
    private static final int RECEIVER_NOT_EXPORTED = 0x4;
    /** PendingIntent.FLAG_IMMUTABLE (API 23). */
    private static final int FLAG_IMMUTABLE = 0x04000000;
    /** PendingIntent.FLAG_MUTABLE (API 31). */
    private static final int FLAG_MUTABLE = 0x02000000;
    /** BluetoothDevice.TRANSPORT_LE (API 23). */
    private static final int TRANSPORT_LE = 2;

    private static final CompositeLogger logger = CompositeLogger.getLogger(AndroidCompat.class);

    private AndroidCompat() {
    }

    public static boolean hasPermission(Context context, String permission) {
        if (context == null || permission == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void ensureBluetoothConnectPermission(Context context) throws Exception {
        if (Build.VERSION.SDK_INT < 31) {
            return;
        }
        if (context == null) {
            throw new Exception("Context is not set");
        }
        if (!hasPermission(context, PERMISSION_BLUETOOTH_CONNECT)) {
            throw new Exception("Missing runtime permission BLUETOOTH_CONNECT");
        }
    }

    public static void ensureBluetoothScanPermission(Context context) throws Exception {
        if (Build.VERSION.SDK_INT < 31) {
            return;
        }
        if (context == null) {
            throw new Exception("Context is not set");
        }
        if (!hasPermission(context, PERMISSION_BLUETOOTH_SCAN)) {
            throw new Exception("Missing runtime permission BLUETOOTH_SCAN");
        }
    }

    public static void ensureBluetoothConnectAndScan(Context context) throws Exception {
        ensureBluetoothConnectPermission(context);
        ensureBluetoothScanPermission(context);
    }

    public static Intent registerReceiver(Context context, BroadcastReceiver receiver, IntentFilter filter) {
        if (context == null || receiver == null || filter == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 33) {
            try {
                Method method = Context.class.getMethod(
                        "registerReceiver", BroadcastReceiver.class, IntentFilter.class, int.class);
                return (Intent) method.invoke(context, receiver, filter, RECEIVER_NOT_EXPORTED);
            } catch (Exception e) {
                logger.error("registerReceiver with flags failed, falling back", e);
            }
        }
        return context.registerReceiver(receiver, filter);
    }

    public static int pendingIntentMutableFlag() {
        if (Build.VERSION.SDK_INT >= 31) {
            return FLAG_MUTABLE;
        }
        return 0;
    }

    public static int pendingIntentImmutableFlag() {
        if (Build.VERSION.SDK_INT >= 23) {
            return FLAG_IMMUTABLE;
        }
        return 0;
    }

    public static BluetoothGatt connectGatt(BluetoothDevice device, Context context,
                                            boolean autoConnect, BluetoothGattCallback callback) {
        if (device == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Method method = BluetoothDevice.class.getMethod(
                        "connectGatt", Context.class, boolean.class, BluetoothGattCallback.class, int.class);
                return (BluetoothGatt) method.invoke(device, context, autoConnect, callback, TRANSPORT_LE);
            } catch (Exception e) {
                logger.error("connectGatt(TRANSPORT_LE) failed, falling back", e);
            }
        }
        return device.connectGatt(context, autoConnect, callback);
    }

    /**
     * Ensures USB device permission is granted. Requests it and waits if needed.
     * Must not be called on the main thread when a permission dialog is required.
     */
    public static void ensureUsbPermission(final Context context, final UsbManager usbManager, final UsbDevice device)
            throws Exception {
        if (context == null) {
            throw new Exception("Context is not set");
        }
        if (usbManager == null || device == null) {
            throw new Exception("USB device is not available");
        }
        if (usbManager.hasPermission(device)) {
            return;
        }

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean granted = new AtomicBoolean(false);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                String action = intent.getAction();
                if (!ACTION_USB_PERMISSION.equals(action)) {
                    return;
                }
                synchronized (this) {
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (usbDevice != null && usbDevice.equals(device)) {
                        granted.set(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false));
                        latch.countDown();
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(context, receiver, filter);

        try {
            Intent permissionIntent = new Intent(ACTION_USB_PERMISSION);
            permissionIntent.setPackage(context.getPackageName());
            final PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, 0, permissionIntent, pendingIntentMutableFlag());

            if (Looper.myLooper() == Looper.getMainLooper()) {
                usbManager.requestPermission(device, pendingIntent);
            } else {
                final CountDownLatch requestLatch = new CountDownLatch(1);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            usbManager.requestPermission(device, pendingIntent);
                        } finally {
                            requestLatch.countDown();
                        }
                    }
                });
                if (!requestLatch.await(5, TimeUnit.SECONDS)) {
                    throw new Exception("Timed out requesting USB permission");
                }
            }

            if (!latch.await(60, TimeUnit.SECONDS)) {
                throw new Exception("Timed out waiting for USB permission");
            }
            if (!granted.get()) {
                throw new Exception("USB permission denied for device " + device.getDeviceName());
            }
        } finally {
            try {
                context.unregisterReceiver(receiver);
            } catch (Exception e) {
                logger.error("Failed to unregister USB permission receiver", e);
            }
        }
    }
}
