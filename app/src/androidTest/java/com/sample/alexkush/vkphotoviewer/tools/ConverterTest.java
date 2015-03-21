package com.sample.alexkush.vkphotoviewer.tools;

import junit.framework.TestCase;

public class ConverterTest extends TestCase {
    public void testFormatDateMillis() throws Exception {
        String date = Converter.formatDate(1000000000000L, Converter.TimeUnit.MILLISECOND);
        assertEquals("2001.09.09 04:46", date);
    }
    public void testFormatDateSec() throws Exception {
        String date = Converter.formatDate(1000000000L, Converter.TimeUnit.SECOND);
        assertEquals("2001.09.09 04:46", date);
    }
}