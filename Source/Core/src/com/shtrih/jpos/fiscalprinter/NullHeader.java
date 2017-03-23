package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.util.Localizer;
import jpos.JposConst;
import jpos.JposException;

import java.util.Vector;

public class NullHeader implements PrinterHeader {

	private final SMFiscalPrinter printer;
	private final Vector<HeaderLine> header = new Vector<HeaderLine>();
	private final Vector<HeaderLine> trailer = new Vector<HeaderLine>();

	public NullHeader(SMFiscalPrinter printer) {
		this.printer = printer;
	}

	@Override
	public int getNumHeaderLines() throws Exception {
		return 0;
	}

	@Override
	public int getNumTrailerLines() throws Exception {
		return 0;
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
	}

	@Override
	public void setTrailerLine(int number, String text, boolean doubleWidth)
			throws Exception {
	}

	@Override
	public void beginDocument(String additionalHeader, String additionalTrailer)
			throws Exception {
	}

	@Override
	public void endDocument(String additionalHeader, String additionalTrailer)
			throws Exception {
	}
}
