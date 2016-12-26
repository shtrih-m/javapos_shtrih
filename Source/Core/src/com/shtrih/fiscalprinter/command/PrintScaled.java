/*
 * PrintGraphics.java
 *
 * Created on April 15 2008, 13:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.util.MethodParameter;


public class PrintScaled extends PrinterCommand {
    // in
    private int password; // Operator password
    private int line1; // Number of first line
    private int line2; // Number of last line
    private int vScale; 
    private int hScale; 
    // out
    private int operator = 0;

    /**
     * Creates a new instance of PrintGraphics
     */
    public PrintScaled(){
    }

    
    public final int getCode() {
        return 0x4F;
    }

    
    public final String getText() {
        return "Print scaled graphics";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getLine1());
        out.writeByte(getLine2());
        out.writeByte(getvScale());
        out.writeByte(gethScale());
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
    }

    public int getOperator() {
        return operator;
    }

    /**
     * @return the password
     */
    public int getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @return the line1
     */
    public int getLine1() {
        return line1;
    }

    /**
     * @param line1 the line1 to set
     */
    public void setLine1(int line1) {
        this.line1 = line1;
    }

    /**
     * @return the line2
     */
    public int getLine2() {
        return line2;
    }

    /**
     * @param line2 the line2 to set
     */
    public void setLine2(int line2) {
        this.line2 = line2;
    }

    /**
     * @return the vScale
     */
    public int getvScale() {
        return vScale;
    }

    /**
     * @param vScale the vScale to set
     */
    public void setvScale(int vScale) {
        this.vScale = vScale;
    }

    /**
     * @return the hScale
     */
    public int gethScale() {
        return hScale;
    }

    /**
     * @param hScale the hScale to set
     */
    public void sethScale(int hScale) {
        this.hScale = hScale;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(int operator) {
        this.operator = operator;
    }
}
