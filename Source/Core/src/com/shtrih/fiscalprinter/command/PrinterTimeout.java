/*
 * PrinterTimeout.java
 *
 * Created on April 2 2008, 21:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

/*****************************************************************************
 * FP supports for Port 0 baud rates of 2400, 4800, 9600, 19200, 38400, 57600,
 * 115200 bauds which corresponds to codes of 0 to 6. For other FP ports baud
 * rates range can be smaller, thus if any port does not support some baud rate
 * FP reports an error. Inter-character time out is not a linear parameter. Time
 * out value range [0..255] is divided into three sub-ranges: 1. [0..150] – each
 * unit equals to 1 ms which means that this range sets inter-character time out
 * value from 0 to 150 ms 2. [151..249] – each unit equals to 150 ms which means
 * that this range sets inter-character time out values from 300 ms to 15 sec 3.
 * [250..255] – each unit equals to 15 sec which means that this range sets
 * inter-character time out values from 30 to 105 sec. By default all ports have
 * the following preset parameters: speed 4800 baud with 100 ms of
 * inter-character time out. If data exchange parameters are to be set for the
 * port through which communication is currently done, than ACK and answer to
 * the command are transmitted with the old parameters.
 *****************************************************************************/

public class PrinterTimeout {
    /** Creates a new instance of PrinterTimeout */
    private PrinterTimeout() {
    }

    public static int getTimeoutCode(int value) {
        if ((value >= 0) && (value <= 150)) {
            return value;
        }
        if ((value > 150) && (value <= 15000)) {
            return ((value / 150) + 149);
        }
        return ((value / 15000) + 248);
    }

    public static int getTimeoutValue(int value) {
        if ((value >= 0) && (value <= 150)) {
            return value;
        }
        if ((value > 150) && (value <= 249)) {
            return (value - 149) * 150;
        }
        return (value - 248) * 15000;
    }
}
