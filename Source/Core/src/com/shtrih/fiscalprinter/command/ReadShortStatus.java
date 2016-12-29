/*
 * ReadShortStatus.java
 *
 * Created on 2 April 2008, 18:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 * Read short status Command: 10H. Length: 5 bytes. · Operator password (4
 * bytes) Answer: 10H. Length: 16 bytes. · Result Code (1 byte) · Operator index
 * number (1 byte) 1…30 · FP flags (2 bytes) · FP mode (1 byte) · FP submode (1
 * byte) · Quantity of operations on the current receipt (1 byte) lower byte of
 * a two-byte digit (see below) · Battery voltage (1 byte) · Power source
 * voltage (1 byte) · Fiscal Memory error code (1 byte) · EJ error code (1 byte)
 * EJ=Electronic Cryptographic Journal · Quantity of operations on the current
 * receipt (1 byte) upper byte of a two-byte digit (see below) · Reserved (3
 * bytes) FP flags Flags (bits): Bit 0 – Journal station low paper (0 – yes, 1 –
 * no) Bit 1 – Receipt station low paper (0 – yes, 1 – no) Bit 2 – Paper in slip
 * station upper sensor (0 – no, 1 – yes) Bit 3 – Paper in slip station lower
 * sensor (0 – no, 1 – yes) Bit 4 – Decimal dot position ( 0 – 0 digits after
 * the dot, 1 – 2 digits after the dot) Bit 5 – EJ in FP (0 – no, 1 – yes) Bit 6
 * – Journal station out-of-paper 0 – no paper, 1 – paper in printing mechanism)
 * Bit 7 – Receipt station out-of-paper ( 0 – no paper, 1 – paper in printing
 * mechanism) Bit 8 –Thermal head lever position of journal station ( 0 – lever
 * up, 1 – lever down) Bit 9 – Thermal head lever position of receipt station (
 * 0 – lever up, 1 – lever down) Bit 10 – FP cabinet lid position (0 – lid down,
 * 1 – lid up) Bit 11 – Cash drawer (0 – drawer closed, 1 – drawer open) Bit 12a
 * –Failure of right sensor of printing mechanism (0 – no, 1 – yes) Bit 12b –
 * Paper on input to presenter (0 – no, 1 – yes) Bit 12c – Printing mechanism
 * model (0 – MLT-286, 1 – MLT-286-1) Bit 13a – Failure of left sensor of
 * printing mechanism (0 – no, 1 – yes) Bit 13b – Paper in presenter (0 – no, 1
 * – yes) Bit 14 – EJ almost full (0 – no, 1 – yes) Bit 15a – Quantity accuracy·
 * for fiscal printers without EJ: 0 – standard accuracy, 1 – higher accuracy·
 * for fiscal printers with EJ: 1 – standard accuracy, 0 – higher accuracyBit
 * 15b – Printer buffer status (0 – empty, 1 – not empty) [for fiscal module of
 * SHTRIH-POS-F]
 ****************************************************************************/

public class ReadShortStatus extends PrinterCommand {
    // in
    private final int password;
    // out
    private ShortPrinterStatus status;

    /**
     * Creates a new instance of ReadShortStatus
     */
    public ReadShortStatus(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x10;
    }

    public final String getText() {
        return "Read short status";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        int operatorNumber = in.readByte();
        int flags = in.readShort();
        int mode = ((int) in.readByte() & 0x0F);
        int subMode = in.readByte();
        int receiptOperationsLo = in.readByte();
        int batteryState = in.readByte();
        int powerState = in.readByte();
        int FMResultCode = in.readByte();
        int EJResultCode = in.readByte();
        int receiptOperationsHi = in.readByte();

        double batteryVoltage = (batteryState / 255.0 * 100 * 5) / 100;
        double powerVoltage = (powerState * 24.0 / 0xD8 * 100) / 100;
        int receiptOperations = receiptOperationsLo
                + (receiptOperationsHi << 8);
        status = new ShortPrinterStatus(mode, flags, subMode, FMResultCode,
                EJResultCode, receiptOperations, batteryVoltage, powerVoltage,
                operatorNumber);
    }

    public ShortPrinterStatus getStatus() {
        return status;
    }

    public boolean getIsRepeatable() {
        return true;
    }
}
