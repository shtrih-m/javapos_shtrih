/*
 * PrintBarcode.java
 *
 * Created on March 7 2008, 14:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/**
 * ***************************************************************************
 * Печать многомерного штрих-кода Команда: DEH. Длина сообщения: 15 байт. "
 * Пароль (4 байта) "	Тип штрих-кода (1 байт) "	Длина данных штрих-кода (2
 * байта) 1...70891 "	Номер начального блока данных (1 байт) 0...127 "	Параметр
 * 1 (1 байт) "	Параметр 2 (1 байт) "	Параметр 3 (1 байт) "	Параметр 4 (1 байт)
 * "	Параметр 5 (1 байт) "	Выравнивание (1 байт) Ответ:	DEH. Длина сообщения: 3
 * байт или 122 байт. "	Код ошибки (1 байт) "	Порядковый номер оператора (1
 * байт) 1…30 "	Параметр 1 (1 байт) 2 "	Параметр 2 (1 байт) 2 "	Параметр 3 (1
 * байт) 2 "	Параметр 4 (1 байт) 2 "	Параметр 5 (1 байт) 2 "	Размер штрих-кода
 * (горизонтальный) в точках (2 байта) 2 "	Размер штрих-кода (вертикальный) в
 * точках (2 байта) 2
 *
 * Тип штрих-кода	Штрих-код 0	PDF 417 1	DATAMATRIX 2	AZTEC 3	QR code 1312	QR
 * code2
 *
 * Номер параметра	PDF 417	DATAMATRIX	AZTEC	QR Code 1	Number of columns	Encoding
 * scheme	Encoding scheme	Version, 0=auto; 40 (max) 2	Number of rows	Rotate	-
 * Mask; 8 (max) 3	Width of module	Dot size	Dot size	Dot size; 3...8 4	Module
 * height	Symbol size	Symbol size	- 5	Error correction level	-	Error correction
 * level	Error correction level; 0...3=L,M,Q,H
 *
 * Выравнивание	Тип выравнивания 0	По левому краю 1	По центру 2	По правому краю
 * Примечания: 1 - в зависимости от версии печатаемого QR кода и типа данных; 2
 * - для типа штрих-кода (QR код).
 ****************************************************************************
 */
public final class PrintBarcode3 extends PrinterCommand {

    // in
    private int password;
    private int barcodeType;
    private int dataLength;
    private int blockNumber;
    private int inParameter1;
    private int inParameter2;
    private int inParameter3;
    private int inParameter4;
    private int inParameter5;
    private int alignment;
    // out
    private int operatorNumber;
    private int outParameter1;
    private int outParameter2;
    private int outParameter3;
    private int outParameter4;
    private int outParameter5;
    private int barcodeWidth;
    private int barcodeHeight;

    //////////////////////////////////////////////////////////////////////////
    // Barcode type constants
    public static final int PDF_417 = 0;
    public static final int DATAMATRIX = 1;
    public static final int AZTEC = 2;
    public static final int QRCODE = 3;

    //////////////////////////////////////////////////////////////////////////
    // Alignment constants
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;

    public PrintBarcode3() {
        super();
    }

    public final int getCode() {
        return 0xDE;
    }

    public final String getText() {
        return "Print barcode 3";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getBarcodeType());
        out.writeShort(getDataLength());
        out.writeByte(getBlockNumber());
        out.writeByte(getInParameter1());
        out.writeByte(getInParameter2());
        out.writeByte(getInParameter3());
        out.writeByte(getInParameter4());
        out.writeByte(getInParameter5());
        out.writeByte(getAlignment());
    }

    public final void decode(CommandInputStream in) throws Exception {
        operatorNumber = in.readByte();
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
     * @return the barcodeType
     */
    public int getBarcodeType() {
        return barcodeType;
    }

    /**
     * @param barcodeType the barcodeType to set
     */
    public void setBarcodeType(int barcodeType) {
        this.barcodeType = barcodeType;
    }

    /**
     * @return the dataLength
     */
    public int getDataLength() {
        return dataLength;
    }

    /**
     * @param dataLength the dataLength to set
     */
    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    /**
     * @return the blockNumber
     */
    public int getBlockNumber() {
        return blockNumber;
    }

    /**
     * @param blockNumber the blockNumber to set
     */
    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    /**
     * @return the inParameter1
     */
    public int getInParameter1() {
        return inParameter1;
    }

    /**
     * @param inParameter1 the inParameter1 to set
     */
    public void setInParameter1(int inParameter1) {
        this.inParameter1 = inParameter1;
    }

    /**
     * @return the inParameter2
     */
    public int getInParameter2() {
        return inParameter2;
    }

    /**
     * @param inParameter2 the inParameter2 to set
     */
    public void setInParameter2(int inParameter2) {
        this.inParameter2 = inParameter2;
    }

    /**
     * @return the inParameter3
     */
    public int getInParameter3() {
        return inParameter3;
    }

    /**
     * @param inParameter3 the inParameter3 to set
     */
    public void setInParameter3(int inParameter3) {
        this.inParameter3 = inParameter3;
    }

    /**
     * @return the inParameter4
     */
    public int getInParameter4() {
        return inParameter4;
    }

    /**
     * @param inParameter4 the inParameter4 to set
     */
    public void setInParameter4(int inParameter4) {
        this.inParameter4 = inParameter4;
    }

    /**
     * @return the inParameter5
     */
    public int getInParameter5() {
        return inParameter5;
    }

    /**
     * @param inParameter5 the inParameter5 to set
     */
    public void setInParameter5(int inParameter5) {
        this.inParameter5 = inParameter5;
    }

    /**
     * @return the alignment
     */
    public int getAlignment() {
        return alignment;
    }

    /**
     * @param alignment the alignment to set
     */
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    /**
     * @return the operatorNumber
     */
    public int getOperatorNumber() {
        return operatorNumber;
    }

    /**
     * @return the outParameter1
     */
    public int getOutParameter1() {
        return outParameter1;
    }

    /**
     * @return the outParameter2
     */
    public int getOutParameter2() {
        return outParameter2;
    }

    /**
     * @return the outParameter3
     */
    public int getOutParameter3() {
        return outParameter3;
    }

    /**
     * @return the outParameter4
     */
    public int getOutParameter4() {
        return outParameter4;
    }

    /**
     * @return the outParameter5
     */
    public int getOutParameter5() {
        return outParameter5;
    }

    /**
     * @return the barcodeWidth
     */
    public int getBarcodeWidth() {
        return barcodeWidth;
    }

    /**
     * @return the barcodeHeight
     */
    public int getBarcodeHeight() {
        return barcodeHeight;
    }

}
