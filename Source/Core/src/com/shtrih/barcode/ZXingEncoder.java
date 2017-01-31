package com.shtrih.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;

import java.util.HashMap;
import java.util.Map;

public class ZXingEncoder implements SmBarcodeEncoder {

    private final int xScale;
    private final int yScale;

    public ZXingEncoder(int xScale, int yScale) {
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public SmBarcode encode(PrinterBarcode barcode) throws Exception 
    {
        BarcodeFormat format = getBarcodeFormat(barcode.getType());
        if (format == null) return null;
        
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
                
            default: 
            {
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
