package com.shtrih.jpos.fiscalprinter.receipt;


import com.shtrih.fiscalprinter.FontNumber;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Виталий
 */
    public class FSTLVItem {

        private final byte[] data;
        private final boolean print;
        private final FontNumber font;

        public FSTLVItem(byte[] data, FontNumber font, boolean print) {
            this.data = data;
            this.font = font;
            this.print = print;
        }

        public byte[] getData() {
            return data;
        }

        public FontNumber getFont() {
            return font;
        }

        public boolean getPrint() {
            return print;
        }
    }
