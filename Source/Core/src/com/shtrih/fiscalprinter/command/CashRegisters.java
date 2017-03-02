/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

public class CashRegisters {

    private final Vector<CashRegister> items = new Vector<CashRegister>();

    public CashRegisters() {
    }

    public int size() {
        return items.size();
    }

    public CashRegister get(int index) throws Exception {
        return items.get(index);
    }

    public CashRegister find(int number) throws Exception {
        for (int i = 0; i < items.size(); i++) {
            CashRegister register = items.get(i);
            if (register.getNumber() == number) {
                return register;
            }
        }
        return null;
    }

    public void add(CashRegister item) throws Exception {
        items.add(item);
    }

}
