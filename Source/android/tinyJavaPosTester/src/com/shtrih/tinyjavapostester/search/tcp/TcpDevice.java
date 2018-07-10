package com.shtrih.tinyjavapostester.search.tcp;

public class TcpDevice {
    public final String serialNumber;
    public final String address;

    TcpDevice(String serialNumber, String address) {
        this.serialNumber = serialNumber;
        this.address = address;
    }
}
