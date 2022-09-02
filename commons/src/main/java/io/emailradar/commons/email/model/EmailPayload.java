package io.emailradar.commons.email.model;

import io.emailradar.commons.metadata.Metadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class EmailPayload implements Serializable {
    @NonNull
    private UUID _id;
    private Metadata metadata;
    private CivilisedEmail civilisedEmail;

    public EmailPayload() {
        this(UUID.randomUUID(), null, null);
    }

    public EmailPayload(UUID _id) {
        this(_id, null, null);
    }

    public static class EmailPayloadBuilder {
        public EmailPayload.EmailPayloadBuilder _id() {
            this._id = UUID.randomUUID();
            return this;
        }

        public EmailPayload.EmailPayloadBuilder _id(UUID _id) {
            this._id = _id;
            return this;
        }
    }
}
