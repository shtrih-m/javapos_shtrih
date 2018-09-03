/*
 * ConfirmDate.java
 *
 * Created on 2 April 2008, 21:14
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
    Привязка  маркированного товара к позиции FF67H
    Код команды FF67h. Длина сообщения: 7 байт.
    Пароль оператора: 4 байта
    Длина кода маркировки: 1 байт
    (данные должны быть загружены командой 0xDD)
    ККТ проверяет был ли данный КМ проверен ранее. 
    Если был то КМ включается в состав предмета, независимо от результата 
    проверки. Если КМ ранее не проверялся, то будет возвращена ошибка и КМ 
    не будет привязан к предмету расчета.
    Данная команда должна вызываться после привязки всех тегов к предмету расчета.
    
    Ответ: FF67h	    Длина сообщения: 8 байт.
    Код ошибки: 1 байт
    Результат локальной проверки кода маркировки: 1 байт
    Код обработки пакета : 1 байт. 
    Разрешение на продажу товара от ИСМ : 1 байт.
    Статус КМ: 1 байт.
    Код ошибки от сервера КМ: 1 байт 
    Статус проверок сервера : 1 байт
    Тип символики: 1 байт
 ****************************************************************************/

public final class FSBindItemCode extends PrinterCommand {
    // in
    private int password;
    private int codeLength;
    // out
    private int localResultCode;
    private int processingCode;
    private int salePermission;
    private int serverItemStatus;
    private int serverResultCode;
    private int serverCheckStatus;
    private int symbolicType;

    /**
     * Creates a new instance of ConfirmDate
     */
    public FSBindItemCode() {
        super();
    }

    public final int getCode() {
        return 0xFF67;
    }

    public final String getText() {
        return "FS: Bind item marking";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getCodeLength());
    }

    public final void decode(CommandInputStream in) throws Exception {
        localResultCode = in.readByte();
        processingCode = in.readByte();
        salePermission = in.readByte();
        serverItemStatus = in.readByte();
        serverResultCode = in.readByte();
        serverCheckStatus = in.readByte();
        symbolicType = in.readByte();
   }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @return the codeLength
     */
    public int getCodeLength() {
        return codeLength;
    }

    /**
     * @param codeLength the codeLength to set
     */
    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    /**
     * @return the localResultCode
     */
    public int getLocalResultCode() {
        return localResultCode;
    }

    /**
     * @param localResultCode the localResultCode to set
     */
    public void setLocalResultCode(int localResultCode) {
        this.localResultCode = localResultCode;
    }

    /**
     * @return the processingCode
     */
    public int getProcessingCode() {
        return processingCode;
    }

    /**
     * @param processingCode the processingCode to set
     */
    public void setProcessingCode(int processingCode) {
        this.processingCode = processingCode;
    }

    /**
     * @return the salePermission
     */
    public int getSalePermission() {
        return salePermission;
    }

    /**
     * @param salePermission the salePermission to set
     */
    public void setSalePermission(int salePermission) {
        this.salePermission = salePermission;
    }

    /**
     * @return the serverItemStatus
     */
    public int getServerItemStatus() {
        return serverItemStatus;
    }

    /**
     * @param serverItemStatus the serverItemStatus to set
     */
    public void setServerItemStatus(int serverItemStatus) {
        this.serverItemStatus = serverItemStatus;
    }

    /**
     * @return the serverResultCode
     */
    public int getServerResultCode() {
        return serverResultCode;
    }

    /**
     * @param serverResultCode the serverResultCode to set
     */
    public void setServerResultCode(int serverResultCode) {
        this.serverResultCode = serverResultCode;
    }

    /**
     * @return the serverCheckStatus
     */
    public int getServerCheckStatus() {
        return serverCheckStatus;
    }

    /**
     * @param serverCheckStatus the serverCheckStatus to set
     */
    public void setServerCheckStatus(int serverCheckStatus) {
        this.serverCheckStatus = serverCheckStatus;
    }

    /**
     * @return the symbolicType
     */
    public int getSymbolicType() {
        return symbolicType;
    }

    /**
     * @param symbolicType the symbolicType to set
     */
    public void setSymbolicType(int symbolicType) {
        this.symbolicType = symbolicType;
    }

}
