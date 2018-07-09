import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.fiscalprinter.command.DeviceMetrics;
import jpos.FiscalPrinter;
import jpos.FiscalPrinterConst;

public class PrinterConnectionTester {
    public static void main(String args[]) {
        // Установка вывода консольных сообщений в нужной кодировке
//        try {
//            System.setOut(new CodepagePrintStream(
//                    System.out, System.getProperty("console.encoding", "Cp866")));
//        } catch (UnsupportedEncodingException e) {
//            System.out.println("Unable to setup console codepage: " + e);
//        }

        try {
            ShtrihFiscalPrinter printer = new ShtrihFiscalPrinter(new FiscalPrinter());
            printer.open("ShtrihFptr");
            printer.claim(3000);
            printer.setDeviceEnabled(true);

            String[] lines = new String[1];
            printer.getData(FiscalPrinterConst.FPTR_GD_PRINTER_ID, null, lines);
            String serialNumber = lines[0];
            DeviceMetrics deviceMetrics = printer.readDeviceMetrics();

            System.out.println(deviceMetrics.getDeviceName() + " " + serialNumber);
        } catch (Exception exc) {
            System.out.println("Failure" + exc);
        }
    }
}
