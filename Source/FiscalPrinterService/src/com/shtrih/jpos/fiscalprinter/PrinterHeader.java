package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import java.util.Vector;

public interface PrinterHeader {

	public void clear();

	public int size();

	public void setCount(int count);

	public HeaderLine get(int index) throws Exception;

	public boolean validNumber(int number) throws Exception;

	public void addLine(String text) throws Exception;

	public void print() throws Exception;

	public void initDevice() throws Exception;

	public void setLine(int number, String text)
			throws Exception;

	public void writeLine(int number, String text) throws Exception;

        public void setAdditionalText(String text);
        
        public void printRecMessages(Vector printItems) throws Exception;
        
}
