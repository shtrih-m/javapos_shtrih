/*
 * PrinterSubmode.java
 *
 * Created on 13 March 2008, 13:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */

public class PrinterSubmode {
    private final int value;

    public PrinterSubmode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return value + ", " + getText(value);
    }

    public static String getText(int value) {
        switch (value) {
            case PrinterConst.ECR_SUBMODE_IDLE:
                return PrinterConst.S_ECR_SUBMODE_IDLE;

            case PrinterConst.ECR_SUBMODE_PASSIVE:
                return PrinterConst.S_ECR_SUBMODE_PASSIVE;

            case PrinterConst.ECR_SUBMODE_ACTIVE:
                return PrinterConst.S_ECR_SUBMODE_ACTIVE;

            case PrinterConst.ECR_SUBMODE_AFTER:
                return PrinterConst.S_ECR_SUBMODE_AFTER;

            case PrinterConst.ECR_SUBMODE_REPORT:
                return PrinterConst.S_ECR_SUBMODE_REPORT;

            case PrinterConst.ECR_SUBMODE_PRINT:
                return PrinterConst.S_ECR_SUBMODE_REPORT;

            default:
                return PrinterConst.S_ECR_SUBMODE_UNKNOWN;
        }
    }

}
