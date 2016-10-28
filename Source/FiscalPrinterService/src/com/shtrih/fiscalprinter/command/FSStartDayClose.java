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
������ �������� ����� FF42H
��� ������� FF42h . ����� ���������: 6 ����.
    ������ ���������� ��������������: 4 �����
�����:	    FF42h ����� ���������: 1 ����.
    ��� ������: 1 ����
*/
public class FSStartDayClose extends PrinterCommand {

    // in
    private int sysPassword; // System sdministrator password (4 bytes)

    public FSStartDayClose() {
    }

    public final int getCode() {
        return 0xFF42;
    }

    public final String getText() {
        return "Fiscal storage: start day close";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(sysPassword);
    }

    public void decode(CommandInputStream in) throws Exception {
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
}
