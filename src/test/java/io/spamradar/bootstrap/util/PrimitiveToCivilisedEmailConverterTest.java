package io.spamradar.bootstrap.util;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class PrimitiveToCivilisedEmailConverterTest {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss X");
    private String dateTime = "Fri, 25 Feb 2000 03:34:00 -0000";

    @Test
    void testFormatter() {
        System.out.println(dateTime.charAt(29));
        ZonedDateTime localDateTime = ZonedDateTime.parse(dateTime, formatter);
        System.out.println(localDateTime);
        System.out.println(localDateTime.getOffset());
    }
}