package com.shtrih.fiscalprinter.model;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author nyx
 */
@RunWith(AndroidJUnit4.class)
public class PrinterModelsTests {
    @Test
    public void should_load_from_resource() {
        PrinterModels models = new PrinterModels();
        models.load();

        assertEquals(24, models.size());

        PrinterModel mobile = models.itemByID(25);
        assertEquals("SHTRIH-MOBILE-PTK", mobile.getName());
        assertEquals(1200, mobile.getMaxGraphicsHeight());
    }
}
