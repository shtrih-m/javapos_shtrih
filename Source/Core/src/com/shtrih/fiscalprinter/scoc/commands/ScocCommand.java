package com.shtrih.fiscalprinter.scoc.commands;

import com.shtrih.fiscalprinter.command.CommandInputStream;
import com.shtrih.fiscalprinter.command.CommandOutputStream;
import com.shtrih.util.StringUtils;

import java.io.InputStream;

public class ScocCommand {
    private final String serialNumber;
    private final long uin;
    private final byte[] data;

    public String getSerialNumber() {
        return serialNumber;
    }

    public long getUin() {
        return uin;
    }

    public byte[] getData() {
        return data;
    }

    public static ScocCommand read(InputStream inputStream) throws Exception {

        CommandInputStream in = new CommandInputStream("utf-8", inputStream);

        in.readInt(); // Сигнатура 06DF0942

        String serialNumber = in.readString(20);
        String sVersion = in.readString(4);
        String aVersion = in.readString(4);

        long dataSize = sVersion.equals("0201") ? in.readLong(4) : in.readLong(2);

        long uin = in.readLong(8);

        long flags = in.readLong(2);
        long crc = in.readLong(2);

        byte[] data = in.readBytes((int) dataSize);


        return new ScocCommand(serialNumber, uin, data);
    }

    public ScocCommand(String serialNumber, long uin, byte[] data) {

        this.serialNumber = serialNumber;
        this.uin = uin;
        this.data = data;
    }

    public byte[] toBytes() throws Exception {
        CommandOutputStream out = new CommandOutputStream("utf-8");

        out.writeBytes(new byte[]{0x06, (byte) 0xDF, 0x09, 0x42});
        out.writeString(getNormalizedSerialNumber());
        out.writeString("0101");
        out.writeString("0101");
        out.writeShort(data.length);
        out.writeLong(uin, 8);
        out.writeShort(0x0000);
        out.writeShort(0x0000);
        out.writeBytes(data);

        return out.getData();
    }


    private String getNormalizedSerialNumber() {
        if (serialNumber.length() == 20)
            return serialNumber;

        return StringUtils.alignRight(serialNumber, 20);
    }
}