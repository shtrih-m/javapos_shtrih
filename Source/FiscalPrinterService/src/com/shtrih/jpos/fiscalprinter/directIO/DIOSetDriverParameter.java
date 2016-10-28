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
        }
    }
}
