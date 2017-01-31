package com.shtrih.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.pdf417.encoder.BarcodeMatrix;
import com.google.zxing.pdf417.encoder.Compaction;
import com.google.zxing.pdf417.encoder.Dimensions;
import com.google.zxing.pdf417.encoder.PDF417;
import java.nio.charset.Charset;

import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import java.nio.charset.Charset;

import java.util.HashMap;
import java.util.Map;

public class ZXingEncoder implements SmBarcodeEncoder {

    private final int maxWidth;
    private final int xScale;
    private final int yScale;

    public ZXingEncoder(int maxWidth, int xScale, int yScale) {
        this.maxWidth = maxWidth;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public SmBarcode encode(PrinterBarcode barcode) throws Exception {
        switch (barcode.getType()) {
            case SmFptrConst.SMFPTR_BARCODE_PDF417:
                return encodePDF417(barcode);
        }

        BarcodeFormat format = getBarcodeFormat(barcode.getType());
        if (format == null) {
            return null;
        }

        int barcodeWidth = 0;     // !!!
        int barcodeHeight = 0;    // !!!

        Map hints = (Map) barcode.getParameter(0);
        if (hints == null) {
            hints = new HashMap();
        }
        if (!hints.containsKey(EncodeHintType.MARGIN)) {
            hints.put(EncodeHintType.MARGIN, new Integer(0));
        }
        BitMatrix bm = new MultiFormatWriter().encode(barcode.getText(),
                format, barcodeWidth, barcodeHeight, hints);

        return getSmBarcode(bm);
    }

    /**
     * This takes an array holding the values of the PDF 417
     *
     * @param input a byte array of information with 0 is black, and 1 is white
     * @param margin border around the barcode
     * @return BitMatrix of the input
     */
    private static BitMatrix bitMatrixFrombitArray(byte[][] input, int margin) {
        // Creates the bitmatrix with extra space for whitespace
        BitMatrix output = new BitMatrix(input[0].length + 2 * margin, input.length + 2 * margin);
        output.clear();
        for (int y = 0, yOutput = output.getHeight() - margin - 1; y < input.length; y++, yOutput--) {
            for (int x = 0; x < input[0].length; x++) {
                // Zero is white in the bytematrix
                if (input[y][x] == 1) {
                    output.set(x + margin, yOutput);
                }
            }
        }
        return output;
    }

    public SmBarcode encodePDF417(PrinterBarcode barcode) throws Exception {
        int startLine = 1;
        PDF417Writer writer = new PDF417Writer();

        Map<EncodeHintType, ?> hints = null;
        if (barcode.getParameters().size() > 0) {
            hints = (Map<EncodeHintType, ?>) barcode.getParameters().get(0);
        }

        PDF417 encoder = new PDF417();
        int margin = 0;
        int errorCorrectionLevel = 0;
        boolean isCompact = false;
        Compaction compaction = Compaction.AUTO;
        Dimensions dimensions = null;

        if (hints != null) {
            if (hints.containsKey(EncodeHintType.PDF417_COMPACT)) {
                isCompact = (Boolean) hints.get(EncodeHintType.PDF417_COMPACT);
            }
            if (hints.containsKey(EncodeHintType.PDF417_COMPACTION)) {
                compaction = (Compaction) hints.get(EncodeHintType.PDF417_COMPACTION);
            }
            if (hints.containsKey(EncodeHintType.PDF417_DIMENSIONS)) {
                dimensions = (Dimensions) hints.get(EncodeHintType.PDF417_DIMENSIONS);
            }
            if (hints.containsKey(EncodeHintType.MARGIN)) {
                margin = ((Number) hints.get(EncodeHintType.MARGIN)).intValue();
            }
            if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ((Number) hints.get(EncodeHintType.ERROR_CORRECTION)).intValue();
            }
        }
        encoder.setCompaction(compaction);
        encoder.setCompact(isCompact);
        if (dimensions == null) {
            if (maxWidth == 320) {
                encoder.setDimensions(3, 3, 60, 2);
            } else {
                encoder.setDimensions(9, 2, 60, 2);
            }
        } else {
            encoder.setDimensions(dimensions.getMaxCols(), dimensions.getMinCols(), dimensions.getMaxRows(),
                    dimensions.getMinRows());
        }

        encoder.generateBarcodeLogic(barcode.getText(), errorCorrectionLevel);
        BarcodeMatrix barcodeMatrix = encoder.getBarcodeMatrix();
        byte[][] originalScale = barcodeMatrix.getScaledMatrix(xScale, yScale);
        BitMatrix bm = bitMatrixFrombitArray(originalScale, 0);

        BitArray row = null;
        int height = bm.getHeight();
        int width = bm.getRow(0, row).getSize();
        int widthInBytes = (width + 7) / 8;

        byte[][] data = new byte[height][widthInBytes];
        for (int y = 0; y < height; y++) {
            row = bm.getRow(y, row);
            for (int x = 0; x < row.getSizeInBytes(); x++) {
                data[y][x] = 0;
            }
            for (int x = 0; x < row.getSize(); x++) {
                if (row.get(x)) {
                    byte b = (byte) (1 << (x % 8));
                    data[y][x / 8] = (byte) (data[y][x / 8] + b);
                }

            }
        }
        return new SmBarcode(data, width, height);
    }

