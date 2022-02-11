/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.shtrih_m.kktnetd;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.shtrih.util.StringUtils;

/**
 *
 * @author Виталий
 */
public class ConfigTest {

    public ConfigTest() {
    }

    /**
     * 
     * Test of toJson method, of class Config.
     *
    @Test
    public void testToJson() throws Exception {
        System.out.println("toJson");
        Config instance = new Config();
        String expResult = StringUtils.InputStreamToString(getClass().getResourceAsStream("Config.json"));
        String result = instance.toJson();
        assertEquals(expResult, result);
    }

*/
    /**
     * Test of fromJson method, of class PPPConfig.
     */
    @Test
    public void testFromJson() throws Exception {
        System.out.println("fromJson");
        String jsonText = "{\"handle_signals\":false}";
        PPPConfig instance = new PPPConfig();
        PPPConfig result = instance.fromJson(jsonText);
        assertEquals(false, result.handle_signals);
    }

}
