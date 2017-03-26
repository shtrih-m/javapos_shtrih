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

    @Test
    public void should_load_cap_fs_close_check() throws Exception {
        PrinterModels models = new PrinterModels();
        models.load();

        assertEquals(24, models.size());

        PrinterModel mobile = models.find(19, 2, 0);
        assertEquals("SHTRIH-MOBILE-PTK", mobile.getName());
        assertEquals(false, mobile.getCapFSCloseCheck());

        PrinterModel retail = models.find(22, 1, 14);
        assertEquals("Retail-01K", retail.getName());
        assertEquals(true, retail.getCapFSCloseCheck());
    }
}
