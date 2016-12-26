/*
 * SlipCloseParams.java
 *
 * Created on January 16 2009, 11:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 * @author V.Kravtsov
 */
public class SlipCloseParams {

    // Number of lines in transaction block (1 byte) 1…17
    public int lineNumber = 0;
    // Line number of Receipt Total element in transaction block (1 byte) 1…17
    public int totalLine = 0;
    // Line number of Text element in transaction block (1 byte) 0…17, «0» – do
    // not print
    public int textLine = 0;
    // Line number of Cash Payment element in transaction block (1 byte) 0…17,
    // «0» – do not print
    public int cashLine = 0;
    // Line number of Payment Type 2 element in transaction block (1 byte) 0…17,
    // «0» – do not print
    public int amount2Line = 0;
    // Line number of Payment Type 3 element in transaction block (1 byte) 0…17,
    // «0» – do not print
    public int amount3Line = 0;
    // Line number of Payment Type 4 element in transaction block (1 byte) 0…17,
    // «0» – do not print
    public int amount4Line = 0;
    // Line number of Change element in transaction block (1 byte) 0…17, «0» –
    // do not print
    public int changeLine = 0;
    // Line number of Tax 1 Turnover element in transaction block (1 byte) 0…17,
    // «0» – do not print
    public int tax1Line = 0;
    // Line number of Tax 2 Turnover element in transaction block (1 byte) 0…17,
    // «0» – do not print
    public int tax2Line = 0;
    // Line number of Tax 3 Turnover element in transaction block (1 byte) 0…17,
    // «0» – do not print
    public int tax3Line = 0;
    // Line number of Tax 4 Turnover element in transaction block (1 byte) 0…17,
    // «0» – do not print
    public int tax4Line = 0;
    // Line number of Tax 1 Sum element in transaction block (1 byte) 0…17, «0»
    // – do not print
    public int tax1TotalLine = 0;
    // Line number of Tax 2 Sum element in transaction block (1 byte) 0…17, «0»
    // – do not print
    public int tax2TotalLine = 0;
    // Line number of Tax 3 Sum element in transaction block (1 byte) 0…17, «0»
    // – do not print
    public int tax3TotalLine = 0;
    // Line number of Tax 4 Sum element in transaction block (1 byte) 0…17, «0»
    // – do not print
    public int tax4TotalLine = 0;
    // Line number of Receipt Subtotal Before Discount/Surcharge element in
    // transaction block (1 byte) 0…17, «0» – do not print
    public int subtotalLine = 0;
    // Line number of Discount/Surcharge Value element in transaction block (1
    // byte) 0…17, «0» – do not print
    public int discountLine = 0;
    // Font type of Text element (1 byte)
    public int textFont = 0;
    // Font type of «TOTAL» element (1 byte)
    public int totalTextFont = 0;
    // Font type of Receipt Total Value element (1 byte)
    public int totalAmountFont = 0;
    // Font type of «CASH» element (1 byte)
    public int cashTextFont = 0;
    // Font type of Cash Payment Value element (1 byte)
    public int cashAmountFont = 0;
    // Font type of Payment Type 2 Name element (1 byte)
    public int amount2TextFont = 0;
    // Font type of Payment Type 2 Value element (1 byte)
    public int amount2ValueFont = 0;
    // Font type of Payment Type 3 Name element (1 byte)
    public int amount3TextFont = 0;
    // Font type of Payment Type 3 Value element (1 byte)
    public int amount3ValueFont = 0;
    // Font type of Payment Type 4Name element (1 byte)
    public int amount4TextFont = 0;
    // Font type of Payment Type 4Value element (1 byte)
    public int amount4ValueFont = 0;
    // Font type of «CHANGE» element (1 byte)
    public int chnageTextFont = 0;
    // Font type of Change Value element (1 byte)
    public int chnageValueFont = 0;
    // Font type of Tax 1 Name element (1 byte)
    public int tax1TextFont = 0;
    // Font type of Tax 1 Turnover Value element (1 byte)
    public int tax1AmountFont = 0;
    // Font type of Tax 1 Rate element (1 byte)
    public int tax1RateFont = 0;
    // Font type of Tax 1 Value element (1 byte)
    public int tax1ValueFont = 0;
    // Font type of Tax 2 Name element (1 byte)
    public int tax2TextFont = 0;
    // Font type of Tax 2 Turnover Value element (1 byte)
    public int tax2AmountFont = 0;
    // Font type of Tax 2 Rate element (1 byte)
    public int tax2RateFont = 0;
    // Font type of Tax 2 Value element (1 byte)
    public int tax2ValueFont = 0;
    // Font type of Tax 3 Name element (1 byte)
    public int tax3TextFont = 0;
    // Font type of Tax 3 Turnover Value element (1 byte)
    public int tax3AmountFont = 0;
    // Font type of Tax 3 Rate element (1 byte)
    public int tax3RateFont = 0;
    // Font type of Tax 3 Value element (1 byte)
    public int tax3ValueFont = 0;
    // Font type of Tax 4 Name element (1 byte)
    public int tax4TextFont = 0;
    // Font type of Tax 4 Turnover Value element (1 byte)
    public int tax4AmountFont = 0;
    // Font type of Tax 4 Rate element (1 byte)
    public int tax4RateFont = 0;
    // Font type of Tax 4 Value element (1 byte)
    public int tax4ValueFont = 0;
    // Font type of «SUBTOTAL» element (1 byte)
    public int subtotalTextFont = 0;
    // Font type of Receipt Subtotal Before Discount/Surcharge Value element (1
    // byte)
    public int subtotalValueFont = 0;
    // Font type of «DISCOUNT XX.XX%» element (1 byte)
    public int discountTextFont = 0;
    // Font type of Receipt Discount Value element (1 byte)
    public int discountValueFont = 0;
    // Length of Text element in characters (1 byte)
    public int textLength = 0;
    // Length of Receipt Total Value element in characters (1 byte)
    public int totalLength = 0;
    // Length of Cash Payment Value element in characters (1 byte)
    public int cashLength = 0;
    // Length of Payment Type 2 Value element in characters (1 byte)
    public int payment2Length = 0;
    // Length of Payment Type 3 Value element in characters (1 byte)
    public int payment3Length = 0;
    // Length of Payment Type 4Value element in characters (1 byte)
    public int payment4Length = 0;
    // Length of Change Value element in characters (1 byte)
    public int changeLength = 0;
    // Length of Tax 1 Name element in characters (1 byte)
    public int tax1TextLength = 0;
    // Length of Tax 1 Turnover element in characters (1 byte)
    public int tax1AmountLength = 0;
    // Length of Tax 1 Rate element in characters (1 byte)
    public int tax1RateLength = 0;
    // Length of Tax 1 Value element in characters (1 byte)
    public int tax1ValueLength = 0;
    // Length of Tax 2 Name element in characters (1 byte)
    public int tax2TextLength = 0;
    // Length of Tax 2 Turnover element in characters (1 byte)
    public int tax2AmountLength = 0;
    // Length of Tax 2 Rate element in characters (1 byte)
    public int tax2RateLength = 0;
    // Length of Tax 2 Value element in characters (1 byte)
    public int tax2ValueLength = 0;
    // Length of Tax 3 Name element in characters (1 byte)
    public int tax3TextLength = 0;
    // Length of Tax 3 Turnover element in characters (1 byte)
    public int tax3AmountLength = 0;
    // Length of Tax 3 Rate element in characters (1 byte)
    public int tax3RateLength = 0;
    // Length of Tax 3 Value element in characters (1 byte)
    public int tax3ValueLength = 0;
    // Length of Tax 4 Name element in characters (1 byte)
    public int tax4TextLength = 0;
    // Length of Tax 4 Turnover element in characters (1 byte)
    public int tax4AmountLength = 0;
    // Length of Tax 4 Rate element in characters (1 byte)
    public int tax4RateLength = 0;
    // Length of Tax 4 Value element in characters (1 byte)
    public int tax4ValueLength = 0;
    // Length of Receipt Subtotal Before Discount/Surcharge Value element in
    // characters (1 byte)
    public int subtotalLength = 0;
    // Length of «DISCOUNT XX.XX%» element in characters (1 byte)
    public int discountTextLength = 0;
    // Length of Receipt Discount Value element in characters (1 byte)
    public int discountValueLength = 0;
    // Position in line of Text element (1 byte)
    public int textOffset = 0;
    // Position in line of «TOTAL» element (1 byte)
    public int totalTextOffset = 0;
    // Position in line of Receipt Total Value element (1 byte)
    public int totalValueOffset = 0;
    // Position in line of «CASH» element (1 byte)
    public int payment1TextOffset = 0;
    // Position in line of Cash Payment Value element (1 byte)
    public int payment1ValueOffset = 0;
    // Position in line of Payment Type 2 Name element (1 byte)
    public int payment2TextOffset = 0;
    // Position in line of Payment Type 2 Value element (1 byte)
    public int payment2ValueOffset = 0;
    // Position in line of Payment Type 3 Name element (1 byte)
    public int payment3TextOffset = 0;
    // Position in line of Payment Type 3 Value element (1 byte)
    public int payment3ValueOffset = 0;
    // Position in line of Payment Type 4 Name element (1 byte)
    public int payment4TextOffset = 0;
    // Position in line of Payment Type 4 Value element (1 byte)
    public int payment4ValueOffset = 0;
    // Position in line of «CHANGE» element (1 byte)
    public int changeTextOffset = 0;
    // Position in line of Change Value element (1 byte)
    public int changeValueOffset = 0;
    // Position in line of Tax 1 Name element (1 byte)
    public int tax1TextOffset = 0;
    // Position in line of Tax 1 Turnover Value element (1 byte)
    public int tax1AmountOffset = 0;
    // Position in line of Tax 1 Rate element (1 byte)
    public int tax1RateOffset = 0;
    // Position in line of Tax 1 Value element (1 byte)
    public int tax1ValueOffset = 0;
    // Position in line of Tax 2 Name element (1 byte)
    public int tax2TextOffset = 0;
    // Position in line of Tax 2 Turnover Value element (1 byte)
    public int tax2AmountOffset = 0;
    // Position in line of Tax 2 Rate element (1 byte)
    public int tax2RateOffset = 0;
    // Position in line of Tax 2 Value element (1 byte)
    public int tax2ValueOffset = 0;
    // Position in line of Tax 3 Name element (1 byte)
    public int tax3TextOffset = 0;
    // Position in line of Tax 3 Turnover Value element (1 byte)
    public int tax3AmountOffset = 0;
    // Position in line of Tax 3 Rate element (1 byte)
    public int tax3RateOffset = 0;
    // Position in line of Tax 3 Value element (1 byte)
    public int tax3ValueOffset = 0;
    // Position in line of Tax 4 Name element (1 byte)
    public int tax4TextOffset = 0;
    // Position in line of Tax 4 Turnover Value element (1 byte)
    public int tax4AmountOffset = 0;
    // Position in line of Tax 4 Rate element (1 byte)
    public int tax4RateOffset = 0;
    // Position in line of Tax 4 Value element (1 byte)
    public int tax4ValueOffset = 0;
    // Position in line of «SUBTOTAL» element (1 byte)
    public int subtotalTextOffset = 0;
    // Position in line of Receipt Subtotal Before Discount/Surcharge Value
    // element (1 byte)
    public int subtotalValueOffset = 0;
    // Position in line of «DISCOUNT XX.XX%» element (1 byte)
    public int discountTextOffset = 0;
    // Position in line of Receipt Discount Value element (1 byte)
    public int discountValueOffset = 0;

    /** Creates a new instance of SlipCloseParams */
    public SlipCloseParams() {
    }
}
