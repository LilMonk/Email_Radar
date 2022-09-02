package io.emailradar.commons.email.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * This data class contains a curated header fields, body and nested multipart emails.
 */
@Data
@Builder
@AllArgsConstructor
public class CivilisedEmail implements Serializable {
    @NonNull
    private final UUID _id;
    private CivilisedEmailHeader header;
    private CivilisedEmailNodeInfo nodeInfo;
    private String body;

    public CivilisedEmail() {
        this(UUID.randomUUID(), null, null, null);
    }

    public CivilisedEmail(UUID _id) {
        this(_id, null, null, null);
    }

    public static class CivilisedEmailBuilder {
        public CivilisedEmailBuilder _id() {
            this._id = UUID.randomUUID();
            return this;
        }

        public CivilisedEmailBuilder _id(UUID _id) {
            this._id = _id;
            return this;
        }
    }
}
