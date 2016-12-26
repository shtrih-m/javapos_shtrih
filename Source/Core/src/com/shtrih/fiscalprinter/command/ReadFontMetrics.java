/*
 * CommandReadFontMetrics.java
 *
 * Created on 15 January 2009, 12:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/****************************************************************************
 * Get Font Parameters Command: 26H. Length: 6 bytes. · System Administrator
 * password (4 bytes) 30 · Font type (1 byte) Answer: 26H. Length: 7 bytes. ·
 * Result Code (1 byte) · Print width in dots (2 bytes) · Character width in
 * dots (1 byte) the width is given together with inter-character spacing ·
 * Character height in dots (1 byte) the height is given together with
 * inter-line spacing · Number of fonts in FP (1 byte)
 ****************************************************************************/
public final class ReadFontMetrics extends PrinterCommand {

    // in params
    private int password;
    private int font;
    // out params
    private int paperWidth;
    private int charWidth;
    private int charHeight;
    private int fontCount;

    /**
     * Creates a new instance of CommandReadFontMetrics
     */
    public ReadFontMetrics() {
    }

    public final int getCode() {
        return 0x26;
    }

    public final String getText() {
        return "Get font info";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getFont());
    }

    public final void decode(CommandInputStream in) throws Exception {
        paperWidth = in.readShort();
        charWidth = in.readByte();
        charHeight = in.readByte();
        fontCount = in.readByte();
    }

    public boolean getIsRepeatable() {
        return true;
    }

    public int getPassword() {
        return password;
    }

    public int getFont() {
        return font;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public int getPaperWidth() {
        return paperWidth;
    }

    public int getCharWidth() {
        return charWidth;
    }

    public int getCharHeight() {
        return charHeight;
    }

    public int getFontCount() {
        return fontCount;
    }

    public void setPaperWidth(int paperWidth) {
        this.paperWidth = paperWidth;
    }

    public void setCharWidth(int charWidth) {
        this.charWidth = charWidth;
    }

    public void setCharHeight(int charHeight) {
        this.charHeight = charHeight;
    }

    public void setFontCount(int fontCount) {
        this.fontCount = fontCount;
    }
}
