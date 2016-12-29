package com.shtrih.fiscalprinter.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.PrinterHeader;
import com.shtrih.util.MathUtils;
//import com.shtrih.util.StaticContext;
import com.shtrih.util.StringUtils;

//import android.os.Environment;

public class TextDocumentFilter implements IPrinterEvents {

    private static CompositeLogger logger = CompositeLogger.getLogger(TextDocumentFilter.class);

	private boolean enabled = true;
	private String deviceName = "ККМ";
	private int operatorNumber = 0;
	private LongPrinterStatus status = null;
	private long receiptTotal = 0;
	private boolean receiptOpened = false;
	private boolean isFiscal = false;
	private boolean isEJPresent = false;
	private boolean isDocumentPrinted = false;
	private boolean connected = false;
	private final PrinterHeader header;
	private final SMFiscalPrinter printer;
	private XReport report = new XReport();
	private final String[] paymentNames = new String[4];
	private final List<Operator> operators = new ArrayList<Operator>();

	private static String SFiscalSign = "ФП";
	private static String SSaleText = "ПРОДАЖА";
	private static String SBuyText = "ПОКУПКА";
	private static String SRetSaleText = "ВОЗВРАТ ПРОДАЖИ";
	private static String SRetBuyText = "ВОЗВРАТ ПОКУПКИ";
	private static String SCashInText = "ВНЕСЕНИЕ";
	private static String SCashOutText = "ВЫПЛАТА";
	private static String SStornoText = "СТОРНО";
	private static String STotalText = "ВСЕГО";
	private static String SDiscountText = "СКИДКА";
	private static String SChargeText = "НАДБАВКА";
	private static String SReceiptTotal = "ИТОГ";
	private static String SCashPayment = "НАЛИЧНЫМИ";
	private static String SCreditPayment = "КРЕДИТОМ";
	private static String STarePayment = "ТАРОЙ";
	private static String SCardPayment = "ПЛАТ. КАРТОЙ";
	private static String SChangeText = "СДАЧА";
	private static String SReceiptCancelled = "ЧЕК АННУЛИРОВАН";
	private static String SDiscountStornoText = "СТОРНО СКИДКИ";
	private static String SChargeStornoText = "СТОРНО НАДБАВКИ";
	private static String SXReportText = "СУТОЧНЫЙ ОТЧЕТ БЕЗ ГАШЕНИЯ";
	private static String SZReportText = "СУТОЧНЫЙ ОТЧЕТ С ГАШЕНИЕМ";
	private static String SDayClosed = "СМЕНА ЗАКРЫТА";
	private static String[] docNames = { SSaleText, SBuyText, SRetSaleText, SRetBuyText };

	public TextDocumentFilter(SMFiscalPrinter printer, PrinterHeader header) {
		this.header = header;
		this.printer = printer;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean value) {
		this.enabled = value;
	}

