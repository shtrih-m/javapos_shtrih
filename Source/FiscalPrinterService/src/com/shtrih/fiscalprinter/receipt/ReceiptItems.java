/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

import com.shtrih.util.CompositeLogger;

public class ReceiptItems {

    private Vector list = new Vector();
    private static CompositeLogger logger = CompositeLogger.getLogger(ReceiptItems.class);

    /** Creates a new instance of ReceiptItems */
    public ReceiptItems() {
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public void add(ReceiptItem item) {
        list.add(item);
    }

    public void remove(ReceiptItem item) {
        list.remove(item);
    }

    public ReceiptItem get(int index) {
        return (ReceiptItem) list.get(index);
    }

    public ReceiptItems getItemsById(int Id) {
        ReceiptItems result = new ReceiptItems();
        for (int i = 0; i < size(); i++) {
            ReceiptItem item = get(i);
            if (item.getId() == Id) {
                result.add(item);
            }
        }
        return result;
    }

    public ReceiptItems getItemsByDescription(String description) {
        ReceiptItems result = new ReceiptItems();
        for (int i = 0; i < size(); i++) {
            ReceiptItem item = get(i);
            if (description.equalsIgnoreCase(item.getDescription())) {
                result.add(item);
            }
        }
        return result;
    }
}
