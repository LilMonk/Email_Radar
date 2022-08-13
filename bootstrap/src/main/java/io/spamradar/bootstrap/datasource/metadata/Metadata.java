package io.spamradar.bootstrap.datasource.metadata;

import io.spamradar.bootstrap.constant.Label;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Metadata {
    private String sourceUrl;
    private Label label;

    public static class MetadataBuilder{
        public MetadataBuilder label(String label){
            this.label = Label.getLabelOrDefault(label);
            return this;
        }
    }
}