	@Override
	public void beforeCommand(PrinterCommand command) {
		if (!enabled)
			return;
		if (command.isFailed())
			return;
		if (!printer.getParams().textReportEnabled)
			return;

		try {
			switch (command.getCode()) {
				case 0x85:
					receiptTotal = printer.getSubtotal();
					break;

				case 0x40:
				case 0x41:
                    connect();
					report = readXReport();
					break;

			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public void afterCommand(PrinterCommand command) {
		if (!enabled)
			return;
		if (command.isFailed())
			return;
		if (!printer.getParams().textReportEnabled)
			return;

		try {
			switch (command.getCode()) {

				case 0x12:
					add(((PrintBoldString) command).getLine());
					break;

				case 0x17:
					add(((PrintString) command).getLine());
					break;

				case 0x2F:
					add(((PrintStringFont) command).getLine());
					break;

				case 0x40:
					printXReport((PrintXReport) command);
					break;

				case 0x41:
					printZReport((PrintZReport) command);
					break;

				case 0x50:
					printCashIn((PrintCashIn) command);
					break;

				case 0x51:
					printCashOut((PrintCashOut) command);
					break;

				case 0x80:
					printSale((PrintSale) command);
					break;

				case 0x81:
					printRefund((PrintRefund) command);
					break;

				case 0x82:
					printVoidSale((PrintVoidSale) command);
					break;

				case 0x83:
					printVoidRefund((PrintVoidRefund) command);
					break;

				case 0x84:
					printVoidItem((PrintVoidItem) command);
					break;

				case 0x85:
					endFiscalReceipt((EndFiscalReceipt) command);
					break;

				case 0x86:
					printDiscount((PrintDiscount) command);
					break;

				case 0x87:
					printCharge((PrintCharge) command);
					break;

				case 0x88:
					cancelReceipt();
					break;

				case 0x8A:
					printVoidCharge((PrintVoidCharge) command);
					break;

				case 0x8B:
					printVoidDiscount((PrintVoidDiscount) command);
					break;

				case 0x8D:
					openReceipt((OpenReceipt) command);
					break;
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void openReceipt(OpenReceipt command) throws Exception {
		openReceipt2(command.getReceiptType());
	}

	private void openReceipt2(int receiptType) throws Exception {
		if (!receiptOpened) {
			receiptOpened = true;
			isDocumentPrinted = false;
			beginDocument();
			long recNumber = getNextRecNumber(getRecNumber(receiptType));
			add(docNames[receiptType], String.format("№%04d", recNumber));
		}
	}

	private long getNextRecNumber(long recNumber) throws Exception {
		recNumber = recNumber + 1;
		if (recNumber > 9999) {
			recNumber = 1;
		}
		return recNumber;
	}

	private long getRecNumber(int docType) throws Exception {
		switch (docType) {
			case PrinterConst.SMFP_RECTYPE_SALE:
				return printer.readOperationRegister(148);

			case PrinterConst.SMFP_RECTYPE_BUY:
				return printer.readOperationRegister(149);

			case PrinterConst.SMFP_RECTYPE_RETSALE:
				return printer.readOperationRegister(150);

			case PrinterConst.SMFP_RECTYPE_RETBUY:
				return printer.readOperationRegister(151);
			default: {
				return 0;
			}
		}
	}

	private void cancelReceipt() throws Exception {
		receiptOpened = false;
		add(SReceiptCancelled);
		endDocument();
	}

	private void printDiscount(PrintDiscount command) throws Exception {
		add(command.getItem().getText());
		add(SDiscountText, summToStr(command.getItem().getAmount()));
	}

	private void printVoidDiscount(PrintVoidDiscount command) throws Exception {
		add(command.getItem().getText());
		add(SDiscountStornoText, summToStr(command.getItem().getAmount()));
	}

	private void printCharge(PrintCharge command) throws Exception {
		add(command.getItem().getText());
		add(SChargeText, summToStr(command.getItem().getAmount()));
	}

	private void printVoidCharge(PrintVoidCharge command) throws Exception {
		add(command.getItem().getText());
		add(SChargeStornoText, summToStr(command.getItem().getAmount()));
	}

	private void endFiscalReceipt(EndFiscalReceipt command) throws Exception {
		CloseRecParams params = command.getParams();
		receiptOpened = false;

		add(params.getText());

		if (params.getDiscount() > 0) {
			// ВСЕГО
			add(STotalText, summToStr(receiptTotal));
			// СКИДКА
			long discountAmount = MathUtils.round(receiptTotal * params.getDiscount() / 100);
			String line = String.format("%s %s %%", SDiscountText, amountToStr(params.getDiscount()));
			add(line, summToStr(discountAmount)
					+ getTaxData(params.getTax1(), params.getTax2(), params.getTax3(), params.getTax4()));

			receiptTotal = receiptTotal - discountAmount;
		}
		// TOTAL =123.34
		add(SReceiptTotal, summToStr(receiptTotal));
		// Payments
		if (params.getSum1() > 0) {
			add(SCashPayment, summToStr(params.getSum1()));
		}
		if (params.getSum2() > 0) {
			add(SCreditPayment, summToStr(params.getSum2()));
		}
		if (params.getSum3() > 0) {
			add(STarePayment, summToStr(params.getSum3()));
		}
		if (params.getSum4() > 0) {
			add(SCardPayment, summToStr(params.getSum4()));
		}
		// Change
		if (command.getChange() > 0)
			add(SChangeText, summToStr(command.getChange()));

		addFiscalSign();
		readEJReport(true);
		endDocument();
	}

	private void printSale(PrintSale command) throws Exception {
		operatorNumber = command.getOperator();
		openReceipt2(PrinterConst.SMFP_RECTYPE_SALE);
		printReceiptItem(command.getItem());
	}

	private void printRefund(PrintRefund command) throws Exception {
		operatorNumber = command.getOperator();
		openReceipt2(PrinterConst.SMFP_RECTYPE_BUY);
		printReceiptItem(command.getItem());
	}

	private void printVoidSale(PrintVoidSale command) throws Exception {
		operatorNumber = command.getOperator();
		openReceipt2(PrinterConst.SMFP_RECTYPE_RETSALE);
		printReceiptItem(command.getItem());
	}

	private void printVoidRefund(PrintVoidRefund command) throws Exception {
		operatorNumber = command.getOperator();
		openReceipt2(PrinterConst.SMFP_RECTYPE_RETBUY);
		printReceiptItem(command.getItem());
	}

	private void printVoidItem(PrintVoidItem command) throws Exception {
		operatorNumber = command.getOperator();
		PriceItem item = command.getItem();
		// Line 1
		add(item.getText());
		// Line 2
		String line = String.format("%s X %s", quantityToStr(item.getQuantity()), amountToStr(item.getAmount()));
		add(SStornoText, line);
		// Line 3
		line = summToStr(item.getAmount(), item.getQuantity());
		add(String.format("%02d", item.getDepartment()), line);
	}

	private String amountToStr(long value) {
		return String.format("%d.%02d", value / 100, value % 100);
	}

	private String quantityToStr(long value) {
		return String.format("%d.%03d", value / 1000, value % 1000);
	}

	public String getTaxData(int tax1, int tax2, int tax3, int tax4) throws Exception {
		String result = "";
		String taxLetters = " АБВГ";
		if (tax1 > 0)
			result += taxLetters.charAt(tax1);
		if (tax2 > 0)
			result += taxLetters.charAt(tax2);
		if (tax3 > 0)
			result += taxLetters.charAt(tax3);
		if (tax4 > 0)
			result += taxLetters.charAt(tax4);
		if (result.length() > 0) {
			result = "_" + result;
		}
		return result;
	}

	private String summToStr(long price, long quantity) {
		long amount = MathUtils.round(quantity / 1000 * price);
		return summToStr(amount);
	}

	private String summToStr(long amount) {
		return "=" + amountToStr(amount);
	}

	private void printReceiptItem(PriceItem item) throws Exception {
		String line = "";
		add(item.getText());
		if (item.getQuantity() != 1000) {
			line = String.format("%s X %s", quantityToStr(item.getQuantity()), amountToStr(item.getPrice()));
			add("", line);

			line = summToStr(item.getPrice(), item.getQuantity())
					+ getTaxData(item.getTax1(), item.getTax2(), item.getTax3(), item.getTax4());
			add(String.format("%02d", item.getDepartment()), line);
		} else {
			long amount = MathUtils.round(item.getQuantity() / 1000 * item.getPrice());
			line = "=" + amountToStr(amount)
					+ getTaxData(item.getTax1(), item.getTax2(), item.getTax3(), item.getTax4());
			add(String.format("%02d", item.getDepartment()), line);
		}
	}

	public class Operator {
		private final int number;
		private final String name;

		public Operator(int number, String name) {
			this.number = number;
			this.name = name;
		}

		public int getNumber() {
			return number;
		}

		public String getName() {
			return name;
		}

	}

	public class Payment {
		public long amount;
		public String text;
	}

	public class Cash {
		public long number;
		public long count;
		public long total;
	}

	public class Receipt {
		public Receipt() {
			for (int i = 0; i < 4; i++) {
				payments.add(new Payment());
			}
		}

		public long number;
		public long count;
		public long total;
		public Vector<Payment> payments = new Vector<Payment>();
	}

	public class VoidReceipt {
		public long count;
		public long total;
	}

	public class XReport {
		public XReport() {
			for (int i = 0; i < 4; i++) {
				receipts.add(new Receipt());
			}
			for (int i = 0; i < 4; i++) {
				voidedReceipts.add(new VoidReceipt());
			}
		}

		public long salesAmountBefore;
		public long buyAmountBefore;
		public long xReportNumber;
		public long zReportNumber;
		public Vector<Receipt> receipts = new Vector<Receipt>();
		public Vector<VoidReceipt> voidedReceipts = new Vector<VoidReceipt>();
		public Cash cashIn = new Cash();
		public Cash cashOut = new Cash();
		public long voidCount;
		public long dayVoidCount;
		public long cashInDrawer;
		public long income;
		public long salesAmount;
		public long buyAmount;
	}

	public void connect() throws Exception {
		if (!connected) {
			deviceName = printer.getDeviceMetrics().getDeviceName();
			if (deviceName.contains("ПТК")) {
				deviceName = "ПТК";
			} else {
				deviceName = "ККМ";
			}
			status = printer.readLongStatus();
			operatorNumber = status.getOperatorNumber();
			isFiscal = (status.getRegistrationNumber() > 0);
			isEJPresent = status.getPrinterFlags().isEJPresent();
			for (int i = 0; i <= 3; i++) {
				String[] fieldValue = new String[1];
				printer.check(printer.readTable(PrinterConst.SMFP_TABLE_PAYTYPE, i + 1, 1, fieldValue));
				paymentNames[i] = fieldValue[0];
			}
			connected = true;
		}
	}

	public int getDayNumber() throws Exception {
		return printer.readLongStatus().getDayNumber();
	}

	public XReport readXReport() throws Exception {
		XReport report = new XReport();
		ReadFMTotals totals = printer.readFMTotals(1);
		report.salesAmountBefore = totals.getSalesTotal();
		report.buyAmountBefore = totals.getBuyTotal();
		report.xReportNumber = printer.readOperationRegister(158) + 1;
		report.zReportNumber = getDayNumber() + 1;
		for (int i = 0; i <= 3; i++) {
			Receipt receipt = report.receipts.get(i);
			receipt.number = printer.readOperationRegister(148 + i);
			receipt.count = printer.readOperationRegister(144 + i);
			long total = 0;
			for (int j = 0; j <= 3; j++) {
				long amount = printer.readCashRegister(193 + i + j * 4);
				total = total + amount;
				receipt.payments.get(j).amount = amount;
				receipt.payments.get(j).text = paymentNames[j];
			}
			receipt.total = total;
		}
		report.cashIn.number = printer.readOperationRegister(155);
		report.cashIn.count = printer.readOperationRegister(153);
		report.cashIn.total = printer.readCashRegister(242);
		report.cashOut.number = printer.readOperationRegister(156);
		report.cashOut.count = printer.readOperationRegister(154);
		report.cashOut.total = printer.readCashRegister(243);
		report.voidCount = printer.readOperationRegister(166);
		report.dayVoidCount = printer.readOperationRegister(157);
		report.cashInDrawer = printer.readCashRegister(241);
		report.income = report.receipts.get(0).total - report.receipts.get(1).total - report.receipts.get(2).total
				+ report.receipts.get(3).total;
		report.salesAmount = report.salesAmountBefore + report.receipts.get(0).total;
		report.buyAmount = report.buyAmountBefore + report.receipts.get(1).total;
		for (int i = 0; i <= 3; i++) {
			report.voidedReceipts.get(i).count = printer.readOperationRegister(179 + i);
			report.voidedReceipts.get(i).total = printer.readCashRegister(249 + i);
		}
		return report;
	}

	@Override
	public void printerStatusRead(PrinterStatus status) {
	}

	public void printCashIn(PrintCashIn command) throws Exception {
		isDocumentPrinted = true;
		operatorNumber = command.getOperator();
		long docNumber = printer.readCashRegister(155);
		printer.waitForPrinting();

		beginDocument();
		add(SCashInText, getDocumentNumber(docNumber));
		add("", amountToStr(command.getAmount()));
		endDocument();
	}

	public void printCashOut(PrintCashOut command) throws Exception {
		isDocumentPrinted = true;
		operatorNumber = command.getOperator();
		long docNumber = printer.readCashRegister(156);
		printer.waitForPrinting();

		beginDocument();
		add(SCashOutText, getDocumentNumber(docNumber));
		add("", amountToStr(command.getAmount()));
		endDocument();
	}

	public String getDocumentNumber(long value) {
		return String.format("%04d", value);
	}

	public void add(String text, long value) throws Exception {
		add(text, amountToStr(value));
	}

	public int getLineLength() throws Exception {
		return printer.getModel().getTextLength(FontNumber.getNormalFont());
	}

	public void add(String s1, String s2) throws Exception {
		int lineLength = getLineLength();
		if (s2.length() > lineLength) {
			s2 = s2.substring(0, lineLength);
		}
		int len = lineLength - s2.length();
		if (s1.length() > len) {
			s1 = s1.substring(0, len);
		}
		len = lineLength - s1.length() - s2.length();
		String line = s1 + StringUtils.stringOfChar(' ', len) + s2;
		add(line);
	}

	@Override
	public void init() {

	}

	@Override
	public void done() {
	}

	public void add(String line) throws Exception {
		logger.debug("add(" + line + ")");
		File path = new File("."); //StaticContext.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
		File file = new File(path, printer.getParams().textReportFileName);
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		writer.println(line);
		writer.flush();
		writer.close();            // TODO: with/using
	}

	private void beginDocument() throws Exception {
		logger.debug("beginDocument");

//		int count = header.getNumHeaderLines();
//		for (int i = 1; i <= count; i++) {
//			add(header.getHeaderLine(i).getText());
//		}
		printReceiptHeader();
	}

	public void endDocument() throws Exception {
		logger.debug("endDocument");
//		int count = header.getNumTrailerLines();
//		for (int i = 1; i <= count; i++) {
//			add(header.getTrailerLine(i).getText());
//		}
	}

	private void printReceiptHeader() throws Exception {
		logger.debug("printReceiptHeader");
		status = printer.readLongStatus();
		int documentNumber = status.getDocumentNumber();
		if (!isDocumentPrinted)
			documentNumber += 1;

		// ККМ
		String s1 = String.format("%s %s", deviceName, status.getSerial());
		String s2 = String.format("ИНН %s #%04d", status.getFiscalIDText(), documentNumber);
		add(s1, s2);
		// Кассир
		PrinterDate date = status.getDate();
		PrinterTime time = status.getTime();
		String s = String.format("%02d.%02d.%02d %02d:%02d", date.getDay(), date.getMonth(), date.getYear(),
				time.getHour(), time.getMin());
		add(s, getOperatorName());
	}

	public String getOperatorName() throws Exception {
		for (int i = 0; i < operators.size(); i++) {
			Operator operator = operators.get(i);
			if (operator.getNumber() == operatorNumber) {
				return operator.getName();
			}
		}
		String[] fieldValue = new String[1];
		printer.check(printer.readTable(PrinterConst.SMFP_TABLE_CASHIER, operatorNumber, 2, fieldValue));
		Operator operator = new Operator(operatorNumber, fieldValue[0]);
		operators.add(operator);
		return operator.getName();
	}

	public void printXReport(PrintXReport command) throws Exception {
		isDocumentPrinted = true;
		operatorNumber = command.getOperator();
		printer.waitForPrinting();

		beginDocument();
		add(SXReportText, String.format("№%04d", report.xReportNumber));
		printXZReport();
		endDocument();
	}

	public void printZReport(PrintZReport command) throws Exception {
		isDocumentPrinted = true;
		operatorNumber = command.getOperator();
		printer.waitForPrinting();

		beginDocument();
		add(SZReportText, String.format("№%04d", report.zReportNumber));
		printXZReport();
		addFiscalSign();
		addCenter('*', SDayClosed);
		readEJReport(false);
		endDocument();
	}

	public void printXZReport() throws Exception {
		String[] ReceiptText = { "ЧЕКОВ ПРОДАЖ", "ЧЕКОВ ПОКУПОК", "ЧЕКОВ ВОЗВРАТОВ ПРОДАЖ", "ЧЕКОВ ВОЗВРАТОВ ПОКУПОК" };
		String[] voidedReceiptText = { "ПРОДАЖ", "ПОКУПОК", "ВОЗВР.ПРОДАЖ", "ВОЗВР.ПОКУПОК" };
		add("НЕОБНУЛ.СУММА ПРОДАЖ НА НАЧ.СМЕНЫ", report.salesAmountBefore);
		add("НЕОБНУЛ.СУММА ПОКУПОК НА НАЧ.СМЕНЫ", report.buyAmountBefore);
		for (int i = 0; i <= 3; i++) {
			Receipt receipt = report.receipts.get(i);
			add(ReceiptText[i], String.format("%04d", receipt.number));
			add(String.format("%04d", receipt.count), receipt.total);
			add(receipt.payments.get(0).text, receipt.payments.get(0).amount);
			add(receipt.payments.get(1).text, receipt.payments.get(1).amount);
			add(receipt.payments.get(2).text, receipt.payments.get(2).amount);
			add(receipt.payments.get(3).text, receipt.payments.get(3).amount);
		}
		add("ВНЕСЕНИЙ", String.format("%04d", report.cashIn.number));
		add(String.format("%04d", report.cashIn.count), report.cashIn.total);
		add("ВЫПЛАТ", String.format("%04d", report.cashOut.number));
		add(String.format("%04d", report.cashOut.count), report.cashOut.total);
		add("АННУЛИРОВАННЫХ ЧЕКОВ", String.format("%04d", report.voidCount));
		add(String.format("%04d", report.dayVoidCount));
		for (int i = 0; i <= 3; i++) {
			String line = String.format("%04d %s", report.voidedReceipts.get(i).count, voidedReceiptText[i]);
			add(line, report.voidedReceipts.get(i).total);
		}
		add("НАЛ. В КАССЕ", report.cashInDrawer);
		add("ВЫРУЧКА", report.income);
		add("НЕОБНУЛ. СУММА ПРОДАЖ", report.salesAmount);
		add("НЕОБ. СУММА ПОКУПОК", report.buyAmount);
	}

	public void addFiscalSign() throws Exception {
		if (isFiscal) {
			addCenter('-', SFiscalSign);
		}
	}

	public void addCenter(char c, String text) throws Exception {
		int lineLength = getLineLength();
		int l = (lineLength - text.length()) / 2;
		String line = StringUtils.stringOfChar(c, l) + text;
		line = line + StringUtils.stringOfChar(c, lineLength - line.length());
		add(line);
	}

	public void readEJReport(boolean isReceipt) throws Exception {
		if (!(isFiscal && isEJPresent))
			return;

		Vector<String> lines = new Vector();
		long docMACNumber = printer.readEJStatus().getStatus().getDocMACNumber();
		ReadEJDocument command = new ReadEJDocument(printer.getSysPassword(), (int) docMACNumber);
		printer.execute(command);
		lines.add(command.getEcrModel());

		ReadEJDocumentLine command2 = new ReadEJDocumentLine();
		command2.setPassword(printer.getSysPassword());
		int rc = 0;
		while (printer.succeeded(rc)) {
			rc = printer.executeCommand(command2);
			if (printer.succeeded(rc)) {
				String line = command2.getData();
				lines.add(line);
				if ((line.length() > 0) && (line.contains("#"))) {
					printer.cancelEJDocument();
					break;
				}

			}
		}

		if (isReceipt) {
			// 2 last lines
			if (lines.size() > 2) {
				addEJLine(lines.get(2));
				addEJLine(lines.get(lines.size() - 1));
			}
		} else {
			for (int i = 0; i < lines.size(); i++) {
				addEJLine(lines.get(i));
			}
		}
	}

	private void addEJLine(String s) throws Exception {
		add("        " + s);
	}

}
