/*
 * NCR7167Printer.java
 *
 * Created on 14 February 2008, 16:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author V.Kravtsov
 */
package com.shtrih.printer.ncr7167;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.StringUtils;

public class NCR7167Printer {

    private final FiscalPrinterImpl service;
    public static final int ID_DOWNLOAD_BMP_FILE = 1;
    // Select Printing Position for HRI Characters
    public int barcodeTextPosition = 0;
    // Select Pitch for HRI Characters
    public int barcodeTextPitch = 0;
    // PrinterBarcode height
    public int barcodeHeight = 100;
    // PrinterBarcode width
    public int barWidth = 2;
    private final Vector parsers = new Vector();
    private final Vector commands = new Vector();

    public Vector getCommands() {
        return commands;
    }

    public NCR7167Printer(FiscalPrinterImpl service) {
        this.service = service;
        createParsers();
    }

    public void createParsers() {

        parsers.clear();
        // simple commands
        AddParser(0x10, 0, "Clear Printer");
        AddParser(0x11, 0, "Close Form");
        AddParser(0x18, 0, "Open Form");
        AddParser(0x19, 0, "Perform Partial Knife Cut");
        AddParser(0x19, 0, "Perform Partial Knife Cut");
        AddParser(0x1B, 0x69, 0, "Perform Partial Knife Cut");
        AddParser(0x1A, 0, "Perform Partial Knife Cut");
        AddParser(0x1B, 0x6D, 0, "Perform Partial Knife Cut");
        AddParser(0x1B, 0x07, 0, "Generate Tone");
        AddParser(0x1B, 0x40, 0, "Initialize Printer");
        AddParser(0x1B, 0x43, 1, "Set Slip Paper Eject Length");
        AddParser(0x1B, 0x63, 0x30, 1,
                "Select Receipt or Slip for Printing; Slip for MICR Read");
        AddParser(0x1B, 0x63, 0x31, 1,
                "Select Receipt or Slip for Setting Line Spacing");
        AddParser(0x1B, 0x63, 0x34, 1, "Select Sensors to Stop Printing");
        AddParser(0x1B, 0x63, 0x35, 1, "Enable or Disable Panel Buttons");
        AddParser(0x1B, 0x66, 2, "Set Slip Paper Waiting Time");
        AddParser(0x1B, 0x70, 3, "Generate Pulse to Open Cash Drawer");
        AddParser(0x1B, 0x7A, 1,
                "Select or Cancel Parallel Printing Mode on Receipt and Journal");
        AddParser(0x1C, 0, "Select Slip Station");
        AddParser(0x1D, 0x56, 0x00, 0, "Select Cut Mode and Cut Paper");
        AddParser(0x1D, 0x56, 0x30, 0, "Select Cut Mode and Cut Paper");
        AddParser(0x1D, 0x56, 0x01, 0, "Select Cut Mode and Cut Paper");
        AddParser(0x1D, 0x56, 0x31, 0, "Select Cut Mode and Cut Paper");
        AddParser(0x1D, 0x56, 0x41, 1, "Select Cut Mode and Cut Paper");
        AddParser(0x1D, 0x56, 0x42, 1, "Select Cut Mode and Cut Paper");
        AddParser(0x1E, 0, "Select Receipt Station");
        AddParser(0x1F, 0x74, 0, "Print Test Form");
        // AddParser(0x0A, 0, "Print and Feed Paper One Line");
        AddParser(0x0C, 0, "Print and Eject Slip");
        // AddParser(0x0D, 0, "Print and Carriage Return");
        AddParser(0x14, 1, "Feed n Print Lines");
        AddParser(0x15, 1, "Feed n Dot Rows");
        AddParser(0x16, 1, "Add n Extra Dot Rows");
        AddParser(0x17, 0, "Print");
        AddParser(0x1B, 0x32, 0, "Set Line Spacing to 1/6 Inch");
        AddParser(0x1B, 0x33, 1, "Set Line Spacing");
        AddParser(0x1B, 0x4A, 1, "Print and Feed Paper");
        AddParser(0x1B, 0x4B, 1, "Print and Reverse Feed Paper");
        AddParser(0x1B, 0x64, 1, "Print and Feed n Lines");
        AddParser(0x1B, 0x65, 1, "Print and Reverse Feed n Lines");
        AddParser(0x1D, 0x14, 1, "Reverse Feed n Lines");
        AddParser(0x1D, 0x15, 1, "Reverse Feed n Dots");
        AddParser(0x1D, 0x50, 2,
                "Set Horizontal and Vertical Minimum Motion Units");
        AddParser(0x09, 0, "Horizontal Tab");
        AddParser(0x1B, 0x14, 0, "Set Column");
        AddParser(0x1B, 0x24, 2, "Set Absolute Starting Position");
        AddParser(0x1B, 0x44, 0, "Set Horizontal Tabs");
        AddParser(0x1B, 0x5C, 2, "Set Relative Print Position");
        AddParser(0x1B, 0x61, 1, "Select Justification");
        AddParser(0x1D, 0x4C, 2, "Set Left Margin");
        AddParser(0x1D, 0x57, 2, "Set Printing Area Width");
        AddParser(0x12, 0, "Select Double-Wide Characters");
        AddParser(0x13, 0, "Select Single-Wide Characters");
        AddParser(0x1B, 0x12, 0,
                "Select 90 Degree Counter-Clockwise Rotated Print");
        AddParser(0x1D, 0x16, 1, "Select Pitch (Column Width)");
        AddParser(0x1B, 0x20, 1, "Set Character Right-Side Spacing");
        AddParser(0x1B, 0x21, 1, "Select Print Modes");
        AddParser(0x1B, 0x25, 1, "Select or Cancel User-Defined Character Set");
        AddParser(0x1B, 0x2D, 1, "Select or Cancel Underline Mode");
        AddParser(0x1B, 0x3A, 0x30, 0x30, 0x30, 0,
                "Copy Character Set from ROM to RAM");
        AddParser(0x1B, 0x3F, 1, "Cancel User-Defined Characters");
        AddParser(0x1B, 0x45, 1, "Select or Cancel Emphasized Mode");
        AddParser(0x1B, 0x47, 0, "Select Double Strike");
        AddParser(0x1B, 0x48, 0, "Cancel Double Strike");
        AddParser(0x1B, 0x49, 1, "Select or Cancel Italic Print");
        AddParser(0x1B, 0x52, 1, "Select International Character Set");
        AddParser(0x1B, 0x74, 1, "Select International Character Set");
        AddParser(0x1B, 0x55, 1,
                "Select or Cancel Unidirectional Printing Mode");
        AddParser(0x1B, 0x56, 1,
                "Select or Cancel 90 Degrees Clockwise Rotated Print");
        AddParser(0x1B, 0x72, 1, "Select Print Color");
        AddParser(0x1B, 0x7B, 1, "Select or Cancel Upside Down Printing Mode");
        AddParser(0x1D, 0x21, 1, "Select Character Size");
        AddParser(0x1D, 0x42, 1,
                "Select or Cancel White/Black Reverse Print Mode");
        AddParser(0x1D, 0x62, 1, "Select or Cancel Smoothing Mode");
        AddParser(0x1F, 0x05, 1, "Select Superscript or Subscript Modes");
        AddParser(0x11, 72, "Print Raster Graphics");
        AddParser(0x1B, 0x23, 1, "Select the Current Logo");
        AddParser(0x1D, 0x2F, 1, "Print Downloaded Bit Image");
        AddParser(0x1F, 0x04, 1, "Convert 6 Dots/mm Bitmap to 8 Dots/mm Bitmap");
        // status commands
        AddParser(0x1B, 0x75, 0x00, 0, "Transmit Peripheral Device Status");
        AddParser(0x1D, 0x49, 1, "Transmit Printer ID");
        AddParser(0x1D, 0x49, 0x40, 1,
                "Transmit Printer ID, Remote Diagnostics Extension");
        AddParser(0x1D, 0x72, 1, "Transmit Status");
        AddParser(0x1F, 0x56, 1, "Send Printer Software Version");
        // real time commands
        AddParser(0x1D, 0x04, 1, "Real Time Status Transmission");
        AddParser(0x10, 0x04, 1, "Real Time Status Transmission");
        AddParser(0x1D, 0x03, 1, "Real Time Request to Printer");
        AddParser(0x10, 0x03, 1, "Real Time Request to Printer");
        AddParser(0x1D, 0x05, 0, "Real Time Printer Status Transmission");
        AddParser(0x1D, 0x61, 1, "Select or Cancel Automatic Status Back");
        // Bar Code Commands
        AddParser(new SelectBarcodeTextPositionParser());
        AddParser(new SelectBarcodeTextPitchParser());
        AddParser(new SelectBarcodeHeightParser());
        AddParser(new SelectBarcodeWidthParser());
        // Page Mode Commands
        // to correctly detect commands printer mode is needed
        AddParser(0x0C, 0, "Print and Return to Standard Mode");
        AddParser(0x18, 0, "Cancel Print Data in Page Mode");
        AddParser(0x1B, 0x0C, 0, "Print Data in Page Mode");
        AddParser(0x1B, 0x4C, 0, "Select Page Mode");
        AddParser(0x1B, 0x53, 0, "Select Standard Mode");
        AddParser(0x1B, 0x54, 1, "Select Print Direction in Page Mode");
        AddParser(0x1B, 0x57, 8, "Set Printing Area in Page Mode");
        AddParser(0x1D, 0x24, 2,
                "Set Absolute Vertical Print Position in Page Mode");
        AddParser(0x1D, 0x5C, 2,
                "Set Relative Vertical Print Position in Page Mode");
        AddParser(0x1D, 0x3A, 0, "Start or End Macro Definition");
        AddParser(0x1D, 0x5E, 3, "Execute Macro");
        // MICR Commands
        AddParser(0x1B, 0x77, 0x01, 0, "Read MICR Data and Transmit");
        AddParser(0x1B, 0x77, 0x52, 0, "Reread MICR Data");
        // other
        AddParser(0x1B, 0x77, 0x46, 0, "Check Flip Command");
        // User Data Storage Commands
        AddParser(new WriteUserDataStorageParser());
        AddParser(0x1B, 0x34, 4, "Read from User Data Storage");
        AddParser(0x1B, 0x6A, 1, "Read from Non-Volatile Memory");
        AddParser(0x1B, 0x73, 3, "Write to Non-Volatile Memory (NVRAM)");
        AddParser(0x1D, 0x22, 0x30, 0,
                "Select Memory Type (SRAM/Flash) Where to Save Logos or User-Defined Fonts");
        AddParser(0x1D, 0x22, 0x31, 0,
                "Select Memory Type (SRAM/Flash) Where to Save Logos or User-Defined Fonts");
        AddParser(0x1D, 0x22, 0x32, 0,
                "Select Memory Type (SRAM/Flash) Where to Save Logos or User-Defined Fonts");
        AddParser(0x1D, 0x22, 0x33, 0,
                "Select Memory Type (SRAM/Flash) Where to Save Logos or User-Defined Fonts");
        AddParser(0x1D, 0x22, 0x55, 2, "Flash Allocation");
        AddParser(0x1D, 0x40, 1, "Erase User Flash Sector");
        AddParser(new PrinterSettingChangeParser());
        // Asian Character Commands
        AddParser(0x1C, 0x21, 1, "Select print modes for Kanji characters");
        AddParser(0x1C, 0x2D, 1, "FS – Turn underline mode ON/OFF for Kanji");
        AddParser(0x1C, 0x32, 74, "Define user-defined Kanji characters");
        AddParser(0x1C, 0x53, 2, "Set Kanji character spacing");
        AddParser(0x1C, 0x57, 2, "Set quadruple mode ON/OFF for Kanji");
        // Flash Download Commands
        AddParser(0x1B, 0x5B, 0x7D, 0, "Switch to Flash Download Mode");
        AddParser(0x1D, 0x00, 0, "Request Printer ID");
        AddParser(0x1D, 0x01, 0, "Return Segment Number Status of Flash Memory");
        AddParser(0x1D, 0x02, 1, "Select Flash Memory Sector to Download");
        AddParser(0x1D, 0x06, 0, "Get Firmware CRC");
        AddParser(0x1D, 0x07, 0, "Return Microprocessor CRC");
        AddParser(0x1D, 0x0E, 0, "Erase the Flash Memory");
        AddParser(0x1D, 0x0F, 0, "Return Main Program Flash CRC");
        AddParser(0x1D, 0x10, 1, "Erase Selected Flash Sector");
        AddParser(new DownloadActiveFlashSectorParser());
        AddParser(0x1D, 0xFF, 0, "Reboot the Printer");
        // sofisticated commands (data length depends on data)
        AddParser(new DefineCharactersParser());
        AddParser(new DownloadBMPLogoParser());
        AddParser(new SelectBitImageModeParser());
        AddParser(new PrintAdvancedRasterGraphicsParser());
        AddParser(new SelectDoubleDensityGraphicsParser());
        AddParser(new DefineDownloadedBitImageParser());
        AddParser(new PrintBarCodeParser());
        AddParser(new DefineParsingFormatParser());
        AddParser(new DefineParsingFormatNVRAMParser());
        AddParser(new DownloadBMPFileParser());
        AddParser(new DownloadBMPFileParser2());
    }

