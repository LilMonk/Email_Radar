package io.spamradar.bootstrap.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This data class contains a curated header fields, body and nested multipart emails.
 */
@Data
public class CivilisedEmail {
    private ZonedDateTime date;
    private String from;
    private List<String> to;
    private String contentType;
    private String subject;
    private String body;
    private List<CivilisedEmail> childEmails = new ArrayList<>();
}
