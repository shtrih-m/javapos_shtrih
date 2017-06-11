/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import java.util.Vector;

/**
 *
 * @author V.Kravtsov
 */
public class TLVTag {

    private final int id;
    private final int size;
    private final TLVType type;
    private final String displayName;
    private final String printName;
    private final Vector<TLVBit> bits = new Vector<TLVBit>();

    public enum TLVType {
        itByte, itUInt16, itUInt32, itVLN, itFVLN, itBitMask,
        itUnixTime, itASCII, itSTLV, itByteArray
    }

    public TLVTag(int id, String displayName, String printName,
            TLVType type, int size) {
        this.id = id;
        this.displayName = displayName;
        this.printName = printName;
        this.type = type;
        this.size = size;
    }

    public TLVTag(int id, TLVType type) {
        this.id = id;
        this.displayName = "";
        this.printName = "";
        this.type = type;
        this.size = 0;
    }
    
    public int getId() {
        return id;
    }

    public TLVType getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrintName() {
        return printName;
    }

    public Vector<TLVBit> getBits() {
        return bits;
    }

    public TLVBit addBit(int bit, String name) {
        TLVBit item = new TLVBit(bit, name, "");
        bits.add(item);
        return item;
    }

    public TLVBit addBit(int bit, String name, String printName) {
        TLVBit item = new TLVBit(bit, name, printName);
        bits.add(item);
        return item;
    }
}