    private void AddParser(Parser parser) {
        parsers.add(parser);
    }

    private void AddParser(int code, int paramLen, String text) {
        byte[] data = new byte[1];
        data[0] = (byte) code;
        AddParser(new SimplePrinterCommandParser(data, paramLen, text));
    }

    private void AddParser(int code1, int code2, int paramLen, String text) {
        byte[] data = new byte[2];
        data[0] = (byte) code1;
        data[1] = (byte) code2;
        AddParser(new SimplePrinterCommandParser(data, paramLen, text));
    }

    private void AddParser(int code1, int code2, int code3, int paramLen,
            String text) {
        byte[] data = new byte[3];
        data[0] = (byte) code1;
        data[1] = (byte) code2;
        data[2] = (byte) code3;
        AddParser(new SimplePrinterCommandParser(data, paramLen, text));
    }

    private void AddParser(int code1, int code2, int code3, int code4,
            int code5, int paramLen, String text) {
        byte[] data = new byte[5];
        data[0] = (byte) code1;
        data[1] = (byte) code2;
        data[2] = (byte) code3;
        data[3] = (byte) code4;
        data[4] = (byte) code5;
        AddParser(new SimplePrinterCommandParser(data, paramLen, text));
    }

    public void execute() throws Exception {
        for (int i = 0; i < commands.size(); i++) {
            NCR7167Command command = (NCR7167Command) commands.get(i);
            command.execute(this);
        }
    }

