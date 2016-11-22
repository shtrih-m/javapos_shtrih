/*
 * Localizer.java
 *
 * Created on 7 Июль 2010 г., 16:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.shtrih.util.CompositeLogger;

public class Localizer {

	private static CompositeLogger logger = CompositeLogger.getLogger(Localizer.class);
	public static final String receiptDuplicationNotSupported = "ReceiptDuplicationNotSupported";
	public static final String deviceIsEnabled = "DeviceIsEnabled";
	public static final String additionalHeaderNotSupported = "AdditionalHeaderNotSupported";
	public static final String additionalTrailerNotSupported = "AdditionalTrailerNotSupported";
	public static final String changeDueTextNotSupported = "ChangeDueTextNotSupported";
	public static final String multipleContractorsNotSupported = "MultipleContractorsNotSupported";
	public static final String messageTypeNotSupported = "MessageTypeNotSupported";
	public static final String methodNotSupported = "MethodNotSupported";
	public static final String invalidParameterValue = "InvalidParameterValue";
	public static final String invalidPropertyValue = "InvalidPropertyValue";
	public static final String notPaidReceiptsNotSupported = "NotPaidReceiptsNotSupported";
	public static final String invalidFiscalReceiptType = "InvalidFiscalReceiptType";
	public static final String failedConfirmDate = "FailedConfirmDate";
	public static final String wrongPrinterState = "WrongPrinterState";
	public static final String slipStationNotPresent = "SlipStationNotPresent";
	public static final String receiptStationNotPresent = "ReceiptStationNotPresent";
	public static final String journalStationNotPresent = "JournalStationNotPresent";
	public static final String graphicsNotSupported = "GraphicsNotSupported";
	public static final String endDumpFailed = "EndDumpFailed";
	public static final String LockedTaxPassword = "LockedTaxPassword";
	public static final String ConfirmDateFailed = "ConfirmDateFailed";
	public static final String WriteDecimalPointFailed = "WriteDecimalPointFailed";
	public static final String StopTestFailed = "StopTestFailed";
	public static final String PrinterSupportesEAN13Only = "PrinterSupportesEAN13Only";
	public static final String InvalidBarcodeHeight = "InvalidBarcodeHeight";
	public static final String InvalidBarcodePrintType = "InvalidBarcodePrintType";
	public static final String InvalidAnswerLength = "InvalidAnswerLength";
	public static final String InvalidFieldValue = "InvalidFieldValue";
	public static final String NoConnection = "NoConnection";
	public static final String ReadAnswerError = "ReadAnswerError";
	public static final String WriteCommandError = "WriteCommandError";
	public static final String ReceiveTimeoutNotSupported = "ReceiveTimeoutNotSupported";
	public static final String NotImplemented = "NotImplemented";
	public static final String CommandNotFound = "CommandNotFound";
	public static final String NullDataParameter = "NullDataParameter";
	public static final String NullObjectParameter = "NullObjectParameter";
	public static final String InsufficientDataLen = "InsufficientDataLen";
	public static final String InsufficientObjectLen = "InsufficientObjectLen";
	public static final String BarcodeTypeNotSupported = "BarcodeTypeNotSupported";
	public static final String BarcodeExceedsPrintWidth = "BarcodeExceedsPrintWidth";
	public static final String FailedCancelReceipt = "FailedCancelReceipt";
	public static final String BaudrateNotSupported = "BaudrateNotSupported";
	public static final String InvalidLineNumber = "InvalidLineNumber";
	public static final String InvalidImageHeight = "InvalidImageHeight";
	public static final String InvalidImageWidth = "InvalidImageWidth";
	public static final String InvalidImageIndex = "InvalidImageIndex";
	public static final String PropNotFound = "PropNotFound";
	public static final String UnknownPrinterError = "UnknownPrinterError";
	public static final String InternalHealthCheck = "InternalHealthCheck";
	public static final String RecPaperEmpty = "RecPaperEmpty";
	public static final String RecPaperNearEnd = "RecPaperNearEnd";
	public static final String RecLeverUp = "RecLeverUp";
	public static final String JrnPaperEmpty = "JrnPaperEmpty";
	public static final String JrnPaperNearEnd = "JrnPaperNearEnd";
	public static final String JrnLeverUp = "JrnLeverUp";
	public static final String EJNearFull = "EJNearFull";
	public static final String FMOverflow = "FMOverflow";
	public static final String FMLowBattery = "FMLowBattery";
	public static final String FMLastRecordCorrupted = "FMLastRecordCorrupted";
	public static final String FMEndDayRequired = "FMEndDayRequired";
	public static final String NoErrors = "NoErrors";
	public static final String PhysicalDeviceDescription = "PhysicalDeviceDescription";
	private static Localizer instance;
	private ResourceBundle bundle = null;
	private final Properties properties = new Properties();

	/**
	 * Creates a new instance of Localizer
	 */
	private Localizer(String resourceName) {
		initProperties();
		loadFromResource(resourceName);
	}

	public void loadFromResource(String resourceName) {
		try {
                        InputStream stream = getClass().getResourceAsStream(
				"/res/" + resourceName);
                        if (stream == null){
                            logger.debug("Failed to create resource stream");
                            return;
                        }
                        
			bundle = new PropertyResourceBundle(stream);
                        if (bundle == null){
                            logger.debug("Failed to create PropertyresourceBundle");
                            return;
                        }
		} catch (Exception e) {
			logger.error("loadFromResource: " + e.getMessage());
		}
	}

	public static Localizer getInstance() {
		if (instance == null) {
			instance = new Localizer("messages_ru.txt");
		}
		return instance;
	}

	public static void init(String fileName) {
		instance = new Localizer("messages_ru.txt");
	}

	private String getResourceString(String key) throws Exception {
		if (bundle != null){
			String value = bundle.getString(key);
			value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
			return value;
		} else {
			return properties.getProperty(key);
		}
	}

	public static String getString(String key) {
		try {
			return getInstance().getResourceString(key);
		} catch (Exception e) {
			return key;
		}
	}

	private void add(String key, String value) {
		properties.setProperty(key, value);
	}

	private void initProperties() {
		properties.clear();
		add(receiptDuplicationNotSupported,
				"Receipt duplication is not supported");
		add(deviceIsEnabled, "Device is enabled");
		add(additionalHeaderNotSupported, "Additional header is not supported");
		add(additionalTrailerNotSupported,
				"Additional trailer is not supported");
		add(changeDueTextNotSupported, "Change due text is not supported");
		add(multipleContractorsNotSupported,
				"Multiple contractors are not supported");
		add(messageTypeNotSupported, "MessageType is not supported");
		add(methodNotSupported,
				"Method not supported for selected FiscalReceiptType");
		add(invalidParameterValue, "Invalid parameter value");
		add(invalidPropertyValue, "Invalid property value");
		add(notPaidReceiptsNotSupported, "Not paid receipts is not supported");
		add(invalidFiscalReceiptType, "Invalid FiscalReceiptType value");
		add(failedConfirmDate, "Failed to confirm date. ");
		add(wrongPrinterState, "Wrong printer state");
		add(slipStationNotPresent, "Slip station not present");
		add(receiptStationNotPresent, "Receipt station not present");
		add(journalStationNotPresent, "Journal station not present");
		add(graphicsNotSupported, "Graphics not supported");
		add(endDumpFailed, "Can not change dump state");
		add(LockedTaxPassword, "Locked for invalid tax officer password");
		add(ConfirmDateFailed, "Can not change wait date state");
		add(WriteDecimalPointFailed, "Can not change point state");
		add(StopTestFailed, "Can not change test state");
		add(PrinterSupportesEAN13Only, "Printer supportes barcode EAN-13 only");
		add(InvalidBarcodeHeight, "Barcode height <= 0");
		add(InvalidBarcodePrintType, "Invalid barcode print type");
		add(InvalidAnswerLength, "Invalid answer data length");
		add(InvalidFieldValue, "Invalid field value");
		add(NoConnection, "No connection");
		add(ReceiveTimeoutNotSupported,
				"Receive timeout is not supported by port driver");
		add(NotImplemented, "Not implemented");
		add(CommandNotFound, "Command not found");
		add(NullDataParameter, "Data parameter cannot be null");
		add(NullObjectParameter, "Object parameter cannot be null");
		add(InsufficientDataLen, "Data parameter length must be greater than ");
		add(InsufficientObjectLen,
				"Object parameter length must be greater than ");
		add(BarcodeTypeNotSupported, "Barcode type is not supported");
		add(BarcodeExceedsPrintWidth, "Barcode exceeds print width");
		add(FailedCancelReceipt, "Can not cancel receipt");
		add(BaudrateNotSupported, "Baud rate is not supported");
		add(InvalidLineNumber, "Invalid line number value");
		add(InvalidImageHeight, "Image height exceeds maximum");
		add(InvalidImageWidth, "Image width exceeds maximum");
		add(InvalidImageIndex, "Invalid image index");
		add(PropNotFound, "Property not found, ");
                
                add("FSPrinterError01", "FS: Unknown command or invalid format");
                add("FSPrinterError02", "FS: Incorrect status");
                add("FSPrinterError03", "FS: Error, read extended error code");
                add("FSPrinterError04", "FS: CRC error, read extended error code");
                add("FSPrinterError05", "FS: Device expired");
                add("FSPrinterError06", "FS: Archive overflow error");
                add("FSPrinterError07", "FS: Invalid date/time value");
                add("FSPrinterError08", "FS: No data available");
                add("FSPrinterError09", "FS: Invalid parameter value");
                add("FSPrinterError10", "FS: TLV size too large");
                add("FSPrinterError11", "FS: No transport connection ");
                add("FSPrinterError12", "FS: Cryptographic coprocessor resource exhausted");
                add("FSPrinterError14", "FS: Documents resource exhausted");
                add("FSPrinterError15", "FS: Maximum send time limit reached");
                add("FSPrinterError16", "FS: Fiscal day last for 24 hours");
                add("FSPrinterError17", "FS: Invalid time range betseen two operations");
                add("FSPrinterError20", "FS: Server message can not be received");
                
		add("PrinterError00", "No errors");
		add("PrinterError01", "FM: FM1 FM2 or RTC error");
		add("PrinterError02", "FM: FM1 missing");
		add("PrinterError03", "FM: FM2 missing");
		add("PrinterError04", "FM: Incorrect parameters in FM command");
		add("PrinterError05", "FM: No data requested available");
		add("PrinterError06", "FM: FM is in data output mode");
		add("PrinterError07", "FM: Invalid FM command parameters");
		add("PrinterError08", "FM: Command is not supported by FM");
		add("PrinterError09", "FM: Invalid command length");
		add("PrinterError0A", "FM: Data format is not BCD");
		add("PrinterError0B", "FM: FM memory failure");
		add("PrinterError0C", "FM: Gross amount overflow");
		add("PrinterError0D", "FM: Day totals overflow");
		add("PrinterError11", "FM: License in not entered");
		add("PrinterError12", "FM: Serial number is already entered");
		add("PrinterError13", "FM: Current date less than last record date");
		add("PrinterError14", "FM: Day total area overflow");
		add("PrinterError15", "FM: Day is opened");
		add("PrinterError16", "FM: Day is closed");
		add("PrinterError17", "FM: First day number more than last day number");
		add("PrinterError18", "FM: First day date more than last day date");
		add("PrinterError19", "FM: No data available");
		add("PrinterError1A", "FM: Fiscalization area overflow");
		add("PrinterError1B", "FM: Serial number not assigned");
		add("PrinterError1C",
				"FM: There is corrupted record in the defined range");
		add("PrinterError1D", "FM: Last day record is corrupted");
		add("PrinterError1E", "FM: Fiscalizations overflow");
		add("PrinterError1F", "FM: Registers memory is missing");
		add("PrinterError20", "FM: Cash register overflow after add");
		add("PrinterError21",
				"FM: Subtracted amount is more then cash register value");
		add("PrinterError22", "FM: Invalid date");
		add("PrinterError23", "FM: No activation record available");
		add("PrinterError24", "FM: Activation area overflow");
		add("PrinterError25", "FM: Activation with specified number not found");
		add("PrinterError26", "FM: More than 3 records corrupted");
		add("PrinterError27", "FM: Check sum error");

		add("PrinterError28", "FM: Technological sign is present");
		add("PrinterError29", "FM: Technological sign is not present");
		add("PrinterError2A",
				"FM: FM size is not matched with firmware version");
		add("PrinterError2B", "Previous command cannot be cancelled");
		add("PrinterError2C", "Fiscal day is closed");
		add("PrinterError2D", "Receipt total less that discount amount");
		add("PrinterError2E", "There is not enough money in ECR");
		add("PrinterError2F", "Serial number in memory and FM are mismatched");

		add("PrinterError30", "EJ: NAK returned");
		add("PrinterError31", "EJ: Format error");
		add("PrinterError32", "EJ: CRC error");
		add("PrinterError33", "Incorrect command parameters");
		add("PrinterError34", "No data available");
		add("PrinterError35", "Settings: Incorrect command parameters");
		add("PrinterError36", "Model: Incorrect command parameters");
		add("PrinterError37", "Command is not supported");
		add("PrinterError38", "PROM error");
		add("PrinterError39", "Internal software error");
		add("PrinterError3A", "Day charge total overflow");
		add("PrinterError3B", "Day total overflow");
		add("PrinterError3C", "EJ: Invalid registration number");
		add("PrinterError3D", "Day closed, operation is invalid");
		add("PrinterError3E", "Day departments amount overflow");
		add("PrinterError3F", "Day discount total overflow");
		add("PrinterError40", "Discount range error");
		add("PrinterError41", "Cash amount range overflow");
		add("PrinterError42", "Pay type 2 amount range overflow");
		add("PrinterError43", "Pay type 3 amount range overflow");
		add("PrinterError44", "Pay type 4 amount range overflow");
		add("PrinterError45", "Payment total less than receipt total");
		add("PrinterError46", "No enough cash in ECR");
		add("PrinterError47", "Day tax accumulator overflow");
		add("PrinterError48", "Receipt total overflow");
		add("PrinterError49", "Receipt is opened. Command is invalid");
		add("PrinterError4A", "Receipt is opened. Command is invalid");
		add("PrinterError4B", "Receipt buffer overflow");
		add("PrinterError4C", "Dayly tax turnover accumulator overflow");
		add("PrinterError4D", "Cashless amount is greater than receipt total");
		add("PrinterError4E", "Day exceedes 24 hours");
		add("PrinterError4F", "Invalid password");
		add("PrinterError50", "Printing previous command");
		add("PrinterError51", "Day cash accumulator overflow");
		add("PrinterError52", "Day payment type 2 accumulator overflow");
		add("PrinterError53", "Day payment type 3 accumulator overflow");
		add("PrinterError54", "Day payment type 4 accumulator overflow");
		add("PrinterError55", "Receipt closed, operation is invalid");
		add("PrinterError56", "No document to repeat");
		add("PrinterError57", "EJ: Day count not equal to FM day count");
		add("PrinterError58", "Waiting for repeat print command");
		add("PrinterError59", "Document is opened by another operator");
		add("PrinterError5A", "Discount sum more than receipt sum");
		add("PrinterError5B", "Charge accumulator overflow");
		add("PrinterError5C", "Low power supply voltage, 24v");
		add("PrinterError5D", "Table is undefined");
		add("PrinterError5E", "Invalid command");
		add("PrinterError5F", "Negative receipt total");
		add("PrinterError60", "Overflow on multiplication");
		add("PrinterError61", "Price range overflow");
		add("PrinterError62", "Quantity range overflow");
		add("PrinterError63", "Department range overflow");
		add("PrinterError64", "FM missing");
		add("PrinterError65", "No cash in department");
		add("PrinterError66", "Department total overflow");
		add("PrinterError67", "FM connection error");
		add("PrinterError68", "Insufficient tax turnover amount");
		add("PrinterError69", "Tax turnover overflow");
		add("PrinterError6A", "Power supply error on reading I2C answer");
		add("PrinterError6B", "No receipt paper");
		add("PrinterError6C", "No journal paper");
		add("PrinterError6D", "Insufficient tax amount");
		add("PrinterError6E", "Tax amount overflow");
		add("PrinterError6F", "Daily cash out overflow");
		add("PrinterError70", "FM overflow");
		add("PrinterError71", "Cutter failure");
		add("PrinterError72", "Command not supported in this submode");
		add("PrinterError73", "Command not supported in this mode");
		add("PrinterError74", "RAM failure");
		add("PrinterError75", "Power supply failure");
		add("PrinterError76", "Printer failure: no pulse");
		add("PrinterError77", "Printer failure: no sensor");
		add("PrinterError78", "Software replaced");
		add("PrinterError79", "FM replaced");
		add("PrinterError7A", "Field cannot be edited");
		add("PrinterError7B", "Hardware failure");
		add("PrinterError7C", "Invalid date");
		add("PrinterError7D", "Invalid date format");
		add("PrinterError7E", "Invalid frame length");
		add("PrinterError7F", "Total amount overflow");
		add("PrinterError80", "FM connection error");
		add("PrinterError81", "FM connection error");
		add("PrinterError82", "FM connection error");
		add("PrinterError83", "FM connection error");
		add("PrinterError84", "Cash amount overflow");
		add("PrinterError85", "Daily sale total overflow");
		add("PrinterError86", "Daily buy total overflow");
		add("PrinterError87", "Daily return sale total overflow");
		add("PrinterError88", "Daily return buy total overflow");
		add("PrinterError89", "Daily cash in total overflow");
		add("PrinterError8A", "Receipt charge total overflow");
		add("PrinterError8B", "Receipt discount total overflow");
		add("PrinterError8C", "Negative receipt charge total");
		add("PrinterError8D", "Negative receipt discount total");
		add("PrinterError8E", "Zero receipt total");
		add("PrinterError8F", "Non fiscal printer");
		add("PrinterError90", "Field range overflow");
		add("PrinterError91", "Print width error");
		add("PrinterError92", "Field overflow");
		add("PrinterError93", "RAM recovery successful");
		add("PrinterError94", "Receipt operation limit");
		add("PrinterError95", "Unknown electronic journal error");
		add("PrinterError96", "Day end required");
		add("PrinterError9B", "Incorrect operation");
		add("PrinterError9C", "Item code is not found");
		add("PrinterError9D", "Incorrect item data");
		add("PrinterError9E", "Invalid item data size");
		add("PrinterErrorA0", "EJ connection error");
		add("PrinterErrorA1", "EJ missing");
		add("PrinterErrorA2", "EJ: Invalid parameter or command format");
		add("PrinterErrorA3", "EJ: Invalid state");
		add("PrinterErrorA4", "EJ: Failure");
		add("PrinterErrorA5", "EJ: Cryptoprocessor failure");
		add("PrinterErrorA6", "EJ: Time limit exceeded");
		add("PrinterErrorA7", "EJ: Overflow");
		add("PrinterErrorA8", "EJ: Invalid date or time");
		add("PrinterErrorA9", "EJ: No data available");
		add("PrinterErrorAA", "EJ: Negative document total");
		add("PrinterErrorAF", "Incorrect received EJ data");
		add("PrinterErrorB0", "EJ: Quantity parameter overflow");
		add("PrinterErrorB1", "EJ: Amount parameter overflow");
		add("PrinterErrorB2", "EJ: Already activated");
		add("PrinterErrorB4", "Fiscalization record corrupted");
		add("PrinterErrorB5", "Serial number record corrupted");
		add("PrinterErrorB6", "Activation record corrupted");
		add("PrinterErrorB7", "Day totals records not found");
		add("PrinterErrorB8", "Last day total record not written");
		add("PrinterErrorB9", "FM data version mismatch");
		add("PrinterErrorBA", "FM data corrupted");
		add("PrinterErrorBB", "Current date less than EJ activation date");
		add("PrinterErrorBC", "Current date less than fiscalization date");
		add("PrinterErrorBD", "Current date less than last Z-report date");
		add("PrinterErrorBE", "Command is not supported in current state");
		add("PrinterErrorBF", "FM initialization is impossible");
		add("PrinterErrorC0", "Confirm date and time");
		add("PrinterErrorC1", "EJ report can not be interrupted");
		add("PrinterErrorC2", "Increased supply voltage");
		add("PrinterErrorC3", "Receipt total not equal to EJ receipt total");
		add("PrinterErrorC4", "Day number mismatch");
		add("PrinterErrorC5", "Slip buffer is empty");
		add("PrinterErrorC6", "Slip paper is missing");
		add("PrinterErrorC7", "Field is not editable in this mode");
		add("PrinterErrorC8", "Printer connection error");
		add("PrinterErrorC9", "Thermal head overheated");
		add("PrinterErrorCA", "Temperature is not in valid range");
		add("PrinterErrorCB", "Invalid receipt subtotal");
		add("PrinterErrorCC", "Fiscal day in EJ is opened");
		add("PrinterErrorCD", "EJ archive test is failed");
		add("PrinterErrorCE", "RAM or ROM overflow");
		add("PrinterErrorCF", "Invalid date. Please, set date");
		add("PrinterErrorD0", "EJ: Daily journal not printed");
		add("PrinterErrorD1", "No document in buffer");
		add("PrinterErrorD5", "Critical error when loading ERRxx");
		add("PrinterErrorE0", "Bill acceptor connection error");
		add("PrinterErrorE1", "Bill acceptor is busy");
		add("PrinterErrorE2", "Bill acceptor receipt total mismatch");
		add("PrinterErrorE3", "Bill acceptor error");
		add("PrinterErrorE4", "Bill acceptor total not zero");
		add(UnknownPrinterError, "Unknown printer error");
		add(InternalHealthCheck, "Internal health check");
		add(RecPaperEmpty, "Receipt paper is empty.");
		add(RecPaperNearEnd, "Receipt paper is near end.");
		add(RecLeverUp, "Receipt station lever is up.");
		add(JrnPaperEmpty, "Journal paper is empty.");
		add(JrnPaperNearEnd, "Journal paper is near end.");
		add(JrnLeverUp, "Journal station lever is up.");
		add(EJNearFull, "Electronic journal is near full.");
		add(FMOverflow, "Fiscal memory overflow.");
		add(FMLowBattery, "Low fiscal memory battery voltage.");
		add(FMLastRecordCorrupted, "Last fiscal memory record currupted.");
		add(FMEndDayRequired, "Fiscal memory fiscal day is over.");
		add(NoErrors, "No errors");
		add(PhysicalDeviceDescription,
				"%s,  %s, Printer firmware: %s.%d, %s, FM firmware: %s.%d, %s");
	}
}
