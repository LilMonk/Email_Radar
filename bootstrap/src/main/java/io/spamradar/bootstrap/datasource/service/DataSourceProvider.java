package io.spamradar.bootstrap.datasource.service;

import io.spamradar.bootstrap.datasource.model.DataSource;
import io.spamradar.bootstrap.datasource.model.DataSourceType;

import java.net.URI;
import java.net.URISyntaxException;

public class DataSourceProvider {
    public static DataSource getDataSourceInstance(String dataSource) throws URISyntaxException {
        URI uri = new URI(dataSource);
        String protocol = uri.getScheme();
        return new DataSource(uri, DataSourceType.valueOfProtocol(protocol));
    }
}
