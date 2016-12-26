/*
 * InternalCheckHelthReport.java
 *
 * Created on 8 Июль 2010 г., 12:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import java.util.Vector;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.FiscalMemoryFlags;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.fiscalprinter.command.PrinterFlags;
import com.shtrih.fiscalprinter.model.PrinterModel;
import com.shtrih.util.Localizer;

public class InternalCheckHelthReport {

    public static String lineSeparator = System.getProperty("line.separator");

    /** Creates a new instance of InternalCheckHelthReport */
    private InternalCheckHelthReport() {
    }

    public static String getReport(SMFiscalPrinter printer) throws Exception {
        Vector lines = new Vector();
        LongPrinterStatus status = printer.readLongStatus();
        PrinterFlags flags = status.getPrinterFlags();
        FiscalMemoryFlags fmFlags = status.getFiscalMemoryFlags();
        PrinterModel model = printer.getModel();

        if (model.getCapRecPresent()) {
            if (model.getCapRecEmptySensor() && (flags.isRecEmpty())) {
                lines.add(Localizer.getString(Localizer.RecPaperEmpty));
            }

            if (model.getCapRecNearEndSensor() && (flags.isRecNearEnd())) {
                lines.add(Localizer.getString(Localizer.RecPaperNearEnd));
            }

            if (model.getCapRecLeverSensor() && (!flags.isRecLeverUp())) {
                lines.add(Localizer.getString(Localizer.RecLeverUp));
            }
        }

        if (model.getCapJrnPresent()) {
            if (model.getCapJrnEmptySensor() && flags.isJrnEmpty()) {
                lines.add(Localizer.getString(Localizer.JrnPaperEmpty));
            }

            if (model.getCapJrnNearEndSensor() && flags.isJrnNearEnd()) {
                lines.add(Localizer.getString(Localizer.JrnPaperNearEnd));
            }

            if (model.getCapJrnLeverSensor() && flags.isJrnLeverUp()) {
                lines.add(Localizer.getString(Localizer.JrnLeverUp));
            }
        }

        if (model.getCapEJPresent()) {
            if (flags.isEJPresent() && flags.isEJNearEnd()) {
                lines.add(Localizer.getString(Localizer.EJNearFull));
            }
        }

        if (model.getCapFMPresent()) {
            if (fmFlags.isFmOverflow()) {
                lines.add(Localizer.getString(Localizer.FMOverflow));
            }

            if (fmFlags.isBatteryLow()) {
                lines.add(Localizer.getString(Localizer.FMLowBattery));
            }

            if (fmFlags.isFmLastRecordCorrupted()) {
                lines.add(Localizer.getString(Localizer.FMLastRecordCorrupted));
            }

            if (fmFlags.isEndDayRequired()) {
                lines.add(Localizer.getString(Localizer.FMEndDayRequired));
            }
        }

        String result = Localizer.getString(Localizer.InternalHealthCheck)
                + ": " + lineSeparator;

        if (lines.isEmpty()) {
            result += Localizer.getString(Localizer.NoErrors);
        } else {
            while (lines.size() > 0) {
                result += lines.get(0) + lineSeparator;
                lines.remove(0);
            }
        }
        return result;
    }
}
