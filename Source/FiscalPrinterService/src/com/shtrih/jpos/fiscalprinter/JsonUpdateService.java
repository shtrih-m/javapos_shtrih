/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.io.OutputStreamWriter;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.shtrih.util.CompositeLogger;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.SMFiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.PrinterDate;
import java.io.File;

/**
 *
 * @author V.Kravtsov
 */
public class JsonUpdateService implements Runnable {

    private boolean ecrHasKeys;
    private PrinterDate firmwareDate;
    private String firmwareFileName = "";
    private volatile Thread thread = null;
    private final SMFiscalPrinter printer;
    private boolean updateAvailable = false;
    public static CompositeLogger logger = CompositeLogger.getLogger(JsonUpdateService.class);

    public JsonUpdateService(SMFiscalPrinter printer) {
        this.printer = printer;
    }

    public boolean isStarted() {
        return thread != null;
    }

    public void start() throws Exception {
        if (!isStarted()) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() throws Exception {
        if (isStarted()) {
            thread.interrupt();
            thread.join();
            thread = null;
        }
    }

    public void run() {
        try {
            firmwareDate = printer.readLongStatus().getFirmwareDate();
            ecrHasKeys = !printer.readTable(23, 1, 11).contains("-");
            while (!thread.isInterrupted()) {
                checkForUpdates();
                Thread.sleep(printer.getParams().jsonUpdatePeriodInMinutes * 60000);
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 
    private static final String BIN_FILE_NAME = "upd_app.bin";
    private static final String BIN_OLD_FRS_FILE_NAME = "upd_app_for_old_frs.bin";
    private static final String SERVER_URL_DEF = "http://127.0.0.1:8888/check_firmware";
    private static final int SLEEP_TIMEOUT_DEF = 5;

    public void checkForUpdates() throws Exception {
        logger.debug("checkForUpdates: " + printer.getParams().jsonUpdateServerURL);

        if (updateAvailable) {
            updateFirmware();
            return;
        }
        UpdateServerResult result = jsonRequest(new URL(printer.getParams().jsonUpdateServerURL), firmwareDate);
        String path = ".\\firmware" + File.separator + String.valueOf(result.version) + File.separator;
        File file = new File(path);
        file.mkdirs();

        String fileName = "";
        if (ecrHasKeys) {
            fileName = path + BIN_FILE_NAME;
        } else {
            fileName = path + BIN_OLD_FRS_FILE_NAME;
        }
        if (result.updateAvailable) {
            logger.debug("Update available");
            file = new File(fileName);
            if (file.exists() && !file.isDirectory()) {
                firmwareFileName = file.getCanonicalPath();
                updateAvailable = true;
                updateFirmware();
                return;
            }
            String url = result.downloadUrlOld;
            if (ecrHasKeys) {
                url = result.downloadUrl;
            }
            if (downloadFile(url, fileName)) {
                firmwareFileName = fileName;
                updateAvailable = true;
                updateFirmware();
            }
        }
    }

    private void updateFirmware() {
        if (!updateAvailable) {
            return;
        }

        logger.error("updateFirmware");
        try {
            if (printer.readPrinterStatus().getPrinterMode().isDayClosed()) {
                printer.updateFirmware(firmwareFileName);
                updateAvailable = false;
            }
        } catch (Exception e) {
            logger.error("updateFirmware failed: " + e.getMessage());
        }
    }

    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads a file from a URL
     *
     * @param fileURL HTTP URL of the file to be downloaded
     * @param saveDir path of the directory to save the file
     * @throws IOException
     */
    public boolean downloadFile(String fileURL, String saveFileName)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
            InputStream inputStream = httpConn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(saveFileName);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
        } else {
            logger.debug("No file to download. Server replied HTTP code: " + responseCode);
            return false;
        }
        httpConn.disconnect();
        return true;
    }

    public UpdateServerResult jsonRequest(URL url, PrinterDate date) throws Exception {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept-Charset", "UTF-8");
        con.setRequestProperty("build_date", date.toText());
        con.setUseCaches(false);
        con.setDoInput(true);
        con.setDoOutput(true);

        String JsonText = "{\"build_date\": \"" + date.toText() + "\"}";
        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
        out.write(JsonText);
        out.close();

        if (con.getResponseCode() != 200) {
            throw new Exception("Invalid server response code: "
                    + con.getResponseCode() + " " + con.getResponseMessage());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        JSONObject json = new JSONObject(new JSONTokener(in));
        UpdateServerResult result = new UpdateServerResult();
        result.updateAvailable = json.getBoolean("update_available");
        if (!result.updateAvailable) {
            return result;
        }
        result.description = json.getString("description");
        result.downloadUrl = json.getString("url");
        result.downloadUrlOld = json.getString("url_old_frs");
        result.version = json.getInt("version");
        result.critical = json.getBoolean("critical");
        in.close();
        return result;
    }

    /*    
     {
     "update_available": true,
     "critical": true,
     "version": 20180116,
     "description": "version before 20180116 may die anytime",
     "url": "http://127.0.0.1:8888/firmware/20180116/upd_app.bin",
     "url_old_frs": "http://127.0.0.1:8888/firmware/20180116/upd_app_for_old_frs.bin",
     }
     */
    public class UpdateServerResult {

        public boolean updateAvailable;
        public boolean critical;
        public int version;
        public String description;
        public String downloadUrl;
        public String downloadUrlOld;

        public UpdateServerResult() {
        }
    }
}
