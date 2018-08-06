package com.shtrih.tinyjavapostester.search.tcp;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;

public class TcpDeviceSearchViewModel extends AndroidViewModel implements Runnable {
    
    public final ObservableArrayList<TcpDevice> devices = new ObservableArrayList<>();

    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TcpDeviceSearchViewModel.class);

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

                    if (alreadySeen.contains(message))
                        continue;

                    log.debug(message);

                    alreadySeen.add(message);

                    onDeviceFround(message);

                } catch (IOException e) {
                    log.error("Packet receive failed", e);
                }
            }
            socket.close();
        } catch (Exception exc) {
            log.error("UDP listener failed", exc);
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
            log.error("Failed to parse " + msg);
        }
    }

    public void onItemSelected(Activity activity, TcpDevice device) {

        Intent intent = new Intent();
        intent.putExtra("Address", device.address);

        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
}


