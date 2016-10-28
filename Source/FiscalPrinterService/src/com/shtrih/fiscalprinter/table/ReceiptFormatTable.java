/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.table;

/**
 *
 * @author V.Kravtsov
 */
public class ReceiptFormatTable {
    
    class FieldFormat
    {
        private final int line;
        private final int offset;
        private final int size;
        private final int alignment;
        
        public FieldFormat(){
            line = 0;
            offset = 0;
            size = 0;
            alignment = 0;
        }
        
        public FieldFormat(int line, int offset, int size, int alignment)
        {
            this.line = line;
            this.offset = offset;
            this.size = size;
            this.alignment = alignment;
        }
    }
            
    private FieldFormat itemName; // мюхлемнбюмхе б ноепюжхх
    private FieldFormat itemQuantity; // йнкхвеярбн X жемс б ноепюжхх
    private FieldFormat itemDepartment; // яейжхъ б ноепюжхх
    private FieldFormat itemAmount; // ярнхлнярэ б ноепюжхх
    private FieldFormat itemVoidText; // мюдохяэ ярнпмн б ноепюжхх
    private FieldFormat discountText; // рейяр б яйхдйе
    private FieldFormat discountCaption; // мюдохяэ яйхдйю
    private FieldFormat discountAmount; // ясллю яйхдйх
    private FieldFormat chargeText; // рейяр б мюдаюбйе
    private FieldFormat chargeName; // мюдохяэ мюдаюбйю
    private FieldFormat chargeAmount; // ясллю мюдаюбйх
    private FieldFormat discountVoidText; // рейяр б ярнпмн яйхдйх
    private FieldFormat discountVoidName; // мюдохяэ ярнпмн яйхдйх
    private FieldFormat discountVoidAmount; // ясллю ярнпмн яйхдйх
    private FieldFormat chargeVoidText; // рейяр б ярнпмн мюдаюбйх
    private FieldFormat chargeVoidName; // мюдохяэ ярнпмн мюдаюбйх
    private FieldFormat chargeVoidAmount; // ясллю ярнпмн мюдаюбйх
}
