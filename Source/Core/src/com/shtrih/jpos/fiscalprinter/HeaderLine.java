/*
 * HeaderLine.java
 *
 * Created on 23 Ноябрь 2009 г., 20:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 * @author V.Kravtsov
 */
public class HeaderLine {

    private String text = "";
    private boolean doubleWidth = false;

    /**
     * Creates a new instance of HeaderLine
     */
    public HeaderLine() {
    }

    public HeaderLine(String text, boolean doubleWidth) {
        this.text = text;
        this.doubleWidth = doubleWidth;
    }

    public String getText() {
        return text;
    }

    public boolean isDoubleWidth() {
        return doubleWidth;
    }

     public void setText(String text) {
        this.text = text;
    }

    public void setDoubleWidth(boolean doubleWidth) {
        this.doubleWidth = doubleWidth;
    }
}
