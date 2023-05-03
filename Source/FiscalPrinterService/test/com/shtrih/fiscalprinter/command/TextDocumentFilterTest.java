package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author P.Zhirkov
 */
public class TextDocumentFilterTest {

    @Test
    public void should_serialize_request() throws Exception {
        TextDocumentFilter item = new TextDocumentFilter();
        assertEquals("0.123", item.quantityToStr(0.123));
        assertEquals("0.123456", item.quantityToStr(0.123456));
    }

}

