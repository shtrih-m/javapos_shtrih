/*
 * StatusUpdateEventHelper.java
 *
 * Created on July 22 2008, 11:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos;

/**
 *
 * @author V.Kravtsov
 */

import jpos.FiscalPrinterConst;
import jpos.JposConst;

public class StatusUpdateEventHelper {

    /**
     * Creates a new instance of StatusUpdateEventHelper
     */
    private StatusUpdateEventHelper() {
    }

    public static String getName(int value) {
        switch (value) {
        // JPOS
            case JposConst.JPOS_SUE_POWER_ONLINE:
                return "JPOS_SUE_POWER_ONLINE";

            case JposConst.JPOS_SUE_POWER_OFF:
                return "JPOS_SUE_POWER_OFF";

            case JposConst.JPOS_SUE_POWER_OFFLINE:
                return "JPOS_SUE_POWER_OFFLINE";

            case JposConst.JPOS_SUE_POWER_OFF_OFFLINE:
                return "JPOS_SUE_POWER_OFF_OFFLINE";

            case JposConst.JPOS_SUE_UF_PROGRESS:
                return "JPOS_SUE_UF_PROGRESS";

            case JposConst.JPOS_SUE_UF_COMPLETE:
                return "JPOS_SUE_UF_COMPLETE";

            case JposConst.JPOS_SUE_UF_FAILED_DEV_OK:
                return "JPOS_SUE_UF_FAILED_DEV_OK";

            case JposConst.JPOS_SUE_UF_FAILED_DEV_UNRECOVERABLE:
                return "JPOS_SUE_UF_FAILED_DEV_UNRECOVERABLE";

            case JposConst.JPOS_SUE_UF_FAILED_DEV_NEEDS_FIRMWARE:
                return "JPOS_SUE_UF_FAILED_DEV_NEEDS_FIRMWARE";

            case JposConst.JPOS_SUE_UF_FAILED_DEV_UNKNOWN:
                return "JPOS_SUE_UF_FAILED_DEV_UNKNOWN";

            case JposConst.JPOS_SUE_UF_COMPLETE_DEV_NOT_RESTORED:
                return "JPOS_SUE_UF_COMPLETE_DEV_NOT_RESTORED";

                // FPTR
            case FiscalPrinterConst.FPTR_SUE_COVER_OPEN:
                return "FPTR_SUE_COVER_OPEN";

            case FiscalPrinterConst.FPTR_SUE_COVER_OK:
                return "FPTR_SUE_COVER_OK";

            case FiscalPrinterConst.FPTR_SUE_JRN_EMPTY:
                return "FPTR_SUE_JRN_EMPTY";

            case FiscalPrinterConst.FPTR_SUE_JRN_NEAREMPTY:
                return "FPTR_SUE_JRN_NEAREMPTY";

            case FiscalPrinterConst.FPTR_SUE_JRN_PAPEROK:
                return "FPTR_SUE_JRN_PAPEROK";

            case FiscalPrinterConst.FPTR_SUE_REC_EMPTY:
                return "FPTR_SUE_REC_EMPTY";

            case FiscalPrinterConst.FPTR_SUE_REC_NEAREMPTY:
                return "FPTR_SUE_REC_NEAREMPTY";

            case FiscalPrinterConst.FPTR_SUE_REC_PAPEROK:
                return "FPTR_SUE_REC_PAPEROK";

            case FiscalPrinterConst.FPTR_SUE_SLP_EMPTY:
                return "FPTR_SUE_SLP_EMPTY";

            case FiscalPrinterConst.FPTR_SUE_SLP_NEAREMPTY:
                return "FPTR_SUE_SLP_NEAREMPTY";

            case FiscalPrinterConst.FPTR_SUE_SLP_PAPEROK:
                return "FPTR_SUE_SLP_PAPEROK";

            case FiscalPrinterConst.FPTR_SUE_IDLE:
                return "FPTR_SUE_IDLE";

            case FiscalPrinterConst.FPTR_SUE_JRN_COVER_OPEN:
                return "FPTR_SUE_JRN_COVER_OPEN";

            case FiscalPrinterConst.FPTR_SUE_JRN_COVER_OK:
                return "FPTR_SUE_JRN_COVER_OK";

            case FiscalPrinterConst.FPTR_SUE_REC_COVER_OPEN:
                return "FPTR_SUE_REC_COVER_OPEN";

            case FiscalPrinterConst.FPTR_SUE_REC_COVER_OK:
                return "FPTR_SUE_REC_COVER_OK";

            case FiscalPrinterConst.FPTR_SUE_SLP_COVER_OPEN:
                return "FPTR_SUE_SLP_COVER_OPEN";

            case FiscalPrinterConst.FPTR_SUE_SLP_COVER_OK:
                return "FPTR_SUE_SLP_COVER_OK";

            default:
                return "Unknown value";
        }
    }
}
