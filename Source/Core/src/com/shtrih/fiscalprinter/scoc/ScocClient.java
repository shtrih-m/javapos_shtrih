package com.shtrih.fiscalprinter.scoc;

import com.shtrih.fiscalprinter.scoc.commands.DeviceFirmwareCommand;
import com.shtrih.fiscalprinter.scoc.commands.DeviceFirmwareResponse;
import com.shtrih.fiscalprinter.scoc.commands.DeviceStatusCommand;
import com.shtrih.fiscalprinter.scoc.commands.DeviceStatusResponse;
import com.shtrih.fiscalprinter.scoc.commands.ScocCommand;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Logger2;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.TimeZone;

public class ScocClient {

    private CompositeLogger logger = CompositeLogger.getLogger(ScocClient.class);

    private static final String Host = "skok.shtrih-m.ru";
    private static final int Port = 4243;
    private static final int ConnectionTimeout = 30 * 1000;
    private static final int ResponseTimeout = 300 * 1000;

    private final String serialNumber;
    private final long uin;

    public ScocClient(String serialNumber, long uin) {
        this.serialNumber = serialNumber;
        this.uin = uin;
    }

    public DeviceStatusResponse sendStatus(long firmwareVersion) throws Exception {
        Socket socket = new Socket();
        try {
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(ConnectionTimeout);
            socket.connect(new InetSocketAddress(Host, Port));

            long now = getDateTime();
            byte[] payload = new DeviceStatusCommand(1, now, firmwareVersion).toBytes();

            ScocCommand request = new ScocCommand(serialNumber, uin, payload);
            OutputStream os = socket.getOutputStream();
            os.write(request.toBytes());
            os.close();

            socket.setSoTimeout(ResponseTimeout);
            InputStream in = socket.getInputStream();

            ScocCommand response = ScocCommand.read(in);

            return DeviceStatusResponse.read(response.getData());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                logger.error("Socket close failed", e);
            }
        }
    }

    public DeviceFirmwareResponse readFirmware(long firmwareVersion, int partNumber) throws Exception {
        Socket socket = new Socket();
        try {
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(ConnectionTimeout);
            socket.connect(new InetSocketAddress(Host, Port));

            long now = getDateTime();
            byte[] payload = new DeviceFirmwareCommand(now, firmwareVersion, partNumber).toBytes();

            ScocCommand request = new ScocCommand(serialNumber, uin, payload);
            OutputStream os = socket.getOutputStream();
            os.write(request.toBytes());
            os.close();

            socket.setSoTimeout(ResponseTimeout);
            InputStream in = socket.getInputStream();
            ScocCommand response = ScocCommand.read(in);

            DeviceStatusResponse status = DeviceStatusResponse.read(response.getData());

            if (status.getResultCode() != 0)
                throw new Exception("Server response contains error " + status.getResultCode() + ", flags " + status.getFlags());

            return DeviceFirmwareResponse.read(response.getData());

        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                logger.error("Socket close failed", e);
            }
        }
    }

    private long getDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        return calendar.getTimeInMillis() / 1000L;
    }
}
