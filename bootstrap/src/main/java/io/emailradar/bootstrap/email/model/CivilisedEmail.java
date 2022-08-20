package io.emailradar.bootstrap.email.model;

import io.emailradar.bootstrap.datasource.metadata.Metadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * This data class contains a curated header fields, body and nested multipart emails.
 */
@Data
@Builder
@AllArgsConstructor
public class CivilisedEmail {
    private final UUID _id = UUID.randomUUID();
    private Metadata metadata;

    // TODO: Put all the header data into separate header object.
    private ZonedDateTime date;
    private String from;
    private List<String> to;
    private String contentType;
    private String subject;

    private String body;

    private List<CivilisedMultipartEmail> childEmails;
}
