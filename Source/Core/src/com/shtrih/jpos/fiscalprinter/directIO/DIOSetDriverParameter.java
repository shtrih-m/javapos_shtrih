/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;

public class DIOSetDriverParameter extends DIOItem {

    public DIOSetDriverParameter(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);

        switch (data[0]) {
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
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_USR_PASSWORD:
                service.getPrinter().setUsrPassword(((int[]) object)[0]);
                break;
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_SYS_PASSWORD:
                service.getPrinter().setSysPassword(((int[]) object)[0]);
                break;
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_TAX_PASSWORD:
                service.getPrinter().setTaxPassword(((int[]) object)[0]);
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_0:
                service.getParams().taxValue[0] = ((long[]) object)[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_1:
                service.getParams().taxValue[1] = ((long[]) object)[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_2:
                service.getParams().taxValue[2] = ((long[]) object)[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_3:
                service.getParams().taxValue[3] = ((long[]) object)[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_4:
                service.getParams().taxValue[4] = ((long[]) object)[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_5:
                service.getParams().taxValue[5] = ((long[]) object)[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_SYSTEM:
                service.getParams().taxSystem = ((int[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_ITEM_TOTAL_AMOUNT:
                service.getParams().itemTotalAmount = ((int[]) object)[0];
                break;
                
        }
    }
}
