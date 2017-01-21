package com.shtrih.util;

import android.content.Context;

import com.shtrih.fiscalprinter.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LocalizerTests {

    @Before
    public void SetupContext(){
        Context context = RuntimeEnvironment.application.getApplicationContext();

        StaticContext.setContext(context);
    }

    @Test
    public void testRuLocale() {
        Localizer.init("messages_ru.txt");
        assertEquals("Нет связи", Localizer.getString(Localizer.NoConnection));
        assertEquals("SomeUnknownKey", Localizer.getString("SomeUnknownKey"));
    }

    @Test
    public void testRuLocale2() {
        Localizer.init("shtrihjavapos_ru.properties");
        assertEquals("Нет связи", Localizer.getString(Localizer.NoConnection));
        assertEquals("SomeUnknownKey", Localizer.getString("SomeUnknownKey"));
    }

    @Test
    public void testEngLocale() {
        Localizer.init("messages_en.txt");
        assertEquals("No connection", Localizer.getString(Localizer.NoConnection));
        assertEquals("SomeUnknownKey", Localizer.getString("SomeUnknownKey"));
    }

    @Test
    public void testEngLocale2() {
        Localizer.init("shtrihjavapos_en.properties");
        assertEquals("No connection", Localizer.getString(Localizer.NoConnection));
        assertEquals("SomeUnknownKey", Localizer.getString("SomeUnknownKey"));
    }

    @Test
    public void testDefaultLocale() {
        assertEquals("Нет связи", Localizer.getString(Localizer.NoConnection));
        assertEquals("SomeUnknownKey", Localizer.getString("SomeUnknownKey"));
    }

    @Test
    public void testDefaultLocale2() {
        Localizer.init("messages_unknown.txt");
        assertEquals("No connection", Localizer.getString(Localizer.NoConnection));
        assertEquals("SomeUnknownKey", Localizer.getString("SomeUnknownKey"));
    }
}

