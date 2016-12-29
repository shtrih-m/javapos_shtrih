/*
 * PrinterBarcode.java
 *
 * Created on March 4 2008, 21:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.barcode;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import java.util.Vector;

import com.shtrih.jpos.fiscalprinter.PrintItem;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

/**
 * @author V.Kravtsov
 */
public class PrinterBarcode implements PrintItem {

    // barcode data
    private String text = "";
    // barcode label
    private String label = "";
    // barcode bar width, pixels
    private int barWidth = 2;
    // barcode height, pixels
    private int height = 100;
    // barcode type
    private int type = SmFptrConst.SMFPTR_BARCODE_EAN13;
    // text position
    private int textPosition = SmFptrConst.SMFPTR_TEXTPOS_BELOW;
    // text font
    private int textFont = 1;
    // print type
    private int printType = SmFptrConst.SMFPTR_PRINTTYPE_DRIVER;
    // aspect ratio
    private int aspectRatio = 3;
    // parameters
    private Vector parameters = new Vector();

    /**
     * Creates a new instance of PrinterBarcode
     */
    public PrinterBarcode() {
    }

    public Vector getParameters() {
        return parameters;
    }

    public Object getParameter(int index) {
        if ((index < 0) || (index >= parameters.size())) {
            return null;
        }
        return parameters.get(index);
    }

    public void setParameters(Vector parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Object item) {
        parameters.add(item);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTextPosition() {
        return textPosition;
    }

    public void setTextPosition(int textPosition) {
        this.textPosition = textPosition;
    }

    public int getTextFont() {
        return textFont;
    }

    public void setTextFont(int textFont) {
        this.textFont = textFont;
    }

    public int getPrintType() {
        return printType;
    }

    public void setPrintType(int printType) {
        this.printType = printType;
    }

    public int getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(int aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public boolean isLinear() {
        return (type == SmFptrConst.SMFPTR_BARCODE_UPCA) || (type == SmFptrConst.SMFPTR_BARCODE_UPCE) || (type == SmFptrConst.SMFPTR_BARCODE_EAN13)
                || (type == SmFptrConst.SMFPTR_BARCODE_EAN8) || (type == SmFptrConst.SMFPTR_BARCODE_CODE39) || (type == SmFptrConst.SMFPTR_BARCODE_ITF)
                || (type == SmFptrConst.SMFPTR_BARCODE_CODABAR) || (type == SmFptrConst.SMFPTR_BARCODE_CODE93) || (type == SmFptrConst.SMFPTR_BARCODE_CODE128);
    }

    public boolean isTextAbove() {
        return (textPosition == SmFptrConst.SMFPTR_TEXTPOS_ABOVE) || (textPosition == SmFptrConst.SMFPTR_TEXTPOS_BOTH);
    }

    public boolean isTextBelow() {
        return (textPosition == SmFptrConst.SMFPTR_TEXTPOS_BELOW) || (textPosition == SmFptrConst.SMFPTR_TEXTPOS_BOTH);
    }

    public void print(SMFiscalPrinter printer) throws Exception {
        printer.printBarcode(this);
    }
}
