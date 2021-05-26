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
/**
 * **************************************************************************
 * Принять или отвергнуть введенный код маркировки FF69H
 * Код команды FF69h. Длина сообщения: 7 байт.
 * Пароль оператора: 4 байта
 * Решение : 1 байт. 0 – отвергнуть, 1 – принять.
 * 
 * Команду необходимо подавать после проверки каждого КМ.
 * Ответ: FF69h	Длина сообщения: 2 байта.
 * Код ошибки: 1 байт
 * Результат проверки (1 байт) тег 2106
 *
 ***************************************************************************
 */
public final class FSAcceptMC extends PrinterCommand {

    // Action values
    public static final int ActionDeny = 0;
    public static final int ActionAccept = 1;
    public static final int ActionClearBuffer = 2;
    
    // in
    private int password;
    private int action;
    // out
    private int errorCode;

    /**
     * Creates a new instance of ConfirmDate
     */
    public FSAcceptMC() {
        super();
    }

    public final int getCode() {
        return 0xFF69;
    }

    public final String getText() {
        return "FS: Accept or reject marking code";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getAction());
    }

    public final void decode(CommandInputStream in) throws Exception 
    {
        setErrorCode(in.readByte());
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @return the action
     */
    public int getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
