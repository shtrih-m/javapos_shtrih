/*
 * SlipOpenParams.java
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
public class SlipOpenParams {

    // Slip type (1 byte) «0» – Sale, «1» – Buy, «2» – Sale Refund, «3» – Buy
    // Refund
    public byte slipType = 0;
    // Slip duplicates type (1 byte) «0» – duplicates as columns, «1» –
    // duplicates as line blocks
    public byte slipDupType = 0;
    // Number of duplicates (1 byte) 0…5
    public byte numCopies = 0;
    // Spacing between Original and Duplicate 1 (1 byte) *
    public byte spacing1 = 0;
    // Spacing between Duplicate 1 and Duplicate 2 (1 byte) *
    public byte spacing2 = 0;
    // Spacing between Duplicate 2 and Duplicate 3 (1 byte) *
    public byte spacing3 = 0;
    // Spacing between Duplicate 3 and Duplicate 4 (1 byte) *
    public byte spacing4 = 0;
    // Spacing between Duplicate 4 and Duplicate 5 (1 byte) *
    public byte spacing5 = 0;

    /** Creates a new instance of SlipOpenParams */
    public SlipOpenParams() {
    }

}
