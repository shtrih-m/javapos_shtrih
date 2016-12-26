/*
 * PrinterImage.java
 *
 * Created on March 21 2008, 6:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.PrinterImage;

public class ImageLoader {

    private int firstLine = 0;
    private boolean centerImage = true;
    private final SMFiscalPrinter printer;

    /**
     * Creates a new instance of PrinterImage
     */
    public ImageLoader(SMFiscalPrinter printer) {
        this.printer = printer;
    }

    public int getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(int value) {
        this.firstLine = value;
    }

    public void setCenterImage(boolean value) {
        this.centerImage = value;
    }

    public void load(String fileName) throws Exception {
        PrinterImage image = new PrinterImage(fileName);
        image.setStartPos(firstLine);
        printer.loadImage(image, centerImage);
    }
}
