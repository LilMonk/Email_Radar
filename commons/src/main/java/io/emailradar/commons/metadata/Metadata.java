package io.emailradar.commons.metadata;

import io.emailradar.commons.constant.Label;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Metadata implements Serializable {
    // TODO: Triage about whether to include source in
    private String sourceUrl;
    private Label label;

    public static class MetadataBuilder {
        public MetadataBuilder label(String label) {
            this.label = Label.getLabelOrDefault(label);
            return this;
        }
    }
}
