package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.CashCoreInstaller;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.IPrinterEvents;
import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.fiscalprinter.scoc.ScocClient;
import com.shtrih.fiscalprinter.scoc.commands.DeviceFirmwareResponse;
import com.shtrih.fiscalprinter.scoc.commands.DeviceStatusResponse;
import com.shtrih.fiscalprinter.table.PrinterTables;
import com.shtrih.util.BitUtils;
import com.shtrih.util.CompositeLogger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class FirmwareUpdaterService implements Runnable, IPrinterEvents {

    private static final int pollPeriodSeconds = 15 * 60;

    private CompositeLogger logger = CompositeLogger.getLogger(FirmwareUpdaterService.class);

    private final SMFiscalPrinter printer;

    private Thread thread = null;
    private volatile boolean stopFlag = true;

    private byte[] firmware;

    public FirmwareUpdaterService(SMFiscalPrinter printer) {
        if (printer == null)
            throw new IllegalArgumentException("printer is null");

        this.printer = printer;
    }

    public void run() {
        try {
            logger.debug("Starting FirmwareUpdaterService");

            while (!stopFlag) {

                if (firmware == null)
                    checkData();

                if (stopFlag)
                    break;

                updateFirmware();

                Thread.sleep(pollPeriodSeconds * 1000);
            }

            logger.error("FirmwareUpdaterService stopped");
        } catch (InterruptedException e) {
            logger.error("FirmwareUpdaterService stopped");
        } catch (Exception e) {
            logger.error("FirmwareUpdaterService unexpected exception", e);
        }
    }

    private void checkData() {
        try {
            if (stopFlag)
                return;

            logger.debug("Checking for firmware update");

            String serialNumber = printer.readFullSerial();

            BigInteger uin;
            long firmwareVersion;

            if (printer.isDesktop()) {
                uin = new BigInteger(printer.readTable(23, 1, 11));
                if (printer.isShtrihNano()) {
                    firmwareVersion = 100000 + printer.readLongStatus().getFirmwareBuild();
                } else {
                    PrinterDate data = printer.readLongStatus().getFirmwareDate();
                    Calendar calendar = new GregorianCalendar();
                    calendar.set(data.getYear() + 2000, data.getMonth() - 1, data.getDay(), 23, 59, 59);
                    calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                    firmwareVersion = calendar.getTimeInMillis() / 1000L;
                }
            } else if (printer.isCashCore()) {
                uin = new BigInteger(serialNumber);
                firmwareVersion = 70000000 + printer.readLongStatus().getFirmwareBuild();
            } else {
                uin = new BigInteger(serialNumber);
                firmwareVersion = printer.getDeviceMetrics().getModel() * 1000000 + printer.readLongStatus().getFirmwareBuild();
            }

            if (stopFlag)
                return;

            ScocClient client = new ScocClient(serialNumber, uin.longValue());

            DeviceStatusResponse response = client.sendStatus(firmwareVersion);

            boolean isUpdateRequired = BitUtils.testBit(response.getFlags(), 6);

            if (!isUpdateRequired) {
                logger.debug("Firmware update is not required");
                return;
            }

            long startedAt = System.currentTimeMillis();

            DeviceFirmwareResponse firstResponse = client.readFirmware(0, 1);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            out.write(firstResponse.getData());

            if (stopFlag)
                return;

            long newVersion = firstResponse.getFirmwareVersion();

            logger.debug("Downloading new firmware version " + newVersion + ", current version is " + firmwareVersion + " in " + firstResponse.getPartsCount() + " parts");

            for (int i = 2; i <= firstResponse.getPartsCount(); i++) {

                if (stopFlag)
                    return;

                DeviceFirmwareResponse nextPart = client.readFirmware(newVersion, i);

                out.write(nextPart.getData());
            }

            out.flush();

            firmware = out.toByteArray();

            long doneAt = System.currentTimeMillis();

            logger.debug("Firmware " + newVersion + " downloading done in " + (doneAt - startedAt) + " ms");

        } catch (Exception e) {
            logger.error("Firmware downloading failed", e);
        }
    }

    private boolean isStarted() {
        return !stopFlag;
    }

    public void start() {
        if (!isStarted()) {
            stopFlag = false;
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        stopFlag = true;
        if (thread != null) {
            thread.interrupt();
//            thread.join();
        }

        thread = null;
        firmware = null;
    }

    @Override
    public void init() {

    }

    @Override
    public void done() {

    }

    @Override
    public void afterCommand(PrinterCommand command) throws Exception {

        if (command.isFailed())
            return;

        try {
            switch (command.getCode()) {

                case 0x41:
                    updateFirmware();
                    break;
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void updateFirmware() {

        try {
            if (firmware == null)
                return;

            if (printer.isDesktop() && !printer.isShtrihNano() && !printer.isSDCardPresent()) {
                logger.debug("Firmware update skipped, no SD card");
                return;
            }

            logger.debug("Firmware update started");

            long startedAt = System.currentTimeMillis();

            PrinterTables tables = null;
            if (!printer.isShtrihNano()) {
                tables = printer.readTables();
            }

            if (printer.isCashCore()) {
                installCashCore();
            } else {

                if (printer.isDesktop()) {
                    printer.writeTable(23, 1, 1, "0");
                }

                writeFirmware();

                if (stopFlag)
                    return;

                long doneAt = System.currentTimeMillis();

                logger.debug("Firmware written in " + (doneAt - startedAt) + " ms");

                if (!printer.isShtrihNano())
                    rebootAndWait();

                if (tables != null)
                    printer.writeTables(tables);
            }

            firmware = null;

            logger.debug("Firmware update done");
        } catch (Exception e) {
            logger.error("Firmware update failed", e);
        }
    }

    private void installCashCore() throws IOException {
        Object appContext = printer.getParams().getAppContext();

        if(appContext == null)
            throw new IllegalArgumentException("AppContext was not set");

        new CashCoreInstaller(appContext).install(firmware);
    }

    private void writeFirmware() throws Exception {
        InputStream stream = new ByteArrayInputStream(firmware);

        int fileType = 1; // 0 - загрузчик; 1 - прошивка;
        int blockNumber = 0;
        byte[] block = new byte[128];

        while (stream.available() > 0) {

            if (stopFlag)
                return;

            stream.read(block, 0, 128);
            printer.writeFirmwareBlockToSDCard(fileType, blockNumber, block);
            blockNumber++;
        }
    }

    private void rebootAndWait() throws Exception {
        printer.reboot();

        Thread.sleep(10 * 1000);

        for (int i = 0; i < 10; i++) {
            try {
                if (stopFlag)
                    return;

                printer.connect();
                break;
            } catch (Exception e) {
                Thread.sleep(5 * 1000);
            }
        }

        printer.connect();
    }

    @Override
    public void beforeCommand(PrinterCommand command) throws Exception {

    }

    @Override
    public void printerStatusRead(PrinterStatus status) {

    }
}
