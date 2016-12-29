/*
 * PrintBarcode.java
 *
 * Created on March 7 2008, 14:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/*****************************************************************************
 * Print barcode 
 * Command: 0xCBh. Length: 57 bytes. 
 * - Operator password (4 bytes)
 * - Barcode height (1 byte) 
 * - Barcode width (1 byte) 
 * - Barcode HRI position (1 byte) 
 * - Barcode HRI pitch (1 byte) 
 * - Barcode type (1 byte) 
 * - Barcode data (1-48 bytes) 
 * 
 * Answer: 0xCBh. Length: 3 bytes. 
 * - Result Code (1 byte) 
 * - Operator number (1 byte) 1…30
 *****************************************************************************/

public final class PrintBarcode2 extends PrinterCommand {
    // in
    private int operatorPassword;
    private int barcodeHeight;
    private int barWidth;
    private int barcodeHRIPosition;
    private int barcodeHRIPitch;
    private int barcodeType;
    private String barcodeData;
    // out
    private int operatorNumber;

    public PrintBarcode2() {
        super();
    }

    public final int getCode() {
        return 0xCB;
    }

    public final String getText() {
        return "Print barcode 2";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getOperatorPassword());
        out.writeByte(getBarcodeHeight());
        out.writeByte(getBarWidth());
        out.writeByte(getBarcodeHRIPosition());
        out.writeByte(getBarcodeHRIPitch());
        out.writeByte(getBarcodeType());
        out.writeString(getBarcodeData(), getBarcodeData().length());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setOperatorNumber(in.readByte());
    }

    public int getOperatorPassword() {
        return operatorPassword;
    }

    public int getBarcodeHeight() {
        return barcodeHeight;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public int getBarcodeHRIPosition() {
        return barcodeHRIPosition;
    }

    public int getBarcodeHRIPitch() {
        return barcodeHRIPitch;
    }

    public int getBarcodeType() {
        return barcodeType;
    }

    public String getBarcodeData() {
        return barcodeData;
    }

    public int getOperatorNumber() {
        return operatorNumber;
    }

    public void setOperatorPassword(int operatorPassword) {
        this.operatorPassword = operatorPassword;
    }

    public void setBarcodeHeight(int barcodeHeight) {
        this.barcodeHeight = barcodeHeight;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public void setBarcodeHRIPosition(int barcodeHRIPosition) {
        this.barcodeHRIPosition = barcodeHRIPosition;
    }

    public void setBarcodeHRIPitch(int barcodeHRIPitch) {
        this.barcodeHRIPitch = barcodeHRIPitch;
    }

    public void setBarcodeType(int barcodeType) {
        this.barcodeType = barcodeType;
    }

    public void setBarcodeData(String barcodeData) {
        this.barcodeData = barcodeData;
    }

    public void setOperatorNumber(int operatorNumber) {
        this.operatorNumber = operatorNumber;
    }

}
