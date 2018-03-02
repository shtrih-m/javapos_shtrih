/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import java.util.Vector;

/**
 *
 * @author V.Kravtsov
 */
public class FieldInfoMap {

    private final Vector<FieldInfo> list = new Vector<FieldInfo>();

    /**
     * Creates a new instance of FieldInfos
     */
    public FieldInfoMap() {
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public void add(FieldInfo item) {
        list.add(item);
    }

    public FieldInfo get(int index) {
        return list.get(index);
    }

    public FieldInfo find(int table, int field) {
        for (int i = 0; i < size(); i++) {
            FieldInfo result = get(i);
            if ((result.getTable() == table) && (result.getField() == field)) {
                return result;
            }
        }
        return null;
    }
}
