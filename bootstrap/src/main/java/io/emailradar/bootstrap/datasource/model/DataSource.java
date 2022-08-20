package io.emailradar.bootstrap.datasource.model;

import io.emailradar.bootstrap.constant.Label;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;

/**
 * POJO for data source.
 */
@Data
@AllArgsConstructor
public class DataSource {
    URI uri;
    DataSourceType dataSourceType;
    Label label;

    public String getUrlAsString(){
        return this.uri.getScheme() + "://" +
                (this.uri.getAuthority() != null ? this.uri.getAuthority() : "") +
                this.uri.getPath();
    }
}