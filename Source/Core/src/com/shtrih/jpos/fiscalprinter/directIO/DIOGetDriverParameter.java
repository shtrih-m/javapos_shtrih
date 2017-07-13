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

public class DIOGetDriverParameter extends DIOItem {

    public DIOGetDriverParameter(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);

        switch (data[0]) {
            case SmFptrConst.SMFPTR_DIO_PARAM_REPORT_DEVICE:
                ((int[]) object)[0] = getParams().reportDevice;
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_REPORT_TYPE:
                ((int[]) object)[0] = getParams().reportType;
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_NUMHEADERLINES:
                ((int[]) object)[0] = getParams().numHeaderLines;
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_NUMTRAILERLINES:
                ((int[]) object)[0] = getParams().numTrailerLines;
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_POLL_ENABLED:
                ((boolean[]) object)[0] = getParams().pollEnabled;
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_CUT_MODE:
                ((int[]) object)[0] = service.getParams().cutMode;
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_FONT_NUMBER:
                ((int[]) object)[0] = service.getFontNumber();
                break;
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_SYS_PASSWORD:
                ((int[]) object)[0] = service.getPrinter().getSysPassword();
                break;
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_USR_PASSWORD:
                ((int[]) object)[0] = service.getPrinter().getUsrPassword();
                break;

            case  SmFptrConst.SMFPTR_DIO_PARAM_TAX_PASSWORD:
                ((int[]) object)[0] = service.getPrinter().getTaxPassword();
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_0:
                ((long[]) object)[0] = service.getParams().taxValue[0];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_1:
                ((long[]) object)[0] = service.getParams().taxValue[1];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_2:
                ((long[]) object)[0] = service.getParams().taxValue[2];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_3:
                ((long[]) object)[0] = service.getParams().taxValue[3];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_4:
                ((long[]) object)[0] = service.getParams().taxValue[4];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_VALUE_5:
                ((long[]) object)[0] = service.getParams().taxValue[5];
                break;
                
            case SmFptrConst.SMFPTR_DIO_PARAM_TAX_SYSTEM:
                ((int[]) object)[0] = service.getParams().taxSystem;
                break;
               
        }
    }
}
