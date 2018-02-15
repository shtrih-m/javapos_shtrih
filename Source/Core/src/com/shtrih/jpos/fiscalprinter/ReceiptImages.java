/*
 * ReceiptImages.java
 *
 * Created on 12 Ноябрь 2009 г., 15:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

import com.shtrih.util.CompositeLogger;

public class ReceiptImages {

    private Vector list = new Vector();
    private static CompositeLogger logger = CompositeLogger.getLogger(ReceiptImages.class);

    /**
     * Creates a new instance of ReceiptImages
     */
    public ReceiptImages() {
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public ReceiptImage get(int index) {
        return (ReceiptImage) list.get(index);
    }

    public ReceiptImage imageByPosition(int position) {
        for (int i = 0; i < size(); i++) {
            ReceiptImage result = get(i);
            if (result.getPosition() == position) {
                return result;
            }
        }
        logger.debug("Image not found, position " + position + ", size: " + size());
        return null;
    }

    public void add(ReceiptImage image) {
        ReceiptImage item = imageByPosition(image.getPosition());
        if (item != null) {
            list.remove(item);
        }
        list.add(image);
    }

}
