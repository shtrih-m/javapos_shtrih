package com.shtrih.tinyjavapostester.search.tcp;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;

public class TcpDeviceSearchViewModel extends AndroidViewModel implements Runnable {
    private static final String TAG = "TcpDeviceSearch";

    public final ObservableArrayList<TcpDevice> devices = new ObservableArrayList<>();

    private DatagramSocket socket;
    private Thread thread;

    public TcpDeviceSearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void onStarted() {
        devices.clear();
        thread = new Thread(this);
        thread.start();
    }

    public void onStopped() {
        if (socket != null)
            socket.close();

        if (thread != null)
            thread.interrupt();
    }

    public void run() {
        HashSet<String> alreadySeen = new HashSet<>();

        try {

            socket = new DatagramSocket(16327);

            byte[] buffer = new byte[512];

            while (!socket.isClosed()) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());

                    Log.d(TAG, message);

                    if (alreadySeen.contains(message))
                        continue;

                    alreadySeen.add(message);

                    onDeviceFround(message);

                } catch (IOException e) {
                    Log.e(TAG, "Packet receive failed", e);
                }
            }
            socket.close();
        } catch (Exception exc) {
            Log.e(TAG, "UDP listener failed", exc);
        }
    }

    private void onDeviceFround(String msg) {
        try {
            String[] parts = msg.split(";");

            if (parts.length < 3) {
                return;
            }

            String serialNumber = parts[0].substring(7).trim();
            String ip = parts[1].substring(4).trim();
            String port = parts[2].substring(6).trim();

            devices.add(new TcpDevice(serialNumber, ip + ":" + port));
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse " + msg);
        }
    }

    public void onItemSelected(Activity activity, TcpDevice device) {

        Intent intent = new Intent();
        intent.putExtra("Address", device.address);

        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
}


