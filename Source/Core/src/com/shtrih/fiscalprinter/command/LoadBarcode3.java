/*
 * LoadGraphics.java
 *
 * Created on April 15 2008, 12:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.util.MethodParameter;


public class LoadBarcode3 extends PrinterCommand {
    // in
    private int password; // Operator password (4 bytes)
    private int blockType; 
    private int blockNumber; 
    private byte[] blockData; // Barcode data
    // out
    private int operator;

    public static final int MAX_BLOCK_SIZE = 64;
    /**
     * Creates a new instance of LoadGraphics
     */
    public LoadBarcode3() {
        super();
    }

    
    public final int getCode() {
        return 0xDD;
    }

    
    public final String getText() {
        return "Load barcode 3";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getBlockType());
        out.writeByte(getBlockNumber());
        out.writeBytes(getBlockData());
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
    }

    public int getOperator() {
        return operator;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    /**
     * @return the blockType
     */
    public int getBlockType() {
        return blockType;
    }

    /**
     * @param blockType the blockType to set
     */
    public void setBlockType(int blockType) {
        this.blockType = blockType;
    }

    /**
     * @return the blockNumber
     */
    public int getBlockNumber() {
        return blockNumber;
    }

    /**
     * @param blockNumber the blockNumber to set
     */
    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    /**
     * @return the blockData
     */
    public byte[] getBlockData() {
        return blockData;
    }

    /**
     * @param blockData the blockData to set
     */
    public void setBlockData(byte[] blockData) {
        this.blockData = blockData;
    }
}
