package io.spamradar.bootstrap.util;

import io.spamradar.bootstrap.model.CivilisedEmail;
import io.spamradar.bootstrap.model.PrimitiveEmail;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.apache.james.mime4j.dom.field.FieldName.*;

/**
 * Converts the {@link PrimitiveEmail} instance to {@link CivilisedEmail} instance.
 */
public class PrimitiveToCivilisedEmailConverter {

    private PrimitiveEmail primitiveEmail;
    private CivilisedEmail civilisedEmail;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z");

    public PrimitiveToCivilisedEmailConverter(PrimitiveEmail primitiveEmail) {
        this.primitiveEmail = primitiveEmail;
        this.civilisedEmail = new CivilisedEmail();
        convertToCivilisedEmail(this.primitiveEmail, this.civilisedEmail);
    }

    private void convertToCivilisedEmail(PrimitiveEmail primitiveEmail, CivilisedEmail civilisedEmail) {
        String dateStr = primitiveEmail.getHeaders().getOrDefault(DATE, null);
        String from = primitiveEmail.getHeaders().getOrDefault(FROM, null);
        String toStr = primitiveEmail.getHeaders().getOrDefault(TO, null);
        String contentType = primitiveEmail.getHeaders().getOrDefault(CONTENT_TYPE, null);
        String subject = primitiveEmail.getHeaders().getOrDefault(SUBJECT, null);
        String body = primitiveEmail.getBody();
        ZonedDateTime date = dateStr != null ? ZonedDateTime.parse(dateStr, formatter) : null;
        List<String> to = toStr != null ? List.of(toStr.split(", ")) : null;

        civilisedEmail.setDate(date);
        civilisedEmail.setFrom(from);
        civilisedEmail.setTo(to);
        civilisedEmail.setContentType(contentType);
        civilisedEmail.setSubject(subject);
        civilisedEmail.setBody(body);

        for (PrimitiveEmail pEmail : primitiveEmail.getChildEmails()) {
            CivilisedEmail cEmail = new CivilisedEmail();
            civilisedEmail.getChildEmails().add(cEmail);
            convertToCivilisedEmail(pEmail, cEmail);
        }
    }

    public CivilisedEmail getCivilisedEmail() {
        return civilisedEmail;
    }
}
