/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.fiscalprinter.FontNumber;

/**
 *
 * @author Kravtsov
 */
public class TextReceiptItem {

    public final String text;
    public final FontNumber font;

    public TextReceiptItem(String text, FontNumber font){

        this.text = text;
        this.font = font;
    }
}
