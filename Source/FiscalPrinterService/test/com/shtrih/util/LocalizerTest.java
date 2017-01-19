package com.shtrih.util;

import junit.framework.TestCase;

/**
 * Created by P.Zhirkov on 19.01.2017.
 */
public class LocalizerTest extends TestCase {

    public void testRuLocale() {
        Localizer.init("messages_ru.txt");
        assertEquals("Нет связи", Localizer.getString(Localizer.NoConnection));
    }

    public void testEngLocale() {
        Localizer.init("messages_en.txt");
        assertEquals("No connection", Localizer.getString(Localizer.NoConnection));
    }

    public void testDefaultLocale() {
        assertEquals("Нет связи", Localizer.getString(Localizer.NoConnection));
    }
}
