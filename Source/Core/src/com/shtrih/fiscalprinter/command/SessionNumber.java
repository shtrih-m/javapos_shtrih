/*
 * SessionNumber.java
 *
 * Created on April 2 2008, 17:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

import com.shtrih.util.MethodParameter;

class SessionNumber {
    private int value = 0;

    public void setValue(int newValue) throws Exception {
        MethodParameter.checkRange(newValue, 0, 2100, "Session number");
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
