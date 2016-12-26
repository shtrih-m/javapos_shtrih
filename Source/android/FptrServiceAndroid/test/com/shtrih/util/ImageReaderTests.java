package com.shtrih.util;

import android.test.InstrumentationTestCase;

import junit.framework.TestCase;

import org.junit.Ignore;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

@Ignore
public class ImageReaderTests extends InstrumentationTestCase {

    public void testLoading() throws Exception {
        //InputStream in = getClass().getResourceAsStream("/qrcode_110.bmp");
        InputStream in = getInstrumentation().getTargetContext().getResources().getAssets().open("qrcode_110.bmp");

        try {
            byte[] buffer = new byte[1024];

            FileOutputStream out = new FileOutputStream("qrcode.bmp");

            try {
                int byteCount;
                while ((byteCount = in.read(buffer)) > 0) {
                    out.write(buffer, 0, byteCount);
                }
            } finally {
                out.close();
            }

        } finally {
            in.close();
        }

    }
}
