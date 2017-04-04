/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.ej.EJDate;
import com.shtrih.util.BitUtils;

/**
 *
 * @author V.Kravtsov
 */


/**
Сформировать чек коррекции V2 FF4AH
Код команды FF4Ah . Длина сообщения: 69 байт.
Пароль системного администратора: 4 байта
Тип коррекции :1 байт  «0» - самостоятельно, «1» - по предписанию
Признак расчета:1байт («1» (коррекция прихода, операция, при которой пользователь вносит денежные
средства коррекции) и «3» (коррекция расхода, операция, при которой пользователь изымает денежные
средства).
Сумма расчёта :5 байт
Сумма по чеку наличными:5 байт
Сумма по чеку электронными:5 байт
Сумма по чеку предоплатой:5 байт
Сумма по чеку постоплатой:5 байт
Сумма по чеку встречным представлением:5 байт
Сумма НДС 18%:5 байт
Сумма НДС 10%:5 байт
Сумма расчёта по ставке 0%:5 байт
Сумма расчёта по чеку без НДС:5 байт
Сумма расчёта по расч. ставке 18/118:5 байт
Сумма расчёта по расч. ставке 10/110:5 байт
Применяемая система налогообложения:1байт

Ответ: FF4Ah Длина сообщения: 11 байт.
Код ошибки: 1 байт
Номер чека: 2 байта
Номер ФД: 4 байта
Фискальный признак: 4 байт
*/


public class FSPrintCorrectionReceipt2 extends PrinterCommand {

    // in
    private int sysPassword = 0; // System sdministrator password (4 bytes)
    private int correctionType = 0;
    private int paymentType = 0;
    private long total = 0; 
    private long payments[] = new long[5]; 
    private long taxTotals[] = new long[6]; 
    private int taxSystem = 0;
    // out
    private int receiptNumber;
    private int documentNumber;
    private int documentDigest;
     

    public FSPrintCorrectionReceipt2() {
    }

    public final int getCode() {
        return 0xFF4A;
    }

    public final String getText() {
        return "FS: print correction receipt 2";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
        out.writeByte(correctionType);
        out.writeByte(paymentType);
        out.writeLong(total, 5);
        out.writeLong(payments[0], 5);
        out.writeLong(payments[1], 5);
        out.writeLong(payments[2], 5);
        out.writeLong(payments[3], 5);
        out.writeLong(payments[4], 5);
        out.writeLong(taxTotals[0], 5);
        out.writeLong(taxTotals[1], 5);
        out.writeLong(taxTotals[2], 5);
        out.writeLong(taxTotals[3], 5);
        out.writeLong(taxTotals[4], 5);
        out.writeLong(taxTotals[5], 5);
        out.writeByte(taxSystem);
    }

    public void decode(CommandInputStream in) throws Exception {
        setReceiptNumber(in.readShort());
        setDocumentNumber(in.readInt());
        setDocumentDigest(in.readInt());
    }

    /**
     * @return the sysPassword
     */
    public int getSysPassword() {
        return sysPassword;
    }

    /**
     * @param sysPassword the sysPassword to set
     */
    public void setSysPassword(int sysPassword) {
        this.sysPassword = sysPassword;
    }

    /**
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * @return the receiptNumber
     */
    public int getReceiptNumber() {
        return receiptNumber;
    }

    /**
     * @param receiptNumber the receiptNumber to set
     */
    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    /**
     * @return the documentNumber
     */
    public int getDocumentNumber() {
        return documentNumber;
    }

    /**
     * @param documentNumber the documentNumber to set
     */
    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * @return the documentDigest
     */
    public int getDocumentDigest() {
        return documentDigest;
    }

    /**
     * @param documentDigest the documentDigest to set
     */
    public void setDocumentDigest(int documentDigest) {
        this.documentDigest = documentDigest;
    }

    /**
     * @return the correctionType
     */
    public int getCorrectionType() {
        return correctionType;
    }

    /**
     * @param correctionType the correctionType to set
     */
    public void setCorrectionType(int correctionType) {
        this.correctionType = correctionType;
    }

    /**
     * @return the paymentType
     */
    public int getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the payments
     */
    public long[] getPayments() {
        return payments;
    }

    /**
     * @param payments the payments to set
     */
    public void setPayments(long[] payments) {
        this.payments = payments;
    }

    /**
     * @return the taxTotals
     */
    public long[] getTaxTotals() {
        return taxTotals;
    }

    /**
     * @param taxTotals the taxTotals to set
     */
    public void setTaxTotals(long[] taxTotals) {
        this.taxTotals = taxTotals;
    }

    /**
     * @return the taxSystem
     */
    public int getTaxSystem() {
        return taxSystem;
    }

    /**
     * @param taxSystem the taxSystem to set
     */
    public void setTaxSystem(int taxSystem) {
        this.taxSystem = taxSystem;
    }
}
