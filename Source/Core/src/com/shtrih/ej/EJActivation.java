/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shtrih.ej;

/**
 * @author V.Kravtsov
 */
public class EJActivation {
    private String serial = "";
    private String date = "";
    private String time = "";

    public EJActivation() {
    }

    public String getEJSerial() {
        return serial;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setEJSerial(String EJSerial) {
        this.serial = EJSerial;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
