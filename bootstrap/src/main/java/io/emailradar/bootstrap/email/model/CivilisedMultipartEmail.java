package io.emailradar.bootstrap.email.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CivilisedMultipartEmail {
    private final UUID _id = UUID.randomUUID();

    @NonNull
    private final UUID rootId;

    // TODO: Put all the header data into separate header object.
    private String contentType;
    private String contentTransferEncoding;

    private String body;

    List<CivilisedMultipartEmail> childEmails;
}
