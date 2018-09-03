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
 * Команду необходимо подавать после проверки каждого КМ.
 * Ответ: FF69h	Длина сообщения: 1 байт.
 * Код ошибки: 1 байт
 *
 ***************************************************************************
 */
public final class FSAcceptItemCode extends PrinterCommand {

    // in
    private int password;
    private int action;

    /**
     * Creates a new instance of ConfirmDate
     */
    public FSAcceptItemCode() {
        super();
    }

    public final int getCode() {
        return 0xFF69;
    }

    public final String getText() {
        return "FS: Accept or reject item marking";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getAction());
    }

    public final void decode(CommandInputStream in) throws Exception {
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

}
