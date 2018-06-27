package com.shtrih.fiscalprinter.scoc;

import com.shtrih.fiscalprinter.scoc.commands.DeviceFirmwareResponse;
import com.shtrih.fiscalprinter.scoc.commands.DeviceStatusResponse;

import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class ScocClientTests {

    @Ignore
    @Test
    public void Should_send_status() throws Exception {
        ScocClient client = new ScocClient("0075590041015000", 410030054L);

        DeviceStatusResponse response = client.sendStatus(100000);
        System.out.println("Result code: " + response.getResultCode());
        System.out.println("Flags: " + response.getFlags());
    }

    @Ignore
    @Test
    public void Should_load_firmware() throws Exception {
        ScocClient client = new ScocClient("0075590041015000", 410030054L);

        int firmwareVersion = 0;

        DeviceFirmwareResponse firstResponse = client.readFirmware(firmwareVersion, 1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        out.write(firstResponse.getData());

        for (int i = 2; i <= firstResponse.getPartsCount(); i++) {

            DeviceFirmwareResponse firstResponse1 = client.readFirmware(firmwareVersion, i);
            out.write(firstResponse1.getData());
        }

        out.flush();

        byte[] firmware = out.toByteArray();

        System.out.println("Firmware length: " + firmware);

        try (FileOutputStream writer = new FileOutputStream("C:\\firmware.bin")) {
            writer.write(firmware);
        }

    }
}
