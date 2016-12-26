/*
 * PrinterFlags.java
 *
 * Created on April 2 2008, 17:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.util.BitUtils;

public class PrinterFlags {

    private final int value;

    public PrinterFlags(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // journal paper is near end
    public boolean isJrnNearEnd() {
        return !BitUtils.testBit(value, 0);
    }

    public boolean isJrnRollPresent() {
        return BitUtils.testBit(value, 0);
    }

    // receipt paper is near end
    public boolean isRecNearEnd() {
        return !BitUtils.testBit(value, 1);
    }

    public boolean isRecRollPresent() {
        return BitUtils.testBit(value, 1);
    }

    public boolean isSlipPresent() {
        return BitUtils.testBit(value, 2);
    }

    public boolean isSlpEmpty() {
        return !BitUtils.testBit(value, 2);
    }

    public boolean isSlipMoving() {
        return BitUtils.testBit(value, 3);
    }

    public boolean isSlpNearEnd() {
        return BitUtils.testBit(value, 3);
    }

    public boolean getAmountPointPosition() {
        return BitUtils.testBit(value, 4);
    }

    // electronic journal present
    public boolean isEJPresent() {
        return BitUtils.testBit(value, 5);
    }

    // journal paper is empty
    public boolean isJrnEmpty() {
        return !BitUtils.testBit(value, 6);
    }

    // journal paper is present
    public boolean isJrnPresent() {
        return BitUtils.testBit(value, 6);
    }

    // receipt paper is empty
    public boolean isRecEmpty() {
        return !BitUtils.testBit(value, 7);
    }

    // receipt paper is present
    public boolean isRecPresent() {
        return BitUtils.testBit(value, 7);
    }

    // journal station lever is up
    public boolean isJrnLeverUp() {
        return BitUtils.testBit(value, 8);
    }

    // receipt station lever is up
    public boolean isRecLeverUp() {
        return BitUtils.testBit(value, 9);
    }

    // printer cover is opened
    public boolean isCoverOpened() {
        return BitUtils.testBit(value, 10);
    }

    // cash drawer is opened
    public boolean isDrawerOpened() {
        return BitUtils.testBit(value, 11);
    }

    // right printer sensor failure
    public boolean isRSensorFailure() {
        return BitUtils.testBit(value, 12);
    }

    // Presenter paper
    public boolean isPresenterIn() {
        return BitUtils.testBit(value, 12);
    }

    // left printer sensor failure
    public boolean isLSensorFailure() {
        return BitUtils.testBit(value, 13);
    }

    // Presenter paper out
    public boolean isPresenterOut() {
        return BitUtils.testBit(value, 13);
    }

    // electronic journal near end
    public boolean isEJNearEnd() {
        return BitUtils.testBit(value, 14);
    }

    // extended quantity enabled
    public boolean isExtQuantity() {
        return BitUtils.testBit(value, 15);
    }
}
