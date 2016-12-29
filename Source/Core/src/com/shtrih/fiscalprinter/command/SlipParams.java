/*
 * SlipParams.java
 *
 * Created on January 15 2009, 15:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */
public class SlipParams {

    // Font number of fixed header (1 byte)
    public byte fixedHeaderFont = 0;
    // Font number of header (1 byte)
    public byte headerFont = 0;
    // Font number of EJ serial number (1 byte)
    public byte EJFont = 0;
    // Font number of CRC value and CRC number (1 byte)
    public byte EJCRCFont = 0;
    // Vertical position of the first line of fixed header (1 byte)
    public byte fixedHeaderY = 0;
    // Vertical position of the first line of header (1 byte)
    public byte headerY = 0;
    // Vertical position of line with EJ number (1 byte)
    public byte EJY = 0;
    // Vertical position of line with duplicate marker (1 byte)
    public byte copySignY = 0;
    // Horizontal position of fixed header in line (1 byte)
    public byte fixedHeaderX = 0;
    // Horizontal position of header in line (1 byte)
    public byte headerX = 0;
    // Horizontal position of EJ number in line (1 byte)
    public byte EJX = 0;
    // Horizontal position of CRC value and CRC number in line (1 byte)
    public byte EJCRC = 0;
    // Horizontal position of duplicate marker in line (1 byte)
    public byte copySignX = 0;

    /** Creates a new instance of SlipParams */
    public SlipParams() {
    }

}
