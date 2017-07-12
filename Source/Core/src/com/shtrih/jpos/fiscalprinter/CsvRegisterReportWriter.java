package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import com.shtrih.fiscalprinter.command.FMTotals;
import com.shtrih.fiscalprinter.command.CashRegister;
import com.shtrih.fiscalprinter.command.CashRegisters;
import com.shtrih.fiscalprinter.command.FSReadCommStatus;
import com.shtrih.fiscalprinter.command.OperationRegister;

public class CsvRegisterReportWriter {

    private static final String Separator = ";";

    private CsvRegisterReportWriter() {
    }

    public static void execute(RegisterReport report, String fileName)
            throws Exception {
        String line = "";
        if (fileName.equals("")) {
            fileName = "ZReport.csv";
        }

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), "UTF8"));
        try {
            CashRegisters cashRegisters = report.getCashRegisters();
            for (int i = 0; i < cashRegisters.size(); i++) {
                CashRegister cashRegister = (CashRegister) cashRegisters.get(i);

                line = "0"
                        + Separator
                        + String.valueOf(cashRegister.getNumber())
                        + Separator
                        + String.valueOf(cashRegister.getValue())
                        + Separator
                        + String.valueOf(CashRegister.getEngName(cashRegister
                                .getNumber()));
                writer.write(line);
                writer.newLine();
            }

            Vector operRegisters = report.getOperRegisters();

            for (int i = 0; i < operRegisters.size(); i++) {
                OperationRegister operRegister = (OperationRegister) operRegisters
                        .get(i);

                line = "1"
                        + Separator
                        + String.valueOf(operRegister.getNumber())
                        + Separator
                        + String.valueOf(operRegister.getValue())
                        + Separator
                        + String.valueOf(OperationRegister
                                .getEngName(operRegister.getNumber()));
                writer.write(line);
                writer.newLine();
            }
            // All fiscalizations
            FMTotals totals = report.getAllFiscalizations();
            line = "2"
                    + Separator
                    + String.valueOf(totals.getSalesAmount())
                    + Separator
                    + String.valueOf(totals.getBuyAmount())
                    + Separator
                    + String.valueOf(totals.getRetSaleAmount())
                    + Separator
                    + String.valueOf(totals.getRetBuyAmount());
            writer.write(line);
            writer.newLine();
            // Last fiscalization
            totals = report.getLastFiscalization();
            line = "3"
                    + Separator
                    + String.valueOf(totals.getSalesAmount())
                    + Separator
                    + String.valueOf(totals.getBuyAmount())
                    + Separator
                    + String.valueOf(totals.getRetSaleAmount())
                    + Separator
                    + String.valueOf(totals.getRetBuyAmount());
            writer.write(line);
            writer.newLine();
            // Comm status
            if (report.getCapCommStatus())
            {
            FSReadCommStatus commStatus = report.getCommStatus();
            line = "4"
                    + Separator
                    + String.valueOf(commStatus.getQueueSize())
                    + Separator
                    + String.valueOf(commStatus.getDocumentNumber())
                    + Separator
                    + commStatus.getDocumentDate().toString()
                    + Separator
                    + commStatus.getDocumentTime().toString();
            writer.write(line);
            writer.newLine();
            // Fiscal printer parameters
            line = "5"
                    + Separator
                    + String.valueOf(report.getDayNumber())
                    + Separator
                    + report.getFsSerial();
            writer.write(line);
            writer.newLine();
            }

            writer.flush();
        } finally {
            writer.close();
        }
    }
}
