package com.skoove.challenge.utils.extension

import org.junit.Assert.assertEquals
import org.junit.Test

class TimestampToDurationTest {
    @Test
    fun test() {
        assertEquals("--:--", (-20000).timeStampToDuration())
        assertEquals("0:20", 20000.timeStampToDuration())
        assertEquals("0:00", 0.timeStampToDuration())
        assertEquals("1:00", 60000.timeStampToDuration())
        assertEquals("10:00", 600001.timeStampToDuration())
        assertEquals("11:44", 704123.timeStampToDuration())
        assertEquals("35791:23", (Integer.MAX_VALUE).timeStampToDuration())
    }
}