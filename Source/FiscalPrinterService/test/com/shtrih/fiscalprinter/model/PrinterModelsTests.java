package com.shtrih.fiscalprinter.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
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
