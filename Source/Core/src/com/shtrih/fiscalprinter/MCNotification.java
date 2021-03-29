/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 *
 * @author Виталий
 */
import com.shtrih.util.MathUtils;

public class MCNotification {

    private final int number;
    private final byte[] data;
    private final int crc;

    public MCNotification(int number, byte[] data) {
        this.number = number;
        this.data = data;
        crc = MathUtils.CRC16CCITT(data);
    }

    public int getNumber() {
        return number;
    }

    public byte[] getData() {
        return data;
    }

    public int getCrc() {
        return crc;
    }
}
