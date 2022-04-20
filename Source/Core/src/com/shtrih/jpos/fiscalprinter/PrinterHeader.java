package com.shtrih.jpos.fiscalprinter;

import java.util.List;

public interface PrinterHeader {

	public void initDevice() throws Exception;

	public int getNumHeaderLines() throws Exception;

	public int getNumTrailerLines() throws Exception;

	public void setNumHeaderLines(int numHeaderLines) throws Exception;

	public void setNumTrailerLines(int numHeaderLines) throws Exception;

	public void setHeaderLine(int number, String text, boolean doubleWidth)
			throws Exception;

	public void setTrailerLine(int number, String text, boolean doubleWidth)
			throws Exception;

	public ReceiptLine getHeaderLine(int number) throws Exception;

	public ReceiptLine getTrailerLine(int number) throws Exception;

	public void beginDocument(String additionalHeader)
			throws Exception;

	public void endFiscal(String additionalTrailer)
			throws Exception;

	public void endNonFiscal(String additionalTrailer)
			throws Exception;
        
        public ReceiptLines getHeaderLines();
        
        public ReceiptLines getTrailerLines();
}
