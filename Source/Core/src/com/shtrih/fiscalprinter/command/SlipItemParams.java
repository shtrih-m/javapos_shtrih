/*
 * SlipItemParams.java
 *
 * Created on January 15 2009, 16:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */
public class SlipItemParams {

    // Quantity format (1 byte) «0» – no digits after decimal dot, «1» – digits
    // after decimal dot
    public byte quantityFormat = 0;
    // Number of lines in transaction block (1 byte) 1…3
    public byte lineNumber = 0;
    // Line number of Text element in transaction block (1 byte) 0…3, «0» – do
    // not print
    public byte textLine = 0;
    // Line number of Quantity Times Unit Price element in transaction block (1
    // byte) 0…3, «0» – do not print
    public byte quantityLine = 0;
    // Line number of Transaction Sum element in transaction block (1 byte) 1…3
    public byte amountLine = 0;
    // Line number of Department element in transaction block (1 byte) 1…3
    public byte departmentLine = 0;
    // Font type of Text element (1 byte)
    public byte textFont = 0;
    // Font type of Quantity element (1 byte)
    public byte quantityFont = 0;
    // Font type of Multiplication sign element (1 byte)
    public byte multSignFont = 0;
    // Font type of Unit Price element (1 byte)
    public byte priceFont = 0;
    // Font type of Transaction Sum element (1 byte)
    public byte amountFont = 0;
    // Font type of Department element (1 byte)
    public byte departmentFont = 0;
    // Length of Text element in characters (1 byte)
    public byte textLength = 0;
    // Length of Quantity element in characters (1 byte)
    public byte quantityLength = 0;
    // Length of Unit Price element in characters (1 byte)
    public byte priceLength = 0;
    // Length of Transaction Sum element in characters (1 byte)
    public byte amountLength = 0;
    // Length of Department element in characters (1 byte)
    public byte departmentLength = 0;
    // Position in line of Text element (1 byte)
    public byte textOffset = 0;
    // Position in line of Quantity Times Unit Price element (1 byte)
    public byte quantityOffset = 0;
    // Position in line of Transaction Sum element (1 byte)
    public byte amountOffset = 0;
    // Position in line of Department element (1 byte)
    public byte departmentOffset = 0;

    /** Creates a new instance of SlipItemParams */
    public SlipItemParams() {
    }

}
