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
            
    private FieldFormat itemName; // НАИМЕНОВАНИЕ В ОПЕРАЦИИ
    private FieldFormat itemQuantity; // КОЛИЧЕСТВО X ЦЕНУ В ОПЕРАЦИИ
    private FieldFormat itemDepartment; // СЕКЦИЯ В ОПЕРАЦИИ
    private FieldFormat itemAmount; // СТОИМОСТЬ В ОПЕРАЦИИ
    private FieldFormat itemVoidText; // НАДПИСЬ СТОРНО В ОПЕРАЦИИ
    private FieldFormat discountText; // ТЕКСТ В СКИДКЕ
    private FieldFormat discountCaption; // НАДПИСЬ СКИДКА
    private FieldFormat discountAmount; // СУММА СКИДКИ
    private FieldFormat chargeText; // ТЕКСТ В НАДБАВКЕ
    private FieldFormat chargeName; // НАДПИСЬ НАДБАВКА
    private FieldFormat chargeAmount; // СУММА НАДБАВКИ
    private FieldFormat discountVoidText; // ТЕКСТ В СТОРНО СКИДКИ
    private FieldFormat discountVoidName; // НАДПИСЬ СТОРНО СКИДКИ
    private FieldFormat discountVoidAmount; // СУММА СТОРНО СКИДКИ
    private FieldFormat chargeVoidText; // ТЕКСТ В СТОРНО НАДБАВКИ
    private FieldFormat chargeVoidName; // НАДПИСЬ СТОРНО НАДБАВКИ
    private FieldFormat chargeVoidAmount; // СУММА СТОРНО НАДБАВКИ
}
