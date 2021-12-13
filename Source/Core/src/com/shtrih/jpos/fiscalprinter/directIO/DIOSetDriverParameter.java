/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 * @author V.Kravtsov
 */

import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FirmwareUpdateObserver;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.util.CompositeLogger;

public class DIOSetDriverParameter extends DIOItem {

    private static CompositeLogger logger = CompositeLogger.getLogger(DIOSetDriverParameter.class);

    public DIOSetDriverParameter(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        int paramId = data[0];
        
        logger.debug("directIo(SMFPTR_DIO_SET_DRIVER_PARAMETER, " + 
            SmFptrConst.getParameterName(paramId) + ", '" + 
                object.toString() + "')");
        
        switch (paramId) {
            case SmFptrConst.SMFPTR_DIO_PARAM_REPORT_DEVICE:
                service.getParams().reportDevice = ((int[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_REPORT_TYPE:
                service.getParams().reportType = ((int[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_NUMHEADERLINES:
                service.setNumHeaderLines(((int[]) object)[0]);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_NUMTRAILERLINES:
                service.setNumTrailerLines(((int[]) object)[0]);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_POLL_ENABLED:
                service.setPollEnabled(((boolean[]) object)[0]);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_CUT_MODE:
                getParams().cutMode = (((int[]) object)[0]);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_FONT_NUMBER:
                service.setFontNumber(((int[]) object)[0]);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_USR_PASSWORD:
                service.getPrinter().setUsrPassword(((int[]) object)[0]);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_SYS_PASSWORD:
                service.getPrinter().setSysPassword(((int[]) object)[0]);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_PASSWORD:
                service.getPrinter().setTaxPassword(((int[]) object)[0]);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_0:
                service.getParams().taxAmount[0] = ((long[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_1:
                service.getParams().taxAmount[1] = ((long[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_2:
                service.getParams().taxAmount[2] = ((long[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_3:
                service.getParams().taxAmount[3] = ((long[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_4:
                service.getParams().taxAmount[4] = ((long[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_5:
                service.getParams().taxAmount[5] = ((long[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_SYSTEM:
                service.getParams().taxSystem = ((int[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_ITEM_TOTAL_AMOUNT:
                service.getParams().itemTotalAmount = (long) ((int[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_ITEM_PAYMENT_TYPE:
                service.getParams().paymentType = (byte) ((int[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_ITEM_SUBJECT_TYPE:
                service.getParams().subjectType = (byte) ((int[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_FIRMWARE_UPDATE_OBSERVER:
                service.setFirmwareUpdateObserver((FirmwareUpdateObserver) object);
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_NEW_ITEM_STATUS:
                service.getParams().newItemStatus = ((int[]) object)[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_ITEM_CHECK_MODE:
                service.getParams().itemCheckMode = ((int[]) object)[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_ITEM_MARK_TYPE:
                service.getParams().itemMarkType = ((int[]) object)[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_ITEM_TAX_AMOUNT:
                service.getParams().itemTaxAmount = (long) ((int[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_ITEM_UNIT:
                service.getParams().itemUnit = ((int[]) object)[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_AMOUNT_FACTOR:
                service.getParams().amountFactor = ((double[]) object)[0];
                break;
   
            case SmFptrConst.SMFPTR_DIO_PARAM_QUANTITY_FACTOR:
                service.getParams().quantityFactor = ((double[]) object)[0];
                break;
                
        }
    }
}
