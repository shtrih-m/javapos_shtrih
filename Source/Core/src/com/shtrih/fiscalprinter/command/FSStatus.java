/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.util.BitUtils;

/**
 *
 * @author V.Kravtsov
 */
public class FSStatus {

    private final int code;

    public FSStatus(int code) {
        this.code = code;
    }

    public boolean isConfigured() {
        return BitUtils.testBit(code, 0);
    }

    public boolean isFiscalModeOpened() {
        return BitUtils.testBit(code, 1);
    }

    public boolean isFiscalModeClosed() {
        return BitUtils.testBit(code, 2);
    }

    public boolean isTransferCompleted() {
        return BitUtils.testBit(code, 3);
    }
}