    public String parse(String text, String charsetName) throws Exception {

        byte[] data = StringUtils.getBytes(text, charsetName);
        String result = "";
        boolean parsed = false;

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DataInputStream in = new DataInputStream(stream);

        commands.clear();
        while (true) {
            parsed = false;
            for (int i = 0; i < parsers.size(); i++) {
                in.mark(0);
                Parser parser = (Parser) parsers.get(i);
                try {
                    NCR7167Command command = parser.parse(in, charsetName);
                    if (command != null) {
                        parsed = true;
                        commands.add(command.newInstance(command));
                        break;
                    } else {
                        in.reset();
                    }
                } catch (IOException e) {
                    // swallow IOException, because we dont check input stream
                    // size
                    // it is better to check this exception in command parsers
                    // (?)
                    in.reset();
                }
            }
            if (in.available() == 0) {
                break;
            }

            if (!parsed) {
                b.write(in.readByte());
            }
        }
        if (b.size() > 0) {
            result = new String(b.toByteArray(), charsetName);
        }
        return result;
    }

    public NCR7167Command find(int id) {
        NCR7167Command result;
        for (int i = 0; i < commands.size(); i++) {
            result = (NCR7167Command) commands.get(i);
            if (result.getId() == id) {
                return result;
            }
        }
        return null;
    }

