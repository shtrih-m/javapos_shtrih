package com.shtrih.util;

import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;


import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileOutputStream;
import java.io.InputStream;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ImageReaderTests extends InstrumentationTestCase {

    @Test
    public void testLoading() throws Exception {

        throw new Exception("123");

        //InputStream in = getClass().getResourceAsStream("/qrcode_110.bmp");
//        InputStream in = getInstrumentation().getTargetContext().getResources().getAssets().open("qrcode_110.bmp");
//           System.out.print("aaaa");
//        try {
//            byte[] buffer = new byte[1024];
//
//            FileOutputStream out = new FileOutputStream("qrcode.bmp");
//
//            try {
//                int byteCount;
//                while ((byteCount = in.read(buffer)) > 0) {
//                    out.write(buffer, 0, byteCount);
//                }
//            } finally {
//                out.close();
//            }
//
//        } finally {
//            in.close();
//        }

    }
}
