/*
 * ReceiptImage.java
 *
 * Created on 12 Ноябрь 2009 г., 15:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 * @author V.Kravtsov
 */

public class ReceiptImage {

    private final int imageIndex;
    private final int position;

    /**
     * Creates a new instance of ReceiptImage
     */
    public ReceiptImage(int imageIndex, int position) {
        this.imageIndex = imageIndex;
        this.position = position;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public int getPosition() {
        return position;
    }

    public boolean valid(int position) {
        return (this.position == position);
    }
}
