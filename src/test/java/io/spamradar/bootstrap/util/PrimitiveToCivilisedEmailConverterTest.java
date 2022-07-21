package io.spamradar.bootstrap.util;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class PrimitiveToCivilisedEmailConverterTest {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, d MMM yyyy HH:mm:ss X");
    private String dateTime = "Fri, 10 Jun 2005 08:31:24 -0500";

    @Test
    void testFormatter() {
        System.out.println(dateTime.charAt(29));
        ZonedDateTime localDateTime = ZonedDateTime.parse(dateTime, formatter);
        System.out.println(localDateTime);
        System.out.println(localDateTime.getOffset());
    }
}