    public abstract class Parser {

        public abstract NCR7167Command parse(DataInputStream in,
                String charsetName) throws Exception;

        public boolean checkSignature(DataInputStream in, byte[] sig)
                throws Exception {
            byte[] data = new byte[sig.length];
            in.read(data, 0, sig.length);
            return Arrays.equals(data, sig);
        }
    }

    public final class PrintAdvancedRasterGraphicsParser extends Parser {

        
        public final NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            if ((in.readByte() == 0x1B) && (in.readByte() == 0x2E)) {
                int m = in.readByte();
                int n = in.readByte();
                int rl = in.readByte();
                int rh = in.readByte();
                byte[] data = new byte[n];
                in.read(data, 0, n);
                return new PrintAdvancedRasterGraphics(n, m, rl, rh, data);
            }
            return null;
        }
    }

    public final class PrintBarCodeParser extends Parser {

        // 1D 6B 41 0B 30 31 32 33 34 35 36 37 38 39 30
        
        public NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            if ((in.readByte() == 0x1D) && (in.readByte() == 0x6B)) {
                int m = in.readByte();
                // First Variation
                if (((m >= 0) && (m <= 6)) || (m == 10)) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte b;
                    while ((b = in.readByte()) != 0) {
                        baos.write(b);
                    }
                    String data = baos.toString(charsetName);
                    return new PrintBarCode(m, data);
                } else {
                    int n = in.readByte();
                    byte[] b = new byte[n];
                    in.read(b, 0, n);
                    String data = new String(b, charsetName);
                    return new PrintBarCode(m, data);
                }
            }
            return null;
        }
    }

    public final class PrinterSettingChangeParser extends Parser {

        
        public NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            if ((in.readByte() == 0x1F) && (in.readByte() == 0x11)) {
                byte m;
                byte n;

                do {
                    m = in.readByte();
                    if (m == (byte) 0xFF) {
                        return new PrinterSettingChange();
                    }
                    n = in.readByte();
                } while (true);
            }
            return null;
        }
    }

    public final class SelectBarcodeHeightParser extends Parser {

        
        public NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            byte[] signature = {0x1D, 0x68};
            boolean result = checkSignature(in, signature);
            if (result) {
                int n = in.readByte();
                return new SelectBarcodeHeight(n);
            }
            return null;
        }
    }

    public final class SelectBarcodeTextPitchParser extends Parser {

        
        public NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            byte[] signature = {0x1D, 0x66};
            boolean result = checkSignature(in, signature);
            if (result) {
                return new SelectBarcodeTextPitch(in.readByte());
            }
            return null;
        }
    }

    public final class SelectBarcodeTextPositionParser extends Parser {

        
        public NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            byte[] signature = {0x1D, 0x48};
            if (checkSignature(in, signature)) {
                return new SelectBarcodeTextPosition(in.readByte());
            } else {
                return null;
            }
        }
    }

    public final class SelectBarcodeWidthParser extends Parser {

        
        public NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            byte[] signature = {0x1D, 0x77};
            if (checkSignature(in, signature)) {
                return new SelectBarcodeWidth(in.readByte());
            }
            return null;
        }
    }

    public final class SelectBitImageModeParser extends Parser {

        
        public NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            if ((in.readByte() == 0x1B) && (in.readByte() == 0x2A)) {
                int m = in.readByte();
                int n1 = in.readByte();
                int n2 = in.readByte();
                int len = 0;

                switch (m) {
                    case 0:
                    case 1:
                        len = n1 + (256 * n2);
                        byte[] data = new byte[len];
                        in.read(data, 0, len);
                        return new SelectBitImageMode(m, n1, n2, data);

                    case 32:
                    case 33:
                        len = 3 * (n1 + (256 * n2));
                        data = new byte[len];
                        in.read(data, 0, len);
                        return new SelectBitImageMode(m, n1, n2, data);
                        // ignore
                    default:
                }

            }
            return null;
        }
    }

    public final class SelectDoubleDensityGraphicsParser extends Parser {

        
        public NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            byte mode = 0; // suppose mode is 8-Dot

            if ((in.readByte() == 0x1B) && (in.readByte() == 0x59)) {
                int n = 0;
                int n1 = in.readByte();
                int n2 = in.readByte();
                if (mode == 0) {
                    n = n1 + (256 * n2);
                } else {
                    n = 3 * (n1 + (256 * n2));
                }
                byte[] data = new byte[n];
                in.read(data, 0, n);
                return new SelectDoubleDensityGraphics(n1, n2, data);
            }
            return null;
        }
    }

    public final class SimplePrinterCommandParser extends Parser {

        private final byte[] code;
        private final int paramLen;
        private final String text;

        public SimplePrinterCommandParser(byte[] code, int paramLen, String text) {
            this.code = code;
            this.paramLen = paramLen;
            this.text = text;
        }

        
        public NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            byte[] data = new byte[code.length];
            in.read(data, 0, code.length);
            if (Arrays.equals(data, code)) {
                if (paramLen > 0) {
                    // read parameters
                    byte[] params = new byte[paramLen];
                    in.read(params, 0, paramLen);
                }
                return new SimplePrinterCommand(code, paramLen, text);
            }
            return null;
        }
    }

    public final class WriteUserDataStorageParser extends Parser {

        
        public final NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            if ((in.readByte() == 0x1B) && (in.readByte() == 0x27)) {
                int m = in.readByte();
                int a0 = in.readByte();
                int a1 = in.readByte();
                int a2 = in.readByte();
                byte[] data = new byte[m];
                in.read(data, 0, m);
                return new WriteUserDataStorage(m, a0, a1, a2, data);
            }
            return null;
        }
    }

    public final class DownloadActiveFlashSectorParser extends Parser {

        public DownloadActiveFlashSectorParser() {
        }

        
        public final NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            if ((in.readByte() == 0x1D) && (in.readByte() == 0x11)) {
                byte al = in.readByte();
                byte ah = in.readByte();
                byte cl = in.readByte();
                byte ch = in.readByte();
                int n = (ch * 256) + cl;
                in.skipBytes(n);
                return new DownloadActiveFlashSector();
            }
            return null;
        }
    }

    public final class DefineCharactersParser extends Parser {

        
        public final NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            byte[] code = {0x1B, 0x26};

            int mode; // 0 - slip, 3 - receipt
            int c1;
            int c2;
            int n1;
            byte[] data;

            boolean result;
            data = new byte[2];
            in.read(data, 0, 2);
            if ((data[0] == code[0]) && (data[1] == code[1])) {
                mode = in.readByte();
                // receipt
                if (mode == 3) {
                    c1 = in.readByte();
                    c2 = in.readByte();
                    n1 = in.readByte();
                    data = new byte[n1];
                    in.read(data, 0, n1);
                    return new DefineCharacters(mode, c1, c2, n1, data);
                }
                // slip
                if (mode == 0) {
                    c1 = in.readByte();
                    c2 = in.readByte();
                    n1 = (byte) ((c2 - c1 + 1) * 12);
                    data = new byte[n1];
                    in.read(data, 0, n1);
                    return new DefineCharacters(mode, c1, c2, n1, data);
                }
            }
            return null;
        }
    }

    public final class DownloadBMPLogoParser extends Parser {

        
        public NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            if (in.readByte() == 0x1B) {
                if (in.readByte() == 0x42) {
                    if (in.readByte() == 0x4D) {
                        int len = in.readInt() - 7;
                        byte[] data = new byte[len];
                        in.read(data, 0, len);
                        return new DownloadBMPLogo(data);
                    }
                }
            }
            return null;
        }
    }

    public final class DefineDownloadedBitImageParser extends Parser {

        
        public final NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            int n1;
            int n2;
            byte[] data;

            if ((in.readByte() == 0x1D) && (in.readByte() == 0x2A)) {
                n1 = in.readByte();
                n2 = in.readByte();
                int n = 8 * n1 * n2;
                data = new byte[n];
                in.read(data, 0, n);
                return new DefineDownloadedBitImage(n1, n2, data);
            }
            return null;
        }
    }

    public final class DefineParsingFormatNVRAMParser extends Parser {

        
        public final NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            if ((in.readByte() == 0x1B) && (in.readByte() == 0x77)
                    && (in.readByte() == 0x50)) {
                byte b;
                do {
                    b = in.readByte();
                } while (b != 0x0D);
                return new DefineParsingFormatNVRAM();
            }
            return null;
        }
    }

    public final class DefineParsingFormatParser extends Parser {

        
        public final NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            if ((in.readByte() == 0x1B) && (in.readByte() == 0x77)
                    && (in.readByte() == 0x70)) {
                byte b;
                do {
                    b = in.readByte();
                } while (b != 0x0D);
                return new DefineParsingFormat();
            }
            return null;
        }
    }

    public final class DownloadBMPFileParser extends Parser {

        
        public final NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            byte[] bs = new byte[2];
            bs[0] = in.readByte();
            bs[1] = in.readByte();

            if ((bs[0] == 0x1B) && (bs[1] == 0x62)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (in.available() > 0) {
                    byte b = in.readByte();
                    if (b == 0x0A) {
                        break;
                    }
                    baos.write(b);
                }
                bs = baos.toByteArray();
                String fileName = baos.toString(charsetName);
                return new DownloadBMPFile(fileName, service);
            }
            return null;
        }
    }

    // %L:Logo.bmp% 25 4C 3A 25
    public final class DownloadBMPFileParser2 extends Parser {

        
        public final NCR7167Command parse(DataInputStream in, String charsetName)
                throws Exception {
            byte[] sig = {0x25, 0x4C, 0x3A};

            if (checkSignature(in, sig)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while (in.available() > 0) {
                    byte b = in.readByte();
                    if (b == 0x25) {
                        break;
                    }
                    baos.write(b);
                }
                String fileName = baos.toString(charsetName);
                return new DownloadBMPFile(fileName, service);
            }
            return null;
        }
    }

    public void printBarcode(PrinterBarcode barcode) throws Exception {
        service.getPrinter().printBarcode(barcode);
    }
}
