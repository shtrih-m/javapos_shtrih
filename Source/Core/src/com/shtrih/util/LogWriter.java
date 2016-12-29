/*
 * LogWriter.java
 *
 * Created on 13 Июль 2010 г., 13:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.command.DeviceMetrics;
import com.shtrih.fiscalprinter.command.FiscalMemoryFlags;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.fiscalprinter.command.PrinterFlags;
import com.shtrih.fiscalprinter.command.ShortPrinterStatus;

public class LogWriter {

    private static String logSeparator = "------------------------------------------------------------";
    private static String[] boolToStr = {"[ ]", "[X]"};
    private static String[] batteryToStr = {"[ > 80% ]", "[ < 80% ]"};
    private static String[] recordToStr = {"[ correct ]", "[ corrupted ]"};
    private static String[] dayOpenedToStr = {"[ closed ]", "[ opened ]"};
    private static String[] dayOverToStr = {"[ < 24 hours ]", "[ > 24 hours ]"};
    private static String[] pointToStr = {"[0 digits]", "[2 digits]"};
    private static String[] leverToStr = {"[up]", "[down]"};
    private static String[] closedToStr = {"[closed]", "[opened]"};
    private static CompositeLogger logger = CompositeLogger.getLogger(LogWriter.class);

    /** Creates a new instance of LogWriter */
    private LogWriter() {
    }

    public static void writeSeparator() {
        logger.debug(logSeparator);
    }

    public static void write(DeviceMetrics data) {
        logger.debug(logSeparator);
        logger.debug("Printer parameters");
        logger.debug(logSeparator);
        logger.debug("Device type                    : "
                + String.valueOf(data.getDeviceType()) + "."
                + String.valueOf(data.getDeviceSubType()));

        logger.debug("Protocol version               : "
                + String.valueOf(data.getProtocolVersion()) + "."
                + String.valueOf(data.getProtocolSubVersion()));

        logger.debug("Model ID                       : "
                + String.valueOf(data.getModel()));

        logger.debug("Device language                : "
                + String.valueOf(data.getLanguage()));

        logger.debug("Device name                    : " + data.getDeviceName());
    }

    public static int boolToInt(boolean value) {
        if (value) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void write(FiscalMemoryFlags flags) {
        logger.debug(logSeparator);
        logger.debug("FM flags                       : 0x"
                + Hex.toHex(flags.getValue()));

        logger.debug(logSeparator);
        logger.debug("FM memory bank1                : "
                + boolToStr[boolToInt(flags.isFm1Present())]);

        logger.debug("FM memory bank2                : "
                + boolToStr[boolToInt(flags.isFm2Present())]);

        logger.debug("License present                : "
                + boolToStr[boolToInt(flags.isLicensePresent())]);

        logger.debug("FM overflow                    : "
                + boolToStr[boolToInt(flags.isFmOverflow())]);

        logger.debug("FM battery state               : "
                + batteryToStr[boolToInt(flags.isBatteryLow())]);

        logger.debug("Last FM record                 : "
                + recordToStr[boolToInt(flags.isFmLastRecordCorrupted())]);

        logger.debug("FM day                         : "
                + dayOpenedToStr[boolToInt(flags.isDayOpened())]);

        logger.debug("Day end required               : "
                + dayOverToStr[boolToInt(flags.isEndDayRequired())]);
    }

    public static void write(PrinterFlags flags) {
        logger.debug(logSeparator);
        logger.debug("Printer flags                  : 0x"
                + Hex.toHex(flags.getValue()));

        logger.debug(logSeparator);
        logger.debug("Journal paper near end         : "
                + boolToStr[boolToInt(flags.isRecNearEnd())]);

        logger.debug("Receipt paper near end         : "
                + boolToStr[boolToInt(flags.isRecNearEnd())]);

        logger.debug("Slip upper sensor              : "
                + boolToStr[boolToInt(flags.isSlpEmpty())]);

        logger.debug("Slip low sensor                : "
                + boolToStr[boolToInt(flags.isSlpNearEnd())]);

        logger.debug("Amount decimal point position  : "
                + pointToStr[boolToInt(flags.getAmountPointPosition())]);

        logger.debug("Electronic journal present     : "
                + boolToStr[boolToInt(flags.isEJPresent())]);

        logger.debug("Journal paper                  : "
                + boolToStr[boolToInt(flags.isJrnEmpty())]);

        logger.debug("Receipt paper                  : "
                + boolToStr[boolToInt(flags.isRecEmpty())]);

        logger.debug("Journal lever                  : "
                + leverToStr[boolToInt(flags.isJrnLeverUp())]);

        logger.debug("Receipt lever                  : "
                + leverToStr[boolToInt(flags.isRecLeverUp())]);

        logger.debug("Cover status                   : "
                + closedToStr[boolToInt(flags.isCoverOpened())]);

        logger.debug("Cash drawer                    : "
                + closedToStr[boolToInt(flags.isDrawerOpened())]);

        logger.debug("EJ is near end                 : "
                + boolToStr[boolToInt(flags.isEJNearEnd())]);
    }

    public static void write(ShortPrinterStatus status) {
        logger.debug(logSeparator);
        logger.debug("Short printer status");
        logger.debug(logSeparator);

        logger.debug("FM result code                 : "
                + String.valueOf(status.getFMResultCode()));

        logger.debug("EJ result code                 : "
                + String.valueOf(status.getEJResultCode()));

        logger.debug("Receipt operations             : "
                + String.valueOf(status.getReceiptOperations()));

        NumberFormat formatter = new DecimalFormat("#0.00");
        String text = formatter.format(status.getBatteryVoltage());
        logger.debug("Battery voltage                : " + text + " V");

        formatter = new DecimalFormat("#0.00");
        text = formatter.format(status.getPowerVoltage());
        logger.debug("Power voltage                  : " + text + " V");

        logger.debug("Operator number                : "
                + String.valueOf(status.getOperatorNumber()));
    }

    public static void write(LongPrinterStatus status) {
        logger.debug(logSeparator);
        logger.debug("Long printer status");
        logger.debug(logSeparator);
        logger.debug("Operator number                : "
                + String.valueOf(status.getOperatorNumber()));

        logger.debug("Printer firmware version       : "
                + status.getFirmwareRevision());

        logger.debug("Printer store number           : "
                + String.valueOf(status.getLogicalNumber()));

        logger.debug("Document number                : "
                + String.valueOf(status.getDocumentNumber()));

        logger.debug("Printer mode                   : "
                + String.valueOf(status.getMode()) + ", "
                + status.getPrinterMode().getText());

        logger.debug("Printer submode                : "
                + String.valueOf(status.getSubmode()) + ", "
                + status.getPrinterSubmode().getText());

        logger.debug("Printer internal port number   : "
                + String.valueOf(status.getPortNumber()));

        logger.debug("FM firmware                    : "
                + status.getFMFirmwareVersion() + "."
                + String.valueOf(status.getFMFirmwareBuild()) + ", "
                + status.getFMFirmwareDate().toString());

        logger.debug("Printer date and time          : "
                + status.getDate().toString() + ", "
                + status.getTime().toString());

        logger.debug("Serial number                  : "
                + String.valueOf(status.getSerialNumber()));

        logger.debug("Day number                     : "
                + String.valueOf(status.getDayNumber()));

        logger.debug("FM free records                : "
                + String.valueOf(status.getFMFreeRecords()));

        logger.debug("Registration number            : "
                + String.valueOf(status.getRegistrationNumber()));

        logger.debug("Fiscalizations left            : "
                + String.valueOf(status.getFreeRegistration()));

        logger.debug("Fiscal ID                      : "
                + status.getFiscalIDText());

        write(status.getPrinterFlags());
        write(status.getFiscalMemoryFlags());
    }
}
