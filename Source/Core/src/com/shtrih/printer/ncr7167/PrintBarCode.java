/*
 * PrintBarCode.java
 *
 * Created on 28 March 2008, 11:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.printer.ncr7167;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.util.CompositeLogger;

/*********************************************************************
 * Print Bar Code First Variation Second Variation ASCII: GSkm d1…dk NUL or GS k
 * m n d1…dn Hexadecimal: 1D 6B m d1…dk 00 or 1D 6B m n d1…dn First Variation:
 * String terminated with NUL Character m =0 – 6, 10 d = 32 - 126 (see the
 * table) n = 1 - 255 (see the table) Second Variation: Length of Byte Specified
 * at Beginning of String m = 65 - 73, 75 (see the table) d = 0 - 127 (see the
 * table) n = 1 - 255 (see the table) m Bar Code D n, Length 0 UPC-A 48- 57
 * (ASCII numerals) Fixed Length: 11, 12 1 UPC-E 48- 57 Fixed Length: 11, 12 2
 * JAN13 (EAN13) 48- 57 Fixed Length: 12, 13 3 JAN8 (EAN8) 48- 57 Fixed Length:
 * 7, 8 4 Code 39 48- 57, 65- 90 (ASCII alphabet), 32, 36, 37, 43, 45, 46, 47
 * (ASCII special characters) d1 = dk = 42 (start/stop code is supplied by
 * printer if necessary) Variable Length 5 Interleaved 2 of 5(ITF) 48- 57
 * Variable Length (Even Number) 6 CODABAR (NW-7) 65- 68, start code 48- 57, 36,
 * 43, 45, 46, 47, 58 Variable Length 10 PDF 417 (7158 Native Mode) 1-255
 * Variable Length 7158 NativeMode
 **********************************************************************/
public class PrintBarCode extends NCR7167Command {

	private final int m;
	private final String data;
	private final CompositeLogger logger = CompositeLogger.getLogger(PrintBarCode.class);

	/**
	 * Creates a new instance of PrintBarCode
	 */
	public PrintBarCode(int m, String data) {
		this.m = m;
		this.data = data;
	}

	public int getM() {
		return m;
	}

	public String getData() {
		return data;
	}

	public final String getText() {
		return "Print barcode";
	}

	public final NCR7167Command newInstance(NCR7167Command command) {
		PrintBarCode src = (PrintBarCode) command;
		return new PrintBarCode(src.getM(), src.getData());
	}

	private int getBarcodeType(int m) {
		switch (m) {
		case 0:
			return SmFptrConst.SMFPTR_BARCODE_UPCA;
		case 1:
			return SmFptrConst.SMFPTR_BARCODE_UPCE;
		case 2:
			return SmFptrConst.SMFPTR_BARCODE_EAN13;
		case 3:
			return SmFptrConst.SMFPTR_BARCODE_EAN8;
		case 4:
			return SmFptrConst.SMFPTR_BARCODE_CODE39;
		case 5:
			return SmFptrConst.SMFPTR_BARCODE_ITF;
		case 6:
			return SmFptrConst.SMFPTR_BARCODE_CODABAR;
		case 10:
			return SmFptrConst.SMFPTR_BARCODE_PDF417;

		case 65:
			return SmFptrConst.SMFPTR_BARCODE_UPCA;
		case 66:
			return SmFptrConst.SMFPTR_BARCODE_UPCE;
		case 67:
			return SmFptrConst.SMFPTR_BARCODE_EAN13;
		case 68:
			return SmFptrConst.SMFPTR_BARCODE_EAN8;
		case 69:
			return SmFptrConst.SMFPTR_BARCODE_CODE39;
		case 70:
			return SmFptrConst.SMFPTR_BARCODE_ITF;
		case 71:
			return SmFptrConst.SMFPTR_BARCODE_CODABAR;
		case 72:
			return SmFptrConst.SMFPTR_BARCODE_CODE93;
		case 73:
			return SmFptrConst.SMFPTR_BARCODE_CODE128;
		case 75:
			return SmFptrConst.SMFPTR_BARCODE_PDF417;
		default:
			return 0;
		}
	}

	public void execute(NCR7167Printer printer) throws Exception {
		logger.debug("execute");

		PrinterBarcode barcode = new PrinterBarcode();
		barcode.setText(data);
		barcode.setLabel(data);
		barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
		barcode.setTextPosition(printer.barcodeTextPosition);
		barcode.setBarWidth(printer.barWidth);
		barcode.setHeight(printer.barcodeHeight);
		barcode.setType(getBarcodeType(m));
		barcode.setTextFont(1);
		barcode.setAspectRatio(3);
		printer.printBarcode(barcode);
	}
}
