package com.shtrih.tinyjavapostester;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.google.zxing.EncodeHintType;
import com.google.zxing.pdf417.encoder.Compaction;
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.util.StaticContext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import jpos.FiscalPrinter;
import jpos.JposConst;
import jpos.JposException;

public class MainActivity extends Activity implements OnClickListener {

	private static Logger logger = Logger.getLogger(MainActivity.class);
	private ShtrihFiscalPrinter printer = null;
	private final static String TAG = MainActivity.class.getSimpleName();
	private final Random rand = new Random();
	public static boolean stopPlease = false;
	private final String[] items = { "Кружка", "Ложка", "Миска", "Нож" };
	TextView printReceipt, btnPrintZReport, stopReceipt, btnOpenFiscalDay, btnPrintDayJournal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ConfigureLog4J.configure();
		StaticContext.setContext(getApplicationContext());
		setContentView(R.layout.activity_main);
		printer = new ShtrihFiscalPrinter(new FiscalPrinter());
		printReceipt = (TextView) findViewById(R.id.printReceipt);
		btnPrintZReport = (TextView) findViewById(R.id.btnPrintZReport);
		btnOpenFiscalDay = (TextView) findViewById(R.id.btnOpenFiscalDay);
		stopReceipt = (TextView) findViewById(R.id.StopReceipt);
		printReceipt.setOnClickListener(this);
		btnPrintZReport.setOnClickListener(this);
		stopReceipt.setOnClickListener(this);
		btnOpenFiscalDay.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case DeviceListActivity.REQUEST_CONNECT_BT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {

				String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				try {
					connectToDevice(address);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	public void connectToDevice(final String address) throws Exception {
		JposConfig.configure("ShtrihFptr", address);

		if (printer.getState() != JposConst.JPOS_S_CLOSED) {
			printer.close();
		}
		printer.open("ShtrihFptr");
		printer.claim(3000);
		printer.setDeviceEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_connect_ptk) {
			Intent i = new Intent(this, DeviceListActivity.class);
			startActivityForResult(i, DeviceListActivity.REQUEST_CONNECT_BT_DEVICE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	final Runnable printRecRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				stopPlease = false;
				while (!stopPlease) {
					printReceipt2();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	private void printBarcodeSimple() throws JposException, InterruptedException {
		PrinterBarcode barcode = new PrinterBarcode();
		barcode.setText("SHTRIH-M, Moscow, 2015");
		barcode.setLabel("PDF417 test");
		barcode.setType(SmFptrConst.SMFPTR_BARCODE_PDF417);
		barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
		barcode.setHeight(100);
		barcode.setBarWidth(2);
		printer.printBarcode(barcode);
	}

	private byte[] readFile(String filePath) throws Exception {
		FileInputStream is = new FileInputStream(new File(filePath));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		while (is.available() > 0) {
			os.write(is.read());
		}
		is.close();
		return os.toByteArray();
	}

	private void printBarcodeBinary() throws Exception {
		File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File picsDir = new File(dcimDir, "Camera");
		picsDir.mkdirs(); // make if not exist
		File barcodeFile = new File(picsDir, "barcode.bin");
		byte[] bc = readFile(barcodeFile.getAbsolutePath());
		PrinterBarcode barcode = new PrinterBarcode();
		barcode.setText(new String(bc, "ISO-8859-1"));
		barcode.setLabel("PDF417 test");
		barcode.setType(SmFptrConst.SMFPTR_BARCODE_PDF417);
		barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
		barcode.setTextPosition(SmFptrConst.SMFPTR_TEXTPOS_NOTPRINTED);

		Map<EncodeHintType, Object> params = new HashMap<EncodeHintType, Object>(0);
		params.put(EncodeHintType.PDF417_COMPACTION, Compaction.BYTE);
		// params.put(EncodeHintType.PDF417_DIMENSIONS, new Dimensions(5, 5, 2,
		// 60));
		params.put(EncodeHintType.MARGIN, new Integer(0));
		params.put(EncodeHintType.ERROR_CORRECTION, new Integer(0));
		barcode.addParameter(params);

		printer.printText("_________________________________________");
		printer.printText("PDF417 binary, data length: " + bc.length);
		printer.printText("Module width: " + 2);
		long time = System.currentTimeMillis();
		printer.printBarcode(barcode);
		time = System.currentTimeMillis() - time;
		printer.printText(String.format("Barcode print time: %d ms", time));
		printer.printText("_________________________________________");
	}

	private void printImage() throws JposException, InterruptedException {
		printer.resetPrinter();
		File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File picsDir = new File(dcimDir, "Camera");
		picsDir.mkdirs(); // make if not exist
		// File newFile = new File(picsDir, "PDF417.bmp");
		File newFile = new File(picsDir, "printer.bmp");
		int imageIndex = printer.loadImage(newFile.getAbsolutePath());
		printer.printImage(imageIndex);
	}

	private void printReceipt() throws Exception {
		try {
			printSalesReceipt();
			printRefundReceipt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printSalesReceipt() throws JposException, InterruptedException {
		int howMuch = rand.nextInt(10);
		howMuch += 1; // guarantee
		if (printer == null) {
			return;
		}

		long payment = 0;
		printer.resetPrinter();
		printer.setFiscalReceiptType(jpos.FiscalPrinterConst.FPTR_RT_SALES);
		printer.beginFiscalReceipt(false);
		for (int i = 0; i < howMuch; i++) {
			long price = Math.abs(rand.nextLong() % 1000);
			payment += price;

			String itemName = items[rand.nextInt(items.length)];
			printer.printRecItem(itemName, price, 0, 0, 0, "");
		}
		printer.printRecTotal(payment, payment, "1");
		printer.endFiscalReceipt(false);
	}

	private void printRefundReceipt() throws JposException, InterruptedException {
		int howMuch = rand.nextInt(10);
		howMuch += 1; // guarantee
		if (printer == null) {
			return;
		}

		long payment = 0;
		printer.resetPrinter();
		printer.setFiscalReceiptType(jpos.FiscalPrinterConst.FPTR_RT_REFUND);
		printer.beginFiscalReceipt(false);
		for (int i = 0; i < howMuch; i++) {
			long price = Math.abs(rand.nextLong() % 1000);
			payment += price;

			String itemName = items[rand.nextInt(items.length)];
			printer.printRecItem(itemName, price, 0, 0, 0, "");
		}
		printer.printRecTotal(payment, payment, "1");
		printer.endFiscalReceipt(false);
	}

	private void printReceipt2() throws JposException, InterruptedException {
		printer.resetPrinter();
		int numHeaderLines = printer.getNumHeaderLines();
		for (int i = 1; i <= numHeaderLines; i++) {
			printer.setHeaderLine(i, "Header line " + i, false);
		}
		int numTrailerLines = printer.getNumTrailerLines();
		for (int i = 1; i <= numTrailerLines; i++) {
			printer.setTrailerLine(i, "Trailer line " + i, false);
		}
		printer.setAdditionalHeader("AdditionalHeader line 1\nAdditionalHeader line 2");
		printer.setFiscalReceiptType(jpos.FiscalPrinterConst.FPTR_RT_SALES);
		printer.beginFiscalReceipt(true);
		printer.printRecItem("За газ\nТел.: 123456\nЛ/сч: 789456", 100, 12, 0, 0, "");
		printer.printRecTotal(70, 70, "0");
		printer.printRecTotal(10, 10, "1");
		printer.printRecTotal(10, 10, "2");
		printer.printRecTotal(10, 10, "3");

		printer.endFiscalReceipt(false);
		printer.printDuplicateReceipt();
	}

	private void pollPrinter() throws JposException, InterruptedException {
		stopPlease = false;
		while (!stopPlease) {
			int howMuch = rand.nextInt(10);
			howMuch += 1; // guarantee
			if (printer == null) {
				return;
			}

			String serial = printer.readSerial();
			logger.debug("Serial: " + serial);
			printer.resetPrinter();
		}

	}

	@Override
	protected void onDestroy() {
		stopPlease = true;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.printReceipt:
				printReceipt();
				break;

			case R.id.btnPrintZReport:
				printer.printZReport();
				break;

			case R.id.StopReceipt:
				stopPlease = true;
				break;

			case R.id.btnOpenFiscalDay:
				printer.openFiscalDay();
				break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveTextFile() throws Exception {
		File path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
		File file = new File(path, "archive.txt");
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		writer.println("Test line 1");
		writer.println("Test line 2");
		writer.close();
	}
}
