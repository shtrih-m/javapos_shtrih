/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;

/**
 * @author V.Kravtsov
 */
public final class ProtocolFactory {

    private ProtocolFactory() {
    }

    public static PrinterProtocol getProtocol(FptrParameters params,
            PrinterPort port) {
        PrinterProtocol result;
        if (params.protocolType == SmFptrConst.SMFPTR_PROTOCOL_1) {
            PrinterProtocol_1 protocol1 = new PrinterProtocol_1(port);
            protocol1.setByteTimeout(params.getByteTimeout());
            protocol1.setMaxEnqNumber(params.maxEnqNumber);
            protocol1.setMaxNakCommandNumber(params.maxNakCommandNumber);
            protocol1.setMaxNakAnswerNumber(params.maxNakAnswerNumber);
            protocol1.setMaxAckNumber(params.maxAckNumber);
            protocol1.setMaxRepeatCount(params.maxRepeatCount);
            return protocol1;
        } else {
            PrinterProtocol_2 protocol2 = new PrinterProtocol_2(port);
            protocol2.setByteTimeout(params.getByteTimeout());
            protocol2.setMaxRepeatCount(params.maxRepeatCount);
            return protocol2;
        }
    }

}
