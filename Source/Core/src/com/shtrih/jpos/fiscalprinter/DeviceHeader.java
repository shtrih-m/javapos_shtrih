package com.shtrih.jpos.fiscalprinter;

import java.util.Vector;

import jpos.JposConst;
import jpos.JposException;

import org.apache.log4j.Logger;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.ReadTableInfo;
import com.shtrih.util.Localizer;

public class DeviceHeader implements PrinterHeader {

	private final SMFiscalPrinter printer;
	private final Vector<HeaderLine> header = new Vector<HeaderLine>();
	private final Vector<HeaderLine> trailer = new Vector<HeaderLine>();
	private static Logger logger = Logger.getLogger(DeviceHeader.class);

	public DeviceHeader(SMFiscalPrinter printer) {
		this.printer = printer;
	}

	@Override
	public int getNumHeaderLines() throws Exception {
		return printer.getModel().getNumHeaderLines();
	}

	@Override
	public int getNumTrailerLines() throws Exception {
		return printer.getModel().getNumTrailerLines();
	}

	// numHeaderLines for device cannot be changed
	@Override
	public void setNumHeaderLines(int numHeaderLines) throws Exception {
	}

	// numTrailerLines for device cannot be changed
	@Override
	public void setNumTrailerLines(int numTrailerLines) throws Exception {
	}

	@Override
	public void initDevice() throws Exception {
		logger.debug("initDevice");

		int numHeaderLines = printer.getModel().getNumHeaderLines();
		header.clear();
		for (int i = 1; i <= numHeaderLines; i++) {
			header.add(new HeaderLine());
		}
		int numTrailerLines = printer.getModel().getNumTrailerLines();
		trailer.clear();
		for (int i = 1; i <= numTrailerLines; i++) {
			trailer.add(new HeaderLine());
		}

		String[] fieldValue = new String[1];
		ReadTableInfo tableStructure = printer
				.readTableInfo(PrinterConst.SMFP_TABLE_TEXT);
		int rowCount = tableStructure.getRowCount();
		for (int row = 1; row <= rowCount; row++) {
			int result = printer.readTable(PrinterConst.SMFP_TABLE_TEXT, row,
					1, fieldValue);
			if (printer.failed(result)) {
				break;
			}
			if (fieldValue[0].length() == 0) {
				result = printer.writeTable(PrinterConst.SMFP_TABLE_TEXT, row,
						1, " ");
				if (printer.failed(result)) {
					{
						break;
					}
				}
			}
		}
	}

	@Override
	public HeaderLine getHeaderLine(int number) throws Exception {
		checkHeaderLineNumber(number);
		return  header.get(number - 1);
	}

	@Override
	public HeaderLine getTrailerLine(int number) throws Exception {
		checkTrailerLineNumber(number);
		return  trailer.get(number - 1);
	}

	private void checkHeaderLineNumber(int number) throws Exception {
		if ((number < 1) || (number > getNumHeaderLines())) {
			throw new JposException(JposConst.JPOS_E_ILLEGAL,
					Localizer.getString(Localizer.InvalidLineNumber));
		}
	}

	private void checkTrailerLineNumber(int number) throws Exception {
		if ((number < 1) || (number > getNumTrailerLines())) {
			throw new JposException(JposConst.JPOS_E_ILLEGAL,
					Localizer.getString(Localizer.InvalidLineNumber));
		}
	}

	@Override
	public void setHeaderLine(int number, String text, boolean doubleWidth)
			throws Exception {
		checkHeaderLineNumber(number);
		int table = printer.getModel().getHeaderTableNumber();
		int row = printer.getModel().getHeaderTableRow();
		printer.writeTable(table, row + number - 1, 1, text);
		header.set(number - 1, new HeaderLine(text, doubleWidth));
	}

	@Override
	public void setTrailerLine(int number, String text, boolean doubleWidth)
			throws Exception {
		checkTrailerLineNumber(number);
		int table = printer.getModel().getTrailerTableNumber();
		int row = printer.getModel().getTrailerTableRow();
		printer.writeTable(table, row + number - 1, 1, text);
		trailer.set(number - 1, new HeaderLine(text, doubleWidth));
	}

	@Override
	public void beginDocument(String additionalHeader, String additionalTrailer)
			throws Exception {
		printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
		printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_HEADER);
		if (additionalHeader.length() > 0) {
			printer.printText(PrinterConst.SMFP_STATION_REC, additionalHeader,
					printer.getParams().getFont());
		}
	}

	@Override
	public void endDocument(String additionalHeader, String additionalTrailer)
			throws Exception {
	}
}
