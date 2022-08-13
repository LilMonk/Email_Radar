package io.spamradar.bootstrap.email;

import io.spamradar.bootstrap.email.model.CivilisedEmail;
import io.spamradar.bootstrap.email.model.CivilisedMultipartEmail;
import io.spamradar.bootstrap.datasource.metadata.Metadata;
import io.spamradar.bootstrap.email.model.PrimitiveEmail;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.apache.james.mime4j.dom.field.FieldName.*;

/**
 * Converts the {@link PrimitiveEmail} instance to {@link CivilisedEmail} instance.
 */
@Component
public class PrimitiveToCivilisedEmailConverter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, d MMM yyyy HH:mm:ss Z");

    /**
     * Converts {@link PrimitiveEmail} instance to {@link CivilisedEmail} instance.
     *
     * @param primitiveEmail instance to be converted.
     * @return {@link CivilisedEmail} instance.
     */
    public CivilisedEmail convertToCivilisedEmail(PrimitiveEmail primitiveEmail, Metadata metadata) {
        String dateStr = primitiveEmail.getHeaders().getOrDefault(DATE, null);
        String from = primitiveEmail.getHeaders().getOrDefault(FROM, null);
        String toStr = primitiveEmail.getHeaders().getOrDefault(TO, null);
        String contentType = primitiveEmail.getHeaders().getOrDefault(CONTENT_TYPE, null);
        String subject = primitiveEmail.getHeaders().getOrDefault(SUBJECT, null);
        String body = primitiveEmail.getBody();
        ZonedDateTime date = dateStr != null ? ZonedDateTime.parse(dateStr, formatter) : null;
        List<String> to = toStr != null ? List.of(toStr.split(", ")) : null;

        CivilisedEmail civilisedEmail = CivilisedEmail.builder()
                .metadata(metadata)
                .date(date)
                .from(from)
                .to(to)
                .contentType(contentType)
                .subject(subject)
                .body(body)
                .build();

        List<CivilisedMultipartEmail> childEmails = convertToMultipartEmails(primitiveEmail.getChildEmails(), civilisedEmail.get_id());
        civilisedEmail.setChildEmails(childEmails);

        return civilisedEmail;
    }

    private List<CivilisedMultipartEmail> convertToMultipartEmails(List<PrimitiveEmail> emails, UUID id) {
        if (emails.isEmpty())
            return Collections.emptyList();

        List<CivilisedMultipartEmail> civilisedMultipartEmails = new ArrayList<>();

        for (PrimitiveEmail pEmail : emails) {
            String contentType = pEmail.getHeaders().getOrDefault(CONTENT_TYPE, null);
            String contentTransferEncoding = pEmail.getHeaders().getOrDefault(CONTENT_TRANSFER_ENCODING, null);
            String body = pEmail.getBody();
            List<CivilisedMultipartEmail> civilisedMultipartEmailList = convertToMultipartEmails(pEmail.getChildEmails(), id);

            CivilisedMultipartEmail civilisedMultipartEmail = CivilisedMultipartEmail.builder()
                    .rootId(id)
                    .contentType(contentType)
                    .contentTransferEncoding(contentTransferEncoding)
                    .body(body)
                    .childEmails(civilisedMultipartEmailList).build();

            civilisedMultipartEmails.add(civilisedMultipartEmail);
        }

        return civilisedMultipartEmails;
    }

}
