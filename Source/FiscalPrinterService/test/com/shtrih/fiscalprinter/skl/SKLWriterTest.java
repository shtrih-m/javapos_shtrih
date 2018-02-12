package com.shtrih.fiscalprinter.skl;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SKLWriterTest {

    @Test
    public void should_write_two_string_with_short_text() throws Exception {

        StringBuilder sb = new StringBuilder();
        SKLWriter writer = new SKLWriter(new StringBuilderSKLStorage(sb), 48);

        writer.add("ЗН ККТ 0000000000001358", "12.02.18 14:26");

        assertEquals("ЗН ККТ 0000000000001358           12.02.18 14:26\n", sb.toString());
    }

    @Test
    public void should_write_two_string_with_first_long_text() throws Exception {

        StringBuilder sb = new StringBuilder();
        SKLWriter writer = new SKLWriter(new StringBuilderSKLStorage(sb), 48);

        writer.add("ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКСЕЕВИЧ", "12.02.18 14:26");

        assertEquals("ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКСЕЕВИЧ             \n                                  12.02.18 14:26\n", sb.toString());
    }


    @Test
    public void should_write_two_string_with_first_long_text2() throws Exception {

        StringBuilder sb = new StringBuilder();
        SKLWriter writer = new SKLWriter(new StringBuilderSKLStorage(sb), 30);

        writer.add("ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКСЕЕВИЧ", "12.02.18 14:26");

        assertEquals("ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКС\nЕЕВИЧ           12.02.18 14:26\n", sb.toString());
    }

    @Test
    public void should_write_two_string_with_first_and_second_multiline_text() throws Exception {

        StringBuilder sb = new StringBuilder();
        SKLWriter writer = new SKLWriter(new StringBuilderSKLStorage(sb), 10);

        writer.add("ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКСЕЕВИЧ", "12.02.18 14:26");

        assertEquals("ОПЕРАТОР Б\nЕЛИМОВ ДМИ\nТРИЙ АЛЕКС\nЕЕВИЧ     \n12.02.18 1\n4:26      \n", sb.toString());
    }

    @Test
    public void should_write_two_string_with_second_long_text() throws Exception {

        StringBuilder sb = new StringBuilder();
        SKLWriter writer = new SKLWriter(new StringBuilderSKLStorage(sb), 48);

        writer.add("12.02.18 14:26", "ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКСЕЕВИЧ");

        assertEquals("12.02.18 14:26                                  \n             ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКСЕЕВИЧ\n", sb.toString());
    }

    @Test
    public void should_write_two_string_with_a_bit_long_text() throws Exception {

        StringBuilder sb = new StringBuilder();
        SKLWriter writer = new SKLWriter(new StringBuilderSKLStorage(sb), 48);

        writer.add("12.02.18 14:26", "ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКСЕЕВИ");

        assertEquals("12.02.18 14:26                                  \n              ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКСЕЕВИ\n", sb.toString());
    }

    @Test
    public void should_write_two_string_with_almost_long_text() throws Exception {

        StringBuilder sb = new StringBuilder();
        SKLWriter writer = new SKLWriter(new StringBuilderSKLStorage(sb), 48);

        writer.add("12.02.18 14:26", "ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКСЕЕВ");

        assertEquals("12.02.18 14:26 ОПЕРАТОР БЕЛИМОВ ДМИТРИЙ АЛЕКСЕЕВ\n", sb.toString());
    }

    @Test
    public void should_write_two_string_when_they_both_match_length() throws Exception {

        StringBuilder sb = new StringBuilder();
        SKLWriter writer = new SKLWriter(new StringBuilderSKLStorage(sb), 10);

        writer.add("1234567890", "0987654321");

        assertEquals("1234567890\n0987654321\n", sb.toString());
    }

    @Test
    public void should_write_two_string_when_they_exact_multiple_of_length() throws Exception {

        StringBuilder sb = new StringBuilder();
        SKLWriter writer = new SKLWriter(new StringBuilderSKLStorage(sb), 5);

        writer.add("1234567890", "0987654321");

        assertEquals("12345\n67890\n09876\n54321\n", sb.toString());
    }

    class StringBuilderSKLStorage implements SKLStorage{

        private StringBuilder sb;

        public StringBuilderSKLStorage(StringBuilder sb){
            this.sb = sb;
        }

        @Override
        public void writeLine(String line) throws IOException {
            sb.append(line);
            sb.append('\n');
        }
    }
}