    public BarcodeFormat getBarcodeFormat(int type) throws Exception {
        switch (type) {
            case SmFptrConst.SMFPTR_BARCODE_UPCA:
                return BarcodeFormat.UPC_A;

            case SmFptrConst.SMFPTR_BARCODE_UPCE:
                return BarcodeFormat.UPC_E;

            case SmFptrConst.SMFPTR_BARCODE_EAN13:
                return BarcodeFormat.EAN_13;

            case SmFptrConst.SMFPTR_BARCODE_EAN8:
                return BarcodeFormat.EAN_8;

            case SmFptrConst.SMFPTR_BARCODE_CODE39:
                return BarcodeFormat.CODE_39;

            case SmFptrConst.SMFPTR_BARCODE_ITF:
                return BarcodeFormat.ITF;

            case SmFptrConst.SMFPTR_BARCODE_CODABAR:
                return BarcodeFormat.CODABAR;

            case SmFptrConst.SMFPTR_BARCODE_CODE93:
                return BarcodeFormat.CODE_93;

            case SmFptrConst.SMFPTR_BARCODE_CODE128:
                return BarcodeFormat.CODE_128;

            case SmFptrConst.SMFPTR_BARCODE_PDF417:
                return BarcodeFormat.PDF_417;

            case SmFptrConst.SMFPTR_BARCODE_AZTEC:
                return BarcodeFormat.AZTEC;

            case SmFptrConst.SMFPTR_BARCODE_DATA_MATRIX:
                return BarcodeFormat.DATA_MATRIX;

            case SmFptrConst.SMFPTR_BARCODE_MAXICODE:
                return BarcodeFormat.MAXICODE;

            case SmFptrConst.SMFPTR_BARCODE_QR_CODE:
                return BarcodeFormat.QR_CODE;

            case SmFptrConst.SMFPTR_BARCODE_RSS_14:
                return BarcodeFormat.RSS_14;

            case SmFptrConst.SMFPTR_BARCODE_RSS_EXPANDED:
                return BarcodeFormat.RSS_EXPANDED;

            case SmFptrConst.SMFPTR_BARCODE_UPC_EAN_EXTENSION:
                return BarcodeFormat.UPC_EAN_EXTENSION;

            default: {
                return null;
            }
        }
    }

    public SmBarcode getSmBarcode(BitMatrix bm) {
        BitArray row = null;
        int height = bm.getHeight();
        int width = bm.getRow(0, row).getSize();
        int widthInBytes = (width + 7) / 8;

        byte[][] data = new byte[height * yScale][widthInBytes * xScale];
        for (int y = 0; y < height * yScale; y++) {
            row = bm.getRow(y / yScale, row);
            for (int x = 0; x < (row.getSizeInBytes() * xScale); x++) {
                data[y][x] = 0;
            }
            for (int x = 0; x < (row.getSize() * xScale); x++) {
                if (row.get(x / xScale)) {
                    byte b = (byte) (1 << (x % 8));
                    data[y][x / 8] = (byte) (data[y][x / 8] + b);
                }
            }
        }
        return new SmBarcode(data, width * xScale, height * yScale);
    }

}
