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
public class FSTextReceiptItem {

    public final String text;
    public final String preLine;
    public final String postLine;
    public final FontNumber font;

    public FSTextReceiptItem(String text, String preLine, String postLine, FontNumber font){

        this.text = text;
        this.preLine = preLine;
        this.postLine = postLine;
        this.font = font;
    }
}
