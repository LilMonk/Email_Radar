package io.emailradar.commons.email.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CivilisedEmailHeader implements Serializable {
    private ZonedDateTime date;
    private String from;
    private List<String> to;
    private String contentType;
    private String contentTransferEncoding;
    private String subject;
}
