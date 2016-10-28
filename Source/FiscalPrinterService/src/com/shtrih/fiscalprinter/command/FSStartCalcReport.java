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
������ ������������ ������ � ��������� �������� FF37H 
��� ������� FF37h . ����� ���������: 6 ����.
������ ���������� ��������������: 4 �����
�����:	    FF37h ����� ���������: 1 ����.
��� ������: 1 ����
*/

public class FSStartCalcReport extends PrinterCommand {

    // in
    private int sysPassword = 0; // System sdministrator password (4 bytes)

    public FSStartCalcReport() {
    }

    public final int getCode() {
        return 0xFF37;
    }

    public final String getText() {
        return "Fiscal storage: start calculation report";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getSysPassword());
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
